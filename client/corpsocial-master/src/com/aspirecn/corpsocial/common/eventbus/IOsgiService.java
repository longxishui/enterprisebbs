package com.aspirecn.corpsocial.common.eventbus;

public interface IOsgiService<T, V> {
    T invoke(V params);
}
