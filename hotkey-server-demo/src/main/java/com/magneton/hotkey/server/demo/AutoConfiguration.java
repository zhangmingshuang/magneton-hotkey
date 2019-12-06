package com.magneton.hotkey.server.demo;

import com.magneton.hotkey.server.DefaultHotkeyServer;
import com.magneton.hotkey.server.demo.invoker.HotkeyComponent;
import com.magneton.hotkey.server.demo.invoker.HotkeyInvoker;
import com.magneton.hotkey.server.demo.properties.ConfigProperties;
import com.magneton.hotkey.server.storager.MemoryHotkeyStorager;
import com.magneton.hotkey.server.trigger.DefaultHotkeyTrigger;
import com.magneton.hotkey.server.trigger.HotkeyTrigger;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Configuration
public class AutoConfiguration {

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(4);

    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private List<HotkeyInvoker> hotkeyInvokers;


    @PostConstruct
    public void post() {

        DefaultHotkeyServer defaultHotkeyServer = new DefaultHotkeyServer();
        defaultHotkeyServer.setPort(configProperties.getPort());
        defaultHotkeyServer.afterStart(server -> {
            HotkeyTrigger hotkeyTrigger = new DefaultHotkeyTrigger();
            //配置存储器
            hotkeyTrigger.registerStorager(new MemoryHotkeyStorager());
            //配置规则处理器
            for (HotkeyInvoker hotkeyInvoker : hotkeyInvokers) {
                HotkeyComponent hotkeyComponent = hotkeyInvoker.getClass().getAnnotation(HotkeyComponent.class);
                if (hotkeyComponent == null) {
                    continue;
                }
                String key = hotkeyComponent.value();
                if ("*".equals(key)) {
                    hotkeyTrigger.registerDefaultInvoker(hotkeyInvoker.getRule(), hotkeyInvoker);
                } else {
                    hotkeyTrigger.registerInvoker(key, hotkeyInvoker.getRule(), hotkeyInvoker);
                }
            }
            server.registerListener(hotkeyTrigger);
        });
        defaultHotkeyServer.start();

    }
}
