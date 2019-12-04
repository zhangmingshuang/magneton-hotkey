package com.magneton.hotkey.server.invoker;

import com.magneton.hotkey.common.Hotkey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class MemoryHotkeyInvokerRegister implements HotkeyInvokerRegister {

    private static final Timer timer;
    private static final Map<String, Map<String, List<Invoker>>> INVOKERS = new ConcurrentHashMap<>();
    private static final MemoryHotkeyInvokerRegister REGISTER = new MemoryHotkeyInvokerRegister();
    private List<InvokerRulePostProcess> invokerRulePostProcesses;

    static {
        timer = new Timer("MemoryHotkeyInvokerRegisterTimer", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    REGISTER.invokeTime(System.currentTimeMillis());
                } catch (Throwable e) {
                    //Ignore
                }
            }
        }, 100, 1000);
    }

    public static final HotkeyInvokerRegister getInstance() {
        return REGISTER;
    }

    private MemoryHotkeyListener hotkeyListener = new MemoryHotkeyListener();

    @Override
    public HotkeyListener getHotkeyListener() {
        return hotkeyListener;
    }

    private void invokeTime(long currentTimeMillis) {
        this.invoke(HourInvokerRule.class, (key, invoker) -> {
            Date date = new Date(currentTimeMillis);
            int nowHour = date.getHours();
            HourInvokerRule hourInvokerRule = (HourInvokerRule) invoker.getInvokerRule();
            int gapHours = hourInvokerRule.getHours();
            Integer preHour = (Integer) invoker.getTmpValue();
            if (preHour == null) {
                preHour = nowHour;
                invoker.setTmpValue(preHour);
            }
            if (nowHour - preHour < gapHours) {
                return;
            }
            invoker.getHotkeyInvoker().hold(key);
        });
    }

    private <K, T> void invoke(Class<T> clazz, BiConsumer<String, Invoker> consumer) {
        Map<String, List<Invoker>> ruleInvokers = INVOKERS.get(clazz.getName());
        if (ruleInvokers == null || ruleInvokers.isEmpty()) {
            return;
        }
        Set<Entry<String, List<Invoker>>> entries = ruleInvokers.entrySet();
        String key;
        List<Invoker> invokers;
        Invoker invoker;
        for (Entry<String, List<Invoker>> entry : entries) {
            key = entry.getKey();
            invokers = entry.getValue();
            for (int i = 0, l = invokers.size(); i < l; i++) {
                invoker = invokers.get(i);
                if (invoker == null) {
                    continue;
                }
                consumer.accept(key, invoker);
            }
        }
    }

    @Override
    public void registerInvoker(String key, InvokerRule rule, HotkeyInvoker hotkeyInvoker) {
        synchronized (INVOKERS) {
            String name = rule.getClass().getName();
            Map<String, List<Invoker>> invokers = INVOKERS.get(name);
            if (invokers == null) {
                invokers = new ConcurrentHashMap<>();
                INVOKERS.put(name, invokers);
            }
            List<Invoker> hotkeyInvokers = invokers.get(key);
            if (hotkeyInvokers == null) {
                hotkeyInvokers = new CopyOnWriteArrayList();
                invokers.put(key, hotkeyInvokers);
            }
            Invoker invoker = new Invoker(rule, hotkeyInvoker);
            if (invokerRulePostProcesses != null) {
                for (int i = 0, l = invokerRulePostProcesses.size(); i < l; i++) {
                    InvokerRulePostProcess rulePostProcess = invokerRulePostProcesses.get(i);
                    if (rulePostProcess == null) {
                        continue;
                    }
                    Class<? extends InvokerRule> interest = rulePostProcess.interest();
                    if (!rule.getClass().isAssignableFrom(interest)) {
                        continue;
                    }
                    if (!rulePostProcess.isAccept(invoker)) {
                        return;
                    }
                }
            }
            hotkeyInvokers.add(invoker);
        }
    }

    @Override
    public void registerRulePostProcess(InvokerRulePostProcess invokerRulePostProcess) {
        if (invokerRulePostProcesses == null) {
            invokerRulePostProcesses = new CopyOnWriteArrayList<>();
        }
        invokerRulePostProcesses.add(invokerRulePostProcess);
    }

    @Setter
    @Getter
    @ToString
    public static class Invoker<T extends InvokerRule> {

        private Object tmpValue;
        private T invokerRule;
        private HotkeyInvoker hotkeyInvoker;

        public Invoker(T rule, HotkeyInvoker invoker) {
            this.invokerRule = rule;
            this.hotkeyInvoker = invoker;
        }
    }

    public static class MemoryHotkeyListener implements HotkeyListener {

        private ConcurrentHashMap<String, LongAdder> statistical = new ConcurrentHashMap();

        @Override
        public void fire(Hotkey hotkey) {
            if (hotkey == null) {
                return;
            }
            String key = hotkey.getKey();
            if (key == null || key.isEmpty()) {
                return;
            }
            Map<String, List<Invoker>> map
                = MemoryHotkeyInvokerRegister.INVOKERS.get(AfterNumberInvokerRule.class.getName());
            if (map == null || map.isEmpty()) {
                return;
            }
            List<Invoker> invokers = map.get(key);
            if (invokers == null || invokers.isEmpty()) {
                return;
            }
            LongAdder addr = statistical.get(key);
            if (addr == null) {
                synchronized (invokers) {
                    LongAdder put = new LongAdder();
                    addr = statistical.put(key, put);
                    if (addr == null) {
                        addr = put;
                    }
                }
            }
            addr.increment();

            long staticNum = addr.sum();

            for (int i = 0, l = invokers.size(); i < l; i++) {
                Invoker invoker = invokers.get(i);
                if (invoker == null) {
                    continue;
                }
                Long number = (Long) invoker.getTmpValue();
                if (number == null) {
                    AfterNumberInvokerRule invokerRule = (AfterNumberInvokerRule) invoker.getInvokerRule();
                    number = (long) invokerRule.getNumber();
                    invoker.setTmpValue(number);
                }
                if (staticNum >= number) {
                    AfterNumberInvokerRule invokerRule = (AfterNumberInvokerRule) invoker.getInvokerRule();
                    number = (long) invokerRule.getNumber();
                    invoker.setTmpValue(number + staticNum);
                    invoker.getHotkeyInvoker().hold(key);
                }
            }
        }

    }

}
