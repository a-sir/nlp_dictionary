package assoc_net;

import org.jetbrains.annotations.NotNull;

/**
 * @author A.Sirenko
 * Date: 7/28/13
 */
public class Connection {

	private final @NotNull String stim;

	private final @NotNull String reak;

	private final int count;

	public Connection(@NotNull String stim, @NotNull String react, int count) {
		this.stim = stim;
		this.reak = react;
		if (count <= 0)
			throw new IllegalArgumentException(
					"Connection " + stim + " -> " + react + " should be > 0. count(" + count + ")");
		this.count = count;
	}

	@NotNull
	public Connection add(@NotNull Connection other) {
		if (this.equals(other)) {
			return new Connection(stim, reak, count + other.getCount());
		} else {
			throw new IllegalArgumentException("Can't add " + other + " to " + this);
		}
	}

	@NotNull
	public String getStim() {
		return stim;
	}

	@NotNull
	public String getReak() {
		return reak;
	}

	public int getCount() {
		return count;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Connection that = (Connection) o;

		if (!reak.equals(that.reak)) return false;
		if (!stim.equals(that.stim)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = stim.hashCode();
		result = 31 * result + reak.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Connection{" +
				"stim='" + stim + '\'' +
				", reak='" + reak + '\'' +
				", count=" + count +
				'}';
	}
}
