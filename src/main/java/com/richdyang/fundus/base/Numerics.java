package com.richdyang.fundus.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import com.richdyang.fundus.base.primitives.Primitives;
import com.richdyang.fundus.base.text.Texts;

import static com.richdyang.fundus.base.ArgumentAssert.isTrue;
import static com.richdyang.fundus.base.ArgumentAssert.notNull;
import static com.richdyang.fundus.base.primitives.Primitives.wrap;
import static com.richdyang.fundus.base.text.Texts.MutateHelper;
import static com.richdyang.fundus.base.text.Texts.MutateHelper.trimAllWhitespace;
import static java.lang.Byte.*;
import static java.math.BigInteger.valueOf;


/**
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @version $Revision: 1.15 $Date:2010-1-26 10:55:12 $
 * @since fundus
 */
public final class Numerics {

    private static Class<?>[] PRIMITIVE_NUMBER_TYPE = new Class<?>[]{
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class
    };

    /**
     * determine whether class is primitive number type
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveNumericType(Class<?> clazz) {
        for (Class cl : PRIMITIVE_NUMBER_TYPE) {
            if (clazz == cl) {
                return true;
            }
        }
        return false;
    }

    /**
     * determine whether class is primitive number type or Number type
     *
     * @param clazz
     * @return
     */
    public static boolean isNumericType(Class<?> clazz) {
        return isPrimitiveNumericType(clazz) || Number.class.isAssignableFrom(clazz);
    }

    /**
     * Convert the given number into an instance of the given target class.
     *
     * @param number      the number to convert
     * @param targetClass the target class to convert to
     * @return the converted number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see Byte
     * @see Short
     * @see Integer
     * @see Long
     * @see BigInteger
     * @see Float
     * @see Double
     * @see BigDecimal
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumeric(Number number, Class<T> targetClass)
            throws IllegalArgumentException {

        notNull(number, "Number must not be null");
        notNull(targetClass, "Target class must not be null");

        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (targetClass.equals(Byte.class)) {
            long value = number.longValue();
            if (value < MIN_VALUE || value > MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Byte(number.byteValue());
        } else if (targetClass.equals(Short.class)) {
            long value = number.longValue();
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Short(number.shortValue());
        } else if (targetClass.equals(Integer.class)) {
            long value = number.longValue();
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Integer(number.intValue());
        } else if (targetClass.equals(Long.class)) {
            return (T) new Long(number.longValue());
        } else if (targetClass.equals(BigInteger.class)) {
            if (number instanceof BigDecimal) {
                // do not lose precision - use BigDecimal's own conversion
                return (T) ((BigDecimal) number).toBigInteger();
            } else {
                // original value is not a Big* number - use standard long conversion
                return (T) valueOf(number.longValue());
            }
        } else if (targetClass.equals(Float.class)) {
            return (T) new Float(number.floatValue());
        } else if (targetClass.equals(Double.class)) {
            return (T) new Double(number.doubleValue());
        } else if (targetClass.equals(BigDecimal.class)) {
            // always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
            // (see BigDecimal javadoc for details)
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                    number.getClass().getName() + "] to unknown target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Raise an overflow exception for the given number and target class.
     *
     * @param number      the number we tried to convert
     * @param targetClass the target class we tried to convert to
     */
    private static void raiseOverflowException(Number number, Class targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    /**
     * Parse the given text into a number instance of the given target class,
     * using the corresponding <code>decode</code> / <code>valueOf</code> methods.
     * <p>Trims the input <code>String</code> before attempting to parse the number.
     * Supports numbers in hex format (with leading "0x", "0X" or "#") as well.
     * <p>
     * NOTE: <strong>Primitive number type supported!!</strong> (such as int.class, short.class ...)
     *
     * @param text        the text to convert [$must be not null$]
     * @param targetClass the target class to parse into
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see Byte#decode
     * @see Short#decode
     * @see Integer#decode
     * @see Long#decode
     * @see #decodeBigInteger(String)
     * @see Float#valueOf
     * @see Double#valueOf
     * @see BigDecimal#BigDecimal(String)
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseNumeric(String text, Class<T> targetClass) {
        notNull(text, "Text must not be null");
        notNull(targetClass, "Target class must not be null");
        isTrue(isNumericType(targetClass), "Target class must be number type");

        if (isPrimitiveNumericType(targetClass)) {
            targetClass = (Class<T>) wrap(targetClass);
        }
        String trimmed = trimAllWhitespace(text);

        if (targetClass.equals(Byte.class)) {
            return (T) (isHexNumber(trimmed) ? decode(trimmed) : Byte.valueOf(trimmed));
        } else if (targetClass.equals(Short.class)) {
            return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
        } else if (targetClass.equals(Integer.class)) {
            return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
        } else if (targetClass.equals(Long.class)) {
            return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
        } else if (targetClass.equals(BigInteger.class)) {
            return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
        } else if (targetClass.equals(Float.class)) {
            return (T) Float.valueOf(trimmed);
        } else if (targetClass.equals(Double.class)) {
            return (T) Double.valueOf(trimmed);
        } else if (targetClass.equals(BigDecimal.class) || targetClass.equals(Number.class)) {
            return (T) new BigDecimal(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Parse the given text into a number instance of the given target class,
     * using the given NumberFormat. Trims the input <code>String</code>
     * before attempting to parse the number.
     *
     * @param text         the text to convert
     * @param targetClass  the target class to parse into
     * @param numberFormat the NumberFormat to use for parsing (if <code>null</code>,
     *                     this method falls back to <code>parseNumber(String, Class)</code>)
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see NumberFormat#parse
     * @see #convertNumberToTargetClass
     * @see #parseNumeric(String, Class)
     */
    public static <T extends Number> T parseNumeric(String text, Class<T> targetClass, NumberFormat numberFormat) {
        if (numberFormat != null) {
            notNull(text, "Text must not be null");
            notNull(targetClass, "Target class must not be null");
            DecimalFormat decimalFormat = null;
            boolean resetBigDecimal = false;
            if (numberFormat instanceof DecimalFormat) {
                decimalFormat = (DecimalFormat) numberFormat;
                if (BigDecimal.class.equals(targetClass) && !decimalFormat.isParseBigDecimal()) {
                    decimalFormat.setParseBigDecimal(true);
                    resetBigDecimal = true;
                }
            }
            try {
                Number number = numberFormat.parse(trimAllWhitespace(text));
                return convertNumeric(number, targetClass);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse number: " + ex.getMessage());
            } finally {
                if (resetBigDecimal) {
                    decimalFormat.setParseBigDecimal(false);
                }
            }
        } else {
            return parseNumeric(text, targetClass);
        }
    }

    /**
     * Determine whether the given value String indicates a hex number, i.e. needs to be
     * passed into <code>Integer.decode</code> instead of <code>Integer.valueOf</code> (etc).
     */
    private static boolean isHexNumber(String value) {
        int index = (value.startsWith("-") ? 1 : 0);
        return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
    }

    /**
     * Decode a {@link BigInteger} from a {@link String} value.
     * Supports decimal, hex and octal notation.
     *
     * @see BigInteger#BigInteger(String, int)
     */
    private static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;

        // Handle minus sign, if present.
        if (value.startsWith("-")) {
            negative = true;
            index++;
        }

        // Handle radix specifier, if present.
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith("#", index)) {
            index++;
            radix = 16;
        } else if (value.startsWith("0", index) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return (negative ? result.negate() : result);
    }
}
