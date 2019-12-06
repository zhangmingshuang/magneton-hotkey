package com.magneton.hotkey.server.demo.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "hotkey")
public class ConfigProperties {

    private int port;
}
