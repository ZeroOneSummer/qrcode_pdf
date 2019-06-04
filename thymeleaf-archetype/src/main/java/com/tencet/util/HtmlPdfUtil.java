package com.tencet.util;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pijiang on 2019/6/4.
 * html(thymeleaf)转pdf工具类
 */
public class HtmlPdfUtil {

    public static void createPdf(String content,String path) throws DocumentException, FileNotFoundException {
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        try {
            //设置字体，否则不支持中文,在html中使用字体，html{ font-family: SimSun;}
            fontResolver.addFont("static/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderer.setDocumentFromString(content);
        renderer.layout();
        renderer.createPDF(new FileOutputStream(new File(path)));
        System.out.println("the html to pdf completed...");
    }
}