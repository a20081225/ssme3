package com.yw.e3.item.pojo;

import com.yw.e3.pojo.TbItem;

public class Item extends TbItem {

    public Item (TbItem tbItem){
        this.setId(tbItem.getId());
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setStatus(tbItem.getStatus());
        this.setTitle(tbItem.getTitle());
        this.setUpdated(tbItem.getUpdated());
    }

    public String[] getImages(){
        String image2 = this.getImage();
        if (image2 != null && !"".equals(image2)){
            return image2.split(",");
        }
        return null;
    }

}
