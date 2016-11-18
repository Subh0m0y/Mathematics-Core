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

package mathcore.ops.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Helper {
    private static final double LOG2_INV = 1 / Math.log(2);

    public static BigDecimal generateRandom(int precision,
                                            int scaleLimit,
                                            Random random) {
        int numBits = (int) (Math.log(precision) * LOG2_INV + 1);
        BigInteger magnitude = new BigInteger(numBits, random);
        int scale = random.nextInt(scaleLimit);
        if (random.nextBoolean())
            scale = -scale;
        return new BigDecimal(magnitude, scale);
    }
}
