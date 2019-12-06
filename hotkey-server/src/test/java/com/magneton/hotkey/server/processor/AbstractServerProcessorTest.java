package com.magneton.hotkey.server.processor;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.HotkeyListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class AbstractServerProcessorTest {


    public static class O1 implements HotkeyListener {

        private Consumer consumer;

        public O1(Consumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public int order() {
            return 1;
        }

        @Override
        public void listen(Hotkey hotkey) {
            consumer.accept(hotkey);
        }
    }

    public static class O2 implements HotkeyListener {

        private Consumer consumer;

        public O2(Consumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public int order() {
            return 5;
        }

        @Override
        public void listen(Hotkey hotkey) {
            consumer.accept(hotkey);
        }
    }

    private static AbstractServerProcessor processor;

    @BeforeClass
    public static void classBefore() {
        processor = new AbstractServerProcessor() {
            @Override
            public void start() {
                System.out.println("start");
            }
        };
    }

    @Test
    public void testListenerOrder() {
        AtomicBoolean first = new AtomicBoolean(true);
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) {
                Assert.assertFalse(first.get());
            }
        };
        Consumer consumer2 = new Consumer() {
            @Override
            public void accept(Object o) {
                Assert.assertTrue(first.get());
                first.set(false);
            }
        };
        O1 o1 = new O1(consumer);
        O2 o2 = new O2(consumer2);
        processor.registerListener(o1);
        processor.registerListener(o2);

        processor.handleHotkey(Hotkey.of("AbstractServerProcessorTest", "test"));

        Assert.assertTrue(processor.removeListener(o1));
        Assert.assertTrue(processor.removeListener(o2));
    }

    @Test
    public void testHotkeyPostProcessor() {
        processor.regiseterHotkeyPostProcess(new HotkeyPostProcess() {
            @Override
            public boolean before(Hotkey hotkey) {
                return !hotkey.getKey().equals("skip");
            }
        });
        processor.registerListener(new HotkeyListener() {
            @Override
            public void listen(Hotkey hotkey) {
                Assert.assertTrue(false);
            }
        });
        processor.handleHotkey(Hotkey.of("skip", "skip"));
    }
}
