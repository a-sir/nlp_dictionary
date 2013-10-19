package util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author A.Sirenko
 * Date: 7/28/13
 */
public class TestUtilsTest {

	@Test
	public void testSetComparison() {
		Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
		assertTrue(TestUtils.setEquals(set, 2, 3, 1));
		assertFalse(TestUtils.setEquals(set, 2, 3));
		assertFalse(TestUtils.setEquals(null, 2, 3));
		assertFalse(TestUtils.setEquals(set));
	}
}
