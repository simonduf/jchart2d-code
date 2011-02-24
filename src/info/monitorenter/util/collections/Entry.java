/*
 * Entry.java, base data container for mapping two elements.
 * Copyright (C) 2003 Achim Westermann, created on 28.11.2003
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * If you modify or optimize the code in a useful way please let me know.
 * Achim.Westermann@gmx.de
 */
package info.monitorenter.util.collections;

/**
 * <p>
 * I have written implementations of <tt>java.util.Map.Entry</tt> in form of
 * <ul>
 * <li> Static inner classes. </li>
 * <li> Non-static inner classes. </li>
 * <li> Non-public classes. </li>
 * <li> Anonymous classes. </li>
 * </ul>
 * </p>
 * <p>
 * Almost all implementations were plainforward and not hiding any complexity.
 * One could not downcast them to get more methods, and they were replaceable.
 * <br>
 * That's it! Finally i decided to hardcode it here... .
 * </p>
 * <p>
 * But don't you start writing methods like:
 * 
 * <pre>
 *  public Entry getEntry(String name);
 *  public void setEntry(Entry entry);
 * </pre>
 * 
 * Try sticking to the interface <tt>java.util.Map.Entry</tt>.
 * </p>
 * 
 * @see java.util.Map.Entry
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 */
public final class Entry implements java.util.Map.Entry {
  /** The key instance. */
  private Object m_key;

  /** The value instance. */
  private Object m_value;

  /**
   * Creates an instance with the given key and value.
   * <p>
   * 
   * @param key
   *          the key instance to use.
   * 
   * @param value
   *          the value instance to use.
   */
  public Entry(final Object key, final Object value) {
    this.m_key = key;
    this.m_value = value;
  }

  /**
   * Maybe null!
   * 
   * @see java.util.Map.Entry#getKey()
   */
  public Object getKey() {
    return this.m_key;
  }

  /**
   * Maybe null!
   * 
   * @see java.util.Map.Entry#getValue()
   */
  public Object getValue() {
    return this.m_value;
  }

  /**
   * Sets a new value instance overwriting the old value which is returned.
   * <p>
   * 
   * You may use null. But you will get it back next call!
   * <p>
   * 
   * @see java.util.Map.Entry#setValue(java.lang.Object)
   * 
   * @return the previous value instance.
   */
  public Object setValue(final Object value) {
    Object ret = this.m_value;
    this.m_value = value;
    return ret;
  }

}
