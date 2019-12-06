package com.magneton.hotkey.client.summarier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Setter
@Getter
@ToString
public class BoltSummarierConfig {

    /**
     * Bolt地址配置规则
     * 127.0.0.1:18903?_CONNECTIONNUM=30&_CONNECTIONWARMUP=true
     * 表示针对这个 IP 地址，需要建立30个连接，同时需要预热连接。其中预热与不预热的区别是：
     *
     * 预热：即第一次调用（比如 Sync 同步调用），就建立30个连接
     * 不预热：每一次调用，创建一个连接，直到创建满30个连接
     *
     * 多个地址支持轮循加权
     * 如： {@code {"127.0.0.1:18903;weight=1","127.0.0.1:18904;weight=10"}}
     */
    private String[] addrs = {"127.0.0.1:18903?_CONNECTIONNUM=3&_CONNECTIONWARMUP=true"};

    private int connectionTimeout;

}
