# magneton-hotkey

一个高效的，容易扩展、稳定的`简单`业务热点数据采集系统，
适用于系统中需要对热点数据进行采集，汇总、在汇总服务端配置规则，进行通知读取热点到缓存的场景中。

###### 为什么不采用MQ或其他采集系统？
首先开发这个系统的时候，由于运维成本、开发体系、服务器环境系列问题，
被不允许使用MQ。更没办法采集其他较重的框架了。所以，需要自己开发一套较为高效的简单采集系统。

## Client使用
### SpringBoot
```java
@Bean
public HotkeyCollector hotkeyCollector() {
    //配置
    BoltSummarierConfig boltSummarierConfig = new BoltSummarierConfig();
    boltSummarierConfig.setAddrs(configProperties.getBoltAddr());
    //使用默认实现
    DefaultHotkeyCollector collector = new DefaultHotkeyCollector();
    collector.setBoltSummarierConfig(boltSummarierConfig);
    collector.setCollectConfig(configProperties);
    collector.setExecutor(Executors.newFixedThreadPool(3));
    collector.start();
    return collector;
}
```
### 其他
```java
BoltSummarierConfig config = new BoltSummarierConfig();
//配置汇总器上报地址
config.setAddrs(new String[]{"127.0.0.1:18903?_CONNECTIONNUM=3&_CONNECTIONWARMUP=true"});
//配置连接超时时间
config.setConnectionTimeout(3000);
DefaultHotkeyCollector collector = new DefaultHotkeyCollector();
collector.setBoltSummarierConfig(config);
//设置线程池
//collector.setExecutor();
//配置采集到多少数据量时进行上报
collector.setMaximumSize(1024);
//配置每隔10S上报一次
collector.setIntervalSeconds(10);

collector.start();
```

## Server使用
### SpringBoot
```java
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
```
#### 规则处理器 
```java
/**
 * 该类表示的，监听热点数据key为test
 * 并且，被触发了 {@link NumberTriggerRule#getNumber()}次数之后
 * 回调{@link #invoke(String, NumberTriggerRule)}方法
 */
@HotkeyComponent("test")
public class TestNumInvoker implements HotkeyInvoker<NumberTriggerRule> {

    @Override
    public NumberTriggerRule getRule() {
        //每被触发2次，回调
        return new NumberTriggerRule(2);
    }

    @Override
    public void invoke(String key, NumberTriggerRule triggerRule) {
        System.out.println("TestNumInvoker:" + key + "," + triggerRule);
        System.out.println("加载需要预热的数据。。。。。。");
    }
}
```
### 其他
```java
DefaultHotkeyServer defaultHotkeyServer = new DefaultHotkeyServer();
defaultHotkeyServer.setPort(port);
defaultHotkeyServer.afterStart(server -> {
    //注册监听器...
});
defaultHotkeyServer.start();
```


  





