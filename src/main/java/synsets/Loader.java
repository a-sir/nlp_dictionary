package synsets;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author A.Sirenko
 * Date: 8/22/13
 */
public class Loader {

    private static final String PATH = Loader.class.getResource("/princeton_wp_synsets").getPath();

	@NotNull
	public static Synsets read(@NotNull Path path) throws IOException {
		Reader r = new Reader();
		Files.readLines(path.toFile(), Charset.forName("UTF-8"), r);
		return new Synsets(r.getResult());
	}

    @NotNull
    public static Synsets readDefault() throws IOException {
        return read(Paths.get(PATH));
    }

	private static class Reader implements LineProcessor<List<String[]>> {

		private Multimap<String, String> map = HashMultimap.create();

		private  List<String[]> result;

		@Override
		public boolean processLine(String s) throws IOException {

			String key = s.substring(0, s.indexOf('\t'));
			String syn = s.substring(s.lastIndexOf('\t') + 1);
			map.put(key, syn);

			return true;
		}

		@Override
		public List<String[]> getResult() {
			if (result == null) {
				List<String[]> res = new ArrayList<>(map.keySet().size());
				for (String k : map.keySet()) {
					Collection<String> c  = map.get(k);
					String[] arr = new String[c.size()];
					int i = 0;
					for (String value : c) {
						arr[i] = value;
						i++;
					}
					res.add(arr);
				}
				result = res;
				map = null;
			}
			return result;
		}
	}

}
