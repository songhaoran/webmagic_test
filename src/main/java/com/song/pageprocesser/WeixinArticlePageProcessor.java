package com.song.pageprocesser;

import com.alibaba.fastjson.JSONPath;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * Created by Song on 2018/2/27.
 */
public class WeixinArticlePageProcessor implements PageProcessor {
    private String ARTICLE_URL = "/s?timestamp=\\d+&src=\\d+&ver=\\d+&signature=\\w+";
    private String ARTICEL_LIST = "https://mp.weixin.qq.com/profile?src=\\d+&timestamp=\\d+&ver=\\d+&signature=\\w+";

    @Override
    public void process(Page page) {
        if (page.getUrl().toString().contains("http://weixin.sogou.com/weixin?type=1&s_from=input&query=")) {
            String requestString = page.getHtml().xpath("//div[@class='txt-box']/p[@class='tit']/a[1]/@href").toString();
            System.out.println(page.getHtml().toString());
            page.addTargetRequest(requestString);
        } else {
            if (page.getUrl().toString().contains("mp.weixin.qq.com/profile?src=")) {
                String s = page.getHtml().toString();
                s = s.substring(s.indexOf("msgList ="));
                s = s.substring(9, s.indexOf("seajs.use(\"sougou/profile.js\");") - 3);
                s = s.substring(0, s.lastIndexOf(";")).replaceAll("amp;", "");
                Object read = JSONPath.read(s, "$..content_url");
                if (read != null) {
                    List<String> list = (List<String>) read;
                    for (String url : list) {
                        page.addTargetRequest("https://mp.weixin.qq.com" + url);
                    }
                }
            } else {
                page.putField("title", page.getHtml().xpath("//div[@id='img-content']/h2[@class='rich_media_title']/text()").get());
                page.putField("date", page.getHtml().xpath("//div[@id='meta_content']/em/text()").get());
                page.putField("subscriptionAccounts", page.getHtml().xpath("//div[@id='meta_content']/span[1]/text()").get());
                page.putField("richMediaContent", page.getHtml().xpath("//div[@class='rich_media_content ']").get());
            }
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
