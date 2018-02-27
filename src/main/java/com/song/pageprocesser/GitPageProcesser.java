package com.song.pageprocesser;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;

/**
 * Created by Song on 2018/2/26.
 */
public class GitPageProcesser implements PageProcessor {

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex(("(https://github\\\\.com/\\\\w+/\\\\w+)")).all());
        page.putField("author", page.getUrl().regex("https://github\\\\.com/(\\\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name") == null) {
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='su']/tidyText()"));
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(1000);
    }

    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor())
                .addUrl("https://www.baidu.com/")
                .addPipeline(new JsonFilePipeline("E:\\webmagic\\"))
                .thread(5)
                .run();
    }
}
