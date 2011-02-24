/*
 *  IntegerReusable.java, a mutable Integer for  jchart2d.
 *  Copyright (C) 2004 - 2010 Achim Westermann.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.util.math;

/**
 * Mutable {@link java.lang.Integer}.
 * <p>
 *
 * I needed an wrapper of an primitive int to share the same value between
 * different instances and to have the changes made to the primitive value take
 * effect on all owners of the same instance.
 * <p>
 *
 * What a pity that java.lang.Integer does not allow to change it's internal
 * value at runtime. Every time a new Integer has to be constructed.
 * <p>
 *
 */
public class IntegerReuseable {

  /**
   * The largest value of type <code>int</code>. The constant value of this
   * field is <tt>2147483647</tt>.
   */
  public static final int MAX_VALUE = 0x7fffffff;

  /**
   * The smallest value of type <code>int</code>. The constant value of this
   * field is <tt>-2147483648</tt>.
   */
  public static final int MIN_VALUE = 0x80000000;

  /**
   * The value of the Integer.
   */
  private int m_value;

  /**
   * Constructs a newly allocated <code>Integer</code> object that represents
   * the primitive <code>int</code> argument.
   *
   * @param value
   *          the value to be represented by the <code>Integer</code>.
   */
  public IntegerReuseable(final int value) {
    this.m_value = value;
  }

  /**
   * Adds the given value to the internal value.
   * <p>
   *
   * @param i
   *          the value to add.
   *
   * @throws ArithmeticException
   *           if an overflow ({@link Integer#MAX_VALUE}) occurs.
   */
  public void add(final int i) throws ArithmeticException {
    int oldval = this.m_value;
    this.m_value += i;
    if (oldval > this.m_value) {
      this.m_value = oldval;
      throw new ArithmeticException("Overflow detected. Value saved unchanged.");
    }
  }

  /**
   * Adds the given value to the internal value.
   * <p>
   *
   * @param i
   *          the value to add.
   *
   * @throws ArithmeticException
   *           if an overflow ({@link Integer#MAX_VALUE}) occurs.
   */
  public void add(final Integer i) throws ArithmeticException {
    this.add(i.intValue());
  }

  /**
   * Adds the given value to the internal value.
   * <p>
   *
   * @param i
   *          the value to add.
   *
   * @throws ArithmeticException
   *           if an overflow ({@link Integer#MAX_VALUE}) occurs.
   */
  public void add(final IntegerReuseable i) throws ArithmeticException {
    this.add(i.getValue());
  }

  /**
   * Returns the value as an int.
   * <p>
   *
   * @return the value as an int.
   */
  public int getValue() {
    return this.m_value;
  }

  /**
   * Returns the value as an int.
   * <p>
   *
   * @return the value as an int.
   */
  public int intValue() {
    return this.m_value;
  }

  /**
   * Sets the value.
   * <p>
   *
   * @param value
   *          the value.
   */
  public void setValue(final int value) {
    this.m_value = value;
  }

  /**
   * Substracts the given value from the internal value.
   * <p>
   *
   * @param i
   *          the value to subtract.
   *
   * @throws ArithmeticException
   *           if a carry ({@link Integer#MIN_VALUE}) occurs.
   */
  public void sub(final int i) throws ArithmeticException {
    int oldval = this.m_value;
    this.m_value -= i;
    if (oldval < this.m_value) {
      this.m_value = oldval;
      throw new ArithmeticException("Carry detected. Value saved unchanged.");
    }
  }

  /**
   * Substracts the given value from the internal value.
   * <p>
   *
   * @param i
   *          the value to subtract.
   *
   * @throws ArithmeticException
   *           if a carry ({@link Integer#MIN_VALUE}) occurs.
   */
  public void sub(final Integer i) throws ArithmeticException {
    this.sub(i.intValue());
  }

  /**
   * Substracts the given value from the internal value.
   * <p>
   *
   * @param i
   *          the value to subtract.
   *
   * @throws ArithmeticException
   *           if a carry ({@link Integer#MIN_VALUE}) occurs.
   */
  public void sub(final IntegerReuseable i) throws ArithmeticException {
    this.sub(i.intValue());
  }

  /**
   * Returns a String object representing this Integer's value.
   * <p>
   *
   * The value is converted to signed decimal representation and returned as a
   * string, exactly as if the integer value were given as an argument to the
   * {@link java.lang.Integer#toString(int)} method.
   * <p>
   *
   *
   * @return a string representation of the value of this object in
   *         base&nbsp;10.
   */
  @Override
  public String toString() {
    return String.valueOf(this.m_value);
  }
}
