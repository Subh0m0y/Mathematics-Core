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

import mathcore.ops.BigMath;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

import static java.math.BigDecimal.*;
import static org.testng.AssertJUnit.assertEquals;
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
            BigDecimal a = TestHelper.scaledPositive(PRECISION, SCALE_LIMIT, RANDOM);
            int n = RANDOM.nextInt(ROOT_LIMIT - 2) + 2;

            BigDecimal root = BigMath.principalRoot(a, n, CONTEXT);

            assertTrue(root.pow(n, CONTEXT).compareTo(a) == 0);
        }
    }

    private static final int EXP_LOG_LIMIT = 1000;
    private static final int EXP_LIMIT = 1000;

    private static final BigDecimal EXP_LIM = valueOf(EXP_LIMIT);

    @Test
    public void testExpLog() throws Exception {
        for (int i = 0; i < EXP_LOG_LIMIT; i++) {
            BigDecimal a = TestHelper.rangedValue(ZERO, EXP_LIM, RANDOM);
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
            BigDecimal a = TestHelper.scaledPositive(PRECISION, SCALE_LIMIT, RANDOM);
            a = a.add(ONE, CONTEXT);

            BigDecimal l = BigMath.log(a, CONTEXT);
            BigDecimal e = BigMath.exp(l, CONTEXT);

            assertTrue(e.round(CONTEXT).compareTo(a) == 0);
        }
    }

    private static final int POW_LIMIT = 10;
    private static final int POW_VAL_LIMIT = 1000;

    @Test
    public void testPow() throws Exception {
        for (int i = 0; i < POW_LIMIT; i++) {
            BigDecimal x = TestHelper.scaledPositive(PRECISION, 1, RANDOM);
            x = x.add(ONE, CONTEXT);

            BigDecimal y = new BigDecimal(RANDOM.nextInt(POW_VAL_LIMIT) + RANDOM.nextDouble());
            if (RANDOM.nextBoolean()) y = y.negate();

            BigDecimal p = BigMath.pow(x, y, CONTEXT);
            BigDecimal r = ONE.divide(y, BigMath.expandContext(CONTEXT, (int) (PRECISION * 1.2)));
            BigDecimal n = BigMath.pow(p, r, CONTEXT);

            assertTrue(n.round(CONTEXT).compareTo(x) == 0);
        }
    }

    private static final int TAN_LIMIT = 20000;
    private static final BigDecimal PI = BigMath.PI(CONTEXT);
    private static final BigDecimal PI_2 = PI.divide(valueOf(2), CONTEXT);


    @Test
    public void testTanAndAtan2() throws Exception {
        assertTrue(BigMath.atan2(ONE, ZERO, CONTEXT).round(CONTEXT)
                .compareTo(PI_2) == 0);
        for (int i = 0; i < TAN_LIMIT; i++) {
            BigDecimal a = TestHelper.rangedValue(PI_2.negate(), PI_2, RANDOM);
            a = a.round(CONTEXT);

            BigDecimal tan = BigMath.tan(a, CONTEXT);
            BigDecimal inv = BigMath.arctan(tan, CONTEXT);

            assertTrue(inv.round(CONTEXT).compareTo(a) == 0);
        }
    }

    private static final int RANGE = 10_000;
    private static final double EPS = 1e-8;

    @Test
    public void testBasicSinAndCos() throws Exception {
        for (int i = -RANGE; i <= RANGE; i++) {
            BigDecimal[] v = BigMath.sinAndCos(BigDecimal.valueOf(i), CONTEXT);
            assertEquals(Math.sin(i), v[0].doubleValue(), EPS);
            assertEquals(Math.cos(i), v[1].doubleValue(), EPS);
        }
    }

    @Test
    public void testBasicTanAndAtan() throws Exception {
        for (int i = -RANGE; i <= RANGE; i++) {
            BigDecimal tan = BigMath.tan(BigDecimal.valueOf(i), CONTEXT);
            BigDecimal atn = BigMath.arctan(BigDecimal.valueOf(i), CONTEXT);
            assertEquals(Math.tan(i), tan.doubleValue(), 1e-8);
            assertEquals(Math.atan(i), atn.doubleValue(), 1e-8);
        }
    }

    private static final int SIN_LIMIT = TAN_LIMIT;

    @Test
    public void testSinAndArcsin() throws Exception {
        MathContext context = BigMath.expandContext(CONTEXT, (int) (PRECISION * 1.2));
        for (int i = 0; i < SIN_LIMIT; i++) {
            BigDecimal a = TestHelper.rangedValue(PI_2.negate(), PI_2, RANDOM);
            a = a.round(CONTEXT);

            BigDecimal sin = BigMath.sin(a, context);
            BigDecimal inv = BigMath.arcsin(sin, CONTEXT);

            assertTrue(inv.round(CONTEXT).compareTo(a) == 0);
        }
    }

    private static final int COS_LIMIT = TAN_LIMIT;

    @Test
    public void testCosAndArccos() throws Exception {
        MathContext context = BigMath.expandContext(CONTEXT, (int) (PRECISION * 1.2));
        for (int i = 0; i < COS_LIMIT; i++) {
            BigDecimal a = TestHelper.rangedValue(ZERO, PI, RANDOM);
            a = a.round(CONTEXT);

            BigDecimal cos = BigMath.cos(a, context);
            BigDecimal inv = BigMath.arccos(cos, CONTEXT);

            assertTrue(inv.round(CONTEXT).compareTo(a) == 0);
        }
    }

    @Test
    public void testSqrt() throws Exception {
        for (int i = 0; i < NTH_ROOT_LIMIT; i++) {
            BigDecimal a = TestHelper.scaledPositive(PRECISION, SCALE_LIMIT, RANDOM);
            a = a.round(CONTEXT);

            BigDecimal sqrt = BigMath.sqrt(a, CONTEXT);

            assertTrue(a.compareTo(sqrt.pow(2, CONTEXT)) == 0);
        }
    }

    @Test
    public void testE() throws Exception {
        assertEquals(
                "2.718281828459045235360287471352662497761",
                BigMath.E(new MathContext(40, RoundingMode.HALF_EVEN)).toString()
        );
    }

    @Test
    public void testPI() throws Exception {
        assertEquals(
                "3.141592653589793238462643383279502884197",
                BigMath.PI(new MathContext(40, RoundingMode.HALF_EVEN)).toString()
        );
    }

    @Test
    public void testToDegreesAndToRadians() throws Exception {
        MathContext context = BigMath.expandContext(CONTEXT, PRECISION + 2);
        for (int i = -RANGE; i < RANGE; i++) {
            BigDecimal val = BigDecimal.valueOf(i);
            BigDecimal rad = BigMath.toRadians(val, context);
            BigDecimal deg = BigMath.toDegrees(rad, CONTEXT);
            assertEquals(Math.toRadians(i), rad.doubleValue(), EPS);
            assertTrue(deg.compareTo(val) == 0);
        }
    }
}