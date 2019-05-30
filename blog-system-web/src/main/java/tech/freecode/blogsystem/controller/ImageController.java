package tech.freecode.blogsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

@Controller
public class ImageController {
    @Value("${markdown-file-base:}")
    private String basePath;

    @GetMapping("/blog/**/*.svg")
    public void getSVG(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

        }
        response.setContentType("image/svg+xml");
        try{
            writeFile2Response(path,response.getOutputStream());
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @GetMapping("/blog/**/*.png")
    public void getPNG(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

        }

        response.setContentType("image/png");
        try{
            writeFile2Response(path,response.getOutputStream());
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void writeFile2Response(String filename, OutputStream outputStream) throws IOException{
        filename = basePath + filename;
        FileInputStream inputStream = new FileInputStream(new File(filename));
        int byteRead = -1;
        while ((byteRead = inputStream.read()) != -1){
            outputStream.write(byteRead);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
