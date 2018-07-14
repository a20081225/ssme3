package com.yw.e3.cart.service;

import com.yw.e3.common.jedis.JedisClient;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.common.utils.JsonUtils;
import com.yw.e3.mapper.TbItemMapper;
import com.yw.e3.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Autowired
    private TbItemMapper itemMapper;

    //添加商品，写入redis
    @Override
    public E3Result addCart(long userId, long itemId,int num) {
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, String.valueOf(itemId));
        if (hexists){//redis中存在商品
            String hget = jedisClient.hget(REDIS_CART_PRE + ":" + userId, String.valueOf(itemId));
            TbItem item = JsonUtils.jsonToPojo(hget, TbItem.class);
            item.setNum(item.getNum() + num);
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, String.valueOf(itemId),JsonUtils.objectToJson(item));
            return E3Result.ok();
        }else {//redis中不存在商品
            TbItem item = itemMapper.selectByPrimaryKey(itemId);
            item.setNum(num);
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)){
                item.setImage(image.split(",")[0]);
            }
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, String.valueOf(itemId),JsonUtils.objectToJson(item));
            return E3Result.ok();
        }
    }

    //合并购物车
    @Override
    public E3Result mergeCart(long userId, List<TbItem> itemList) {
        for (TbItem tbItem : itemList) {
            addCart(userId,tbItem.getId(),tbItem.getNum());
        }
        return E3Result.ok();
    }

    //获取购物车列表
    @Override
    public List<TbItem> getCartList(long userId) {
        List<String> list = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String s : list) {
            TbItem tbItem = JsonUtils.jsonToPojo(s,TbItem.class);
            itemList.add(tbItem);
        }
        return itemList;
    }

    @Override
    public E3Result updateCartNum(long userId, long itemId, int num) {
        String hget = jedisClient.hget(REDIS_CART_PRE + ":" + userId, String.valueOf(itemId));
        TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
        tbItem.setNum(num);
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, String.valueOf(itemId),JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId,String.valueOf(itemId));
        return E3Result.ok();
    }


}
