/*
 *  Range.java, a simple data structure to express min and max.
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
package info.monitorenter.util;

import java.io.Serializable;

/**
 * A simple data structure that defines a minimum and a maximum and knows, what
 * lies within it and what not.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class Range implements Serializable {

  /** The unbounded range. */
  public static final Range RANGE_UNBOUNDED = new Range(-Double.MAX_VALUE, +Double.MAX_VALUE);

  /**
   * Generated for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3760565278089754419L;

  /** The upper bound of this range. */
  protected double m_max;

  /** The lower bound of this range. */
  protected double m_min;

  /**
   * Constructs a new Range that covers the given bounds.
   * <p>
   * 
   * @param min
   *          the lower bound for the range.
   * 
   * @param max
   *          the upper bound for the range.
   */
  public Range(final double min, final double max) {
    if (Double.isNaN(min)) {
      throw new IllegalArgumentException("Cannot work on Double.NaN for min.");
    }
    MathUtil.assertDouble(min);
    MathUtil.assertDouble(max);
    if (min < max) {
      this.m_min = min;
      this.m_max = max;
    } else {
      this.m_min = max;
      this.m_max = min;
    }
  }

  /**
   * Force this Range to cover the given value.
   * <p>
   * 
   * @param contain
   *          the value that has to be contained within this range.
   * 
   * @return true, if an internal modification of one bound took place, false
   *         else.
   * 
   */
  public boolean ensureContained(final double contain) {
    boolean ret = false;
    if (contain < this.m_min) {
      ret = true;
      this.m_min = contain;
    } else if (contain > this.m_max) {
      ret = true;
      this.m_max = contain;
    }
    return ret;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    boolean result = true;
    if (obj instanceof Range) {
      Range other = (Range) obj;
      result = (this.m_max == other.m_max) && (this.m_min == other.m_min);
    } else {
      result = false;
    }
    return result;
  }

  /**
   * Returns the extent of this range.
   * <p>
   * 
   * @return the extent of this range.
   */
  public double getExtent() {
    return this.m_max - this.m_min;
  }

  /**
   * Returns the upper bound of this range.
   * <p>
   * 
   * @return the upper bound of this range.
   */
  public double getMax() {
    return this.m_max;
  }

  /**
   * Returns the lower bound of this range.
   * <p>
   * 
   * @return the lower bound of this range.
   */
  public double getMin() {
    return this.m_min;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  /**
   * Returns true if the given value is covered by this range.
   * <p>
   * 
   * @param contained
   *          the value to test wether it is contained within this range.
   * 
   * @return true if the given value is covered by this range.
   */
  public boolean isContained(final double contained) {
    return ((this.m_min <= contained) && (this.m_max >= contained));
  }

  /**
   * Mutator that shifts this range to the given one.
   * <p>
   * 
   * This is support for "clone" without allocations in case range instances are
   * reused.
   * <p>
   * 
   * @param r
   *          the range to copy from.
   */
  public void mimic(final Range r) {
    this.m_max = r.m_max;
    this.m_min = r.m_min;
  }

  /**
   * Sets the max value of this range.
   * <p>
   * 
   * @param max
   *          the max to set.
   */
  public final void setMax(final double max) {
    this.m_max = max;
  }

  /**
   * Sets the min value of this range.
   * <p>
   * 
   * @param min
   *          the min to set
   */
  public final void setMin(final double min) {
    this.m_min = min;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer ret = new StringBuffer("Range[");
    ret.append(this.m_min).append(',');
    ret.append(this.m_max).append(']');
    return ret.toString();
  }

}
