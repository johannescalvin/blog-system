package tech.freecode.commonmark.ext;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import tech.freecode.commonmark.ext.languages.LanguageVisitor;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class LanguageVisitorTest {

    @Test
    public void testLanguages(){
        String markdown = "# seq\n" +
                "\n" +
                "```seq\n" +
                "    A->B: Does something\n" +
                "```\n" +
                "\n" +
                "```java\n" +
                "int i = 1;\n" +
                "```\n" +
                "\n" +
                "```bash\n" +
                "echo $pwd\n" +
                "```";
        Parser parser = Parser.builder().build();
        LanguageVisitor languageVisitor = new LanguageVisitor();
        Node document = parser.parse(markdown);
        document.accept(languageVisitor);
        Set<String> languages = languageVisitor.getLanguages();
        Set<String> expected = new HashSet<>(Arrays.asList("seq","java","bash"));

        assertEquals(expected,languages);


    }
}
