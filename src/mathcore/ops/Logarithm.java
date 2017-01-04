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
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static mathcore.ops.BigMath.expandContext;
import static mathcore.ops.test.Helper.eps;

/**
 * A portion of BigMath refactored out to reduce overall complexity.
 * <p>
 * This class handles the logarithm algorithm.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class Logarithm {
    // Make this class un-instantiable
    private Logarithm() {
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
    static BigDecimal log(BigDecimal x, MathContext context)
            throws ArithmeticException {
        if (x.signum() <= 0) {
            throw new ArithmeticException("Invalid value: can't handle 0 and negatives.");
        }
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.5));
        BigDecimal E = Exponential.E(c);
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
}
