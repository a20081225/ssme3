package com.yw.e3.service;

import com.yw.e3.common.pojo.TreeNode;

import java.util.List;

public interface ItemCatService {
    List<TreeNode> getItemCatList(long parentId);
}
