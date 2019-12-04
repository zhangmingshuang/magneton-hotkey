package com.magneton.hotkey.server.invoker;

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
public class AfterNumberInvokerRule implements InvokerRule {

    public AfterNumberInvokerRule() {

    }

    /**
     * 每隔触发次数
     */
    private int number = 1024;
}
