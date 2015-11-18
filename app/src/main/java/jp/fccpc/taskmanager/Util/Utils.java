package jp.fccpc.taskmanager.Util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created by nakac on 15/10/23.
 */
public class Utils {

    public static <E> E find(Collection<E> collection, Function<E, Boolean> predicate) {
        if (collection == null) throw new IllegalArgumentException();
        if (predicate == null) throw new IllegalArgumentException();

        for (E e: collection) {
            Boolean b = predicate.apply(e);
            if (b != null && b) return e;
        }
        return null;
    }

    public static <E> List<E> findAll(Collection<E> collection, Function<E, Boolean> predicate) {
        List<E> result = Lists.newArrayList();
        for (E e: collection) {
            Boolean b = predicate.apply(e);
            if (b != null && b) result.add(e);
        }
        return result;
    }

    public static <E> E find(Collection<E> collection, Predicate<E> predicate){
        if (collection == null) throw new IllegalArgumentException();
        if (predicate == null) throw new IllegalArgumentException();

        for (E e: collection) {
            Boolean b = predicate.apply(e);
            if (b) return e;
        }
        return null;
    }

    public static <E> List<E> findAll(Collection<E> collection, Predicate<E> predicate) {
        List<E> result = Lists.newArrayList();
        for (E e: collection) {
            Boolean b = predicate.apply(e);
            if (b) result.add(e);
        }
        return result;
    }

    public static <E, F> List<F> map(Collection<E> collection, Function<E, F> func) {
        List<F> result = Lists.newArrayList();
        for (E e: collection)
            result.add(func.apply(e));
        return result;
    }

    public static <E> boolean all(Collection<E> collection, Predicate<E> predicate) {
        for (E e: collection) {
            if (!predicate.apply(e)) return false;
        }
        return true;
    }

    public static <E> boolean any(Collection<E> collection, Predicate<E> predicate) {
        for (E e: collection) {
            if (predicate.apply(e)) return true;
        }
        return false;
    }
}
