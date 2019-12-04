package com.magneton.hotkey.client.demo;

import com.magneton.hotkey.client.properties.CollectProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@ConfigurationProperties(prefix = "hotkey")
public class ConfigProperties extends CollectProperties {

}
