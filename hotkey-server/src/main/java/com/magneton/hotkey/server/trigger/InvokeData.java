package com.magneton.hotkey.server.trigger;

import com.magneton.hotkey.server.trigger.rule.TriggerRule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
@Setter
@Getter
@ToString
public class InvokeData {

    private TriggerRule triggerRule;
    private TriggerInvoker triggerInvoker;

}
