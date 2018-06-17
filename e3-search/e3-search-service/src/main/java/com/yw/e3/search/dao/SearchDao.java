package com.yw.e3.search.dao;

import com.yw.e3.common.pojo.SearchItem;
import com.yw.e3.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品查询Dao
 */
@Repository
public class SearchDao {
    @Autowired
    private SolrClient solrClient;

    public SearchResult searchResult(SolrQuery solrQuery){
        try {
            QueryResponse queryResponse =solrClient.query(solrQuery);
            SolrDocumentList solrDocumentList = queryResponse.getResults();
            long numFound = solrDocumentList.getNumFound();
            SearchResult result = new SearchResult();
            result.setRecourdCount(numFound);
            List<SearchItem> itemList = new ArrayList<>();
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            String title = "";
            for (SolrDocument solrDocument : solrDocumentList) {
                SearchItem item = new SearchItem();
                item.setId((String)solrDocument.get("id"));
                item.setSell_point((String)solrDocument.get("item_sell_point"));
                item.setPrice((Long) solrDocument.get("item_price"));
                item.setImage((String) solrDocument.get("item_image"));
                item.setCategory_name((String) solrDocument.get("item_category_name"));
                //高亮
                List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
                if (list!=null&&list.size()>0){
                    title = list.get(0);
                }else {
                    title = (String)solrDocument.get("item_title");
                }
                item.setTitle(title);
                itemList.add(item);
            }
            result.setItemList(itemList);
            return result;

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
