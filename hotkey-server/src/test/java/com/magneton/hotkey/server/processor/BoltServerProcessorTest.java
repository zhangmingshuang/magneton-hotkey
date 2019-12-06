package com.magneton.hotkey.server.processor;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.HotkeyListener;
import com.magneton.hotkey.server.support.BoltClientUtil;
import java.util.concurrent.CountDownLatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class BoltServerProcessorTest {

    public static final int port = 18904;
    private static boolean init = false;
    public static BoltServerProcessor serverProcessor;

    @BeforeClass
    public static void init() {
        BoltProcessorConfig boltProcessorConfig = new BoltProcessorConfig();
        boltProcessorConfig.setPort(port);

        serverProcessor = new BoltServerProcessor(boltProcessorConfig);
        serverProcessor.prepareContext();

        CountDownLatch cdl = new CountDownLatch(1);
        Thread thread = new Thread() {
            @Override
            public void run() {
                serverProcessor.start();
                cdl.countDown();
            }
        };
        thread.start();
        try {
            cdl.await();
        } catch (InterruptedException e) {

        }
        System.out.println("init finish...");
        init = true;
    }

    @Before
    public void before() {
        Assert.assertTrue(init);
    }

    @Test
    public void testSend() {
        serverProcessor.registerListener(new HotkeyListener() {
            @Override
            public void listen(Hotkey hotkey) {
                Assert.assertEquals(hotkey.getKey(), "test");
            }
        });
        Assert.assertTrue(BoltClientUtil.sendMsg(port, Hotkey.of("test", "value")));
    }
}
