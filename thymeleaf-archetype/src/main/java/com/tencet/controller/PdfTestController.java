package com.tencet.controller;

import com.tencet.util.HtmlPdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class PdfTestController {

	private static String IMG_PATH = "/img/logo.jpg";
	private static String HTML_PATH = "/prview";
	private static String DES_PATH = "";

	@Autowired
	private TemplateEngine templateEngine;

	@GetMapping("/index.html")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "index";
	}

	/**
     * 预览
	 * @param request
     * @param response
     * @return
     */
	@GetMapping("/prview")
    public String prviewPdf(HttpServletRequest request, HttpServletResponse response){
		this.addData(request, response);
		return "prview";
    }

    /**
	 * pdf下载
	 */
	@GetMapping("/download")
	@ResponseBody
	public void downPdf(HttpServletRequest request, HttpServletResponse response){
		try {
			DES_PATH = this.getClass().getClassLoader().getResource("download").getPath() + File.separator + "rs.pdf";
			String htmlcontext = this.addData(request, response);
			HtmlPdfUtil.createPdf(htmlcontext, DES_PATH);
			this.downloadPDF(request, response, new File(DES_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//数据封装&页面渲染
	private String addData(HttpServletRequest request, HttpServletResponse response){
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
		ctx.setVariable("imgUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + IMG_PATH);
		ctx.setVariable("title", "[安迪苏生命科学制品（上海）有限公司]");
		ctx.setVariable("message", "【编号9528】");
		ctx.setVariable("name", "z.o.s");
		return templateEngine.process(HTML_PATH, ctx);
	}

	public void downloadPDF(HttpServletRequest request, HttpServletResponse response, File file){
		BufferedInputStream bis=null;
		ServletOutputStream out = null;
				String fileName = null;
		try {
			out = response.getOutputStream();
			fileName = new String("result.pdf".getBytes("UTF-8"), "iso-8859-1");
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			bis=new BufferedInputStream(new FileInputStream(file));
			byte[] b=new byte[bis.available()+1000];
			int i=0;
			while((i=bis.read(b))!=-1) {
				out.write(b, 0, i);
			}
			out.flush();
			out.close();
			System.out.println("pdf download completed...");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
