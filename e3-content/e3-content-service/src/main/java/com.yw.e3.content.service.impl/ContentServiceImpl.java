package com.yw.e3.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yw.e3.common.jedis.JedisClient;
import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.common.utils.JsonUtils;
import com.yw.e3.content.service.ContentService;
import com.yw.e3.mapper.TbContentMapper;
import com.yw.e3.pojo.TbContent;
import com.yw.e3.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private JedisClient jedisClient;
    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        //缓存同步
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        return E3Result.ok();
    }

    /**
     * 根据分类查询内容列表
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentListByCid(long cid) {
        //缓存
        try {
            String hget = jedisClient.hget(CONTENT_LIST, String.valueOf(cid));
            if (StringUtils.isNotBlank(hget)){
                List<TbContent> contents = JsonUtils.jsonToList(hget, TbContent.class);
                return contents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> contents = contentMapper.selectByExampleWithBLOBs(contentExample);
        try {
            jedisClient.hset(CONTENT_LIST, String.valueOf(cid), JsonUtils.objectToJson(contents));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
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
        List<TbContent> contents = contentMapper.selectByExample(contentExample);
        for (TbContent content : contents) {
            jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        }
        contentMapper.deleteByExample(contentExample);
        return E3Result.ok();
    }
}
