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

package mathcore.ops;

import mathcore.number.Int;
import mathcore.number.Rational;

import java.util.Arrays;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class GCFraction {
    private final Int[] a;
    private final Int[] b;
    private final int n;

    private Int A;
    private Int B;

    public GCFraction(final Int[] a,
                      final Int[] b) {
        n = a.length;
        if (n + 1 != b.length)
            throw new ArithmeticException("Array b must have one more element than a");
        this.a = Arrays.copyOf(a, n);
        this.b = Arrays.copyOf(b, n + 1);
    }

    public Rational calculate() {
        if (A != null) {
            return new Rational(A, B);
        }
        // The initial cases
        Int A_1 = Int.ONE;
        Int B_1 = Int.ZERO;
        Int A_2 = b[0];
        Int B_2 = Int.ONE;

        for (int i = 0; i < n; i++) {
            // The next terms
            Int A = b[i + 1].multiply(A_2)
                    .add(a[i].multiply(A_1));
            Int B = b[i + 1].multiply(B_2)
                    .add(a[i].multiply(B_1));

            // Progress
            A_1 = A_2;
            A_2 = A;

            B_1 = B_2;
            B_2 = B;
        }

        A = A_2;
        B = B_2;

        return new Rational(A, B);
    }
}
