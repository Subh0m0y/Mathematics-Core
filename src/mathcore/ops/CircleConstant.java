package mathcore.ops;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static mathcore.ops.BigMath.expandContext;
import static mathcore.ops.BigMath.sqrt;
import static mathcore.ops.test.Helper.eps;

/**
 * A portion of BigMath refactored out to reduce overall complexity.
 * <p>
 * This class handles the generation of the circle constant: &pi; (pi).
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class CircleConstant {
    // Make this class un-instantiable
    private CircleConstant() {

    }

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);

    private static final BigDecimal HALF = new BigDecimal("0.5");
    private static final BigDecimal FOURTH = new BigDecimal("0.25");

    private static final BigDecimal PI_40 = new BigDecimal("3.141592653589793238462643383279502884197");

    static BigDecimal PI(MathContext context) {
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

        BigDecimal eps = eps(c);

        while (a.subtract(b).abs().compareTo(eps) > 0) {
            // The basic steps are:
            // 1. A.M. = (a + b) / 2
            // 2. G.M. = sqrt(a * b)
            // 3. t = t - p * (A.M. - a)^2
            // 4. p = 2 * p
            // 5. a = A.M.
            // 6. b = G.M.
            // Repeat until a and b are equal (within context)
            BigDecimal A = a.add(b).multiply(HALF, c);
            b = sqrt(a.multiply(b), c);
            t = t.subtract(p.multiply(A.subtract(a).pow(2)), c);
            p = p.add(p);
            a = A;
        }
        //      (a + b)^2
        // pi = ---------
        //          4t
        return a.add(b).pow(2).divide(FOUR.multiply(t), context);
    }
}
