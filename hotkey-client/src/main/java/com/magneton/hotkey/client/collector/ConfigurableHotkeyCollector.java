package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.summarier.HotkeySummarier;
import com.magneton.hotkey.client.config.CollectConfig;
import java.util.concurrent.Executor;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public interface ConfigurableHotkeyCollector extends HotkeyCollector {

    void setCollectConfig(CollectConfig collectConfig);

    void setExecutor(Executor executor);

    void setHotkeySummarier(HotkeySummarier hotkeySummarier);

    void init();
}
