package com.magneton.hotkey.server.storager;

import com.magneton.hotkey.common.Hotkey;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class MemoryHotkeyStorager implements HotkeyStorager, StorageConnection {

    private static final class Data {

        private LinkedHashMap values = new LinkedHashMap() {
            @Override
            protected boolean removeEldestEntry(Entry eldest) {
                return this.size() > Integer.MAX_VALUE >> 1;
            }
        };
        private LongAdder triggerNum;
        private long cursorTime;

        public Data() {

        }
    }

    private List<StorageProcess> storageProcesses;
    private static final Map<String, Data> staticial = new ConcurrentHashMap<>();

    @Override
    public StorageConnection getStorageConnection() {
        return this;
    }

    @Override
    public StorageData getData(String key) {
        Data data = staticial.get(key);
        if (data == null) {
            return null;
        }
        return new StorageData(key,
                               data.triggerNum.sum(),
                               data.values.size(),
                               data.cursorTime);
    }

    @Override
    public boolean updateCursorTime(String key, long time) {
        Data data = staticial.get(key);
        if (data == null) {
            return false;
        }
        data.cursorTime = time;
        return true;
    }

    @Override
    public boolean incrKeyTriggerCount(String key, int incr) {
        Data data = staticial.get(key);
        if (data == null) {
            return false;
        }
        data.triggerNum.add(incr);
        return true;
    }

    @Override
    public void save(Hotkey hotkey) {
        if (!doBefore(hotkey)) {
            return;
        }
        String key = hotkey.getKey();
        Data data = staticial.get(key);
        if (data == null) {
            data = new Data();
            data.values.put(hotkey.getValue(), key);
            data.triggerNum = new LongAdder();
            data.cursorTime = System.currentTimeMillis();
            staticial.putIfAbsent(key, data);
        }
        data.triggerNum.increment();
        doAfter(hotkey);
    }

    private void doAfter(Hotkey hotkey) {
        if (storageProcesses == null) {
            return;
        }
        for (int i = 0, l = storageProcesses.size(); i < l; i++) {
            StorageProcess process = storageProcesses.get(i);
            process.after(this, hotkey);
        }
    }

    private boolean doBefore(Hotkey hotkey) {
        if (storageProcesses == null) {
            return true;
        }
        for (int i = 0, l = storageProcesses.size(); i < l; i++) {
            StorageProcess process = storageProcesses.get(i);
            if (!process.before(this, hotkey)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void registerSaveProcess(StorageProcess storageProcess) {
        if (storageProcesses == null) {
            synchronized (StorageProcess.class) {
                if (storageProcesses == null) {
                    storageProcesses = new CopyOnWriteArrayList<>();
                }
            }
        }
        storageProcesses.add(storageProcess);
    }

}
