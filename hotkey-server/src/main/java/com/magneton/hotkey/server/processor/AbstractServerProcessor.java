package com.magneton.hotkey.server.processor;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.ConfigableHotkeyListener;
import com.magneton.hotkey.server.HotkeyListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public abstract class AbstractServerProcessor implements ConfigableServerProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServerProcessor.class);

    private List<HotkeyListener> hotkeyListeners;
    private List<HotkeyPostProcess> hotkeyPostProcesses;
    /**
     * 线程处理
     */
    private Executor executor;

    @Override
    public void regiseterHotkeyPostProcess(HotkeyPostProcess hotkeyPostProcess) {
        if (hotkeyPostProcesses == null) {
            synchronized (HotkeyPostProcess.class) {
                if (hotkeyPostProcesses == null) {
                    hotkeyPostProcesses = new CopyOnWriteArrayList<>();
                }
            }
        }
        hotkeyPostProcesses.add(hotkeyPostProcess);
    }

    @Override
    public boolean removeListener(HotkeyListener hotkeyListener) {
        if (hotkeyListeners == null) {
            return false;
        }
        return hotkeyListeners.remove(hotkeyListener);
    }

    @Override
    public void registerListener(HotkeyListener hotkeyListener) {
        if (hotkeyListeners == null) {
            synchronized (HotkeyListener.class) {
                if (hotkeyListeners == null) {
                    hotkeyListeners = new CopyOnWriteArrayList<>();
                }
            }
        }
        if (hotkeyListener instanceof ConfigableHotkeyListener) {
            ((ConfigableHotkeyListener) hotkeyListener).init();
        }
        hotkeyListeners.add(hotkeyListener);
        hotkeyListeners.sort((l1, l2) -> l2.order() - l1.order());
    }

    protected void handleHotkeys(Hotkey[] hotkeys) {
        if (hotkeys == null || hotkeys.length < 1) {
            return;
        }
        for (Hotkey hotkey : hotkeys) {
            if (executor != null) {
                executor.execute(() -> AbstractServerProcessor.this.handleHotkey(hotkey));
            } else {
                AbstractServerProcessor.this.handleHotkey(hotkey);
            }
        }
    }

    protected void handleHotkey(Hotkey hotkey) {
        if (!isHotkeyPostPorcessAllow(hotkey)) {
            return;
        }
        if (hotkeyListeners == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("hasn't listeners. hotkey:{}", hotkey);
            }
            return;
        }
        String key = hotkey.getKey();
        for (int i = 0, l = hotkeyListeners.size(); i < l; i++) {
            HotkeyListener hotkeyListener = hotkeyListeners.get(i);
            hotkeyListener.listen(hotkey);
        }
    }

    protected boolean isHotkeyPostPorcessAllow(Hotkey hotkey) {
        if (hotkeyPostProcesses != null) {
            for (int i = 0, l = hotkeyPostProcesses.size(); i < l; i++) {
                HotkeyPostProcess process = hotkeyPostProcesses.get(i);
                if (!process.before(hotkey)) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("{} before not allow. hotkey:{}", process.getClass(), hotkey);
                    }
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void prepareContext() {
        //nothing.
    }

}
