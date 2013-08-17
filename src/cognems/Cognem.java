package cognems;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

	public static class CognemBuilder {
		public final String name;
		public String sense;
		public String[] context;


		public CognemBuilder(@NotNull String name) {
			this.name = name;
		}

		public CognemBuilder setAttributes(@NotNull String sense, @NotNull String ... context) {
			if (sense.length() == 0) {
				throw new RuntimeException("Empty sense");
			}
			this.sense = sense;
			this.context = new String[context.length];
			System.arraycopy(context, 0, this.context, 0, context.length);
			return this;
		}

		@NotNull
		public Cognem build() {
			return new Cognem(name, sense, context);
		}
	}
}
