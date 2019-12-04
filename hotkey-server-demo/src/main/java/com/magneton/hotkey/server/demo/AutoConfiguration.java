package com.magneton.hotkey.server.demo;

import com.magneton.hotkey.server.HotkeyServer;
import com.magneton.hotkey.server.consumer.HotkeyConsumer;
import com.magneton.hotkey.server.demo.consumer.HotkeyComponent;
import com.magneton.hotkey.server.starter.Starter;
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
    private List<HotkeyConsumer> hotkeyConsumers;

    @PostConstruct
    public void post() {
        try {
            Starter starter = HotkeyServer.loadStarter(null, EXECUTOR);
            for (int i = 0, l = hotkeyConsumers.size(); i < l; i++) {
                HotkeyConsumer hotkeyConsumer = hotkeyConsumers.get(i);
                HotkeyComponent hotkey = hotkeyConsumer.getClass().getAnnotation(HotkeyComponent.class);
                String value = hotkey.value();
                if (value.equals("*")) {
                    starter.registerDefaultConsumer(hotkeyConsumer);
                } else {
                    starter.registerConsumer(hotkey.value(), hotkeyConsumer);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
