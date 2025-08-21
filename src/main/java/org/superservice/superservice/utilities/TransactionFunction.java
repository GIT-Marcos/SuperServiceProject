package org.superservice.superservice.utilities;

import org.hibernate.Session;

@FunctionalInterface
public interface TransactionFunction<T> {
    T apply(Session session);
}
