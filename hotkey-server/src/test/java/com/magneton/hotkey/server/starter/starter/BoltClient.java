package com.magneton.hotkey.server.starter.starter;

import com.alipay.remoting.rpc.RpcClient;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class BoltClient {

    private RpcClient rpcClient;

    public BoltClient() {
        rpcClient = new RpcClient();
        rpcClient.enableConnectionMonitorSwitch();
        rpcClient.enableReconnectSwitch();
        rpcClient.init();
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }
}
