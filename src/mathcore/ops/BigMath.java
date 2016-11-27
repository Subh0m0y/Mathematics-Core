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
import java.math.BigInteger;
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
        return principalRoot(decimal, 2, context);
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
                                           final MathContext context)
            throws ArithmeticException {
        if (n < 2)
            throw new ArithmeticException("'n' must at least be 2.");
        // Quick exits
        if (decimal.signum() == 0)
            return ZERO;
        if (decimal.compareTo(ONE) == 0)
            return ONE;
        if (decimal.signum() < 0)
            throw new ArithmeticException("Only positive values are supported.");
        return nthRoot(decimal, n, context);
    }

    /**
     * Uses the n-th root algorithm to find principal root of a verified value.
     *
     * @param a  The value whose n-th root is sought.
     * @param n  The root to find.
     * @param c0 The initial (unexpanded) MathContext.
     * @return The required principal root.
     */
    private static BigDecimal nthRoot(final BigDecimal a,
                                      final int n,
                                      final MathContext c0) {
        // Obtain constants that will be used in every iteration
        final BigDecimal N = valueOf(n);
        final int n_1 = n - 1;

        // Increase precision by "n";
        final int newPrecision = c0.getPrecision() + n;

        final MathContext c = expandContext(c0, newPrecision);

        // The iteration limit (quadratic convergence)
        final int limit = n * n * (31 - Integer.numberOfLeadingZeros(newPrecision)) >>> 1;

        // Make an initial guess:

        // 1. Obtain first (1/n)th of total bits of magnitude
        BigInteger magnitude = a.unscaledValue();
        final int length = magnitude.bitLength() * n_1 / n;
        magnitude = magnitude.shiftRight(length);

        // 2. Obtain the correct scale
        final int newScale = a.scale() / n;

        // 3. Construct the initial guess
        BigDecimal x = new BigDecimal(magnitude, newScale);
        BigDecimal x0;

        // Iterate
        for (int i = 0; i < limit; i++) {
            x0 = x;
            BigDecimal delta = a.divide(x0.pow(n_1), c)
                    .subtract(x0, c)
                    .divide(N, c);
            x = x0.add(delta, c);
        }

        return x.round(c);
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

    private static final BigDecimal E_40 = new BigDecimal("2.718281828459045235360287471352662497761");

    /**
     * Returns the value of {@code e}, or Euler's constant, the base of the natural logarithm
     * with the specified precision.
     *
     * @param context The precision needed.
     * @return The value of e with the desired precision.
     */
    public static BigDecimal E(MathContext context) {
        if (context.getPrecision() <= 40)   // (int) (1.2 * 34) == 40
            return E_40.round(context);
        return smallExp(ONE, context);
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
        // Quick exit
        if (x.signum() == 0) return ONE;

        // Perform the calculations with a bigger context
        int newPrecision = (int) (context.getPrecision() * 1.2);
        MathContext c = expandContext(context, newPrecision);
        // The value of e for the integral portion
        BigDecimal E = E(c);
        BigDecimal abs = x.abs();
        BigInteger intExp = abs.toBigInteger();
        BigDecimal fraction = abs.subtract(new BigDecimal(intExp));

        // If the integral part is too large, an exception maybe thrown.
        BigDecimal exp = E.pow(intExp.intValueExact(), c).multiply(
                smallExp(fraction, c), c
        );

        return x.signum() < 0 ? ONE.divide(exp, c) : exp;
    }

    /**
     * Uses the Taylor series to approximate exp() for small values of x.
     *
     * @param x       A small positive argument (less than e).
     * @param context The The MathContext to specify the precision and RoundingMode.
     * @return exp(x) for small positive value of x.
     */
    private static BigDecimal smallExp(BigDecimal x, MathContext context) {
        // Quick exit
        if (x.signum() == 0) return ONE;

        BigDecimal num = x;                 // Numerator
        BigDecimal den = ONE;               // Denominator
        // The actual of result of num/den
        BigDecimal term = num.divide(den, context);
        BigDecimal sum = ONE;               // Accumulator
        // The tolerable error
        BigDecimal eps = eps(context);
        long i = 1; // The factorial variable
        while (term.compareTo(eps) > 0) {
            term = num.divide(den, context);
            sum = sum.add(term, context);
            den = den.multiply(valueOf(++i));
            num = num.multiply(x);
        }
        return sum;
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
        if (x.signum() <= 0) {
            throw new ArithmeticException("Invalid value: can't handle 0 and negatives.");
        }
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.5));
        BigDecimal E = E(c);
        BigDecimal intExp = ZERO;

        // Work out the integral part of the exponent
        while (x.compareTo(E) > 0) {
            x = x.divide(E, c);
            intExp = intExp.add(ONE);
        }
        // Correction for subnormal arguments
        while (x.compareTo(ONE) < 0) {
            x = x.multiply(E, c);
            intExp = intExp.subtract(ONE);
        }

        return intExp.add(smallLog(x, c), c);
    }

    /**
     * Returns the natural logarithm of "small", normalized values of x.
     *
     * @param x The normalized argument.
     * @param c The expanded MathContext.
     * @return The natural logarithm of "small", normalized values of x.
     */
    private static BigDecimal smallLog(BigDecimal x, MathContext c) {
        BigDecimal term = (x.subtract(ONE)).divide(x.add(ONE), c);
        BigDecimal sq = term.multiply(term, c);
        BigDecimal eps = eps(c);

        BigDecimal sum = term;  // The accumulator
        long den = 3;           // The denominator

        while (term.compareTo(eps) > 0) {
            term = term.multiply(sq, c);
            sum = sum.add(term.divide(valueOf(den), c));
            den += 2;
        }

        return sum.add(sum, c); // The final multiplication by 2
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
        // Quick exits
        if (x.signum() < 0) {
            throw new ArithmeticException("Negative numbers not supported.");
        }
        if (x.signum() == 0) {
            return ZERO;
        }
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.2));

        // Tackle the integral and the fractional part separately
        BigDecimal abs = y.abs();
        int p = abs.toBigInteger().intValueExact();
        BigDecimal f = abs.remainder(ONE);

        BigDecimal v = x.pow(p).multiply(exp(f.multiply(log(x, c)), c));

        // Finally tackle the sign
        return y.signum() < 0 ? ONE.divide(v, c) : v;
    }

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);

    private static final BigDecimal HALF = new BigDecimal("0.5");
    private static final BigDecimal FOURTH = new BigDecimal("0.25");

    private static final BigDecimal PI_40 = new BigDecimal("3.141592653589793238462643383279502884197");

    public static BigDecimal PI(MathContext context) {
        if (context.getPrecision() <= 0) {
            return PI_40.round(context);
        }

        // Use the AGM algorithm (aka Gauss–Legendre algorithm)
        // https://en.wikipedia.org/wiki/Gauss%E2%80%93Legendre_algorithm

        MathContext c = expandContext(context, context.getPrecision() + 1);

        BigDecimal a = ONE;
        BigDecimal b = ONE.divide(sqrt(TWO, c), c);
        BigDecimal t = FOURTH;
        BigDecimal p = ONE;

        BigDecimal eps = eps(c);

        while (a.subtract(b).abs().compareTo(eps) > 0) {
            // The basic steps are:
            // 1. A.M. = (a + b) / 2
            // 2. G.M. = sqrt(a * b)
            // 3. t = t - p * (A.M. - a)^2
            // 4. p = 2 * p
            // 5. a = A.M.
            // 6. b = G.M.
            // Repeat until a and b are equal (within context)
            BigDecimal A = a.add(b).multiply(HALF, c);
            b = BigMath.sqrt(a.multiply(b), c);
            t = t.subtract(p.multiply(A.subtract(a).pow(2)), c);
            p = p.add(p);
            a = A;
        }
        //      (a + b)^2
        // pi = ---------
        //          4t
        return a.add(b).pow(2).divide(FOUR.multiply(t), context);
    }

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
            return TWO.multiply(atan(x.divide(
                    sqrt(ONE.add(x.pow(2)), c).add(ONE), c), c));
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

    /**
     * Returns the epsilon required for the specified context.
     *
     * @param context The context to generate the epsilon for.
     * @return The required epsilon.
     */
    private static BigDecimal eps(MathContext context) {
        return new BigDecimal(BigInteger.ONE, context.getPrecision() + 1);
    }
}
