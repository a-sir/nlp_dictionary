package synsets;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.LineProcessor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author A.Sirenko
 * Date: 8/22/13
 */
public class Loader {

    private static final String RESOURCE_PATH = "/princeton_wp_synsets";

	@NotNull
	public static Synsets read(@NotNull InputStream inputStream) throws IOException {
		Reader r = new Reader();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            r.processLine(line);
        }
		return new Synsets(r.getResult());
	}

    @NotNull
    public static Synsets readDefault() throws IOException {
        return read(Loader.class.getResourceAsStream(RESOURCE_PATH));
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
