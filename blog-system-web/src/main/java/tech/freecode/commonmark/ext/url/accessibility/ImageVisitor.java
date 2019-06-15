package tech.freecode.commonmark.ext.url.accessibility;

import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.List;

public class ImageVisitor extends AbstractVisitor {

    private List<Image> images = new ArrayList<>();

    public List<Image> getImages() {
        return images;
    }

    @Override
    public void visit(Image image) {
        super.visit(image);
        images.add(image);
    }
}
