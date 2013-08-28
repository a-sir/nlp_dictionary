package synsets;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author A.Sirenko
 * Date: 8/29/13
 */
public class Synsets {

	private final Map<String, Integer> lemmaToSynset;

	private final List<String[]> synsets;

	public Synsets(@NotNull List<String[]> synsets) {
		this.synsets = Collections.unmodifiableList(new ArrayList<>(synsets));

		Set<String> duplicates = new HashSet<>();
		lemmaToSynset = new HashMap<>();

		for (int i = 0; i < synsets.size(); i++) {
			String[] set = synsets.get(i);
			if (set.length > 1) {
				for (String lemma : set) {
					if (!duplicates.contains(lemma)) {
						if (lemmaToSynset.containsKey(lemma)) {
							duplicates.add(lemma);
							lemmaToSynset.remove(lemma);
						} else {
							lemmaToSynset.put(lemma, i);
						}
					}
				}
			}
		}
	}

	private final String[] EMPTY_SYNSET = new String[0];

	@NotNull
	public String[] getSyms(@NotNull String lemma) {
		if (lemmaToSynset.containsKey(lemma)) {
			return synsets.get(lemmaToSynset.get(lemma));
		} else {
			return EMPTY_SYNSET;
		}
	}

	public int size() {
		return lemmaToSynset.size();
	}
}
