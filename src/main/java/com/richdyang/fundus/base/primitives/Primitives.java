package com.richdyang.fundus.base.primitives;

import static com.richdyang.fundus.base.ArgumentAssert.notNull;
import static java.util.Collections.unmodifiableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains static utility methods pertaining to primitive types and their
 * corresponding wrapper types.
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public final class Primitives {
    private Primitives() {
    }

    /**
     * A map from primitive types to their corresponding wrapper types.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;

    /**
     * A map from wrapper types to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    // Sad that we can't use a BiMap. :(

    static {
        Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
        Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);

        add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
        add(primToWrap, wrapToPrim, byte.class, Byte.class);
        add(primToWrap, wrapToPrim, char.class, Character.class);
        add(primToWrap, wrapToPrim, double.class, Double.class);
        add(primToWrap, wrapToPrim, float.class, Float.class);
        add(primToWrap, wrapToPrim, int.class, Integer.class);
        add(primToWrap, wrapToPrim, long.class, Long.class);
        add(primToWrap, wrapToPrim, short.class, Short.class);
        add(primToWrap, wrapToPrim, void.class, Void.class);

        PRIMITIVE_TO_WRAPPER_TYPE = unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = unmodifiableMap(wrapToPrim);
    }

    private static void add(Map<Class<?>, Class<?>> forward,
                            Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    /**
     * Returns an immutable set of all nine primitive types (including {@code
     * void}). Note that a simpler way to test whether a {@code Class} instance
     * is a member of this set is to call {@link Class#isPrimitive}.
     *
     * @since fundus
     */
    public static Set<Class<?>> allPrimitiveTypes() {
        return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
    }

    /**
     * Returns an immutable set of all nine primitive-wrapper types (including
     * {@link Void}).
     *
     * @since fundus
     */
    public static Set<Class<?>> allWrapperTypes() {
        return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
    }

    /**
     * Returns {@code true} if {@code type} is one of the nine primitive-wrapper
     * types, such as {@link Integer}.
     *
     * @see Class#isPrimitive
     */
    public static boolean isWrapperType(Class<?> type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(notNull(type));
    }

    /**
     * Returns {@code true} if {@code type} is one of the nine primitive
     * types, such as {@link int}.
     *
     * @see Class#isPrimitive
     */
    public static boolean isPrimitiveType(Class<?> type) {
        return type.isPrimitive();
    }

    /**
     * Returns the corresponding wrapper type of {@code type} if it is a
     * primitive type; otherwise returns {@code type} itself. Idempotent.
     * <p>
     * <pre>
     *     wrap(int.class) == Integer.class
     *     wrap(Integer.class) == Integer.class
     *     wrap(String.class) == String.class
     * </pre>
     */
    public static <T> Class<T> wrap(Class<T> type) {
        notNull(type);

        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(type);
        return (wrapped == null) ? type : wrapped;
    }

    /**
     * Returns the corresponding primitive type of {@code type} if it is a
     * wrapper type; otherwise returns {@code type} itself. Idempotent.
     * <p>
     * <pre>
     *     unwrap(Integer.class) == int.class
     *     unwrap(int.class) == int.class
     *     unwrap(String.class) == String.class
     * </pre>
     */
    public static <T> Class<T> unwrap(Class<T> type) {
        notNull(type);

        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(type);
        return (unwrapped == null) ? type : unwrapped;
    }
}
