package wordforms;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author A.Sirenko
 * Date: 7/27/13
 */
public class WF implements Serializable {

	private final @NotNull String name;

	public WF(@NotNull String name) {
		this.name = name;
	}

	public @NotNull String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WF wf = (WF) o;

		if (!name.equals(wf.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "WF{" +
				"name='" + name + '\'' +
				'}';
	}
}
