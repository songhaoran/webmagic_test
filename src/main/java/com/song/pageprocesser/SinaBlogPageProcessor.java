package com.song.pageprocesser;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * Created by Song on 2018/2/27.
 */
public class SinaBlogPageProcessor implements PageProcessor {
    private String LIST_URL = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";
    private String DETAIL_URL = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";

    @Override
    public void process(Page page) {
        Html html = page.getHtml();

        if (page.getUrl().regex(LIST_URL).match()) {
            page.setSkip(true);
            page.addTargetRequests(page.getHtml().xpath("//div[@class='articleList']").links().regex(DETAIL_URL).all());
            page.addTargetRequests(page.getHtml().links().regex(LIST_URL).all());
        } else {
            page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2/text()").toString());
            page.putField("content", page.getHtml().xpath("//div[@class='articalContent']/text()").get());
            page.putField("date", page.getHtml().xpath("//div[@class='articalTitle']/span[@class='time SG_txtc']/text()").toString());
        }
    }

    @Override
    public Site getSite() {
        return Site.me().
                setDomain("blog.sina.com.cn")
                .setRetryTimes(3)
                .setSleepTime(3000)
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    }
}
