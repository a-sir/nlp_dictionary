package cognems;

import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import util.StringUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author A.Sirenko
 * Date: 10/23/13
 */
public class RuWiktionaryParseTool {

	private static Logger LOG = LoggerFactory.getLogger(EnWiktionaryParseTool.class);

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		if (args.length < 2) {
			LOG.error("Use with args: <wikitionary dump> <out file>");
			System.exit(1);
		}
		try (
				InputStream input = Files.newInputStream(Paths.get(args[0]));
				Writer bw = new OutputStreamWriter(
                        new BufferedOutputStream(new FileOutputStream(args[1])))
		) {
			RuWiktionaryDumpHandler handler = new RuWiktionaryDumpHandler(bw);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(input, handler);
		} catch (IOException e) {
			LOG.error(e.toString());
		}
	}

	private static final List<Cognem> NODATA = Collections.unmodifiableList(new ArrayList<Cognem>(0));

	private static Splitter SPLIT_BY_NEW_LINE = Splitter.on("\n").trimResults().omitEmptyStrings();

	static List<Cognem> parseDescription(@NotNull String cognemName, @NotNull String text) {
		List<Cognem> res = new ArrayList<>();

		int i = text.indexOf("=Значение=");
        if (i== -1) i = text.indexOf("= Значение =");

		if (i == -1) {
			return NODATA;
		}
		int j = text.indexOf('\n', i + 5);
		if (j != -1) {
			text = text.substring(j);
			Cognem.Builder builder = new Cognem.Builder(cognemName);
			for (String l : SPLIT_BY_NEW_LINE.split(text)) {
				if (l.startsWith("=")) {
					break;
				}
				Cognem cogn;
				String sense;
                if (l.startsWith("# ")) {  // "# " or "#: "
                    sense = l.substring(2).trim();
				} else if (l.startsWith("#: ")) {
                    sense = l.substring(3).trim();
				} else {
                    continue;
                }

                if (sense.contains("{{пример}}") || sense.contains("{{пример|")) {
                    sense = sense.substring(0, sense.indexOf("{{пример"));
                }

                cogn = parseLine(sense, builder);
                if (cogn != null && russianAndSpaces(cogn.name)) {
					res.add(cogn);
				}
			}
		}
		return res;
	}

    private static boolean russianAndSpaces(@NotNull String line) {
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (!((c >= 'а' && c <= 'я') || (c >= 'А' && c <= 'Я') || c == ' ' || (c >= 0 && c <= 9))) {
                return false;
            }
        }
        return true;
    }

	private static final String wikiLinkOpen = "[[";
	private static final String wikiLinkClose = "]]";

	private static final String CONTEXT_OPEN = "{{";
    private static final String CONTEXT_CLOSE = "}}";

	@Nullable
	public static Cognem parseLine(@NotNull String line, @NotNull Cognem.Builder builder) {
		if (line.contains("{{=|")) { // skip synonims
			return null;
		}

        List<String> context = new ArrayList<>(1);
        int offset = cutContextFromPrefix(line, context);
        while (offset != 0 ) {
            if (offset >= line.length()) {
                return null;
            }
            line = line.substring(offset);
            offset = cutContextFromPrefix(line, context);
        }

		StringBuilder sb = new StringBuilder(line.length());
		int[] bounds;
		offset = 0;

		while ((bounds = StringUtils.getBounds(line, offset, wikiLinkOpen, wikiLinkClose)) != null) {
			sb.append(line.substring(offset, bounds[0]));
			String link = line.substring(
					bounds[0] + wikiLinkOpen.length(),
					bounds[1]
			);
			int i = link.lastIndexOf('|');
			sb.append(
					(i != -1 && i != link.length() - 1) ?
							link.substring(i + 1) : link
			);
			offset = bounds[1] + wikiLinkClose.length();
		}
		sb.append(line.substring(offset));
		String sense = sb.toString().trim();
		return sense.isEmpty() ?
                null : builder.setAttributes(sense, context.toArray(new String[context.size()])).build();
	}

    private static int cutContextFromPrefix(@NotNull String line, @NotNull List<String> context) {
        int start = line.indexOf(CONTEXT_OPEN);
        if (start != -1 || start + 2 < line.length()) {
            if (spaceOrDelim(line, 0, start)) {
                int end = line.indexOf(CONTEXT_CLOSE, start + 2);
                if (end != -1) {
                    String[] candidates = line.substring(start + 2, end).split("[|]");
                    for (String v : candidates) {
                        if (!v.contains("помета")) {
                            context.add(v);
                        }
                    }
                    return end + 2;
                }
            }
        }
        return 0;
    }

    private static boolean spaceOrDelim(@NotNull String line, int start, int endExcluded) {
        for (int i = start ; i < endExcluded; ++i ) {
            char c = line.charAt(i);
            if (c != ' ' && c != ',')
                return false;
        }

        return true;
    }
}