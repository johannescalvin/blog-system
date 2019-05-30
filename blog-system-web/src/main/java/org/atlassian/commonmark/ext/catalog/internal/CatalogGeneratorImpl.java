package org.atlassian.commonmark.ext.catalog.internal;

import org.atlassian.commonmark.ext.catalog.CatalogGenerator;
import org.atlassian.commonmark.ext.catalog.IdGenerator;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;

public class CatalogGeneratorImpl implements CatalogGenerator {
    private final IdGenerator idGenerator;

    public CatalogGeneratorImpl(String defaultId, String prefix, String suffix) {
        idGenerator = IdGenerator.builder()
                .defaultId(defaultId)
                .prefix(prefix)
                .suffix(suffix)
                .build();
    }

    @Override
    public String getCatalog(Node document) {
        StringBuilder builder = new StringBuilder();
        Heading heading = getFirstHeading(document);
        if (heading == null){
            return builder.toString();
        }

        builder.append("<ul class=\"catalog\">\n");

        while (heading != null){
            render(heading,builder);
            heading = getNextHeading(heading);
        }

        builder.append("</ul>\n" );

        return builder.toString();
    }

    private String convertToLinkItem(Heading heading){
        Text text = (Text)heading.getFirstChild();
        String title = text.getLiteral();

        String id = IdGenerator.removeSensitiveChars(title).trim().toLowerCase();
        id = idGenerator.generateId(id);

        String info = "<a href=\"#"+id+"\">"+title+"</a>";
        return info;
    }

    private void render(Heading heading,StringBuilder builder){
        if (!(heading != null
                && heading.getFirstChild() != null
                && heading.getFirstChild() instanceof Text)){
            return;
        }

        Heading nextHeading = getNextHeading(heading);
        if (nextHeading != null && nextHeading.getLevel() > heading.getLevel()){
            builder.append("<li>");
            builder.append(convertToLinkItem(heading) + "\n");
            builder.append("<ul class=\"catalog catalog-level-"+heading.getLevel()+"\">\n");

        }else if (nextHeading != null && nextHeading.getLevel() == heading.getLevel()){
            // 下一个标题是同级
            builder.append("<li>");
            builder.append(convertToLinkItem(heading));
            builder.append("</li>\n");
        }else if (nextHeading != null && nextHeading.getLevel() < heading.getLevel()){
            // 下一个标题上级
            builder.append("<li>");
            builder.append(convertToLinkItem(heading));
            builder.append("</li>\n");
            builder.append("</ul>\n");
            builder.append("</li>\n");
        }else if (nextHeading == null){
            // 没有标题了
            builder.append("<li>");
            builder.append(convertToLinkItem(heading));
            builder.append("</li>\n");
            for (int i = 1; i < heading.getLevel(); i++){

                builder.append("</ul>\n");
                builder.append("</li>\n");
            }
        }



    }

    private Heading getFirstHeading(Node document){
        if (document instanceof Heading){
            return (Heading)document;
        }

        Node node = document.getFirstChild();
        while (node != null){
            if (node instanceof Heading){
                Heading heading = (Heading) node;
                return heading;
            }
            node = node.getNext();
        }

        return null;
    }

    private Heading getNextHeading(Heading heading){
        if (heading == null || heading.getNext() == null){
            return null;
        }

        Node node = heading.getNext();
        while (node != null){
            if (node instanceof Heading){
                return (Heading)node;
            }
            node = node.getNext();
        }
        return null;
    }

}
