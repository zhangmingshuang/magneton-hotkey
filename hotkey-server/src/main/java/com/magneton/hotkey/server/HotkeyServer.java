package com.magneton.hotkey.server;

import com.magneton.hotkey.server.properties.StarterArgsResolver;
import com.magneton.hotkey.server.properties.StarterProperties;
import com.magneton.hotkey.server.starter.Starter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class HotkeyServer {


    public static final Starter loadStarter(String[] args)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return loadStarter(args, Executors.newSingleThreadExecutor());
    }

    public static Starter loadStarter(String[] args, Executor executor)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        StarterArgsResolver starterArgsResolver = new StarterArgsResolver(args);
        StarterProperties properties = starterArgsResolver.getStarterProperties();
        String starterClassName = properties.getStarter();
        Class<?> clazz
            = Class.forName(starterClassName, true, Thread.currentThread().getContextClassLoader());
        Starter starter = (Starter) clazz.newInstance();
        starter.setExecutor(executor);
        starter.setProperties(properties.getStartPort());
        starter.afterPropertiesSet();
        return starter;
    }
}
