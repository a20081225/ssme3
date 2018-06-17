package com.yw.e3.search.service.impl;

import com.yw.e3.common.pojo.SearchResult;
import com.yw.e3.search.dao.SearchDao;
import com.yw.e3.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品搜索
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String keywords, int page, int rows) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(keywords);
        if (page <= 0){
            page = 1;
        }
        //分页
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
        //默认
        solrQuery.set("df","item_title");
        //高亮
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePost("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePre("</em>");
        solrQuery.addHighlightField("item_title");
        //查询
        SearchResult result = searchDao.searchResult(solrQuery);
        long recourdCount = result.getRecourdCount();
        int totalPages = (int) Math.ceil(recourdCount/rows);
        result.setTotalPages(totalPages);
        return result;
    }
}
