package se.inera.certificate.model.util;

import java.util.List;

public final class Iterables {

    private Iterables() {
    }

    public static <T> T find(List<T> list, Predicate<T> predicate, T defaultResult) {
        if (list != null) {
            for (T item: list) {
                if (predicate.apply(item)) {
                    return item;
                }
            }
        }
        return defaultResult;
    }
}
