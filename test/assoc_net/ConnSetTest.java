package assoc_net;

import org.junit.Test;

import static org.junit.Assert.*;
import static util.TestUtils.*;

/**
 * @author A.Sirenko
 * Date: 8/2/13
 */
public class ConnSetTest {

	@Test
	public void testConnSet() {
		ConnSet set = ConnSet.create(
				new Connection("s1", "r1", 5),
				new Connection("s1", "r2", 3),
				new Connection("s2", "r3", 4)
		);

		setEquals(set.getReacts(), "r1", "r2", "r3");
		setEquals(set.getStims(), "s1", "s2");

		setEquals(set.getReacts(), "r1", "r2", "r3");
		setEquals(set.getStims(), "s1", "s2");

		setEquals(set.getReactsFrom("s3"));
		setEquals(set.getReactsFrom("s1"), "r1", "r2");

		setEquals(set.getStimsFor("r3"), "s2");
		setEquals(set.getStimsFor("r4"));

		assertTrue(set.containsStim("s1"));
		assertFalse(set.containsStim("s3"));

		assertTrue(set.containsReact("r1"));
		assertFalse(set.containsReact("r4"));

	}
}
