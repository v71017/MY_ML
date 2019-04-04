package edu.rtu.stl.util;

public interface WithException {

    default <T> T exceptional(SupplierWithException<T> supplier) {
        try {
            return supplier.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default void exceptional(SupplierWithExceptionVoid supplier) {
        try {
            supplier.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    interface SupplierWithException<T> {
        T call() throws Exception;
    }

    @FunctionalInterface
    interface SupplierWithExceptionVoid {
        void call() throws Exception;
    }
}
