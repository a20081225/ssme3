package com.yw.e3.controller;

import com.yw.e3.common.pojo.TreeNode;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCatController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<TreeNode> getItemCatList(@RequestParam(name = "id",defaultValue = "0")Long parentId){
        List<TreeNode> itemCatList = contentCategoryService.getContentCatList(parentId);
        return itemCatList;
    }

    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContentCategory(Long parentId,String name){
        E3Result e3Result = contentCategoryService.addContentCategory(parentId,name);
        return e3Result;
    }

    @RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public E3Result updateContentCategory(Long id,String name){
        E3Result e3Result = contentCategoryService.updateContentCategory(id,name);
        return e3Result;
    }

    @RequestMapping(value = "/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public E3Result delContentCategory(Long id){
        E3Result e3Result = contentCategoryService.delContentCategory(id);
        return e3Result;
    }
}
