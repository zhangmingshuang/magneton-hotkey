package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.summarier.ConfigableHotkeySummarier;
import com.magneton.hotkey.client.summarier.HotkeySummarier;
import com.magneton.hotkey.client.config.CollectConfig;
import com.magneton.hotkey.common.Hotkey;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public abstract class AbstractHotkeyCollector implements ConfigurableHotkeyCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHotkeyCollector.class);
    private static final int DEFAULT_MAXIMUM_SIZE = 1024;

    private CollectConfig collectConfig;
    @Getter
    private int maximumSize;
    @Getter
    private int intervalSeconds;
    /**
     * 已采集次数
     */
    private AtomicInteger collectNum;
    /**
     * 上次采集汇总时间
     */
    private long prevSummaryTime;
    /**
     * 热键容器
     */
    private HotkeyContainer hotkeyContainer;
    /**
     * 热键汇总器
     */
    private HotkeySummarier hotkeySummarier;

    private AtomicBoolean summarizing = new AtomicBoolean(false);

    private boolean working = true;
    private Executor executor;

    public AbstractHotkeyCollector() {

    }

    @Override
    public boolean fire(Hotkey hotkey) {
        int num = collectNum.incrementAndGet();
        if (num >= maximumSize) {
            //需要进行一次汇总
            this.summary();
        }
        return this.offer(hotkey);
    }

    protected boolean offer(Hotkey hotkey) {
        if (!working) {
            return false;
        }
        return this.hotkeyContainer.offer(hotkey);
    }

    protected void summary() {
        if (!working || hotkeyContainer.size() < 1) {
            return;
        }
        if (!summarizing.compareAndSet(false, true)) {
            return;
        }
        this.collectNum.set(0);
        HotkeyContainer newHotkeyContainer = this.getHotkeyContainer();
        HotkeyContainer sumaryHotkeyContainer = hotkeyContainer;
        hotkeyContainer = newHotkeyContainer;
        try {
            if (executor != null) {
                executor.execute(() -> hotkeySummarier.report(sumaryHotkeyContainer));
            } else {
                hotkeySummarier.report(sumaryHotkeyContainer);
            }
        } finally {
            summarizing.set(false);
        }

    }

    /**
     * 热键容器
     *
     * @return HotkeyContainer
     */
    protected abstract HotkeyContainer getHotkeyContainer();


    @Override
    public void init() {
        this.prepareContext();

        if (hotkeySummarier instanceof ConfigableHotkeySummarier) {
            ((ConfigableHotkeySummarier) hotkeySummarier).init();
        }

        this.startIntervalTimeListener();
        this.addShutdownHook();
    }

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void setCollectConfig(CollectConfig collectConfig) {
        this.collectConfig = collectConfig;
    }

    @Override
    public void setHotkeySummarier(HotkeySummarier hotkeySummarier) {
        this.hotkeySummarier = hotkeySummarier;
    }

    protected void startIntervalTimeListener() {
        Timer timer = new Timer("HotkeyTimer", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    AbstractHotkeyCollector.this.summary();
                } catch (Throwable e) {
                    //Ignore
                }
            }
        }, intervalSeconds * 1000, intervalSeconds * 1000);
    }

    private void prepareContext() {
        maximumSize = collectConfig.getMaximumSize();
        if (maximumSize < 0) {
            maximumSize = DEFAULT_MAXIMUM_SIZE;
        }
        this.intervalSeconds = collectConfig.getIntervalSeconds();
        this.prevSummaryTime = 0;
        this.collectNum = new AtomicInteger(0);
        this.hotkeyContainer = this.getHotkeyContainer();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                AbstractHotkeyCollector.this.working = false;
            }
        });
    }
}
