package cognems;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author A.Sirenko
 * Date: 8/4/13
 */
public class WiktionaryParseToolTest {

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
		List<Cognem> res = WiktionaryParseTool.parseDescription("somename", text);
		assertNotNull(res);
		assertEquals(2, res.size());
		assertEquals(l1.replace("[[", "").replace("]]", ""), res.get(0).sense);
		assertEquals(l2, res.get(1).sense);
	}

	@Test
	public void testRemoveMarkupFromDescription() {
		Cognem.Builder builder = new Cognem.Builder("someCognem");
		assertNull(WiktionaryParseTool.parseLine("ab '''{{NUMBEROFARTICLES}}''' c", builder));
		Cognem c = WiktionaryParseTool.parseLine("ab [[multilingual]] c", builder);
		assertNotNull(c);
		assertEquals("ab multilingual c", c.sense);

		c = WiktionaryParseTool.parseLine("even [[Help:Starting a new page|create a page]] for a term", builder);
		assertNotNull(c);
		assertEquals("even create a page for a term", c.sense);
	}

	@Test
	public void testParsingOfContext() {
		Cognem.Builder builder = new Cognem.Builder("someCognem");
		Cognem c = WiktionaryParseTool.parseLine("{{context|colloquial|lang=en}} The Atlantic Ocean.", builder);
		assertNotNull(c);
		assertEquals("The Atlantic Ocean.", c.sense);
		assertEquals(2, c.context.size());
		assertEquals("colloquial", c.context.get(0));
		assertEquals("lang=en", c.context.get(1));
	}
}