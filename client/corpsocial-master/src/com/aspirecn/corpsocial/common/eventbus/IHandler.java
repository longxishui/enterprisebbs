/**
 * @(#) IHandler.java Created on 2013-11-26
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

/**
 * The class <code>IHandler</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public interface IHandler<T, V> {
    T handle(V args);

}
