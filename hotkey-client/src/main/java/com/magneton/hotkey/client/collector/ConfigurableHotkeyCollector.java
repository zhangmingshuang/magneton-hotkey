package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.properties.CollectProperties;
import java.util.concurrent.Executor;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public interface ConfigurableHotkeyCollector extends HotkeyCollector {

    void setProperties(CollectProperties properties);

    void afterPropertiesSet();

    void setExecutor(Executor executor);
}
