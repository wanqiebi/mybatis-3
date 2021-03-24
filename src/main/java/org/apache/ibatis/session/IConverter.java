package org.apache.ibatis.session;

/**
 * @since 3.2.2.m1
 */
public interface IConverter<T, R> {
    R convert(T target);
}
