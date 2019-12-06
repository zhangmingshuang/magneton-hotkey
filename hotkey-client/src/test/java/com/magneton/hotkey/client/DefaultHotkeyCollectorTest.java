package com.magneton.hotkey.client;

import com.magneton.hotkey.client.summarier.BoltSummarierConfig;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
public class DefaultHotkeyCollectorTest {

    @Test
    public void test() {
        BoltSummarierConfig config = new BoltSummarierConfig();
        //配置汇总器上报地址
        config.setAddrs(new String[]{"127.0.0.1:18903?_CONNECTIONNUM=3&_CONNECTIONWARMUP=true"});
        //配置连接超时时间
        config.setConnectionTimeout(3000);
        DefaultHotkeyCollector collector = new DefaultHotkeyCollector();
        collector.setBoltSummarierConfig(config);
        //设置线程池
        //collector.setExecutor();
        //配置采集到多少数据量时进行上报
        collector.setMaximumSize(1024);
        //配置每隔10S上报一次
        collector.setIntervalSeconds(10);

        collector.start();
    }
}
