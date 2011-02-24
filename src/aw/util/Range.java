/*
 *  Range.java, a simple data structure to express min and max.
 *  Copyright (C) Achim Westermann, created on 09.09.2004, 12:38:21
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
package aw.util;

import java.io.Serializable;

/**
 * <p>
 * A simple data structure that defines a minimum and a maximum and knows, what
 * lies within it and what not.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 */
public class Range implements Serializable {

  /**
   * Generated for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3760565278089754419L;

  /** The unbounded range. */
  public static final Range RANGE_UNBOUNDED = new Range(-Double.MAX_VALUE, +Double.MAX_VALUE);

  /** The lower bound of this range. */
  protected double m_min;

  /** The upper bound of this range. */
  protected double m_max;

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
    if (min == Double.NaN) {
      throw new IllegalArgumentException("Cannot work on Double.NaN for min.");
    }
    if (max == Double.NaN) {
      throw new IllegalArgumentException("Cannot work on Double.NaN for min.");
    }
    if (min < max) {
      this.m_min = min;
      this.m_max = max;
    } else {
      this.m_min = max;
      this.m_max = min;
    }
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
   * Returns the upper bound of this range.
   * <p>
   *
   * @return the upper bound of this range.
   */
  public double getMax() {
    return this.m_max;
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
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer ret = new StringBuffer("Range[");
    ret.append(this.m_min).append(',');
    ret.append(this.m_max).append(']');
    return ret.toString();
  }
}
