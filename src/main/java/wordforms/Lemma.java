package wordforms;

import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author A.Sirenko
 */
public class Lemma implements Serializable {

	private final @NotNull String name;

	private final @NotNull Set<WF> wordforms;

	public static @NotNull Lemma create(@NotNull String name, @NotNull String ... wordforms) {
		WF[] wfs = new WF[wordforms.length];
		for (int i = 0 ; i < wordforms.length ; ++i) {
			wfs[i] = new WF(wordforms[i]);
		}
		return new Lemma(name, wfs);
	}

	public Lemma(@NotNull String name, @NotNull WF ... wordforms) {
		this.name = name;
		this.wordforms = new HashSet<>(Arrays.asList(wordforms));
		this.wordforms.add(new WF(name));
	}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull Set<WF> getWordforms() {
		return Collections.unmodifiableSet(wordforms);
	}

	@Override
	public String toString() {
		return "Lemma{" +
				"name='" + name + '\'' +
				", wordforms=" + wordforms +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Lemma lemma = (Lemma) o;

		return name.equals(lemma.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
