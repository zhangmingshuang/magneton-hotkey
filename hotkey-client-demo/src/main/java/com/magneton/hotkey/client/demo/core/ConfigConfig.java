package com.magneton.hotkey.client.demo.core;

import com.magneton.hotkey.client.config.CollectConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "hotkey")
public class ConfigConfig extends CollectConfig {

    private String[] boltAddr = {"127.0.0.1:18903?_CONNECTIONNUM=500&_CONNECTIONWARMUP=true"};
}
