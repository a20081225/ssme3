package com.yw.e3.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.common.utils.IDUtils;
import com.yw.e3.mapper.TbItemDescMapper;
import com.yw.e3.mapper.TbItemMapper;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.pojo.TbItemDesc;
import com.yw.e3.pojo.TbItemExample;
import com.yw.e3.pojo.TbItemExample.Criteria;
import com.yw.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

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

    @Override
    public DataGridResult getItemList(int page, int rows) {
        //分页信息
        PageHelper.startPage(page,rows);
        //查询
        TbItemExample itemExample = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(itemExample);
        //返回值
        DataGridResult result = new DataGridResult();
        result.setRows(list);
        //分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        long itemId = IDUtils.genItemId();
        item.setId(itemId);
        item.setStatus((byte) 1);//1-正常，2-下架，3-删除
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insert(item);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insert(itemDesc);
        return E3Result.ok();
    }
}
