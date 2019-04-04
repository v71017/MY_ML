package edu.rtu.stl.util;

import java.util.List;
import java.util.function.Predicate;

public interface WithCollections {

    default <T> T find(List<T> list, Predicate<T> test) {
        return list.stream().filter(test).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
