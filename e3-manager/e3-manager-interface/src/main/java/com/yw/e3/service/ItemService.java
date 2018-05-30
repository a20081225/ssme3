package com.yw.e3.service;

import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbItem;

public interface ItemService {
    TbItem getItemById(Long itemId);
    DataGridResult getItemList(int page,int rows);
    E3Result addItem(TbItem item,String desc);
}
