package com.magneton.hotkey.client.demo.core;

import com.magneton.hotkey.client.DefaultHotkeyCollector;
import com.magneton.hotkey.client.collector.HotkeyCollector;
import com.magneton.hotkey.client.summarier.BoltHotkeySummarier;
import com.magneton.hotkey.client.summarier.BoltSummarierConfig;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Configuration
@EnableConfigurationProperties(ConfigConfig.class)
public class HotkeyCollectorCongiruation {

    @Autowired
    private ConfigConfig configProperties;

    @Bean
    public HotkeyCollector hotkeyCollector() {
        BoltSummarierConfig boltSummarierConfig = new BoltSummarierConfig();
        boltSummarierConfig.setAddrs(configProperties.getBoltAddr());

        DefaultHotkeyCollector collector = new DefaultHotkeyCollector();
        collector.setBoltSummarierConfig(boltSummarierConfig);
        collector.setCollectConfig(configProperties);
        collector.setExecutor(Executors.newFixedThreadPool(3));
        collector.start();
        return collector;
    }
}
