package com.magneton.hotkey.server.starter.starter;

import com.alipay.remoting.exception.RemotingException;
import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.consumer.HotkeyConsumer;
import com.magneton.hotkey.server.starter.BoltStarter;
import java.util.concurrent.CountDownLatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class BoltStarterTest {

    private static final int port = 18904;
    private static boolean init = false;
    private static BoltStarter boltStarter;

    @BeforeClass
    public static void init() {
        CountDownLatch cdl = new CountDownLatch(1);
        Thread thread = new Thread() {
            @Override
            public void run() {
                boltStarter = new BoltStarter();
                boltStarter.setProperties(port);
                boltStarter.afterPropertiesSet();
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
        boltStarter.registerConsumer("test", new HotkeyConsumer() {
            @Override
            public boolean hold(Hotkey hotkey) {
                System.out.println(hotkey);
                Assert.assertEquals(hotkey.getKey(), "test");
                Assert.assertEquals(hotkey.getValue(), "value");
                return true;
            }
        });
        BoltClient client = new BoltClient();
        try {
            Object o = client.getRpcClient().invokeSync("127.0.0.1:" + port,
                                                        new Hotkey[]{
                                                            Hotkey.of("test", "value")
                                                        },
                                                        3000);
            System.out.println(o);
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
