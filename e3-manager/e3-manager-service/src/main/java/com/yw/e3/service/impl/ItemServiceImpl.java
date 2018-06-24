package com.yw.e3.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yw.e3.common.jedis.JedisClient;
import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.common.utils.IDUtils;
import com.yw.e3.common.utils.JsonUtils;
import com.yw.e3.mapper.TbItemDescMapper;
import com.yw.e3.mapper.TbItemMapper;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.pojo.TbItemDesc;
import com.yw.e3.pojo.TbItemDescExample;
import com.yw.e3.pojo.TbItemExample;
import com.yw.e3.pojo.TbItemExample.Criteria;
import com.yw.e3.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
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
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    @Override
    public TbItem getItemById(Long itemId) {
        //主键查询
//        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //条件查询
        TbItemExample itemExample =new TbItemExample();
        Criteria criteria = itemExample.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> items = itemMapper.selectByExample(itemExample);
        if (items !=null && items.size()>0){

            try {
                jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(items.get(0)));//添加缓存
                jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);//设置过期时间
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        //添加商品
        final long itemId = IDUtils.genItemId();
        item.setId(itemId);
        item.setStatus((byte) 1);//1-正常，2-下架，3-删除
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insert(item);
        //商品描述
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insert(itemDesc);
        //发送消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(String.valueOf(itemId));
                return textMessage;
            }
        });
        return E3Result.ok();
    }

    @Override
    public E3Result updateItem(TbItem item, String desc) {
        Long itemId = item.getId();
        itemMapper.updateByPrimaryKeySelective(item);
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        itemDesc.setItemDesc(desc);
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteItem(String ids) {
        String[] idArray = ids.split(",");
        List<Long> list = null;
        for (String s : idArray) {
            list.add(Long.valueOf(s));
        }
        TbItemExample itemExample = new TbItemExample();
        Criteria criteria = itemExample.createCriteria();
        criteria.andIdIn(list);
        itemMapper.deleteByExample(itemExample);
        TbItemDescExample itemDescExample = new TbItemDescExample();
        TbItemDescExample.Criteria criteria2 = itemDescExample.createCriteria();
        criteria2.andItemIdIn(list);
        itemDescMapper.deleteByExample(itemDescExample);
        return E3Result.ok();
    }

    @Override
    public E3Result instockItem(String ids) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TbItem item = itemMapper.selectByPrimaryKey(Long.valueOf(id));
            item.setStatus((byte) 2);//1-正常，2-下架，3-删除
            itemMapper.updateByPrimaryKeySelective(item);
        }
        return E3Result.ok();
    }

    @Override
    public E3Result reshelfItem(String ids) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TbItem item = itemMapper.selectByPrimaryKey(Long.valueOf(id));
            item.setStatus((byte) 1);//1-正常，2-下架，3-删除
            itemMapper.updateByPrimaryKeySelective(item);
        }
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)){
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));//添加缓存
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);//设置过期时间
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
    }
}
