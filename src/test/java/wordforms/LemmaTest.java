package wordforms;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author A.Sirenko
 * Date: 7/27/13
 */
public class LemmaTest {

	@Test
	public void lemmasShouldBeComparedBasedOnNames() {
		Lemma l1 = Lemma.create("l1", "wf1", "wf2");
		Lemma l2 = Lemma.create("l1", "wf3", "wf4");
		assertEquals(l1, l2);

		String l2Name = "l2";

		l2 = Lemma.create(l2Name, "wf1", "wf2");
		assertNotSame(l1, l2);

		assertEquals(l2.getName(), l2Name);
		Set<WF> wfs = l2.getWordforms();
		try {
			wfs.add(new WF(String.valueOf(System.currentTimeMillis())));
			fail();
		} catch (Exception ignored) {
		}

		assertEquals(3, wfs.size());
		assertTrue(wfs.contains(new WF("wf1")));
		assertTrue(wfs.contains(new WF("wf2")));
		assertTrue(wfs.contains(new WF(l2Name)));
	}

	@Test
	public void lemmaNameShouldBeInWordforms() {
		String l2Name = "l2";
		Lemma l2 = Lemma.create(l2Name, "wf1", "wf2");

		assertEquals(l2.getName(), l2Name);

		Set<WF> wfs = l2.getWordforms();
		try {
			wfs.add(new WF(String.valueOf(System.currentTimeMillis())));
			fail();
		} catch (Exception ignored) {
		}

		assertEquals(3, wfs.size());
		assertTrue(wfs.contains(new WF("wf1")));
		assertTrue(wfs.contains(new WF("wf2")));
		assertTrue(wfs.contains(new WF(l2Name)));
	}
}
