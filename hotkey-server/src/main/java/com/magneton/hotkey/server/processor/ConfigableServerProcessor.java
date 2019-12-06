package com.magneton.hotkey.server.processor;

import java.util.concurrent.Executor;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface ConfigableServerProcessor extends ServerProcessor {

    void setExecutor(Executor executor);

    void prepareContext();
}
