package org.atlassian.commonmark.ext.catalog;



import org.commonmark.node.Node;

public interface CatalogGenerator {

    String getCatalog(Node document);
}
