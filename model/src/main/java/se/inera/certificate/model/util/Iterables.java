package se.inera.certificate.model.util;

import java.util.Collection;
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

    public static <T> void addAll(Collection<T> targetCollection, Collection<T> toAdd) {
        if (targetCollection != null && toAdd != null) {
            targetCollection.addAll(toAdd);
        }
    }
}
