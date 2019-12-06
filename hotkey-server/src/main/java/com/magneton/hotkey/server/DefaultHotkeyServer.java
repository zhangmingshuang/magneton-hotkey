package com.magneton.hotkey.server;

import com.magneton.hotkey.server.processor.BoltProcessorConfig;
import com.magneton.hotkey.server.processor.BoltServerProcessor;
import com.magneton.hotkey.server.processor.ServerProcessor;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.experimental.Delegate;

/**
 * 默认的服务
 *
 * 会默认启用{@link BoltServerProcessor}进行服务处理
 *
 * 但是需要注意的是，此时是没有对应的{@link HotkeyConsumer}处理器的，
 * 需要通过获取 {@link #getServerProcessor()}然后调用{@link ServerProcessor#registerConsumer}进行消费者注册
 *
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class DefaultHotkeyServer extends AbstractHotkeyServer {

    private AtomicBoolean running = new AtomicBoolean(false);
    @Delegate
    private BoltProcessorConfig boltProcessorConfig = new BoltProcessorConfig();

    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) {
            return;
        }
        this.init();
        super.start();
    }

    private void init() {
        if (getServerProcessor() == null) {
            setServerProcessor(new BoltServerProcessor(boltProcessorConfig));
        }
    }
}
