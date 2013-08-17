package cognems;

import javafx.util.Pair;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import util.StringUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author A.Sirenko
 * Date: 8/4/13
 */
public class WikitionaryParser {

	private static Logger LOG = LoggerFactory.getLogger(WikitionaryParser.class);

	public static void main(String[] args) throws ParserConfigurationException, SAXException {

		if (args.length < 2) {
			LOG.error("Use with args: <wikitionary dump> <out file>");
			System.exit(1);
		}

		try (
				InputStream input = Files.newInputStream(Paths.get(args[0]));
				BufferedWriter bw = Files.newBufferedWriter(Paths.get(args[1]), Charset.forName("UTF-8"))
		) {
			WikitionaryDumpHandler handler = new WikitionaryDumpHandler(bw);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(input, handler);
		} catch (IOException e) {
			LOG.error(e.toString());
		}
	}

	private static final Pattern NOUN_PATTERN = Pattern.compile(".*[=]+Noun[=]+\n.+");
	private static final Pattern NEXT_SECTION = Pattern.compile("^[=]+[a-zA-Z]+[=]+$");

	private static final List<Cognem> NODATA = Collections.unmodifiableList(new ArrayList<Cognem>(0));



	static List<Cognem> parseDescription(@NotNull String cognemName, @NotNull String text) {
		List<Cognem> res = new ArrayList<>();

		Matcher m = NOUN_PATTERN.matcher(text);
		int i;
		if (m.find()) {
			i = m.start();
		} else {
			return NODATA;
		}
		int j = text.indexOf('\n', i);
		if (j != -1) {
			text = text.substring(j);
			Cognem.Builder builder = new Cognem.Builder(cognemName);
			for (String l : text.split("\n")) {
				if (NEXT_SECTION.matcher(l).find()) {
					break;
				}
				Cognem cogn = null;
				if (l.startsWith("# ")) {  // "# " or "#: "
					cogn = parseLine(l.substring(2).trim(), builder);
				} else if (l.startsWith("#: ")) {
					cogn = parseLine(l.substring(3).trim(), builder);
				}
				if (cogn != null) {
					res.add(cogn);
				}
			}
		}
		return res;
	}

	private static final String wikiLinkOpen = "[[";
	private static final String wikiLinkClose = "]]";

	private static final Pattern ELEMENTS_TO_SKIP = Pattern.compile(".*[']{3}[{]{2}[A-Z]+[}]{2}[']{3}.*");
	private static final String CONTEXT_OPEN = "{{context|";

	@Nullable
	public static Cognem parseLine(@NotNull String line, @NotNull Cognem.Builder builder) {
		if (ELEMENTS_TO_SKIP.matcher(line).find()
				|| line.contains("{{defdate|")
				|| line.startsWith("{{usex|")
				|| line.contains("{{alternative spelling of|")
				|| line.startsWith("{{label|")
				|| line.startsWith("{{plural of|")
				|| line.startsWith("{{Latn-def|")
				|| line.contains("http://")
				|| line.contains("{{qualifier|")
				|| line.contains("{{term|")
				|| line.contains("{{taxlink|")) {
			return null;
		}
		// {{context|US|lang=en}} sfdsf
		String[] context = null;
		if (line.startsWith(CONTEXT_OPEN)) {
			line = line.substring(10); // skip context
			int i = line.indexOf("}}");
			context = line.substring(0, i).split("[|]");
			line = line.substring(i + 2);
		}

		StringBuilder sb = new StringBuilder(line.length());
		Pair<Integer, Integer> bounds;
		int offset = 0;

		while ((bounds = StringUtils.getBounds(line, offset, wikiLinkOpen, wikiLinkClose)) != null) {
			sb.append(line.substring(offset, bounds.getKey()));
			String link = line.substring(
					bounds.getKey() + wikiLinkOpen.length(),
					bounds.getValue()
			);
			int i = link.lastIndexOf('|');
			sb.append(
					(i != -1 && i != link.length() - 1) ?
							link.substring(i + 1) : link
			);
			offset = bounds.getValue() + wikiLinkClose.length();
		}
		sb.append(line.substring(offset));
		String sense = sb.toString().trim();
		return sense.isEmpty() ? null : builder.setAttributes(sense, context).build();
	}
}
