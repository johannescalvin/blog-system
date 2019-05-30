package org.atlassian.commonmark.ext.url.accessibility;

import org.commonmark.Extension;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;

public class LinkAccessabilityExtension  implements HtmlRenderer.HtmlRendererExtension {

    public static Extension create() {
        return  new LinkAccessabilityExtension();
    }


    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(new HtmlNodeRendererFactory() {
            @Override
            public NodeRenderer create(HtmlNodeRendererContext context) {
                return new UrlAccessibilityDectector();
            }
        });
    }
}
