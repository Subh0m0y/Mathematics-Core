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
 * actually a few digits more than specified.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class BigMath {

    public static BigDecimal sqrt(final BigDecimal decimal,
                                  final MathContext context) {
        return principalRoot(decimal, 2, context);
    }

    public static BigDecimal principalRoot(final BigDecimal decimal,
                                           final int n,
                                           final MathContext context)
            throws ArithmeticException {
        if (n < 2)
            throw new ArithmeticException("'n' must at least be 2.");
        if (decimal.signum() == 0)
            return ZERO;
        if (decimal.signum() < 0)
            throw new ArithmeticException("Only positive values are supported.");
        return nthRoot(decimal, n, context);
    }

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

    public static MathContext expandContext(MathContext c0, int newPrecision) {
        return new MathContext(
                newPrecision,
                c0.getRoundingMode()    // Retain rounding mode
        );
    }

    private static final BigDecimal E_40 = new BigDecimal("2.718281828459045235360287471352662497761");

    public static BigDecimal E(MathContext context) {
        if (context.getPrecision() <= 40)   // (int) (1.2 * 34) == 40
            return E_40.round(context);
        return smallExp(ONE, context);
    }

    public static BigDecimal exp(BigDecimal x, MathContext context) {
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

        BigDecimal exp = E.pow(intExp.intValueExact(), c).multiply(
                smallExp(fraction, c), context
        );

        return x.signum() < 0 ? ONE.divide(exp, context) : exp;
    }

    private static BigDecimal smallExp(BigDecimal x, MathContext context) {
        // Quick exit
        if (x.signum() == 0) return ONE;

        BigDecimal num = x;                 // Numerator
        BigDecimal den = ONE;               // Denominator
        // The actual of result of num/den
        BigDecimal term = num.divide(den, context);
        BigDecimal sum = ONE;               // Accumulator
        // The tolerable error
        BigDecimal eps = new BigDecimal(BigInteger.ONE, context.getPrecision() + 1);
        long i = 1; // The factorial variable
        while (term.compareTo(eps) > 0) {
            term = num.divide(den, context);
            sum = sum.add(term, context);
            den = den.multiply(valueOf(++i));
            num = num.multiply(x);
        }
        return sum;
    }

    public static BigDecimal log(BigDecimal x, MathContext context)
            throws ArithmeticException {
        if (x.signum() <= 0) {
            throw new ArithmeticException("Invalid value: can't handle 0 and negatives.");
        }
        MathContext c = expandContext(context, context.getPrecision() + 1);
        BigDecimal E = E(c);
        BigDecimal intExp = ZERO;

        // Work out the integral part of the exponent
        while (x.compareTo(E) > 0) {
            x = x.divide(E, c);
            intExp = intExp.add(ONE);
        }

        return intExp.add(smallLog(x, c), context);
    }

    private static BigDecimal smallLog(BigDecimal x, MathContext c) {
        BigDecimal term = (x.subtract(ONE)).divide(x.add(ONE), c);
        BigDecimal sq = term.multiply(term, c);
        BigDecimal eps = new BigDecimal(BigInteger.ONE, c.getPrecision() + 1);

        BigDecimal sum = term;  // The accumulator
        long den = 3;           // The denominator

        while (term.compareTo(eps) > 0) {
            term = term.multiply(sq, c);
            sum = sum.add(term.divide(valueOf(den), c));
            den += 2;
        }

        return sum.add(sum, c); // The final multiplication by 2
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

        BigDecimal eps = new BigDecimal(BigInteger.ONE, c.getPrecision() + 1);

        while (a.subtract(b).abs().compareTo(eps) > 0) {
            BigDecimal A = a.add(b).multiply(HALF, c);
            b = BigMath.sqrt(a.multiply(b), c);
            t = t.subtract(p.multiply(A.subtract(a).pow(2)), c);
            p = p.add(p);
            a = A;
        }

        return a.add(b).pow(2).divide(FOUR.multiply(t), context);
    }

    public static BigDecimal[] sinAndCos(BigDecimal x, MathContext context) {
        // Restrict the domain first
        x = x.remainder(PI(context).multiply(HALF));

        return sinCos(x, expandContext(context, context.getPrecision() + 2));
    }

    public static BigDecimal sin(BigDecimal x, MathContext context) {
        return sinAndCos(x, context)[0];
    }

    public static BigDecimal cos(BigDecimal x, MathContext context) {
        return sinAndCos(x, context)[1];
    }

    public static BigDecimal tan(BigDecimal x, MathContext context) {
        if (x.remainder(PI(context).multiply(HALF)).signum() == 0) {
            throw new ArithmeticException("Tan undefined at multiples of pi/2.");
        }
        BigDecimal[] a = sinAndCos(x, context);
        return a[0].round(context).divide(a[1].round(context), context);
    }

    /**
     * Works only for restricted values of x. Restriction is within the interval
     * -pi/2 to +pi/2.
     */
    private static BigDecimal[] sinCos(BigDecimal x, MathContext c) {
        BigDecimal eps = new BigDecimal(BigInteger.ONE, c.getPrecision() + 1);

        BigDecimal term = ONE;
        BigDecimal sin = ZERO;
        BigDecimal cos = ONE;

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
}
