package com.magneton.hotkey.server.storager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 存储的数据要求
 *
 * @author zhangmingshuang
 * @since 2019/12/6
 */
@Getter
@ToString
@AllArgsConstructor
public class StorageData {

    private final String key;
    /**
     * Key的触发次数
     */
    private final long keyTriggerCount;
    /**
     * Key对象的值的大小
     */
    private final int valueCount;
    /**
     * 游标时间
     */
    private final long cursorTime;
}
