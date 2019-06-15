package tech.freecode.commonmark.ext.url.accessibility;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Link;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LinkVisitor extends AbstractVisitor {

    private List<Link> links = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    @Override
    public void visit(Link link) {
        super.visit(link);
        links.add(link);
    }
}
