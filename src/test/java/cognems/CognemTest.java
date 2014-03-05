package cognems;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * @author A.Sirenko
 * Date: 3/3/14
 */
public class CognemTest {

    @Test
    public void shouldParseCorrectSequenceOfLines() {
        List<String> lines = new ArrayList<>();
        lines.add("# dictionary");
        lines.add("---\tA reference work");
        lines.add("hurling,lang=en\tThe usual");
        List<Cognem> cognems = Cognem.parse(lines);

        assertEquals(2, cognems.size());
        Cognem c1 = cognems.get(0);
        Cognem c2 = cognems.get(1);
        assertTrue(
                "dictionary".equals(c1.name)
                &&"A reference work".equals(c1.sense)
                && c1.context.isEmpty()
        );
        assertTrue(
                "dictionary".equals(c2.name)
                        && "The usual".equals(c2.sense)
        );
        assertTrue("hurling".equals(c2.context.get(0)) && "lang=en".equals(c2.context.get(1)));
    }

    @Test
    public void shouldThrowExceptionIfNotEnoughDataToParse() {
        try {
            List<String> data = new ArrayList<>();
            data.add("# name");
            Cognem.parse(data);
            fail();
        } catch (RuntimeException ignored) {
        }

        try {
            List<String> data = new ArrayList<>();
            data.add("---\tarea");
            Cognem.parse(data);
            fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testDefaultResource() {
        try {
            List<Cognem> data = Cognem.loadDefault();
            assertNotNull(data);
            assertTrue(data.size() == 1117697);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
