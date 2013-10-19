package wordforms;

import org.junit.Test;
import util.TestUtils;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * @author A.Sirenko
 * Date: 7/27/13
 */
public class LemmatizerTest {

	@Test
	public void testGettingLemmasWithDefaultData() {
		try{
			Lemmatizer lemmatizer = new Lemmatizer(Loader.loadDefaultSet());
			assertTrue(TestUtils.setEquals(lemmatizer.getLemmas("being"), "be", "being"));
			assertTrue(TestUtils.setEquals(lemmatizer.getLemmas("art"), "be", "art"));
			assertTrue(TestUtils.setEquals(lemmatizer.getLemmas("not_existed")));

			// cause 2 possible lemmas
			assertNull(lemmatizer.getLemmaOrNull("art"));
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testGettingWFsWithDefaultData() {
		try{
			Lemmatizer lemmatizer = new Lemmatizer(Loader.loadDefaultSet());
			assertTrue(TestUtils.setEquals(lemmatizer.getWordforms("between"), "between", "tween"));
			assertTrue(TestUtils.setEquals(lemmatizer.getWordforms("not_existed")));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
