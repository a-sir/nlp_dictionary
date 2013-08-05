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
	public void testParseOfTitle() {
		String line = "    <title>free</title> ";
		String title = WikitionaryParser.parseTitle(line);
		assertNotNull(title);
		assertEquals("free", title);

	}

	@Test
	public void testPageEnds() {
		assertTrue(WikitionaryParser.pageEnds("    </page> sdfdsf "));
		assertFalse(WikitionaryParser.pageEnds("    <page> sdfdsf "));
	}

	@Test
	public void testGoIntoPage() {
		String withPage = "dsdfsdfdsfsdf \n <page> sdf \n dsfdf";
		String withoutPage = "dsdfsdfdsfsdf \n </page> sdf \n dsfdf";
		try {
			assertTrue(WikitionaryParser.goIntoPage(TestUtils.createBufferedReader(withPage)));
			assertFalse(WikitionaryParser.goIntoPage(TestUtils.createBufferedReader(withoutPage)));
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void expectTitleOrLeavePage() {
		String withTitle = " dsfsdf\n kjasldkjlas <title>sdfsdf</title> \n sfdsf";
		String leavePageBeforeTitle = " dsfsdf\n </page> kjasldkjlas <title>sdfsdf</title> \n sfdsf";
		try {
			assertEquals("sdfsdf", WikitionaryParser.expectTitleOrLeavePage(TestUtils.createBufferedReader(withTitle)));
			assertNull(WikitionaryParser.expectTitleOrLeavePage(TestUtils.createBufferedReader(leavePageBeforeTitle)));
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testParseDescriptionOrLeavePage() {
		String desc1 = "A [[publication]], usually in the form of a [[book]], " +
				"that provides [[synonym]]s (and sometimes [[antonym]]s) " +
				"for the [[word]]s of a given [[language]].";
		String desc2 = "''&quot;Roget&quot; is the leading brand name for " +
				"a print English '''thesaurus''''' that lists " +
				"words under general concepts rather than just close synonyms.";
		String withDesc = "===Noun===\n" +
				"{{en-noun|thesauri|pl2=thesauruses}}\n\n" +
				"# " + desc1 + "\n" +
				"#: " + desc2 + "\n\n" +
				"</page>";

		String withoutDesc = " not a description\n </page>";
		try {
			List<String> res = WikitionaryParser.expectDescriptionOrLeavePage(TestUtils.createBufferedReader(withDesc));
			assertNotNull(res);
			assertEquals(2, res.size());
			assertEquals(desc1, res.get(0));
			assertEquals(desc2, res.get(1));

			res = WikitionaryParser.expectDescriptionOrLeavePage(TestUtils.createBufferedReader(withoutDesc));
			assertTrue(res.isEmpty());
		} catch (IOException e) {
			fail(e.toString());
		}

	}
}
