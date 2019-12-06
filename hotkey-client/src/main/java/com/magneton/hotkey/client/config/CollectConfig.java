package com.magneton.hotkey.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 采集配置
 *
 * @author zhangmingshuang
 * @since 2019/12/3
 */
@Setter
@Getter
@ToString
public class CollectConfig {

    /**
     * 设置采集端存储大小
     *
     * 所有的采集器在实现时都会实现一个计数器，当计数器数 量超过size限定时，
     * 会将采集的内容进行一次上报
     */
    private int maximumSize = 1024;
    /**
     * 设置采集端存储时间间隔
     *
     * 采集端数据上报功能首先根据限定的存储大小进行判断，
     * 同时，如果采集端的数据存储停留时间超过设定的存储时间间隔，也会进行一次上报
     */
    private int intervalSeconds = 3;
}
