package com.yw.e3.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.content.service.ContentService;
import com.yw.e3.mapper.TbContentMapper;
import com.yw.e3.pojo.TbContent;
import com.yw.e3.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 内容管理Service
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        return E3Result.ok();
    }

    /**
     * 根据分类查询内容列表
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentListByCid(long cid) {
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> contents = contentMapper.selectByExampleWithBLOBs(contentExample);
        return contents;
    }

    @Override
    public DataGridResult getContentList(long cid,int page, int rows) {
        PageHelper.startPage(page,rows);
        //查询
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExample(contentExample);
        //返回值
        DataGridResult result = new DataGridResult();
        result.setRows(list);
        //分页结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public E3Result updateContent(TbContent content) {
        contentMapper.updateByPrimaryKeySelective(content);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteContent(String ids) {
        String[] idArray = ids.split(",");
        List<Long> list = null;
        for (String s : idArray) {
            list.add(Long.valueOf(s));
        }
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andIdIn(list);
        contentMapper.deleteByExample(contentExample);
        return E3Result.ok();
    }
}
