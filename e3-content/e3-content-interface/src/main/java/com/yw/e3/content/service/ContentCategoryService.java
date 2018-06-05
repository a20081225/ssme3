package com.yw.e3.content.service;

import com.yw.e3.common.pojo.TreeNode;
import com.yw.e3.common.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {
    List<TreeNode> getContentCatList(long parentId);
    E3Result addContentCategory(long parentId,String name);
    E3Result updateContentCategory(long id,String name);
    E3Result delContentCategory(long id);
}
