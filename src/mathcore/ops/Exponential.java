package mathcore.ops;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static mathcore.ops.BigMath.expandContext;
import static mathcore.ops.test.Helper.eps;

/**
 * A portion of BigMath refactored out to reduce overall complexity.
 * <p>
 * This class handles the exponentiation algorithms.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class Exponential {
    // Make this class un-instantiable
    private Exponential() {
    }

    private static final BigDecimal E_40 = new BigDecimal("2.718281828459045235360287471352662497761");

    /**
     * Returns the value of {@code e}, or Euler's constant, the base of the natural logarithm
     * with the specified precision.
     *
     * @param context The precision needed.
     * @return The value of e with the desired precision.
     */
    static BigDecimal E(MathContext context) {
        if (context.getPrecision() <= 40)   // (int) (1.2 * 34) == 40
            return E_40.round(context);
        return Exponential.smallExp(ONE, context);
    }

    /**
     * Uses the Taylor series to approximate exp() for small values of x.
     *
     * @param x       A small positive argument (less than e).
     * @param context The The MathContext to specify the precision and RoundingMode.
     * @return exp(x) for small positive value of x.
     */
    private static BigDecimal smallExp(BigDecimal x, MathContext context) {
        // Quick exit
        if (x.signum() == 0) return ONE;

        BigDecimal num = x;                 // Numerator
        BigDecimal den = ONE;               // Denominator
        // The actual of result of num/den
        BigDecimal term = num.divide(den, context);
        BigDecimal sum = ONE;               // Accumulator
        // The tolerable error
        BigDecimal eps = eps(context);
        long i = 1; // The factorial variable
        while (term.compareTo(eps) > 0) {
            term = num.divide(den, context);
            sum = sum.add(term, context);
            den = den.multiply(valueOf(++i));
            num = num.multiply(x);
        }
        return sum;
    }

    /**
     * Returns the value obtained on raising e to the power x.
     *
     * @param x       The power to raise e to.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The required value of e<sup>x</sup>.
     * @throws ArithmeticException If abs(x) is too big (bigger than 999999999).
     */
    static BigDecimal exp(BigDecimal x, MathContext context)
            throws ArithmeticException {
        // Quick exit
        if (x.signum() == 0) return ONE;

        // Perform the calculations with a bigger context
        int newPrecision = (int) (context.getPrecision() * 1.2);
        MathContext c = expandContext(context, newPrecision);
        // The value of e for the integral portion
        BigDecimal E = E(c);
        BigDecimal abs = x.abs();
        BigInteger intExp = abs.toBigInteger();
        BigDecimal fraction = abs.subtract(new BigDecimal(intExp));

        // If the integral part is too large, an exception maybe thrown.
        BigDecimal exp = E.pow(intExp.intValueExact(), c).multiply(
                Exponential.smallExp(fraction, c), c
        );

        return x.signum() < 0 ? ONE.divide(exp, c) : exp;
    }

    /**
     * Raises the given bass to the specified exponent. All values of y are
     * supported in the range [-999999999, 999999999]. (This is because of
     * the limitation imposed by {@link BigDecimal#pow(int)} internally.)
     * <p>
     * NOTE: Only positive values of x (the base) are supported.
     *
     * @param x       The base.
     * @param y       The exponent.
     * @param context The MathContext to specify the precision and RoundingMode.
     * @return The base raised to the given exponent.
     * @throws ArithmeticException If x is negative or y is too big.
     */
    static BigDecimal pow(BigDecimal x,
                          BigDecimal y,
                          MathContext context) {
        // Quick exits
        if (x.signum() < 0) {
            throw new ArithmeticException("Negative numbers not supported.");
        }
        if (x.signum() == 0) {
            return ZERO;
        }
        MathContext c = expandContext(context, (int) (context.getPrecision() * 1.2));

        // Tackle the integral and the fractional part separately
        BigDecimal abs = y.abs();
        int p = abs.toBigInteger().intValueExact();
        BigDecimal f = abs.remainder(ONE);

        BigDecimal v = x.pow(p).multiply(exp(f.multiply(Logarithm.log(x, c)), c));

        // Finally tackle the sign
        return y.signum() < 0 ? ONE.divide(v, c) : v;
    }

}
