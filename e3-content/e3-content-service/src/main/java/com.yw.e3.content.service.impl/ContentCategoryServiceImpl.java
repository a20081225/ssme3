package com.yw.e3.content.service.impl;

import com.yw.e3.common.pojo.TreeNode;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.content.service.ContentCategoryService;
import com.yw.e3.mapper.TbContentCategoryMapper;
import com.yw.e3.pojo.TbContentCategory;
import com.yw.e3.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yw.e3.pojo.TbContentCategoryExample.Criteria;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<TreeNode> getContentCatList(long parentId) {
        TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
        Criteria criteria = contentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> contentCategorys = contentCategoryMapper.selectByExample(contentCategoryExample);
        List<TreeNode> list = new ArrayList<>();
        for (TbContentCategory contentCategory : contentCategorys) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(contentCategory.getId());
            treeNode.setText(contentCategory.getName());
            treeNode.setState(contentCategory.getIsParent()?"closed":"open");
            list.add(treeNode);
        }
        return list;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setStatus(1);//1正常，2删除
        contentCategory.setSortOrder(1);//默认
        contentCategory.setIsParent(false);//新增节点为叶子节点
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategoryMapper.insert(contentCategory);
        TbContentCategory parentContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parentContentCategory.getIsParent()){
            parentContentCategory.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
        }
        return E3Result.ok(contentCategory);
    }

    @Override
    public E3Result updateContentCategory(long id, String name) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return null;
    }

    @Override
    public E3Result delContentCategory(long id) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if (!contentCategory.getIsParent()) {
            Long parentId = contentCategory.getParentId();
            TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
            Criteria criteria = contentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            long count = contentCategoryMapper.countByExample(contentCategoryExample);
            if (count == 0) {
                TbContentCategory parentContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
                parentContentCategory.setIsParent(false);
                contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
            }
            contentCategoryMapper.deleteByPrimaryKey(id);
            return E3Result.ok();
        }else {
            return E3Result.build(100,"请先删除子节点");
        }
    }
}
