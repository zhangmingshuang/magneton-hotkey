package com.magneton.hotkey.server.invoker;

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
public class HourInvokerRule implements InvokerRule {

    public HourInvokerRule() {

    }

    /**
     * 每隔几小时
     */
    private int hours;
}
