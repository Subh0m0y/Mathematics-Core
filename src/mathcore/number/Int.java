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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * A very thin wrapper around a BigInteger object to help integrate the
 * existing functionality into the system.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Int extends Real {
    /**
     * The constant representing the Integer ONE (1).
     */
    public static final Int ONE = new Int(1);
    /**
     * The constant representing the Integer ZERO (0).
     */
    public static final Int ZERO = new Int(0);

    private final BigInteger integer;

    /**
     * Convenient constructor to create a new Integer from an {@code int}.
     *
     * @param a The {@code int} to be encapsulated.
     */
    public Int(int a) {
        integer = BigInteger.valueOf(a);
    }

    /**
     * Convenient constructor to create a new Integer from a{@code long}.
     *
     * @param a The {@code long} to be encapsulated.
     */
    public Int(long a) {
        integer = BigInteger.valueOf(a);
    }

    /**
     * Constructor to create a new Integer from a {@code BigInteger}.
     *
     * @param integer The {@code BigInteger} to be encapsulated.
     */
    public Int(BigInteger integer) {
        this.integer = integer;
    }

    /**
     * Constructor to parse a String and create an Integer.
     *
     * @param string The String to be parsed.
     */
    public Int(String string) {
        integer = new BigInteger(string);
    }

    /**
     * Returns the signum function of this Integer.
     *
     * @return -1, 0 or 1 as the value of this Integer is negative, zero
     * or positive.
     */
    public int signum() {
        return integer.signum();
    }

    /**
     * Returns the Greatest Common Divisor of this Integer and the given
     * Integer {@code val}.
     *
     * @param val Value with which the GCD is to be computed.
     * @return The GCD of {@code this} and {@code val}.
     */
    public Int gcd(Int val) {
        return new Int(integer.gcd(val.integer));
    }

    /**
     * Tries to convert this Int to a primitive {@code int}. An
     * {@code ArithmeticException} is thrown if overflow occurs.
     *
     * @return The value of this Int as an {@code int}.
     */
    @Override
    public int intValue() {
        return integer.intValueExact();
    }

    /**
     * Tries to convert this Int to a primitive {@code long}. An
     * {@code ArithmeticException} is thrown if overflow occurs.
     *
     * @return The value of this Int as a {@code long}.
     */
    @Override
    public long longValue() {
        return integer.longValueExact();
    }

    /**
     * Returns the value of this Int as a {@code float}.
     *
     * @return The value of this Int as a {@code float}.
     */
    @Override
    public float floatValue() {
        return integer.floatValue();
    }

    /**
     * Returns the value of this Int as a {@code double}.
     *
     * @return The value of this Int as a {@code double}.
     */
    @Override
    public double doubleValue() {
        return integer.doubleValue();
    }

    /**
     * Returns the value of this Int in the form of a Rational.
     *
     * @return The value of this Int in the form of a Rational.
     */
    @Override
    public Rational asRational() {
        return new Rational(this);
    }

    public Int asInt() {
        return this;
    }

    @Override
    public BigInteger toBigInteger() {
        return integer;
    }

    @Override
    public BigDecimal toBigDecimal(MathContext context) {
        return new BigDecimal(integer, context);
    }

    /**
     * Compares the given Real to this Integer. If the numerical value of
     * this Int is less than the given argument, then a negative number is
     * returned. If they are equal, zero is returned. If the argument is
     * smaller, a positive number is returned.
     *
     * @param r The Real argument.
     * @return A negative number, zero or positive number as the argument is
     * greater than, equal to or less than this Int.
     */
    @Override
    public int compareTo(Real r) {
        if (r instanceof Int)
            return integer.compareTo(((Int) r).integer);
        else
            return asRational().compareTo(r.asRational());
    }

    /**
     * Returns the String representation of this Int (in decimal).
     *
     * @return The String representation of this Int.
     */
    @Override
    public String toString() {
        return integer.toString();
    }

    /**
     * Returns {@code true} if the given argument is also an Int and has
     * the exact same value.
     *
     * @param other The argument to compare with.
     * @return {@code true} if the given argument is also an Int and has
     * the exact same value.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Int
                && integer.equals(((Int) other).integer);
    }

    // The arithmetic functions

    /**
     * Returns the sum of this Int and the given Int.
     *
     * @param i The Int to be added.
     * @return The required sum.
     */
    public Int add(Int i) {
        return new Int(integer.add(i.integer));
    }

    /**
     * Returns the result of subtraction performed on this Int with the
     * given argument.
     *
     * @param i The Int to be subtracted.
     * @return The result of subtraction performed on this Int with the
     * given argument.
     */
    public Int subtract(Int i) {
        return new Int(integer.subtract(i.integer));
    }

    /**
     * Returns the product of this Int and the given Int.
     *
     * @param i The Int to be multiplied.
     * @return The required product.
     */
    public Int multiply(Int i) {
        return new Int(integer.multiply(i.integer));
    }

    /**
     * Returns the quotient that would be obtained after dividing this Int
     * with the given argument.
     *
     * @param i The divisor.
     * @return The required quotient.
     */
    public Int divide(Int i) {
        return new Int(integer.divide(i.integer));
    }

    /**
     * Returns the remainder that would be obtained after dividing this Int
     * with the given argument.
     *
     * @param i The divisor.
     * @return The required remainder.
     */
    public Int mod(Int i) {
        return new Int(integer.remainder(i.integer));
    }

    /**
     * Returns the Additive inverse of this Integer.
     *
     * @return The negative of this Int.
     */
    public Int negate() {
        return new Int(integer.negate());
    }
}
