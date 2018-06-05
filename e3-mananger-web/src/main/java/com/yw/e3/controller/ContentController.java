package com.yw.e3.controller;

import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.content.service.ContentService;
import com.yw.e3.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 */
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContent(TbContent content){
        E3Result e3Result = contentService.addContent(content);
        return e3Result;
    }

    @RequestMapping("/content/query/list")
    @ResponseBody
    public DataGridResult getContentList(Long categoryId,Integer page, Integer rows){
        DataGridResult result = contentService.getContentList(categoryId,page, rows);
        return result;
    }

    @RequestMapping("/content/edit")
    @ResponseBody
    public E3Result updateContent(TbContent content) {
        E3Result e3Result = contentService.updateContent(content);
        return e3Result;
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public E3Result deleteContent(String ids) {
        E3Result e3Result = contentService.deleteContent(ids);
        return e3Result;
    }
}
