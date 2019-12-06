package com.magneton.hotkey.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
@Setter
@Getter
@ToString
public class Invoker<T> {

    private int weight;
    private T data;

    public Invoker() {

    }

    public Invoker(T data) {
        this.data = data;
    }

    public Invoker(int weight, T data) {
        this.weight = weight;
        this.data = data;
    }
}
