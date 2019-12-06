package com.magneton.hotkey.server.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.magneton.hotkey.common.Hotkey;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class BoltServerProcessor extends AbstractServerProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoltServerProcessor.class);

    private int port = 18903;
    private RpcServer rpcServer;
    private AtomicBoolean running = new AtomicBoolean(false);

    public BoltServerProcessor(BoltProcessorConfig boltProcessorConfig) {
        this.port = boltProcessorConfig.getPort();
        boltProcessorConfig = null;
    }

    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) {
            return;
        }
        rpcServer = new RpcServer(port);
        rpcServer.registerUserProcessor(new SyncUserProcessor<Hotkey[]>() {
            @Override
            public Object handleRequest(BizContext bizCtx, Hotkey[] request) throws Exception {
                //处理数据请求
                BoltServerProcessor.this.handleHotkeys(request);
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

}
