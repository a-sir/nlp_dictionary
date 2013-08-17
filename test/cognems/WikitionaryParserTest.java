package cognems;

import org.junit.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author A.Sirenko
 * Date: 8/4/13
 */
public class WikitionaryParserTest {
	@Test
	public void testParseDescription() {
		String l1 = "A [[reference work]]";
		String l2 = "By extension, any work th";
		String text = "asdasd \n===Noun===\n" +
				"{{en-noun|dictionaries}}\n" +
				"#  " + l1 + "\n" +
				"#: " + l2 + "\n" +
				"* {{seeCites}}\n" +
				"====Synonyms====\n* [[wordbook]]";
		List<String> res = WikitionaryParser.parseDescription(text);
		assertNotNull(res);
		assertEquals(2, res.size());
		assertEquals(l1, res.get(0));
		assertEquals(l2, res.get(1));
	}

	@Test
	public void testRemoveMarkupFromDescription() {
		assertNull(WikitionaryParser.plainDescription("ab '''{{NUMBEROFARTICLES}}''' c"));
		assertEquals("ab multilingual c", WikitionaryParser.plainDescription("ab [[multilingual]] c"));

		assertEquals(
				"even create a page for a term",
				WikitionaryParser.plainDescription("even [[Help:Starting a new page|create a page]] for a term"));
	}
}