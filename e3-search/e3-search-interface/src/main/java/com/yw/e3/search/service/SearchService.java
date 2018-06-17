package com.yw.e3.search.service;

import com.yw.e3.common.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String keywords,int page,int rows);
}
