package com.yw.e3.controller;

import com.yw.e3.common.pojo.TreeNode;
import com.yw.e3.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品目录管理Controller
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<TreeNode> getItemCatList(@RequestParam(name = "id",defaultValue = "0")Long parentId){
        List<TreeNode> itemCatList = itemCatService.getItemCatList(parentId);
        return itemCatList;
    }
}
