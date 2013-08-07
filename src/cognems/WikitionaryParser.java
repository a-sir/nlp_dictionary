package cognems;

import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

	private static final String NOUN = "===Noun===";

	private static Pattern pattern = Pattern.compile("[=]+");

	static List<String> parseDescription(@NotNull String text) {
		List<String> res = new ArrayList<>();

		int i = text.indexOf(NOUN);
		if (i != -1) {
			Matcher m = pattern.matcher(text.substring(i + NOUN.length()));
			int j;
			if (m.find()) {
				j = m.start();
			} else {
				j = text.length() - 1;
			}

			text = text.substring(i + NOUN.length(), j + 2);
			for (String l : text.split("\n")) {
				if (l.startsWith("# ")) {  // "# " or "#: "
					res.add(l.substring(2).trim());
				} else if (l.startsWith("#: ")) {
					res.add(l.substring(3).trim());
				}
			}
		}
		return res;
	}
}
