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
import static mathcore.ops.BigMath.PI;
import static mathcore.ops.BigMath.expandContext;
import static mathcore.ops.BigMath.sqrt;
import static mathcore.ops.BigMath.eps;

/**
 * A portion of BigMath refactored out to reduce overall complexity.
 * <p>
 * This class handles the inverse trigonometric functions.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class InverseTrigonometry {
    // Make this class un-instantiable
    private InverseTrigonometry() {
    }

    private static final BigDecimal HALF_ANGLE_THRESHOLD = new BigDecimal("0.8");
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal HALF = new BigDecimal("0.5");

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
        // The Taylor series becomes horribly slow for values >= 1.
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
        return atanTaylor(x, c);
    }

    /**
     * Evaluates the Taylor series for arctan for normalized values of x.
     *
     * @param x The normalized argument.
     * @param c The expanded MathContext.
     * @return The arctangent of x.
     */
    private static BigDecimal atanTaylor(BigDecimal x, MathContext c) {
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
     * Calculates the arctangent of the given ratio y/x.
     * <p>
     * NOTE: The order is important. The vertical component (y) comes first,
     * then the horizontal component (x).
     *
     * @param y       The vertical component.
     * @param x       The horizontal component.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The arctangent of the given ratio.
     * @throws ArithmeticException If the ratio is 0/0 or undefined.
     */
    static BigDecimal atan2(BigDecimal y, BigDecimal x, MathContext context)
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
    static BigDecimal arcsin(BigDecimal z, MathContext context)
            throws ArithmeticException {
        int zMinusOne = z.abs().compareTo(ONE);
        if (zMinusOne > 0) {
            throw new ArithmeticException("Illegal argument for arcsine: |z| > 1");
        } else if (zMinusOne == 0) {
            BigDecimal result = PI(context).multiply(HALF);
            return z.signum() > 0 ? result : result.negate();
        }
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.2));
        return atan2(z, sqrt(ONE.subtract(z.pow(2)), c), c);
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
    static BigDecimal arccos(BigDecimal z, MathContext context)
            throws ArithmeticException {
        int zMinusOne = z.abs().compareTo(ONE);
        if (zMinusOne > 0) {
            throw new ArithmeticException("Illegal argument for arccosine: |z| > 1");
        } else if (zMinusOne == 0) {
            return ZERO;
        }
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.2));
        return atan2(sqrt(ONE.subtract(z.pow(2)), c), z, c);
    }
}
