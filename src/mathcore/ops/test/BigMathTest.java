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

import mathcore.ops.BigMath;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

import static org.testng.AssertJUnit.assertTrue;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class BigMathTest {
    private static final int NTH_ROOT_LIMIT = 20;
    private static final int PRECISION = 30;
    private static final int SCALE_LIMIT = 100;
    private static final int ROOT_LIMIT = 100;
    private static final MathContext CONTEXT = new MathContext(PRECISION, RoundingMode.HALF_EVEN);
    private static final Random RANDOM = new Random();

    @Test
    public void testPrincipalRoot() throws Exception {
        for (int i = 0; i < NTH_ROOT_LIMIT; i++) {
            BigDecimal a = Helper.generateRandom(PRECISION, SCALE_LIMIT, RANDOM);
            int n = RANDOM.nextInt(ROOT_LIMIT - 2) + 2;

            BigDecimal root = BigMath.principalRoot(a, n, CONTEXT);

            assertTrue(root.pow(n, CONTEXT).compareTo(a) == 0);
        }
    }

    private static final int EXP_LOG_LIMIT = 1000;
    private static final int EXP_LIMIT = 1000;

    @Test
    public void testExpLog() throws Exception {
        for (int i = 0; i < EXP_LOG_LIMIT; i++) {
            BigDecimal a = new BigDecimal(RANDOM.nextInt(EXP_LIMIT) + RANDOM.nextDouble());
            if (RANDOM.nextBoolean()) a = a.negate();
            a = a.round(CONTEXT);

            BigDecimal e = BigMath.exp(a, CONTEXT);
            BigDecimal l = BigMath.log(e, CONTEXT);

            assertTrue(l.round(CONTEXT).compareTo(a) == 0);
        }
    }

    private final static int LOG_EXP_LIMIT = 1000;

    @Test
    public void testLogExp() throws Exception {
        for (int i = 0; i < LOG_EXP_LIMIT; i++) {
            BigDecimal a = Helper.generateRandom(PRECISION, SCALE_LIMIT, RANDOM);
            a = a.add(BigDecimal.ONE, CONTEXT);

            BigDecimal l = BigMath.log(a, CONTEXT);
            BigDecimal e = BigMath.exp(l, CONTEXT);

            assertTrue(e.round(CONTEXT).compareTo(a) == 0);
        }
    }
}