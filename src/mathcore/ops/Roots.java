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

package mathcore.ops;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

/**
 * A portion of BigMath refactored out to reduce overall complexity.
 * <p>
 * This class handles the calculation of n-th principal roots.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class Roots {
    // Make this class un-instantiable
    private Roots() {
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

        final MathContext c = BigMath.expandContext(c0, newPrecision);

        // The iteration limit (quadratic convergence)
        final int limit = n * n * (31 - Integer.numberOfLeadingZeros(newPrecision)) >>> 1;

        // Make an initial guess:
        BigDecimal x = guessRoot(a, n);
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
     * Constructs an initial guess for the n-th principal root of
     * a given positive value.
     *
     * @param a The value whose n-th root is sought.
     * @param n The root to find.
     * @return An initial guess.
     */
    private static BigDecimal guessRoot(BigDecimal a, int n) {
        // 1. Obtain first (1/n)th of total bits of magnitude
        BigInteger magnitude = a.unscaledValue();
        final int length = magnitude.bitLength() * (n - 1) / n;
        magnitude = magnitude.shiftRight(length);

        // 2. Obtain the correct scale
        final int newScale = a.scale() / n;

        // 3. Construct the initial guess
        return new BigDecimal(magnitude, newScale);
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
    static BigDecimal principalRoot(final BigDecimal decimal,
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
}
