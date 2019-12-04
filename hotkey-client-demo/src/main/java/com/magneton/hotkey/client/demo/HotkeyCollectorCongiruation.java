package com.magneton.hotkey.client.demo;

import com.magneton.hotkey.client.HotkeyCollectorBuilder;
import com.magneton.hotkey.client.collector.HotkeyCollector;
import com.magneton.hotkey.client.properties.CollectProperties;
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
@EnableConfigurationProperties(ConfigProperties.class)
public class HotkeyCollectorCongiruation {

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public HotkeyCollector hotkeyCollector() {
        HotkeyCollectorBuilder builder = new HotkeyCollectorBuilder();
        builder.setCollectProperties(configProperties);
        builder.setExecutor(Executors.newFixedThreadPool(3));
        return builder.build();
    }
}
