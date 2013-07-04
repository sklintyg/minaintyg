package se.inera.certificate.model.util;

public abstract class Predicate<T> {
    public abstract boolean apply(T t);
}
