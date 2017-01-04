/*
 * MIT License
 *
 * Copyright (c) 2016-2017 Subhomoy Haldar (github.com/Subh0m0y)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
//codebeat:disable[TOO_MANY_FUNCTIONS]

package mathcore.ops;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.*;

/**
 * Provides implementations for commonly used mathematical functions. All
 * algorithms are arbitrary-precision and require a MathContext to specify
 * the amount of precision needed.
 * <p>
 * For accuracy reasons, the precision used internally in the algorithms is
 * actually a few digits more than specified. The same approach is encouraged
 * for practical computations as well, to allow room for rounding errors that
 * accumulate in a series of calculations. This means, use an expanded
 * {@link MathContext} for the intermediate calculations and obtain the
 * final answer with the desired MathContext. Use {@link #expandContext(MathContext, int)}
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class BigMath {

    // Make this class un-instantiable
    private BigMath() {
    }

    /**
     * Returns the square root of the given positive {@link BigDecimal} value.
     * The result has two extra bits of precision to ensure better accuracy.
     *
     * @param decimal The value whose square root is sought.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The square root of {@code decimal}.
     * @throws ArithmeticException If {@code decimal} is negative.
     */
    public static BigDecimal sqrt(final BigDecimal decimal,
                                  final MathContext context)
            throws ArithmeticException {
        return Roots.principalRoot(decimal, 2, context);
    }

    /**
     * Returns the principal n-th root of the given positive value.
     *
     * @param decimal The value whose n-th root is sought.
     * @param n       The value of n needed.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The principal n-th root of {@code decimal}.
     * @throws ArithmeticException If n is lesser than 2 or {@code decimal} is negative.
     */
    public static BigDecimal principalRoot(final BigDecimal decimal,
                                           final int n,
                                           final MathContext context) {
        return Roots.principalRoot(decimal, n, context);
    }

    /**
     * A utility method that helps obtain a new {@link MathContext} from an existing
     * one. The new Context has the new precision specified but retains the {@link java.math.RoundingMode}.
     * <p>
     * Usually, it is used to increase the precision and hence "expand" the Context.
     *
     * @param c0           The initial {@link MathContext}.
     * @param newPrecision The required precision.
     * @return The new expanded Context.
     */
    public static MathContext expandContext(MathContext c0, int newPrecision) {
        return new MathContext(
                newPrecision,
                c0.getRoundingMode()    // Retain rounding mode
        );
    }

    /**
     * Returns the value of {@code e}, or Euler's constant, the base of the natural logarithm
     * with the specified precision.
     *
     * @param context The precision needed.
     * @return The value of e with the desired precision.
     */
    public static BigDecimal E(MathContext context) {
        return Exponential.E(context);
    }

    /**
     * Returns the value obtained on raising e to the power x.
     *
     * @param x       The power to raise e to.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The required value of e<sup>x</sup>.
     * @throws ArithmeticException If abs(x) is too big (bigger than 999999999).
     */
    public static BigDecimal exp(BigDecimal x, MathContext context)
            throws ArithmeticException {
        return Exponential.exp(x, context);
    }

    /**
     * Calculates the natural logarithm of all positive values of x (i.e.
     * non-zero and non-negative).
     *
     * @param x       The positive argument.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The natural logarithm of x.
     * @throws ArithmeticException If x is zero or negative.
     */
    public static BigDecimal log(BigDecimal x, MathContext context)
            throws ArithmeticException {
        return Logarithm.log(x, context);
    }

    /**
     * Raises the given bass to the specified exponent. All values of y are
     * supported in the range [-999999999, 999999999]. (This is because of
     * the limitation imposed by {@link BigDecimal#pow(int)} internally.)
     * <p>
     * NOTE: Only positive values of x (the base) are supported.
     *
     * @param x       The base.
     * @param y       The exponent.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The base raised to the given exponent.
     * @throws ArithmeticException If x is negative or y is too big.
     */
    public static BigDecimal pow(BigDecimal x,
                                 BigDecimal y,
                                 MathContext context) {
        return Exponential.pow(x, y, context);
    }

    /**
     * Computes the value of the circle constant: &pi; (pi) as per the
     * specified MathContext.
     *
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The value of pi with the required precision.
     */
    public static BigDecimal PI(MathContext context) {
        return CircleConstant.PI(context);
    }

    private static final BigDecimal ONE80 = BigDecimal.valueOf(180);

    /**
     * Converts the given value in degrees to radians.
     *
     * @param degrees The angle to convert.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The required angle in radians.
     */
    public static BigDecimal toRadians(BigDecimal degrees,
                                       MathContext context) {
        return degrees.multiply(PI(context)).divide(ONE80, context);
    }

    /**
     * Converts the given value in radians to degrees.
     *
     * @param radians The angle to convert.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The required angle in degrees.
     */
    public static BigDecimal toDegrees(BigDecimal radians,
                                       MathContext context) {
        return radians.multiply(ONE80).divide(PI(context), context);
    }

    /**
     * Calculates the sine and cosine of the given value (in radians) at
     * the same time. The first element in the array is the sine and the
     * second element is the cosine.
     * <p>
     * This method is useful because the taylor series of sin and cos are
     * complementary and calculating them together is often faster than
     * calculating each separately.
     * <p>
     * This method works for all values of x.
     *
     * @param x       The argument (in radians).
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The sine and cosine of x.
     */
    public static BigDecimal[] sinAndCos(BigDecimal x, MathContext context) {
        return Trigonometry.sinAndCos(x, context);
    }

    /**
     * Calculates the sine of the given value (in radians).
     * <p>
     * NOTE: If you need both sin(x) and cos(x), it is recommended to use
     * {@link #sinAndCos(BigDecimal, MathContext)}
     *
     * @param x       The argument (in radians).
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The sine of x.
     */
    public static BigDecimal sin(BigDecimal x, MathContext context) {
        return sinAndCos(x, context)[0];
    }

    /**
     * Calculates the cosine of the given value (in radians).
     * <p>
     * NOTE: If you need both sin(x) and cos(x), it is recommended to use
     * {@link #sinAndCos(BigDecimal, MathContext)}
     *
     * @param x       The argument (in radians).
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The cosine of x.
     */
    public static BigDecimal cos(BigDecimal x, MathContext context) {
        return sinAndCos(x, context)[1];
    }

    /**
     * Calculates the tangent of the given argument (in radians).
     * It is (in the simplest sense) calculated as sin(x)/cos(x).
     *
     * @param x       The argument (in radians).
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The tangent of x
     */
    public static BigDecimal tan(BigDecimal x, MathContext context) {
        return Trigonometry.tan(x, context);
    }

    /**
     * Calculates the arcsine of the given BigDecimal.
     * <p>
     * The argument must lie in [-1, 1].
     *
     * @param z       The value whose arcsine is to be calculated.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The arcsine of the given value.
     * @throws ArithmeticException If abs(z) > 1.
     */
    public static BigDecimal arcsin(BigDecimal z, MathContext context)
            throws ArithmeticException {
        return InverseTrigonometry.arcsin(z, context);
    }

    /**
     * Calculates the arccosine of the given BigDecimal.
     * <p>
     * The argument must lie in [-1, 1].
     *
     * @param z       The value whose arccosine is to be calculated.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The arccosine of the given value.
     * @throws ArithmeticException If abs(z) > 1.
     */
    public static BigDecimal arccos(BigDecimal z, MathContext context)
            throws ArithmeticException {
        return InverseTrigonometry.arccos(z, context);
    }

    /**
     * Calculates the arctangent of the given value.
     *
     * @param z       The value whose arctangent is to be calculated.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The arctangent of the given value.
     */
    public static BigDecimal arctan(BigDecimal z, MathContext context) {
        return atan2(z, ONE, context);
    }

    /**
     * Calculates the arctangent of the given ratio y/x.
     * <p>
     * NOTE: The order is important. The vertical component (y) comes first,
     * then the horizontal component (x).
     * <p>
     * This method is advantageous over the single-argument method because
     * it provides the option for specifying 1/0 and -1/0, whose arctangents
     * are &pi;/2 and -&pi;/2 respectively.
     *
     * @param y       The vertical component.
     * @param x       The horizontal component.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The arctangent of the given ratio.
     * @throws ArithmeticException If the ratio is 0/0 or undefined.
     */
    public static BigDecimal atan2(BigDecimal y, BigDecimal x, MathContext context)
            throws ArithmeticException {
        return InverseTrigonometry.atan2(y, x, context);
    }
}
