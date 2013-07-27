package wordforms;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author A.Sirenko
 * Date: 7/27/13
 */
public class Lemmatizer {

	private final @NotNull Multimap<String, String> lemmaToWFs;
	private final @NotNull Multimap<String, String> wfToLemmas;

	public Lemmatizer(@NotNull Set<Lemma> lemmas) {
		lemmaToWFs = HashMultimap.create();
		wfToLemmas = HashMultimap.create();
		for (Lemma lemma : lemmas) {
			for (WF wf : lemma.getWordforms()) {
				lemmaToWFs.put(lemma.getName(), wf.getName());
				wfToLemmas.put(wf.getName(), lemma.getName());
			}
		}
	}

	@Nullable
	public Set<String> getLemmas(@NotNull String wf) {
		Collection<String> lms = wfToLemmas.get(wf);
		return lms == null ? null : Collections.unmodifiableSet(new HashSet<>(lms));
	}

	@Nullable
	public String getLemmaOrNull(@NotNull String wf) {
		Set<String> lemmas = getLemmas(wf);
		if (lemmas == null || lemmas.size() > 1) {
			return null;
		}
		return lemmas.iterator().next();
	}

	public Set<String> getWordforms(@NotNull String lemma) {
		Collection<String> wfs = lemmaToWFs.get(lemma);
		return wfs == null ? null : Collections.unmodifiableSet(new HashSet<>(wfs));
	}

}
