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

}
