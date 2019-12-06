package com.magneton.hotkey.client.demo.controller;

import com.magneton.hotkey.client.collector.HotkeyCollector;
import com.magneton.hotkey.common.Hotkey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private HotkeyCollector hotkeyCollector;

    @RequestMapping("")
    public String test(String userId, String key) {
        String value = "{uid:\"" + userId + "\"}";
        if (key == null) {
            key = "user";
        }
        hotkeyCollector.fire(Hotkey.of(key, value));
        return "hai . uid: " + userId;
    }

    @RequestMapping("/loop")
    public String loop(String key, String userId, Integer num) {
        String value = "{uid:\"" + userId + "\"}";
        num = num == null || num.intValue() < 0 ? 1 : num;
        for (int i = 0; i < num; i++) {
            hotkeyCollector.fire(Hotkey.of("user", value));
        }
        return "hai .key: " + key + " ,userId: " + userId + ", num: " + num;
    }
}
