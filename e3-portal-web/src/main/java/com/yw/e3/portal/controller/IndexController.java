package com.yw.e3.portal.controller;

import com.yw.e3.content.service.ContentService;
import com.yw.e3.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 展示首页
 */
@Controller
public class IndexController {
    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;
    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    private String showindex(Model model){
        List<TbContent> adv1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
        model.addAttribute("adv1List",adv1List);
        return "index";
    }

    @RequestMapping("/page/logout")
    public String logout(){
        return "index";
    }
}
