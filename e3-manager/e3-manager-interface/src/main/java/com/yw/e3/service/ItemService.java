package com.yw.e3.service;

import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.pojo.TbItemDesc;

public interface ItemService {
    TbItem getItemById(Long itemId);
    DataGridResult getItemList(int page,int rows);
    E3Result addItem(TbItem item,String desc);
    E3Result updateItem(TbItem item,String desc);
    E3Result deleteItem(String ids);
    E3Result instockItem(String ids);
    E3Result reshelfItem(String ids);
    TbItemDesc getItemDescById(Long itemId);
}
