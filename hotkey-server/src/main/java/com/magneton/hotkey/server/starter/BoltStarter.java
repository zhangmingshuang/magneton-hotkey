package com.magneton.hotkey.server.starter;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.consumer.HotkeyConsumer;
import com.magneton.hotkey.server.invoker.HotkeyInvokerRegister;
import com.magneton.hotkey.server.invoker.HotkeyListener;
import com.magneton.hotkey.server.invoker.MemoryHotkeyInvokerRegister;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class BoltStarter implements Starter<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoltStarter.class);

    private int port = 18903;
    private RpcServer rpcServer;
    private HotkeyConsumer defaultConsumer;
    private Map<String, HotkeyConsumer> consumer = new ConcurrentHashMap<>(10);
    private HotkeyInvokerRegister hotkeyInvokerRegister;
    private HotkeyListener hotkeyListener;
    private Executor executor;

    @Override
    public void registerDefaultConsumer(HotkeyConsumer hotkeyConsumer) {
        if (hotkeyConsumer == null) {
            LOGGER.error("can't regiser with null HotkeyConsumer.");
            return;
        }
        this.defaultConsumer = hotkeyConsumer;
        LOGGER.info("register default consumer with {}", hotkeyConsumer.getClass());
    }

    @Override
    public void registerConsumer(String key, HotkeyConsumer hotkeyConsumer) {
        if (key == null || key.isEmpty()) {
            LOGGER.error("can't regiser with empty or null key.");
            return;
        }
        if (hotkeyConsumer == null) {
            LOGGER.error("can't regiser with null HotkeyConsumer.");
            return;
        }
        HotkeyConsumer exist = consumer.put(key, hotkeyConsumer);
        if (exist != null) {
            throw new IllegalArgumentException("key [" + key + "] exists."
                                                   + " ref with " + exist.getClass().getName());
        }
        LOGGER.info("register consumer on {} with {}", key, hotkeyConsumer.getClass());
    }

    @Override
    public void afterPropertiesSet() {
        hotkeyInvokerRegister = new MemoryHotkeyInvokerRegister();
        rpcServer = new RpcServer(port);
        hotkeyListener = hotkeyInvokerRegister.getHotkeyListener();
        rpcServer.registerUserProcessor(new SyncUserProcessor<Hotkey[]>() {
            @Override
            public Object handleRequest(BizContext bizCtx, Hotkey[] request) throws Exception {
                if (request != null) {
                    for (Hotkey hotkey : request) {
                        if (executor != null) {
                            executor.execute(() -> BoltStarter.this.doHold(hotkey));
                        } else {
                            BoltStarter.this.doHold(hotkey);
                        }
                    }
                }
                return Boolean.TRUE;
            }

            @Override
            public String interest() {
                return Hotkey[].class.getName();
            }
        });
        boolean start = rpcServer.start();
        LOGGER.info("hotkeyServer start listening on " + port);
    }

    private void doHold(Hotkey hotkey) {
        if (hotkeyListener != null) {
            hotkeyListener.fire(hotkey);
        }
        String key = hotkey.getKey();
        HotkeyConsumer csr = consumer.getOrDefault(key, defaultConsumer);
        if (csr == null) {
            return;
        }
        csr.hold(hotkey);
    }

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public HotkeyInvokerRegister getHotkeyInvokerRegister() {
        return hotkeyInvokerRegister;
    }

    @Override
    public void setProperties(Integer properties) {
        if (properties != null && properties > 0) {
            port = properties.intValue();
        }
    }

}
