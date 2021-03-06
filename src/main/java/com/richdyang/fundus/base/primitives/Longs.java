package com.richdyang.fundus.base.primitives;

import static com.richdyang.fundus.base.ArgumentAssert.indexInBounds;
import static com.richdyang.fundus.base.ArgumentAssert.isTrue;
import static com.richdyang.fundus.base.ArgumentAssert.notNull;
import static com.richdyang.fundus.base.primitives.Longs.LexicographicalComparator.INSTANCE;
import static java.lang.Long.SIZE;
import static java.lang.Math.min;
import static java.lang.System.arraycopy;
import static java.util.Collections.emptyList;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

import com.richdyang.fundus.base.ArgumentAssert;
import com.richdyang.fundus.base.Objects.HashCodeHelper;


/**
 * Static utility methods pertaining to {@code long} primitives, that are not
 * already found in either {@link Long} or {@link Arrays}.
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public final class Longs {
    private Longs() {
    }

    /**
     * The number of bytes required to represent a primitive {@code long} value.
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * Compares the two specified {@code long} values. The sign of the value
     * returned is the same as that of {@code ((Long) a).compareTo(b)}.
     *
     * @param a the first {@code long} to compare
     * @param b the second {@code long} to compare
     * @return a negative value if {@code a} is less than {@code b}; a positive
     * value if {@code a} is greater than {@code b}; or zero if they are
     * equal
     */
    public static int compare(long a, long b) {
        return (a < b) ? -1 : ((a > b) ? 1 : 0);
    }

    /**
     * Returns {@code true} if {@code target} is present as an element anywhere
     * in {@code array}.
     *
     * @param array  an array of {@code long} values, possibly empty
     * @param target a primitive {@code long} value
     * @return {@code true} if {@code array[i] == target} for some value of
     * {@code i}
     */
    public static boolean contains(long[] array, long target) {
        for (long value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the first appearance of the value {@code target} in
     * {@code array}.
     *
     * @param array  an array of {@code long} values, possibly empty
     * @param target a primitive {@code long} value
     * @return the least index {@code i} for which {@code array[i] == target},
     * or {@code -1} if no such index exists.
     */
    public static int indexOf(long[] array, long target) {
        return indexOf(array, target, 0, array.length);
    }

    // TODO: consider making this public
    private static int indexOf(long[] array, long target, int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the start position of the first occurrence of the specified
     * {@code target} within {@code array}, or {@code -1} if there is no such
     * occurrence.
     * <p>
     * <p>
     * More formally, returns the lowest index {@code i} such that {@code
     * java.util.Arrays.copyOfRange(array, i, i + target.length)} contains
     * exactly the same elements as {@code target}.
     *
     * @param array  the array to search for the sequence {@code target}
     * @param target the array to search for as a sub-sequence of {@code array}
     */
    public static int indexOf(long[] array, long[] target) {
        notNull(array, "array");
        notNull(target, "target");
        if (target.length == 0) {
            return 0;
        }

        outer:
        for (int i = 0; i < array.length - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    /**
     * Returns the index of the last appearance of the value {@code target} in
     * {@code array}.
     *
     * @param array  an array of {@code long} values, possibly empty
     * @param target a primitive {@code long} value
     * @return the greatest index {@code i} for which {@code array[i] == target}
     * , or {@code -1} if no such index exists.
     */
    public static int lastIndexOf(long[] array, long target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    // TODO: consider making this public
    private static int lastIndexOf(long[] array, long target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the least value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code long} values
     * @return the value present in {@code array} that is less than or equal to
     * every other value in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static long min(long... array) {
        isTrue(array.length > 0);
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    /**
     * Returns the greatest value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code long} values
     * @return the value present in {@code array} that is greater than or equal
     * to every other value in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static long max(long... array) {
        isTrue(array.length > 0);
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Returns the values from each provided array combined into a single array.
     * For example, {@code concat(new long[] a, b}, new long[] {}, new long[]
     * {c}} returns the array {@code a, b, c} .
     *
     * @param arrays zero or more {@code long} arrays
     * @return a single array containing all the values from the source arrays,
     * in order
     */
    public static long[] concat(long[]... arrays) {
        int length = 0;
        for (long[] array : arrays) {
            length += array.length;
        }
        long[] result = new long[length];
        int pos = 0;
        for (long[] array : arrays) {
            arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    /**
     * Returns a big-endian representation of {@code value} in an 8-element byte
     * array; equivalent to {@code
     * ByteBuffer.allocate(8).putLong(value).array()}. For example, the input
     * value {@code 0x1213141516171819L} would yield the byte array {@code 0x12,
     * 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19} .
     * <p>
     * <p>
     * If you need to convert and concatenate several values (possibly even of
     * different types), use a shared {@link ByteBuffer} instance, or
     * use {@link com.google.common.io.ByteStreams#newDataOutput()} to get a
     * growable buffer.
     * <p>
     * <p>
     * <b>Warning:</b> do not use this method in GWT. It returns wrong answers.
     */
    public static byte[] toByteArray(long value) {
        return new byte[]{(byte) (value >> 56), (byte) (value >> 48),
                (byte) (value >> 40), (byte) (value >> 32),
                (byte) (value >> 24), (byte) (value >> 16),
                (byte) (value >> 8), (byte) value};
    }

    /**
     * Returns the {@code long} value whose big-endian representation is stored
     * in the first 8 bytes of {@code bytes}; equivalent to {@code
     * ByteBuffer.wrap(bytes).getLong()}. For example, the input byte array
     * {@code 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19} would yield the
     * {@code long} value {@code 0x1213141516171819L}.
     * <p>
     * <p>
     * Arguably, it's preferable to use {@link ByteBuffer}; that
     * library exposes much more flexibility at little cost in readability.
     * <p>
     * <p>
     * <b>Warning:</b> do not use this method in GWT. It returns wrong answers.
     *
     * @throws IllegalArgumentException if {@code bytes} has fewer than 8 elements
     */
    public static long fromByteArray(byte[] bytes) {
        isTrue(bytes.length >= BYTES, "array too small: %s < %s",
                bytes.length, BYTES);
        return (bytes[0] & 0xFFL) << 56 | (bytes[1] & 0xFFL) << 48
                | (bytes[2] & 0xFFL) << 40 | (bytes[3] & 0xFFL) << 32
                | (bytes[4] & 0xFFL) << 24 | (bytes[5] & 0xFFL) << 16
                | (bytes[6] & 0xFFL) << 8 | (bytes[7] & 0xFFL);
    }

    /**
     * Returns an array containing the same values as {@code array}, but
     * guaranteed to be of a specified minimum length. If {@code array} already
     * has a length of at least {@code minLength}, it is returned directly.
     * Otherwise, a new array of size {@code minLength + padding} is returned,
     * containing the values of {@code array}, and zeroes in the remaining
     * places.
     *
     * @param array     the source array
     * @param minLength the minimum length the returned array must guarantee
     * @param padding   an extra amount to "grow" the array by if growth is necessary
     * @return an array containing the values of {@code array}, with guaranteed
     * minimum length {@code minLength}
     * @throws IllegalArgumentException if {@code minLength} or {@code padding} is negative
     */
    public static long[] ensureCapacity(long[] array, int minLength, int padding) {
        isTrue(minLength >= 0, "Invalid minLength: %s", minLength);
        isTrue(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding)
                : array;
    }

    // Arrays.copyOf() requires Java 6
    private static long[] copyOf(long[] original, int length) {
        long[] copy = new long[length];
        arraycopy(original, 0, copy, 0, Math
                .min(original.length, length));
        return copy;
    }

    /**
     * Returns a string containing the supplied {@code long} values separated by
     * {@code separator}. For example, {@code join("-", 1L, 2L, 3L)} returns the
     * string {@code "1-2-3"}.
     *
     * @param separator the text that should appear between consecutive values in the
     *                  resulting string (but not at the start or end)
     * @param array     an array of {@code long} values, possibly empty
     */
    public static String join(String separator, long... array) {
        notNull(separator);
        if (array.length == 0) {
            return "";
        }

        // For pre-sizing a builder, just get the right order of magnitude
        StringBuilder builder = new StringBuilder(array.length * 10);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }

    /**
     * Returns a comparator that compares two {@code long} arrays
     * lexicographically. That is, it compares, using
     * {@link #compare(long, long)}), the first pair of values that follow any
     * common prefix, or when one array is a prefix of the other, treats the
     * shorter array as the lesser. For example, {@code [] < [1L] < [1L, 2L] <
     * [2L]}.
     * <p>
     * <p>
     * The returned comparator is inconsistent with
     * {@link Object#equals(Object)} (since arrays support only identity
     * equality), but it is consistent with
     * {@link Arrays#equals(long[], long[])}.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Lexicographical_order">
     * Lexicographical order</a> article at Wikipedia
     * @since fundus
     */
    public static Comparator<long[]> lexicographicalComparator() {
        return INSTANCE;
    }

    private enum LexicographicalComparator implements Comparator<long[]> {
        INSTANCE;

        public int compare(long[] left, long[] right) {
            int minLength = min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = Longs.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }

    /**
     * Copies a collection of {@code Long} instances into a new array of
     * primitive {@code long} values.
     * <p>
     * <p>
     * Elements are copied from the argument collection as if by {@code
     * collection.toArray()}. Calling this method is as thread-safe as calling
     * that method.
     *
     * @param collection a collection of {@code Long} objects
     * @return an array containing the same values as {@code collection}, in the
     * same order, converted to primitives
     * @throws NullPointerException if {@code collection} or any of its elements is null
     */
    public static long[] toArray(Collection<Long> collection) {
        if (collection instanceof LongArrayAsList) {
            return ((LongArrayAsList) collection).toLongArray();
        }

        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        long[] array = new long[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Long) boxedArray[i];
        }
        return array;
    }

    /**
     * Returns a fixed-size list backed by the specified array, similar to
     * {@link Arrays#asList(Object[])}. The list supports
     * {@link List#set(int, Object)}, but any attempt to set a value to {@code
     * null} will result in a {@link NullPointerException}.
     * <p>
     * <p>
     * The returned list maintains the values, but not the identities, of
     * {@code Long} objects written to or read from it. For example, whether
     * {@code list.get(0) == list.get(0)} is true for the returned list is
     * unspecified.
     *
     * @param backingArray the array to back the list
     * @return a list view of the array
     */
    public static List<Long> asList(long... backingArray) {
        if (backingArray.length == 0) {
            return emptyList();
        }
        return new LongArrayAsList(backingArray);
    }

    private static class LongArrayAsList extends AbstractList<Long> implements
            RandomAccess, Serializable {
        final long[] array;
        final int start;
        final int end;

        LongArrayAsList(long[] array) {
            this(array, 0, array.length);
        }

        LongArrayAsList(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public int size() {
            return end - start;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Long get(int index) {
//			checkElementIndex(index, size());
            return array[start + index];
        }

        @Override
        public boolean contains(Object target) {
            // Overridden to prevent a ton of boxing
            return (target instanceof Long)
                    && Longs.indexOf(array, (Long) target, start, end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            // Overridden to prevent a ton of boxing
            if (target instanceof Long) {
                int i = Longs.indexOf(array, (Long) target, start, end);
                if (i >= 0) {
                    return i - start;
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            // Overridden to prevent a ton of boxing
            if (target instanceof Long) {
                int i = Longs.lastIndexOf(array, (Long) target, start, end);
                if (i >= 0) {
                    return i - start;
                }
            }
            return -1;
        }

        @Override
        public Long set(int index, Long element) {
//			checkElementIndex(index, size());
            long oldValue = array[start + index];
            array[start + index] = element;
            return oldValue;
        }

        /**
         * In GWT, List and AbstractList do not have the subList method.
         */
		/* @Override */
        public List<Long> subList(int fromIndex, int toIndex) {
            int size = size();
            indexInBounds(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return emptyList();
            }
            return new LongArrayAsList(array, start + fromIndex, start
                    + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof LongArrayAsList) {
                LongArrayAsList that = (LongArrayAsList) object;
                int size = size();
                if (that.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (array[start + i] != that.array[that.start + i]) {
                        return false;
                    }
                }
                return true;
            }
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int i = start; i < end; i++) {
                result = 31 * result + HashCodeHelper.hashCode(array[i]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(size() * 10);
            builder.append('[').append(array[start]);
            for (int i = start + 1; i < end; i++) {
                builder.append(", ").append(array[i]);
            }
            return builder.append(']').toString();
        }

        long[] toLongArray() {
            // Arrays.copyOfRange() requires Java 6
            int size = size();
            long[] result = new long[size];
            arraycopy(array, start, result, 0, size);
            return result;
        }

        private static final long serialVersionUID = 0;
    }

    public static long[] toPrimitiveArray(Long[] array) {
        if (array == null) {
            return null;
        }

        int length = array.length;
        long[] arr = new long[length];
        for (int i = 0; i < length; ++i) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static Long[] toWrapperArray(long[] array) {
        if (array == null) {
            return null;
        }

        int length = array.length;
        Long[] arr = new Long[length];
        for (int i = 0; i < length; ++i) {
            arr[i] = array[i];
        }
        return arr;
    }
}
