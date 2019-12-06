package com.magneton.hotkey.server.support;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class BoltClientUtil {

    public static boolean sendMsg(int port, Hotkey hotkey) {
        BoltClient client = new BoltClient();
        try {
            Object o = client.getRpcClient().invokeSync("127.0.0.1:" + port,
                                                        new Hotkey[]{hotkey},
                                                        3000);
            System.out.println(o);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
