package com.yw.e3.search.message;

import com.yw.e3.common.pojo.SearchItem;
import com.yw.e3.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 商品添加监听
 */
public class ItemAddMessageListener implements MessageListener {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrClient solrClient;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            Thread.sleep(1000);//等待事务提交
            SearchItem item = itemMapper.getItemById(itemId);
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id",item.getId());
            document.addField("item_title",item.getTitle());
            document.addField("item_sell_point",item.getSell_point());
            document.addField("item_price",item.getPrice());
            document.addField("item_image",item.getImage());
            document.addField("item_category_name",item.getCategory_name());
            solrClient.add(document);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
