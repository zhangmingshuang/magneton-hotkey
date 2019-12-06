package com.magneton.hotkey.server.demo.invoker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
@Component
public @interface HotkeyComponent {

    String value();
}
