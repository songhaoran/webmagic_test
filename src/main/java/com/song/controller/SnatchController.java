package com.song.controller;

import com.song.pageprocesser.SinaBlogPageProcessor;
import com.song.pageprocesser.WeixinArticlePageProcessor;
import com.song.pipeline.SinaBlogPipeline;
import com.song.pipeline.WeixinArticlePipeline;
import com.song.service.SnatchService;
import com.song.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;


/**
 * Created by Song on 2018/2/27.
 */
@RestController
@Slf4j
@RequestMapping(produces = "application/json;charset=UTF-8")
@Api(value = "santch", description = "抓取")
public class SnatchController {
    @Resource
    public SnatchService snatchService;

    @ApiOperation("抓取新浪博客")
    @GetMapping(value = "/snatch/sina_blog")
    public Response<Boolean> sinaBlogSnatch() {
        try {
            Spider.create(new SinaBlogPageProcessor())
                    .addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html")
                    .addPipeline(new SinaBlogPipeline())
                    .thread(5)
                    .run();
        } catch (Exception e) {
            log.error("", e);
            return Response.success(false);
        }
        return Response.success(true);
    }


    @ApiOperation("根据微信公众号文章列表链接抓取文章")
    @GetMapping(value = "/snatch/weixin")
    public Response<Boolean> weixinSnatch(@RequestParam(value = "url") String url) {
        try {
            Spider.create(new WeixinArticlePageProcessor())
                    .addUrl(url)
                    .addPipeline(new WeixinArticlePipeline())
                    .thread(5)
                    .run();
        } catch (Exception e) {
            log.error("", e);
            return Response.success(false);
        }
        return Response.success(true);
    }

    @ApiOperation("抓取指定微信公众号文章")
    @GetMapping(value = "/snatch/weixin/officalAccount")
    public Response<Boolean> officalAccountSnatch(@RequestParam(value = "offical_account_name") String officalAccountName) {
        return snatchService.snatchWeixinArticle(officalAccountName);
    }
}
