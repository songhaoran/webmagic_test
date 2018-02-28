package com.song.service;

import com.song.pageprocesser.WeixinArticlePageProcessor;
import com.song.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

/**
 * Created by Song on 2018/2/28.
 */
@Service
@Slf4j
public class SnatchService {

    private String SOGOU_WEIXIN_SEARCH_PREFIX = "http://weixin.sogou.com/weixin?type=1&s_from=input&query=";
    private String SOGOU_WEIXIN_SEARCH_SUFFIX = "&ie=utf8&_sug_=n&_sug_type_=";

    /**
     * 根据公众号名称抓取文章
     * @param officialAccountName
     * @return
     */
    public Response<Boolean> snatchWeixinArticle(String officialAccountName) {
        try {
            Spider.create(new WeixinArticlePageProcessor())
                    .addUrl(SOGOU_WEIXIN_SEARCH_PREFIX + officialAccountName + SOGOU_WEIXIN_SEARCH_SUFFIX)
                    .thread(2)
                    .run();
        } catch (Exception e) {
            log.error("[snatchWeixinArticle][抓取公众号:{}  文章失败!]", officialAccountName, e);
            return Response.failed(false);
        }
        return Response.success(true);
    }
}
