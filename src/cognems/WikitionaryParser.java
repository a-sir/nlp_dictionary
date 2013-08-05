package cognems;

import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A.Sirenko
 * Date: 8/4/13
 */
public class WikitionaryParser {

	private static Logger LOG = LoggerFactory.getLogger(WikitionaryParser.class);

	private enum STATE {
		NONE,
		PAGE,
		TITLE,
		DESCRIPTION,
		END
	}

	public static void main(String[] args) {

		if (args.length < 2) {
			LOG.error("Use with args: <wikitionary dump> <out file>");
			System.exit(1);
		}

		STATE state = STATE.NONE;
		Charset UTF8 = Charset.forName("UTF-8");

		try (
				BufferedReader br = Files.newBufferedReader(Paths.get(args[0]), UTF8);
				BufferedWriter bw = Files.newBufferedWriter(Paths.get(args[1]), UTF8)
		) {
			boolean first = true;
			String title = null;
			List<String> descriptions = null;

			while (state != STATE.END) {
				switch (state) {
					case NONE:
						if (goIntoPage(br)) {
							state = STATE.PAGE;
						}
						break;
					case PAGE:
						if ((title = expectTitleOrLeavePage(br)) != null) {
							state = STATE.TITLE;
						} else {
							state = STATE.NONE;
						}
						break;
					case TITLE:
						descriptions = expectDescriptionOrLeavePage(br);
						if (!descriptions.isEmpty()) {
							state = STATE.DESCRIPTION;
						} else {
							state = STATE.NONE;
						}
						break;
					case DESCRIPTION:
						if (!expectPageEnd(br)) {
							LOG.error("Expected page end wasn't encountered");
						}
						if (title != null && descriptions != null && !descriptions.isEmpty()) {
							for (String description : descriptions) {
								if (first) {
									first = false;
								} else {
									bw.newLine();
								}
								bw.write(title);
								bw.write('\t');
								bw.write(description);
							}
						}
						title = null;
						descriptions = null;
						break;
				}
			}
		} catch (IOException e) {
			LOG.error(e.toString());
		}

	}

	@Nullable
	private static boolean expectPageEnd(@NotNull BufferedReader br) throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			// if (line.matches("<title>[a-zA-Z]+</title>.*")) {
			if (line.matches(".*</page>.*")) {
				LOG.info(line);
				return true;
			}
		}
		return false;
	}

	@NotNull
	static List<String> expectDescriptionOrLeavePage(@NotNull BufferedReader br) throws IOException {
		String line;
		List<String> res = new ArrayList<>(2);
		while (true) {
			line = br.readLine();
			if (line == null || pageEnds(line)) return res;
			else if (line.equals("===Noun===")) {
				while ((line = br.readLine()) != null) {
					if (pageEnds(line)) {
						return res;
					} else if (line.startsWith("{{") || !line.startsWith("#")) {
						// do nothing
					} else {
						while (line.startsWith("#")) {
							String desc
									= (line.startsWith("#:") ? line.substring(2) : line.substring(1))
											.trim();
							res.add(desc);
							line = br.readLine();
							if (line == null || pageEnds(line)) {
								throw new RuntimeException("EOF or missed </page>");
							}
						}
						return res;
					}
				}
			}
		}
	}

	@Nullable
	static String expectTitleOrLeavePage(@NotNull BufferedReader br) throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			String title;
			if ((title = parseTitle(line)) != null) { // tags on different lines
				return title;
			} else if (pageEnds(line)) {
				return null;
			}
		}
		return null;
	}

	static boolean pageEnds(@NotNull String line) {
		return line.matches(".*</page>.*");
	}

	static String parseTitle(@NotNull String line) {
		// "    <title>free</title> "
		if (!line.matches(".*<title>[a-zA-Z]+</title>.*")) {
			return null;
		} else {
			if (line.contains("</page>")) return null;
			String titleBegin = "<title>";
			int i = line.indexOf(titleBegin);
			if (i >= 0) {
				int j = line.indexOf("</title>", i);
				if (j >= i) {
					return line.substring(i + titleBegin.length(), j);
				}

			}
			return null;
		}
	}

	static boolean goIntoPage(@NotNull BufferedReader br) throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			if (line.contains("<page>")) {
				return true;
			}
		}
		return false;
	}
}
