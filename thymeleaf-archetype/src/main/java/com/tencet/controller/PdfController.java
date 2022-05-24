package com.tencet.controller;

import com.tencet.util.PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pdf/")
public class PdfController {

    @Autowired
    private TemplateEngine templateEngine;

    //模板参数
    final Map<String, Object> dataList = new HashMap<String, Object>(){{
        put("title", "录取通知书");
        put("companyName", "四方精创咨询股份有限公司");
        put("sendDate", new Date());
        put("employName", "李四");
        put("bgName", "开发");
        put("jobName", "JAVA工程师");
        put("salary", 2050);
        put("cycle", 14);
        put("subsidy", 400);
        put("contractYear", 3);
        put("contractMonth", 3);
        put("registerDate", "2022年6月1日");
        put("address", "深圳市南山区高新科技园101号A栋1602");
        put("extension", "112233");
        put("hrName", "陈小姐");
    }};

    @GetMapping("index")
    public ModelAndView toIndex(ModelAndView mv) {
        mv.addAllObjects(new HashMap<String, Object>(){{
            put("title", "主题");
            put("employName", "录用者姓名");
            put("companyName", "xx有限公司");
            put("sendDate", new Date());
        }});
        mv.setViewName("index2");
        return mv;
    }

    /**
     * pdf预览
     */
    @GetMapping(value = "/preview")
    public void preview(HttpServletResponse response) {
        PdfUtil.preview(templateEngine, "index", dataList, response);
    }

    /**
     * pdf渲染 + 下载
     */
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) {
        PdfUtil.download(templateEngine, "index", dataList, response, "录取通知书（测试版）.pdf");
    }
}
