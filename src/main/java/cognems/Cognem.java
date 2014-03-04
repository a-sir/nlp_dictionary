package cognems;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author A.Sirenko
 * Date: 8/4/13
 */
public class Cognem {

	private final static String DEFAULT_WIKITIONARY_PARSED = Cognem.class.getResource("/wikitionary.parsed.gz").getPath();

	public final String name;
	public final String sense;
	public final List<String> context;

	public Cognem(@NotNull String name, @NotNull String sense, @NotNull String[] context) {
		this.name = name;
		this.sense = sense;
		this.context = Collections.unmodifiableList(Arrays.asList(context));
	}

	@Override
	public String toString() {
		return "Cognem{" +
				"name='" + name + '\'' +
				", sense='" + sense + '\'' +
				", context=" + context +
				'}';
	}

    @NotNull
    public static List<Cognem> loadDefault() throws IOException {
        return read(new File(DEFAULT_WIKITIONARY_PARSED));
    }

    @NotNull
    private static List<Cognem> read(@NotNull File file) throws IOException {
        List<Cognem> cognems = new ArrayList<>();
        List<String> currentCognRaw = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(
            new GZIPInputStream(new FileInputStream(file))
        ));
        String line;
        while (true) {
            line = br.readLine();
            if (line == null || line.startsWith("# ")) {
                if (!currentCognRaw.isEmpty()) {
                    cognems.addAll(parse(currentCognRaw));
                }
                if (line == null) {
                    return cognems;
                }
                currentCognRaw = new ArrayList<>();
                currentCognRaw.add(line);
            } else {
                if (currentCognRaw.isEmpty()) {
                    throw new RuntimeException("Description of cognema was met before it's name");
                } else {
                    currentCognRaw.add(line);
                }
            }
        }
    }

    @NotNull
    static List<Cognem> parse(@NotNull List<String> lines) {
        if (!lines.get(0).startsWith("# ") || lines.size() < 2) {
            throw new RuntimeException("Wrong lines: " + lines);
        }
        String name = lines.get(0).substring(2);
        List<Cognem> res = new ArrayList<>();
        for (int i = 1 ; i <= lines.size() - 1; ++i) {
            int delim = lines.get(i).indexOf("|");
            String desc = lines.get(i).substring(delim + 1);
            String[] areas = lines.get(i).substring(0, delim).split(",");
            if (areas.length == 1 && areas[0].equals("---")) {
                areas = new String[0];
            }
            res.add(new Cognem(name, desc, areas));
        }
        return res;
    }

	public static class Builder {
		public final String name;
		public String sense;
		public String[] context;

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Builder setAttributes(@NotNull String sense, @Nullable String ... context) {
			if (sense.length() == 0) {
				throw new RuntimeException("Empty sense");
			}
			this.sense = sense;
			if (context != null) {
				this.context = new String[context.length];
				System.arraycopy(context, 0, this.context, 0, context.length);
			} else {
				this.context = new String[0];
			}
			return this;
		}

		@NotNull
		public Cognem build() {
			return new Cognem(name, sense, context);
		}
	}
}
