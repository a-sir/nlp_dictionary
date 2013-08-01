package assoc_net;

import org.junit.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.*;

/**
 * @author A.Sirenko
 * Date: 7/28/13
 */
public class AssociativeNetTest {

	@Test
	public void processingOfRawSourceShouldLeaveMaxConnections() {
		List<Connection> conns = new ArrayList<>();
		conns.add(new Connection("s", "r", 1));
		conns.add(new Connection("s", "r", 3));
		conns.add(new Connection("s", "r2", 1));
		Set<Connection> set = AssociativeNet.maxUniqueFromSourceWithDuplicates(conns);
		assertEquals(2, set.size());
		assertTrue(set.contains(new Connection("s", "r", 3)));
		assertTrue(set.contains(new Connection("s", "r2", 1)));
	}

	@Test
	public void mergeShouldAddCounts() {
		List<Connection> conns = new ArrayList<>();
		conns.add(new Connection("s", "r", 1));
		conns.add(new Connection("s", "r2", 1));

		AssociativeNet.summarizeDuplicates(conns, new Connection("s", "r", 3));
		assertEquals(2, conns.size());
		assertTrue(conns.get(1).equals(new Connection("s", "r", 4)));
		assertTrue(conns.get(0).equals(new Connection("s", "r2", 1)));
	}

	@Test
	public void testDefaultNet() {
		try {
			AssociativeNet net = AssociativeNet.loadDefaultNet();
			assertEquals(4_994, net.getStims().size());
			assertEquals(9_975, net.getReacts().size());

			TestUtils.setEquals(
					net.getConnsForStim("a").getReacts(),
					"b", "alphabet", "the", "grade", "letter", "plus", "an", "z",
					"and", "apple", "great", "one", "word");
			TestUtils.setEquals(net.getConnsForReact("anteater").getStims(), "aardvark", "ant");
		} catch (IOException e) {
			fail();
		}
	}
}
