package com.magneton.hotkey.server.properties;

import com.magneton.hotkey.server.starter.BoltStarter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Setter
@Getter
@ToString
public class StarterProperties {

    private String starter = BoltStarter.class.getName();

    private int startPort = 18903;
}
