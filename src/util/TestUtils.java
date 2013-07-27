package util;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author A.Sirenko
 * Date: 7/27/13
 */
public class TestUtils {

	@SafeVarargs
	public static <T extends Comparable<T>> boolean setEquals(@Nullable Set<T> set, @Nullable T ... values) {
		if (set == null && values == null) {
			// equals values
			return true;
		} else if (set == null || values == null) {
			return false;
		} else {
			Set<T> expected = new HashSet<>(Arrays.asList(values));
			if(set.size() != expected.size()) {
				return false;
			}

			for (T v : set) {
				if (!expected.contains(v)) {
					return false;
				}
			}
		}
		return true;
	}

}
