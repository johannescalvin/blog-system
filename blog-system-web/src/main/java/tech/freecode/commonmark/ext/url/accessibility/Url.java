package tech.freecode.commonmark.ext.url.accessibility;

import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Text;

public class Url {
    private String destination;
    private String title;

    public String getDestination() {
        return destination;
    }

    public String getTitle() {
        return title;
    }

    public Url(String destination, String title) {
        this.destination = destination;
        this.title = title;
    }

    public static Url of(String dest){
        return new Url(dest,null);
    }

    public static Url of(Link link){
        String destination = link.getDestination();
        String literal = null;
        Node child = link.getFirstChild();
        if (child != null && child instanceof Text){
             literal  = ((Text)child).getLiteral();
        }
        return new Url(destination,literal);
    }

    public static Url of(Image image){
        String dest = image.getDestination();
        String literal = null;
        Node child = image.getFirstChild();
        if (child != null && child instanceof Text){
            literal = ((Text) child).getLiteral();

        }
        return new Url(dest,literal);
    }

}
