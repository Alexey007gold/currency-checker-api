package com.currencycheckerapi.util.uncheck;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Exception;
}