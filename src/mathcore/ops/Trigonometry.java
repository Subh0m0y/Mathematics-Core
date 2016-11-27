package mathcore.ops;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static mathcore.ops.BigMath.PI;
import static mathcore.ops.BigMath.expandContext;
import static mathcore.ops.test.Helper.eps;

/**
 * A portion of BigMath refactored out to reduce overall complexity.
 * <p>
 * This class handles the basic trigonometric functions.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class Trigonometry {
    // Make this class un-instantiable
    private Trigonometry() {
    }

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal HALF = new BigDecimal("0.5");

    /**
     * Calculates the sine and cosine of the given value (in radians) at
     * the same time. The first element in the array is the sine and the
     * second element is the cosine.
     * <p>
     * This method is useful because the taylor series of sin and cos are
     * complementary and calculating them together is often faster than
     * calculating each separately.
     * <p>
     * This method works for all values of x.
     *
     * @param x       The argument (in radians).
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The sine and cosine of x.
     */
    static BigDecimal[] sinAndCos(BigDecimal x, MathContext context) {
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

    /**
     * Calculates the tangent of the given argument (in radians).
     * It is (in the simplest sense) calculated as sin(x)/cos(x).
     *
     * @param x       The argument (in radians).
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The tangent of x
     */
    static BigDecimal tan(BigDecimal x, MathContext context) {
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
}
