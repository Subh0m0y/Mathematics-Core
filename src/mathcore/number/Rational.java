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
    private Int num;
    private Int den;

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
        // Simplify the terms
        Int gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }

    public Rational(Int integer) {
        this(integer, Int.ONE);
    }

    @Override
    public int intValue() {
        return num.divide(den).intValue();
    }

    @Override
    public long longValue() {
        return num.divide(den).longValue();
    }

    @Override
    public float floatValue() {
        return num.divide(den).floatValue();
    }

    @Override
    public double doubleValue() {
        return num.divide(den).doubleValue();
    }

    @Override
    public Rational approximateRational() {
        return this;
    }

    @Override
    public int compareTo(Real real) {
        Rational term = real.approximateRational();
        Int n1 = this.num;
        Int d1 = this.den;
        Int n2 = term.num;
        Int d2 = term.den;
        return n1.multiply(d2).compareTo(n2.multiply(d1));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Real)) return false;
        Rational term = ((Real) other).approximateRational();
        Int n1 = this.num;
        Int d1 = this.den;
        Int n2 = term.num;
        Int d2 = term.den;
        return n1.multiply(d2).equals(n2.multiply(d1));
    }

    // Arithmetic

}
