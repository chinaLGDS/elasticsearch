package com.nly.controller;

import com.nly.service.ItemsESService;
import com.nly.service.impl.ItemESServiceImpl;
import com.nly.utils.JSONResult;
import com.nly.utils.PagedGridResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/items")
public class HelloController {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private ItemsESService itemsESService;
    @RequestMapping("/hello")
    public  Object hello(){
        return "hello,elasticsearch";
    }

    @GetMapping("/es/search")
    public JSONResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize){

        if (StringUtils.isBlank(keywords)){
            return JSONResult.errorMsg(null);
        }
        //判断page是否为空，默认从第一页进行排序
        if (page == null){
            page =1;//表示page是可传可不传的
        }
        if (pageSize == null){
            pageSize = 20 ;
        }
        PagedGridResult grid  = itemsESService.searhItems(keywords,
                sort,
                page,
                pageSize);
        logger.info("欢迎使用elasticsearch");
        return JSONResult.ok(grid);
    }



}
