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
import java.util.Random;

/**
 * @author Subhomoy Haldar
 * @version 2017.01.06
 */
class TestHelper {
    /**
     * Cached copy of the reciprocal of ln(2). Used in converting no. of
     * bits to number of decimal digits.
     */
    private static final double LOG2_INV = 1 / Math.log(2);

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
}
