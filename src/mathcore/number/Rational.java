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

package mathcore.number;

/**
 * This class is meant to encapsulate all Rational numbers. It contains the
 * algorithms to perform basic operations on this set of numbers.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Rational extends Real {
    private Int num;    // The numerator
    private Int den;    // The denominator

    /**
     * Convenient constructor to create a Rational from given ints.
     * If the denominator is zero, an {@link ArithmeticException} is thrown.
     *
     * @param num The numerator.
     * @param den The denominator (must be non-zero).
     */
    public Rational(int num, int den) {
        this(new Int(num), new Int(den));
    }

    /**
     * Constructor to create a Rational from given Ints.
     * If the denominator is zero, an {@link ArithmeticException} is thrown.
     *
     * @param num The numerator.
     * @param den The denominator (must be non-zero).
     */
    public Rational(Int num, Int den) {
        // By definition, denominator is non-zero
        if (den.signum() == 0) {
            throw new ArithmeticException("Denominator cannot be zero.");
        }
        // Keep only one definition for Zero
        if (num.signum() == 0) {
            this.num = Int.ZERO;
            this.den = Int.ONE;
            return;
        }
        // Correct the sign
        if (den.signum() < 0) {
            num = num.negate();
            den = den.negate();
        }
        // Simplify the terms
        Int gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }

    /**
     * Returns the given Integer as a Rational. Every integer can be
     * expressed as a reduced Rational object.
     *
     * @param integer The integer to represent.
     */
    public Rational(Int integer) {
        this(integer, Int.ONE);
    }

    /**
     * @return The value of this Rational as an int, with truncation,
     * provided that overflow doesn't occur.
     */
    @Override
    public int intValue() {
        return num.divide(den).intValue();
    }

    /**
     * @return The value of this Rational as a long, with truncation,
     * provided that overflow doesn't occur.
     */
    @Override
    public long longValue() {
        return num.divide(den).longValue();
    }

    /**
     * @return The value of this Rational as a float.
     */
    @Override
    public float floatValue() {
        return num.divide(den).floatValue();
    }

    /**
     * @return The value of this Rational as a double.
     */
    @Override
    public double doubleValue() {
        return num.divide(den).doubleValue();
    }

    /**
     * @return Returns this Rational as a Rational, which is itself.
     */
    @Override
    public Rational asRational() {
        return this;
    }

    @Override
    public int compareTo(Real real) {
        Rational term = real.asRational();
        Int n1 = this.num;
        Int d1 = this.den;
        Int n2 = term.num;
        Int d2 = term.den;
        return n1.multiply(d2).compareTo(n2.multiply(d1));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Real)) return false;
        Rational term = ((Real) other).asRational();
        Int n1 = this.num;
        Int d1 = this.den;
        Int n2 = term.num;
        Int d2 = term.den;
        return n1.multiply(d2).equals(n2.multiply(d1));
    }

    @Override
    public String toString() {
        if (signum() == 0) return num.toString();
        return num + "/" + den;
    }

    public int signum() {
        return num.signum();
    }

    // Arithmetic
    public Rational add(Rational term) {
        Int a = this.num;
        Int b = this.den;
        Int c = term.num;
        Int d = term.den;
        return new Rational(a.multiply(d).add(b.multiply(c)), b.multiply(d));
    }

    public Rational multiply(Rational term) {
        Int a = this.num;
        Int b = this.den;
        Int c = term.num;
        Int d = term.den;
        return new Rational(a.multiply(c), b.multiply(d));
    }

    public Rational subtract(Rational term) {
        return add(term.negate());
    }

    public Rational negate() {
        return new Rational(num.negate(), den);
    }

    public Rational reciprocate() {
        return new Rational(den, num);
    }
}
