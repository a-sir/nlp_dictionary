package assoc_net;

import org.jetbrains.annotations.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author A.Sirenko
 * Date: 7/28/13
 */
public class ConnSet {

	private final Set<Connection> conns;

	public static ConnSet EMPTY_SET = ConnSet.create();

	private Set<String> stims;
	private Set<String> reacts;

	public ConnSet(@NotNull Collection<Connection> data) {
		this.conns = new HashSet<>(data);
	}

	@NotNull
	public static ConnSet create(@NotNull Connection ... data) {
		return new ConnSet(Arrays.asList(data));
	}

	@NotNull
	public Set<String> getStims() {
		if (stims == null) {
			stims = new HashSet<>();
			for (Connection c : conns) {
				stims.add(c.getStim());
			}
		}
		return stims;
	}

	@NotNull
	public Set<String> getReacts() {
		if (reacts == null) {
			reacts = new HashSet<>();
			for (Connection c : conns) {
				reacts.add(c.getReak());
			}
		}
		return reacts;
	}

	@NotNull
	public Set<String> getStimsFor(@NotNull String react) {
		Set<String> res = new HashSet<>();
		for (Connection c : conns) {
			if (c.getReak().equals(react)) {
				res.add(c.getStim());
			}
		}
		return res;
	}

	@NotNull
	public Set<String> getReactsFrom(@NotNull String stim) {
		Set<String> res = new HashSet<>();
		for (Connection c : conns) {
			if (c.getStim().equals(stim)) {
				res.add(c.getReak());
			}
		}
		return res;
	}

	public boolean containsStim(@NotNull String stim) {
		return getStims().contains(stim);
	}

	public boolean containsReact(@NotNull String react) {
		return getReacts().contains(react);
	}
}
