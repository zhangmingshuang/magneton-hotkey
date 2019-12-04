package com.magneton.hotkey.client.properties;

import com.magneton.hotkey.client.summarier.BoltHotkeySummarier;
import lombok.Data;
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
public class CollectProperties {

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
    /**
     * 指定数据上报器
     */
    private String summarier = BoltHotkeySummarier.class.getName();
    /**
     * 数据上报器上报连接地址
     */
    public String summarierAddr;
}
