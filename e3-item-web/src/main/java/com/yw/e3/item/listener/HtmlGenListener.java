package com.yw.e3.item.listener;

import com.yw.e3.item.pojo.Item;
import com.yw.e3.pojo.TbItem;
import com.yw.e3.pojo.TbItemDesc;
import com.yw.e3.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HtmlGenListener implements MessageListener {
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;
    @Override
    public void onMessage(Message message) {

        try {
            //获取商品信息
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            Thread.sleep(1000);//等待事务提交
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            Map data = new HashMap();
            data.put("item",item);
            data.put("itemDesc",itemDesc);
            //加载模板
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //输出流
            Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
            template.process(data,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
