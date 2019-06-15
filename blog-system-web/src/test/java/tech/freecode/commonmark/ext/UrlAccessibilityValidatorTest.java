package tech.freecode.commonmark.ext;

import org.junit.Test;
import tech.freecode.commonmark.ext.url.accessibility.Url;
import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidator;
import tech.freecode.commonmark.ext.url.accessibility.internal.UrlAccessValidatorImpl;

import static org.junit.Assert.assertTrue;

public class UrlAccessibilityValidatorTest {
    private UrlAccessibilityValidator validator = new UrlAccessValidatorImpl("");
    @Test
    public void testLink(){
        assertTrue(validator.validate(new Url("www.baidu.com",null)).getStatus() == UrlAccessibilityValidator.ValidationResult.Status.OK);
    }
}
