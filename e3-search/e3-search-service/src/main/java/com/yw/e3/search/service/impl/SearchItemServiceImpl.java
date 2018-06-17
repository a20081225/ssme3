package com.yw.e3.search.service.impl;

import com.yw.e3.common.pojo.SearchItem;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.search.mapper.ItemMapper;
import com.yw.e3.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 索引库维护Service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrClient solrClient;

    @Override
    public E3Result importAllItems() {
        List<SearchItem> itemList = itemMapper.getItemList();
        try {
            for (SearchItem item : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id",item.getId());
                document.addField("item_title",item.getTitle());
                document.addField("item_sell_point",item.getSell_point());
                document.addField("item_price",item.getPrice());
                document.addField("item_image",item.getImage());
                document.addField("item_category_name",item.getCategory_name());
                solrClient.add(document);
            }
            solrClient.commit();
            return E3Result.ok();
        } catch (SolrServerException e) {
            e.printStackTrace();
            return E3Result.build(500,"Solr发生异常");
        } catch (IOException e) {
            e.printStackTrace();
            return E3Result.build(500,"导入失败");
        }
    }
}
