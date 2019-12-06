package com.magneton.hotkey.server.trigger;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.storager.HotkeyStorager;
import com.magneton.hotkey.server.storager.StorageConnection;
import com.magneton.hotkey.server.storager.StorageData;
import com.magneton.hotkey.server.storager.StorageProcess;
import com.magneton.hotkey.server.trigger.rule.NumberRule;
import com.magneton.hotkey.server.trigger.rule.TimeRule;
import com.magneton.hotkey.server.trigger.rule.TriggerRule;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
public class DefaultHotkeyTrigger implements HotkeyTrigger {

    @Getter
    private HotkeyStorager hotkeyStorager;
    private Map<String, List<InvokeData>> rules;
    private List<InvokeData> defaultRules;
    private AtomicBoolean inited = new AtomicBoolean(false);

    @Override
    public void init() {
        if (!inited.compareAndSet(false, true)) {
            return;
        }
        if (hotkeyStorager != null) {
            hotkeyStorager.registerSaveProcess(new StorageProcess() {

                @Override
                public void after(StorageConnection connection, Hotkey hotkey) {
                    DefaultHotkeyTrigger.this.ruleDetermine(connection, hotkey);
                }
            });
        }
    }

    @Override
    public void listen(Hotkey hotkey) {
        if (!inited.get()) {
            throw new RuntimeException("before init.");
        }
        hotkeyStorager.getStorageConnection().save(hotkey);
    }

    @Override
    public void registerStorager(HotkeyStorager hotkeyStorager) {
        if (inited.get()) {
            throw new RuntimeException("after inited.");
        }
        this.hotkeyStorager = hotkeyStorager;
    }

    @Override
    public <T extends TriggerRule> void registerDefaultInvoker(T rule, TriggerInvoker<T> triggerInvoker) {
        if (defaultRules == null) {
            synchronized (inited) {
                if (defaultRules == null) {
                    defaultRules = new CopyOnWriteArrayList<>();
                }
            }
        }
        InvokeData data = new InvokeData();
        data.setTriggerRule(rule);
        data.setTriggerInvoker(triggerInvoker);
        defaultRules.add(data);
    }

    @Override
    public <T extends TriggerRule> void registerInvoker(String key, T rule, TriggerInvoker<T> triggerInvoker) {
        this.doRuleReg(key, rule, triggerInvoker);
    }

    protected void doRuleReg(String key, TriggerRule rule, TriggerInvoker triggerInvoker) {
        if (rules == null) {
            synchronized (NumberRule.class) {
                if (rules == null) {
                    rules = new ConcurrentHashMap<>();
                    rules.putIfAbsent(key, new CopyOnWriteArrayList<>());
                }
            }
        }
        List<InvokeData> invokeDatas = rules.get(key);
        if (invokeDatas == null) {
            synchronized (rules) {
                List<InvokeData> list = new CopyOnWriteArrayList<>();
                invokeDatas = rules.putIfAbsent(key, list);
                if (invokeDatas == null) {
                    invokeDatas = list;
                }
            }
        }
        InvokeData data = new InvokeData();
        data.setTriggerRule(rule);
        data.setTriggerInvoker(triggerInvoker);
        invokeDatas.add(data);
    }

    protected void ruleDetermine(StorageConnection connection, Hotkey hotkey) {
        if (hotkey == null
            || hotkey.getKey() == null
            || hotkey.getKey().isEmpty()
            || rules == null) {
            return;
        }
        List<InvokeData> invokeDatas = rules.get(hotkey.getKey());
        if (invokeDatas == null || invokeDatas.isEmpty()) {
            this.doDefaultRuleDetermine(connection, hotkey);
            return;
        }
        this.doRuleInvoker(connection, hotkey, invokeDatas);
    }


    protected void doDefaultRuleDetermine(StorageConnection connection, Hotkey hotkey) {
        if (defaultRules == null) {
            return;
        }
        this.doRuleInvoker(connection, hotkey, defaultRules);
    }

    private void doRuleInvoker(StorageConnection connection, Hotkey hotkey, List<InvokeData> invokeDatas) {
        StorageData data = connection.getData(hotkey.getKey());
        if (data == null) {
            return;
        }
        long keyTriggerCount = data.getKeyTriggerCount();
        long cursorTime = data.getCursorTime();
        for (int i = 0, l = invokeDatas.size(); i < l; i++) {
            InvokeData invokeData = invokeDatas.get(i);
            TriggerRule triggerRule = invokeData.getTriggerRule();
            if (triggerRule instanceof NumberRule) {
                NumberRule rule = (NumberRule) triggerRule;
                //由于Count次数是可能并发时判断，这里在并发时可能没办法得到一次整除
                if (keyTriggerCount % rule.getNumber() == 0) {
                    invokeData.getTriggerInvoker().invoke(data.getKey(), triggerRule);
                    continue;
                }
            } else if (triggerRule instanceof TimeRule) {
                TimeRule rule = (TimeRule) triggerRule;
                long now = System.currentTimeMillis();
                if (now - cursorTime > rule.getTime()) {
                    invokeData.getTriggerInvoker().invoke(data.getKey(), triggerRule);
                    connection.updateCursorTime(data.getKey(), now);
                    continue;
                }
            }
        }
    }
}
