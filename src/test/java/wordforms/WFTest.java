package wordforms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author A.Sirenko
 * Date: 7/27/13
 */
public class WFTest {

	@Test
	public void wordformsShouldBeComparedByName() {
		final String name1 = "somename1";
		final String name2 = "somename2";

		WF wf1 = new WF(name1);
		WF wf2 = new WF(name1);
		WF wf3 = new WF(name2);

		assertEquals(wf1, wf2);
		assertNotSame(wf1, wf3);

	}
}
