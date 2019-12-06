package com.magneton.hotkey.client.summarier;

import com.alipay.remoting.rpc.RpcClient;
import com.magneton.hotkey.client.collector.HotkeyContainer;
import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.common.Invoker;
import com.magneton.hotkey.common.LoadBalance;
import com.magneton.hotkey.common.RandomLoadBalnace;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class BoltHotkeySummarier implements ConfigableHotkeySummarier {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoltHotkeySummarier.class);

    private RpcClient rpcClient;

    private List<Invoker<String>> invokers;
    private int connectionTimeout;

    private BoltSummarierConfig sumarrierConfig;
    private LoadBalance loadBalance;

    public BoltHotkeySummarier(BoltSummarierConfig sumarrierConfig) {
        this.sumarrierConfig = sumarrierConfig;
    }

    @Override
    public void init() {
        this.prepareConfig();
        rpcClient = new RpcClient();
        rpcClient.enableConnectionMonitorSwitch();
        rpcClient.enableReconnectSwitch();
        rpcClient.init();
    }

    private void prepareConfig() {
        String[] addrs = sumarrierConfig.getAddrs();
        if (addrs == null || addrs.length < 1) {
            throw new IllegalArgumentException("hasn't sumarrier address configuration.");
        }

        invokers = new ArrayList<>(addrs.length);
        for (int i = 0; i < addrs.length; i++) {
            String addr = addrs[i];
            int index = addr.indexOf(';');
            if (index == -1) {
                invokers.add(new Invoker<>(addr));
            } else {
                int weight = Integer.parseInt(addr.substring(index + 1));
                invokers.add(new Invoker<>(weight, addr.substring(0, index)));
            }
        }
        if (invokers.size() > 1) {
            loadBalance = new RandomLoadBalnace();
        }

        connectionTimeout = sumarrierConfig.getConnectionTimeout();
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
        String addr;
        if (loadBalance != null) {
            Invoker<String> invoker = loadBalance.doSelect(invokers);
            addr = invoker.getData();
        } else {
            addr = invokers.get(0).getData();
        }
        try {
            rpcClient.oneway(addr, hotkeys);
        } catch (Throwable e) {
            LOGGER.error("invoke " + addr + " exception.", e);
        }
    }
}
