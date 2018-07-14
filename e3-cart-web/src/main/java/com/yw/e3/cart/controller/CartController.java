package com.yw.e3.cart.controller;

import com.yw.e3.cart.service.CartService;
import com.yw.e3.common.utils.CookieUtils;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.common.utils.JsonUtils;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.pojo.TbUser;
import com.yw.e3.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;
    @Autowired
    private CartService cartService;

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request,HttpServletResponse response){
        TbUser user = (TbUser) request.getAttribute("user");//判断
        if (user != null){//用户已登录
            cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }
        List<TbItem> cartList = getCartList(request);//从cookie中取
        boolean flag = false;
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()){
                flag = true;
                tbItem.setNum(tbItem.getNum() + num);
                break;
            }
        }
        if (!flag){//没找到商品
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)){
                tbItem.setImage(image.split(",")[0]);
            }
            cartList.add(tbItem);
        }
        //存入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        return "cartSuccess";
    }

    private List<TbItem> getCartList(HttpServletRequest request){
        String cart = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isBlank(cart)){
            return new ArrayList<>();
        }else {
            List<TbItem> list = JsonUtils.jsonToList(cart, TbItem.class);
            return list;
        }
    }

    //显示购物车列表
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response){
        //未登录状态
        List<TbItem> cartList = getCartList(request);
        //登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null){
            cartService.mergeCart(user.getId(),cartList);//cookie中购物车与服务端购物车合并
            CookieUtils.deleteCookie(request,response,"cart");
            cartList = cartService.getCartList(user.getId());

        }
        request.setAttribute("cartList",cartList);
        return "cart";
    }

    //更新购买商品数量
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletResponse response,HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");//登录状态
        if (user != null){
            cartService.updateCartNum(user.getId(),itemId,num);
            return E3Result.ok();
        }
        List<TbItem> cartList = getCartList(request);//从cookie中取
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()){
                tbItem.setNum(num);
                break;
            }
        }
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        return E3Result.ok();
    }

    //删除
    @RequestMapping("/cart/delete/${itemId}")
    public String deleteCart(@PathVariable Long itemId,
                                  HttpServletResponse response,HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");//登录状态
        if (user != null){
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        List<TbItem> cartList = getCartList(request);//从cookie中取
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()){
                cartList.remove(tbItem);
                break;
            }
        }
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        return "redirect:/cart/cart.html";//重新跳转到页面
    }
}
