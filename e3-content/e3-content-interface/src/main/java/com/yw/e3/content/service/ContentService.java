package com.yw.e3.content.service;

import com.yw.e3.common.pojo.DataGridResult;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbContent;

import java.util.List;

public interface ContentService {
    E3Result addContent(TbContent content);
    List<TbContent> getContentListByCid(long cid);
    DataGridResult getContentList(long cid,int page, int rows);
    E3Result updateContent(TbContent content);
    E3Result deleteContent(String ids);

}
