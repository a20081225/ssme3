package com.yw.e3.controller;

import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理Controller
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }

    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public DataGridResult getItemList(Integer page,Integer rows){
        DataGridResult result = itemService.getItemList(page, rows);
        return result;
    }

    /**
     * 商品添加
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item,String desc){
        E3Result result = itemService.addItem(item, desc);
        return result;
    }

    @RequestMapping(value = "/item/update",method = RequestMethod.POST)
    @ResponseBody
    public E3Result updateItem(TbItem item,String desc){
        E3Result result = itemService.updateItem(item, desc);
        return result;
    }

    @RequestMapping(value = "/item/delete",method = RequestMethod.POST)
    @ResponseBody
    public E3Result deleteItem(String ids){
        E3Result result = itemService.deleteItem(ids);
        return result;
    }

    @RequestMapping(value = "/item/instock",method = RequestMethod.POST)
    @ResponseBody
    public E3Result instockItem(String ids){
        E3Result result = itemService.instockItem(ids);
        return result;
    }

    @RequestMapping(value = "/item/reshelf",method = RequestMethod.POST)
    @ResponseBody
    public E3Result reshelfItem(String ids){
        E3Result result = itemService.reshelfItem(ids);
        return result;
    }
}
