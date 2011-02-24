/*
 *  TimeStampedValue, wrapper class for values marked with a timestamp.
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
package info.monitorenter.util;

import java.util.Map;

/**
 * Simple wrapper around a time in ms and a value Object.
 * <p>
 * 
 * The key is the time in ms and may be used in a Map.
 * <code>{@link #compareTo(java.lang.Object)}</code> compares the key.
 * 
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * 
 * @version $Revision: 1.3 $
 */
public final class TimeStampedValue implements Map.Entry, Comparable {
  /**
   * The timestamp (difference, measured in milliseconds, between the current
   * time and midnight, January 1, 1970 UTC).
   */
  private long m_key;

  /** The timestamped value. */
  private Object m_value;

  /**
   * Creates an instance with the given timestamp key and the value to
   * timestamp.
   * <p>
   * 
   * @param key
   *          the timestamp (difference, measured in milliseconds, between the
   *          current time and midnight, January 1, 1970 UTC).
   * 
   * @param value
   *          the value to timestamp.
   */
  public TimeStampedValue(final long key, final Object value) {
    this.m_key = key;
    this.m_value = value;
  }

  /**
   * Creates an instance for the given value that is timestamped with the
   * current time.
   * <p>
   * 
   * @param value
   *          the value to timestamp.
   * 
   * @see System#currentTimeMillis()
   */
  public TimeStampedValue(final Object value) {
    this(System.currentTimeMillis(), value);
  }

  /**
   * Returns the {@link Long} that marks the timestamp (difference, measured in
   * milliseconds, between the current time and midnight, January 1, 1970 UTC).
   * 
   * @return the {@link Long} that marks the timestamp (difference, measured in
   *         milliseconds, between the current time and midnight, January 1,
   *         1970 UTC).
   * 
   * @see java.util.Map.Entry#getKey()
   */
  public Object getKey() {
    return new Long(this.m_key);
  }

  /**
   * Returns the timestamped instance.
   * <p>
   * 
   * @return the timestamped instance.
   * 
   * @see java.util.Map.Entry#getValue()
   */
  public Object getValue() {
    return this.m_value;
  }

  /**
   * Returns the timestamp (difference, measured in milliseconds, between the
   * current time and midnight, January 1, 1970 UTC).
   * <p>
   * 
   * @return the timestamp (difference, measured in milliseconds, between the
   *         current time and midnight, January 1, 1970 UTC).
   * 
   */
  public long getTime() {
    return this.m_key;
  }

  /**
   * Returns true, if o!=null && this.key.equals(0) &&
   * o.insanceOf(TimeStampedValue).
   * 
   * @param o
   *          the {@link TimeStampedValue} to compare this instance to.
   * 
   * @return true, if o!=null && this.key.equals(0) &&
   *         o.insanceOf(TimeStampedValue).
   */
  public boolean equals(final Object o) {
    if (o == null) {
      return false;
    }
    TimeStampedValue compare = null;
    try {
      compare = (TimeStampedValue) o;
    } catch (ClassCastException e) {
      return false;
    }
    if (this.getTime() == compare.getTime()) {
      return true;
    }
    return false;
  }

  /**
   * 
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    int result = (int) this.m_key;
    if (this.m_key > Integer.MAX_VALUE) {
      result = -result;
    }
    return result;

  }

  /**
   * Assigns a different value to the timestamp.
   * <p>
   * 
   * @param value
   *          the new value to be marked with this timestamp.
   * 
   * @return the previous value that was contained.
   * 
   * @see java.util.Map.Entry#setValue(java.lang.Object)
   */
  public Object setValue(final Object value) {
    Object ret = this.m_value;
    this.m_value = value;
    return ret;
  }

  /**
   * Compares the given {@link TimeStampedValue} to this by the internal
   * {@link #getTime()}.
   * <p>
   * 
   * @see java.lang.Comparable
   */
  public int compareTo(final java.lang.Object obj) {
    TimeStampedValue other = (TimeStampedValue) obj;
    if (this.m_key < other.m_key) {
      return -1;
    }
    if (this.m_key == other.m_key) {
      return 0;
    }
    return 1;
  }

  /**
   * Returns wethter the internal timestamp marks a time in the past or not.
   * <p>
   * 
   * For normal a timestamp represents a value regarded at a time. But it is
   * also thinkable to mark a value for expiration in the future. This method
   * returns true if the internal time- representing key is smaller than the
   * actual time.
   * <p>
   * 
   * @return true if the internal timestamp marks a moment in the past, false
   *         else.
   */
  public boolean isPast() {
    return this.m_key < System.currentTimeMillis();
  }
}
