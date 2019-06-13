package tech.freecode.commonmark.ext.languages;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

public class LanguagesUtils  {


    public static void main(String[] args) {
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

        System.out.println(languageVisitor.getLanguages());
    }
}
