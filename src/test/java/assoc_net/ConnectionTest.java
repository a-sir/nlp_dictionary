package assoc_net;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author A.Sirenko
 * Date: 7/28/13
 */
public class ConnectionTest {

	@Test
	public void countShouldBePositive() {
		new Connection("s", "r", 1);
		try {
			new Connection("s", "r", 0);
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void connectionsAreEqualBasedOnStimAndReact() {
		Connection c1 = new Connection("s", "r", 1);
		Connection c2 = new Connection("s", "r", 2);

		assertEquals(c1, c2);

		Connection c3 = new Connection("s", "r2", 2);
		assertNotSame(c1, c3);
	}

	@Test
	public void testMerge() {
		Connection c1 = new Connection("s", "r", 1);
		Connection c2 = new Connection("s", "r", 2);
		assertEquals(new Connection("s", "r", 3), c1.add(c2));
		try {
			c1.add(new Connection("s2", "r", 1));
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
}
