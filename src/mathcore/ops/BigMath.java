/*
 * MIT License
 *
 * Copyright (c) 2016 Subhomoy Haldar (github.com/Subh0m0y)
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

package mathcore.ops;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.*;
import static mathcore.ops.test.Helper.eps;

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

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal HALF = new BigDecimal("0.5");

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
        // Apply range reduction
        BigDecimal pi = PI(context);
        BigDecimal pi2 = pi.multiply(TWO);

        while (x.signum() < 0) {
            x = x.add(pi2);
        }

        // sin/cos(2pi*n + x) = sin/cos(x)
        if (x.compareTo(pi2) >= 0) {
            return sinAndCos(x.remainder(pi2), context);
        }
        // sin/cos(pi + x) = -sin/cos(x)
        if (x.compareTo(pi) >= 0) {
            BigDecimal[] v = sinAndCos(x.subtract(pi), context);
            v[0] = v[0].negate();
            v[1] = v[1].negate();
            return v;
        }

        BigDecimal piB2 = pi.multiply(HALF);

        // sin/cos(pi/2 + x) = cos/-sin(x)
        if (x.compareTo(piB2) >= 0) {
            BigDecimal[] v = sinAndCos(x.subtract(piB2), context);
            return new BigDecimal[]{v[1], v[0].negate()};
        }

        return sinCos(x, expandContext(context, context.getPrecision() + 2));
    }

    public static BigDecimal sin(BigDecimal x, MathContext context) {
        return sinAndCos(x, context)[0];
    }

    public static BigDecimal cos(BigDecimal x, MathContext context) {
        return sinAndCos(x, context)[1];
    }

    public static BigDecimal tan(BigDecimal x, MathContext context) {
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.2));
        if (x.signum() == 0) {
            return ZERO;
        }
        if (x.remainder(PI(c).multiply(HALF)).signum() == 0) {
            throw new ArithmeticException("Tan undefined at multiples of pi/2.");
        }
        BigDecimal[] a = sinAndCos(x, c);
        return a[0].round(c).divide(a[1].round(c), c);
    }

    /**
     * Accepts only range-reduced values of x.
     */
    private static BigDecimal[] sinCos(BigDecimal x, MathContext c) {
        BigDecimal eps = eps(c);

        BigDecimal term = ONE;
        BigDecimal sin = ZERO;
        BigDecimal cos = ONE;

        // Iterate through all the powers of x and add or subtract to the
        // corresponding variable.
        for (int i = 1; term.abs().compareTo(eps) > 0; i++) {
            term = term.multiply(x).divide(BigDecimal.valueOf(i), c);
            switch (i % 4) {
                case 0:
                    cos = cos.add(term, c);
                    break;
                case 1:
                    sin = sin.add(term, c);
                    break;
                case 2:
                    cos = cos.subtract(term, c);
                    break;
                case 3:
                    sin = sin.subtract(term, c);
                    break;
            }
        }
        return new BigDecimal[]{sin, cos};
    }

    public static BigDecimal arcsin(BigDecimal z, MathContext context) {
        int cmp = z.abs().compareTo(ONE);
        if (cmp > 0) {
            throw new ArithmeticException("Illegal argument for arcsine: |z| > 1");
        } else if (cmp == 0) {
            BigDecimal result = PI(context).multiply(HALF);
            return z.signum() > 0 ? result : result.negate();
        }
        MathContext c = expandContext(context, context.getPrecision() + 2);
        return arctan(z.divide(sqrt(ONE.subtract(z.pow(2)), c), c), context);
    }

    public static BigDecimal arccos(BigDecimal z, MathContext context) {
        if (z.abs().compareTo(ONE) > 0) {
            throw new ArithmeticException("Illegal argument for arccosine: |z| > 1");
        }
        BigDecimal x = sqrt(ONE.subtract(z.pow(2)), context);
        BigDecimal arcsin = arcsin(x, context);
        return z.signum() < 0 ? PI(context).subtract(arcsin) : arcsin;
    }

    public static BigDecimal arctan(BigDecimal z, MathContext context) {
        return atan2(z, ONE, context);
    }

    public static BigDecimal atan2(BigDecimal y, BigDecimal x, MathContext context)
            throws ArithmeticException {
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.2));
        if (x.signum() == 0) {
            // The exceptional cases
            if (y.signum() == 0) {          // 0/0
                throw new ArithmeticException("Undefined: atan2(0,0)");
            } else if (y.signum() > 0) {    // +infinity
                return PI(c).multiply(HALF);
            } else {                        // -infinity
                return PI(c).multiply(HALF).negate();
            }
        } else if (x.signum() < 0) {
            // Negative values
            if (y.signum() == 0) {
                return ZERO;
            } else if (y.signum() > 0) {
                return atan(y.divide(x, c), c).add(PI(c));
            } else {
                return atan(y.divide(x, c), c).subtract(PI(c));
            }
        } else {
            // The normal case
            return atan(y.divide(x, c), c);
        }
    }

    private static final BigDecimal HALF_ANGLE_THRESHOLD = new BigDecimal("0.8");

    /**
     * Returns the arctangent of the given BigDecimal. It works only for
     * finite values (for infinity, use atan2)
     *
     * @param x The argument.
     * @param c The expanded MathContext.
     * @return The arctangent of the given BigDecimal.
     */
    private static BigDecimal atan(BigDecimal x, MathContext c) {
        // atan(-x) = -atan(x)
        if (x.signum() < 0) {
            return atan(x.negate(), c).negate();
        }
        // The Taylor series becomes horribly slow for values ~ 1.
        if (x.compareTo(ONE) > 0) {
            return PI(c).multiply(HALF).subtract(atan(ONE.divide(x, c), c));
        }
        // Even at 1, atan is horribly slow, so reduce the argument
        // to a smaller value.
        if (x.compareTo(HALF_ANGLE_THRESHOLD) > 0) {
            BigDecimal part = ONE.add(x.pow(2));
            BigDecimal den = sqrt(part, c).add(ONE);

            //                 x
            // newX =  -----------------
            //         sqrt(1 + x^2) + 1
            BigDecimal newX = x.divide(den, c);
            return TWO.multiply(atan(newX, c));
        }
        BigDecimal eps = eps(c);

        BigDecimal minusXSquared = x.pow(2, c).negate();

        BigDecimal num = x;
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal term = x;

        for (int i = 1; term.abs().compareTo(eps) > 0; i += 2) {
            term = num.divide(BigDecimal.valueOf(i), c);
            num = num.multiply(minusXSquared);
            sum = sum.add(term, c);
        }

        return sum;
    }
}
