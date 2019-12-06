package com.magneton.hotkey.server.trigger.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class NumberTriggerRule implements NumberRule {

    public NumberTriggerRule() {

    }

    /**
     * 每隔触发次数
     */
    private int number = 1024;
}
