package com.tencet.util;

import com.lowagie.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Map;

@Slf4j
public class PdfUtil {

    /**
     * pdf下载
     * @param templateEngine 配置
     * @param templateName   模板名称
     * @param data           模板参数集
     * @param fileName      下载文件名称(带文件扩展名后缀)
     */
    public static void download(TemplateEngine templateEngine,
                                String templateName,
                                Map<String, Object> data,
                                HttpServletResponse response,
                                String fileName) {
        // 设置编码、文件ContentType类型、文件头、下载文件名
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" +
                    new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        try (ServletOutputStream out = response.getOutputStream()) {
            createPDF(templateEngine, templateName, out, data);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * pdf预览
     * @param templateEngine  配置
     * @param templateName   模板名称
     * @param data           模板参数集
     * @param response       HttpServletResponse
     */
    public static void preview(TemplateEngine templateEngine,
                               String templateName,
                               Map<String, Object> data,
                               HttpServletResponse response) {
        try (ServletOutputStream out = response.getOutputStream()) {
            createPDF(templateEngine, templateName, out, data);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 生成pdf文档
     * @param templateName  模板名称
     * @param data          模板参数
     */
    private static void createPDF(TemplateEngine templateEngine,
                                  String templateName, OutputStream out,
                                  Map<String, Object> data) throws Exception {
        if (CollectionUtils.isEmpty(data)) {
            log.warn("警告: 模板参数为空!");
            return;
        }
        ITextRenderer renderer = new ITextRenderer();
        //设置字符集(宋体), 此处必须与模板中的<body style="font-family: SimSun">一致, 区分大小写, 不能写成汉字"宋体"
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //设置系统字体, 否则不支持中文
        fontResolver.addFont("static/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont("static/font/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Document pageDoc = generateDoc(templateEngine, templateName, data);
        renderer.setDocument(pageDoc, null);
        //展现和输出pdf
        renderer.layout();
        renderer.createPDF(out, false);
        //写下一个pdf页面
        //renderer.writeNextDocument();
        //完成pdf写入
        renderer.finishPDF();
    }

    /**
     * 模板+参数 -> html字符串 -> Document
     */
    private static Document generateDoc(TemplateEngine templateEngine,
                                        String templateName,
                                        Map<String, Object> data) {
        // 声明一个上下文对象，里面放入要存到模板里面的数据
        final Context context = new Context();
        context.setVariables(data);
        StringWriter stringWriter = new StringWriter();
        try (BufferedWriter writer = new BufferedWriter(stringWriter)) {
            templateEngine.process(templateName, context, writer);
            writer.flush();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(stringWriter.toString().getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}