package com.magneton.hotkey.server;

import com.magneton.hotkey.server.processor.ServerProcessor;
import java.util.function.Consumer;
import lombok.Getter;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public abstract class AbstractHotkeyServer implements ConfigableHotkeyServer<ServerProcessor> {

    /**
     * 服务处理器
     */
    @Getter
    private ServerProcessor serverProcessor;

    private boolean prepared = false;
    private Consumer<ServerProcessor> afterConsumer;

    @Override
    public void prepareContext() {
        prepared = true;

        validate();
    }

    @Override
    public void start() {
        serverProcessor.start();
        if (afterConsumer != null) {
            afterConsumer.accept(serverProcessor);
        }
    }

    protected void validate() {
        if (!prepared) {
            throw new RuntimeException("before prepareContext method.");
        }
        if (serverProcessor == null) {
            throw new RuntimeException("serverProcessor is null.");
        }
    }

    @Override
    public void setServerProcessor(ServerProcessor serverProcessor) {
        validateUnprepared();
        this.serverProcessor = serverProcessor;
    }

    @Override
    public void afterStart(Consumer<ServerProcessor> consumer) {
        this.afterConsumer = consumer;
    }

    private void validateUnprepared() {
        if (prepared) {
            throw new RuntimeException("unsupport changing after prepareContext");
        }
    }

}
