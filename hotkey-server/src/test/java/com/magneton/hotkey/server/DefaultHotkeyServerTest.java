package com.magneton.hotkey.server;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.processor.ServerProcessor;
import com.magneton.hotkey.server.support.BoltClientUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class DefaultHotkeyServerTest {


    @Test
    public void test() {
        int port = 13888;
        DefaultHotkeyServer defaultHotkeyServer = new DefaultHotkeyServer();
        defaultHotkeyServer.setPort(port);
        defaultHotkeyServer.afterStart(server -> {

            Assert.assertTrue(BoltClientUtil.sendMsg(port, Hotkey.of("DefaultHotkeyServerTest", "value")));
        });
        defaultHotkeyServer.start();

    }
}
