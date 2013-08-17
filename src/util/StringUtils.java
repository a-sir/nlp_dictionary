package util;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author A.Sirenko
 * Date: 8/17/13
 */
public class StringUtils {

	@Nullable
	public static Pair<Integer, Integer> getBounds(
			@NotNull String string, int offset,
			@NotNull String openTag, String closeTag) {
		int openIndex = string.indexOf(openTag, offset);
		if (openIndex != -1) {
			int closeIndex = string.indexOf(closeTag, openIndex + openTag.length());
			if (closeIndex != -1) {
				return new Pair<>(openIndex, closeIndex);
			}
		}
		return null;
	}

}
