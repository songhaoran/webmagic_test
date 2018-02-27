package com.song.pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * Created by Song on 2018/2/27.
 */
public class WeixinArticlePipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println("key:"+entry.getKey()+";value:"+entry.getValue());
        }
    }
}
