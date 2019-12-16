package com.currencycheckerapi.util.uncheck;

import java.util.function.Consumer;
import java.util.function.Function;

public class ExceptionUtil {

    private ExceptionUtil() {}

    public static <T, R> Function<T, R> uncheckF(ThrowingFunction<T, R> f) {
        return arg -> {
            try {
                return f.apply(arg);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> Consumer<T> uncheckC(ThrowingConsumer<T> r) {
        return arg -> {
            try {
                r.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
