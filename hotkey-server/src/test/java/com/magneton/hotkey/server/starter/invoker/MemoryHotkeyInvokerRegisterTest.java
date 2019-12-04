package com.magneton.hotkey.server.starter.invoker;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.invoker.AfterNumberInvokerRule;
import com.magneton.hotkey.server.invoker.HotkeyInvoker;
import com.magneton.hotkey.server.invoker.HotkeyInvokerRegister;
import com.magneton.hotkey.server.invoker.HotkeyListener;
import com.magneton.hotkey.server.invoker.HourInvokerRule;
import com.magneton.hotkey.server.invoker.InvokerRule;
import com.magneton.hotkey.server.invoker.InvokerRulePostProcess;
import com.magneton.hotkey.server.invoker.MemoryHotkeyInvokerRegister;
import com.magneton.hotkey.server.invoker.MemoryHotkeyInvokerRegister.Invoker;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class MemoryHotkeyInvokerRegisterTest {


    @Test
    public void testTime() {
        HotkeyInvokerRegister register = MemoryHotkeyInvokerRegister.getInstance();
        HourInvokerRule hourInvokerRule = new HourInvokerRule(1);
        LongAdder addr = new LongAdder();
        register.registerRulePostProcess(new InvokerRulePostProcess() {
            @Override
            public Class<? extends InvokerRule> interest() {
                return HourInvokerRule.class;
            }

            @Override
            public boolean isAccept(Invoker invoker) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                invoker.setTmpValue(calendar.get(Calendar.HOUR_OF_DAY));
                return true;
            }
        });
        register.registerInvoker("hourTest",
                                 hourInvokerRule,
                                 new HotkeyInvoker() {
                                     @Override
                                     public void hold(String key) {
                                         System.out.println("收到Key加载通知 ： " + key);
                                         addr.increment();
                                     }
                                 });
        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(addr.sum() > 2);
    }

    @Test
    public void test() {
        int size = 4;
        int num = 3;
        CountDownLatch cdl = new CountDownLatch(size / num);
        HotkeyInvokerRegister register = MemoryHotkeyInvokerRegister.getInstance();
        register.registerInvoker("test",
                                 new AfterNumberInvokerRule(num),
                                 new HotkeyInvoker() {
                                     @Override
                                     public void hold(String key) {
                                         System.out.println("收到Key加载通知 ： " + key);
                                         cdl.countDown();
                                     }
                                 });
        HotkeyListener hotkeyListener = register.getHotkeyListener();
        for (int i = 0; i < size; i++) {
            hotkeyListener.fire(Hotkey.of("test", "testValue" + i));
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
