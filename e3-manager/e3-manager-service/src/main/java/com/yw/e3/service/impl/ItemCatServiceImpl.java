package com.yw.e3.service.impl;

import com.yw.e3.common.pojo.TreeNode;
import com.yw.e3.mapper.TbItemCatMapper;
import com.yw.e3.pojo.TbItemCat;
import com.yw.e3.pojo.TbItemCatExample;
import com.yw.e3.pojo.TbItemCatExample.Criteria;
import com.yw.e3.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类管理Service
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<TreeNode> getItemCatList(long parentId) {
        TbItemCatExample itemCatExample = new TbItemCatExample();
        Criteria criteria = itemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> itemCats = itemCatMapper.selectByExample(itemCatExample);
        List<TreeNode> list = new ArrayList<>();
        for (TbItemCat itemCat : itemCats) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(itemCat.getId());
            treeNode.setText(itemCat.getName());
            treeNode.setState(itemCat.getIsParent()?"closed":"open");
            list.add(treeNode);
        }
        return list;
    }
}
