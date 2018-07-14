package com.yw.e3.cart.service;

import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbItem;

import java.util.List;

public interface CartService {
        E3Result addCart(long userId,long itemId,int num);
        E3Result mergeCart(long userId, List<TbItem> itemList);
        List<TbItem> getCartList(long userId);
        E3Result updateCartNum(long userId,long itemId,int num);
        E3Result deleteCartItem(long userId,long itemId);
}
