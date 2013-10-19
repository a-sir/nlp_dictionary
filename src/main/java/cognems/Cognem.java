package cognems;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author A.Sirenko
 * Date: 8/4/13
 */
public class Cognem {

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
