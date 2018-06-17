package com.yw.e3.search.controller;

import com.yw.e3.common.pojo.SearchResult;
import com.yw.e3.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;
    @RequestMapping("/search")
    public String searchItemList(String keyword,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 Model model) {
        try {
            keyword =new String(keyword.getBytes("iso-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        model.addAttribute("query",keyword);
        model.addAttribute("totalPages",result.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("recourdCount",result.getRecourdCount());
        model.addAttribute("itemList",result.getItemList());
        return "search";
    }
}
