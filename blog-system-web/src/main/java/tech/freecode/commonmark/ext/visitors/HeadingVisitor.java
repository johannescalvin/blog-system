package tech.freecode.commonmark.ext.visitors;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HeadingVisitor extends AbstractVisitor {
    private LinkedHashMap<Integer,List<String>> titles = new LinkedHashMap<>();
    @Override
    public void visit(Heading heading) {
        super.visit(heading);
        int level = heading.getLevel();
        if (heading.getFirstChild() instanceof Text){
            String title = ((Text) heading.getFirstChild()).getLiteral();
            if (title != null && title.trim().length() != 0){
                List<String> list = titles.getOrDefault(level,new ArrayList<>());
                list.add(title);
                titles.put(level,list);
            }
        }
    }

    public Map<Integer, List<String>> getTitles() {
        return titles;
    }
}
