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

import java.math.BigInteger;

/**
 * A very thin wrapper around a BigInteger object to help integrate the
 * existing functionality into the system.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Int extends Real {
    public static final Int ONE = new Int(1);
    public static final Int ZERO = new Int(0);

    private final BigInteger integer;

    public Int(int a) {
        integer = BigInteger.valueOf(a);
    }

    public Int(long a) {
        integer = BigInteger.valueOf(a);
    }

    public Int(BigInteger integer) {
        this.integer = integer;
    }

    public Int(String string) {
        integer = new BigInteger(string);
    }

    public int signum() {
        return integer.signum();
    }

    public Int gcd(Int val) {
        return new Int(integer.gcd(val.integer));
    }

    @Override
    public int intValue() {
        return integer.intValue();
    }

    @Override
    public long longValue() {
        return integer.longValue();
    }

    @Override
    public float floatValue() {
        return integer.floatValue();
    }

    @Override
    public double doubleValue() {
        return integer.doubleValue();
    }

    @Override
    public Rational approximateRational() {
        return new Rational(this);
    }

    @Override
    public int compareTo(Real r) {
        if (r instanceof Int)
            return integer.compareTo(((Int) r).integer);
        else
            return approximateRational().compareTo(r.approximateRational());
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Int
                && integer.equals(((Int) other).integer);
    }

    // The arithmetic functions

    public Int add(Int i) {
        return new Int(integer.add(i.integer));
    }

    public Int subtract(Int i) {
        return new Int(integer.subtract(i.integer));
    }

    public Int multiply(Int i) {
        return new Int(integer.multiply(i.integer));
    }

    public Int divide(Int i) {
        return new Int(integer.divide(i.integer));
    }
}
