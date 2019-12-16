package com.currencycheckerapi.util.uncheck;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void apply(T t) throws Exception;
}