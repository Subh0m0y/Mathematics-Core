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

package mathcore.ops.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

/**
 * This is a "Helper" class in the sense that it contains useful utility
 * methods for generating random BigDecimals (for testing purposes) as per
 * the situation. This helps remove a lot of repetition from the code.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Helper {
    /**
     * Cached copy of the reciprocal of ln(2). Used in converting no. of
     * bits to number of decimal digits.
     */
    private static final double LOG2_INV = 1 / Math.log(2);

    /**
     * Generates a positive BigDecimal with a randomly selected magnitude
     * and scale whose magnitude's range is [0, scaleLimit).
     * <p>
     * NOTE: It may return ZERO as per the specification of {@link BigInteger#BigInteger(int, Random)}.
     *
     * @param precision  The desired number of decimal digits.
     * @param scaleLimit The maximum magnitude (exclusive) of the scale.
     * @param random     The Random used to generate the magnitude and scale.
     * @return A positive
     */
    static BigDecimal scaledPositive(int precision,
                                            int scaleLimit,
                                            Random random) {
        int bitCount = (int) (Math.log(precision) * LOG2_INV + 1);
        BigInteger magnitude = new BigInteger(bitCount, random);
        int scale = random.nextInt(scaleLimit);
        if (random.nextBoolean()) {
            scale = -scale;
        }
        return new BigDecimal(magnitude, scale);
    }

    /**
     * Generates a new BigDecimal that lies within the specified range.
     *
     * @param lower  The inclusive lower limit.
     * @param upper  The exclusive upper limit.
     * @param random The Random used to generate the fraction.
     * @return A new BigDecimal that lies within the specified range.
     */
    static BigDecimal rangedValue(BigDecimal lower,
                                         BigDecimal upper,
                                         Random random) {
        BigDecimal difference = upper.subtract(lower);
        BigDecimal fraction = BigDecimal.valueOf(random.nextDouble());
        return lower.add(difference.multiply(fraction));
    }

    /**
     * Returns the epsilon required for the specified context.
     *
     * @param context The context to generate the epsilon for.
     * @return The required epsilon.
     */
    public static BigDecimal eps(MathContext context) {
        return new BigDecimal(BigInteger.ONE, context.getPrecision() + 1);
    }
}
