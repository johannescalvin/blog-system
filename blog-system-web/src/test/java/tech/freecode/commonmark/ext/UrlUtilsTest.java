package tech.freecode.commonmark.ext;

import org.junit.Test;
import tech.freecode.commonmark.ext.url.accessibility.Url;
import tech.freecode.commonmark.ext.url.accessibility.UrlUtils;

import static org.junit.Assert.*;

public class UrlUtilsTest {

    @Test
    public void testRelativePath(){
        String[] strs = new String[]{
                "../filename",
                "../../filename",
                "../../../filename",
                "../../.././filename",
                "../../../path-0/0123-sep_otherSep.suffix",
                "../../../path-0/file_name",
                "../../../path0/file-name",
                "../../../path0/path1/filename.suffix",
                "./filename",
                "./path/test.html",
                "./path/path1/test.html"
        };

        for (String dest : strs){
            assertTrue(UrlUtils.isRelativePath(new Url(dest,null)));
        }
    }


    @Test
    public void testAbsolutePath(){
        assertTrue(UrlUtils.isAbsolutePath(Url.of("/absolutepath")));
        assertTrue(UrlUtils.isAbsolutePath(Url.of("~/absolutepath")));
    }

    @Test
    public void testExternalUrl(){
        assertTrue(UrlUtils.isExternalUrl(Url.of("http://baidu.com")));
        assertTrue(UrlUtils.isExternalUrl(Url.of("http://www.baidu.com")));
        assertTrue(UrlUtils.isExternalUrl(Url.of("www.baidu.com")));
        assertTrue(UrlUtils.isExternalUrl(Url.of("https://baidu.com")));
        assertTrue(UrlUtils.isExternalUrl(Url.of("https://www.baidu.com")));
        assertTrue(UrlUtils.isExternalUrl(Url.of("www.baidu.com")));

        assertFalse(UrlUtils.isExternalUrl(Url.of("not_a_url")));
    }

    @Test
    public void testEmail(){
        assertFalse(UrlUtils.isEmailLink(new Url(null,null)));
        assertTrue(UrlUtils.isEmailLink(new Url("mailto:yangchengzhu2011@163.com",null)));
        assertTrue(UrlUtils.isEmailLink(new Url("yangchengzhu2011@163",null)) == false);

        assertFalse(UrlUtils.isEmailLink(null));
        assertFalse(UrlUtils.isEmailLink(new Url(null,null)));

    }

    @Test
    public void wellformTest(){
        assertEquals("http://www.baidu.com",UrlUtils.wellform(Url.of("http://www.baidu.com")).getDestination());
        assertEquals("http://www.baidu.com",UrlUtils.wellform(Url.of("www.baidu.com")).getDestination());
        assertEquals("https://www.baidu.com",UrlUtils.wellform(Url.of("https://www.baidu.com")).getDestination());
    }
}
