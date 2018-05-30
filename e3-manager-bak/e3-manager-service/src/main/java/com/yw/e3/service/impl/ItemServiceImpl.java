package com.yw.e3.service.impl;

import com.yw.e3.mapper.TbItemMapper;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.pojo.TbItemExample;
import com.yw.e3.pojo.TbItemExample.Criteria;
import com.yw.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public TbItem getItemById(Long itemId) {
        //主键查询
//        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //条件查询
        TbItemExample itemExample =new TbItemExample();
        Criteria criteria = itemExample.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> items = itemMapper.selectByExample(itemExample);
        if (items !=null && items.size()>0){
            return items.get(0);
        }else {
            return null;
        }
    }
}
