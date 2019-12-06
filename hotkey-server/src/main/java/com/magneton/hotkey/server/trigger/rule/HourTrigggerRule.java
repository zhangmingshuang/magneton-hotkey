package com.magneton.hotkey.server.trigger.rule;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class HourTrigggerRule implements TimeRule {

    public HourTrigggerRule() {

    }

    /**
     * 每隔几小时
     */
    private int hours;

    @Override
    public long getTime() {
        return hours * 3600 * 1000L;
    }
}
