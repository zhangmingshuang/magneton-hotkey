package com.magneton.hotkey.client.summarier;

import com.alipay.remoting.rpc.RpcClient;
import com.magneton.hotkey.client.HotkeyContainer;
import com.magneton.hotkey.client.HotkeySummarier;
import com.magneton.hotkey.common.Hotkey;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class BoltHotkeySummarier implements HotkeySummarier<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoltHotkeySummarier.class);
    private RpcClient rpcClient;
    private ArrayBlockingQueue queue;
    private String addr = "127.0.0.1:18903";
    private int connectionTimeout;

    @Override
    public void afterPropertiesSet() {
        rpcClient = new RpcClient();
        rpcClient.enableConnectionMonitorSwitch();
        rpcClient.enableReconnectSwitch();
        rpcClient.init();
    }

    @Override
    public void report(HotkeyContainer hotkeyContainer) {
        if (hotkeyContainer == null) {
            return;
        }
        Hotkey[] hotkeys = hotkeyContainer.getHotkeys();
        if (hotkeys == null || hotkeys.length < 1) {
            return;
        }
        try {
            rpcClient.oneway(addr, hotkeys);
        } catch (Throwable e) {
            LOGGER.error("invoke " + addr + " exception.", e);
        }
    }

    @Override
    public void setProperties(String properties) {
        if (properties != null && !properties.isEmpty()) {
            this.addr = properties;
        }
    }
}
