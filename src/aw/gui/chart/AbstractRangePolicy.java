/*
 *
 *  AbstractRangePolicy.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 10:34:08
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
package aw.gui.chart;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

import aw.util.Range;

/**
 * <p>
 * A default superclass for IRangePolicy implementations that adds support for
 * setting and getting ranges.
 * </p>
 * <p>
 * Should be used by any implementation that really works on the data of ranges
 * (not unbounded ranges). Subclasses should access the internal member range or
 * use {@link #getRange()}.
 * </p>
 * 
 * <h3>Property Change events</h3>
 * <p>
 * <table border="0">
 * <tr>
 * <th><code>property</code></th>
 * <th><code>oldValue</code></th>
 * <th><code>newValue</code></th>
 * <th>occurance</th>
 * </tr>
 * <tr>
 * <td><code>{@link #PROPERTY_RANGE}</code></td>
 * <td><code>{@link aw.util.Range}</code> that changed</td>
 * <td><code>{@link aw.util.Range}</code>, the new value</td>
 * <td>Fired if any bound of the range changed (min or max).</td>
 * </tr>
 * <tr>
 * <tr>
 * <td><code>{@link #PROPERTY_RANGE_MAX}</code></td>
 * <td><code>{@link java.lang.Double}</code>, the old max value of the
 * range. </td>
 * <td><code>{@link aw.util.Range}</code>, the new max value of the range.
 * </td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><code>{@link #PROPERTY_RANGE_MIN}</code></td>
 * <td><code>{@link java.lang.Double}</code>, the old min value of the
 * range. </td>
 * <td><code>{@link aw.util.Range}</code>, the new min value of the range.
 * </td>
 * <td></td>
 * </tr>
 * </table>
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.8 $
 * 
 */
public abstract class AbstractRangePolicy implements IRangePolicy {
  /**
   * The property key defining the <code>max</code> property.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_RANGE_MAX = "rangepolicy.rangemax";

  /**
   * The property key defining a change of the <code>min</code> or the
   * <code>max</code> property.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_RANGE = "rangepolicy.range";

  /**
   * The property key defining the <code>min</code> property.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_RANGE_MIN = "rangepolicy.rangemin";

  /**
   * The instance that add support for firing <code>PropertyChangeEvents</code>
   * and maintaining <code>PropertyChangeListeners</code>.
   * 
   * {@link PropertyChangeListener} instances.
   */

  protected PropertyChangeSupport m_propertyChangeSupport = new SwingPropertyChangeSupport(this);

  /**
   * The internal range that may be taken into account for returning bounds from
   * {@link IRangePolicy#getMax(double, double)} and
   * {@link IRangePolicy#getMax(double, double)}.
   * <p>
   */
  private Range m_range;

  /**
   * Creates a range policy backed by the given range.
   * <p>
   * 
   * @param range
   *          the range that may be used to decide about the policy of
   *          displaying the range.
   */
  public AbstractRangePolicy(final Range range) {
    this.m_range = range;
  }

  /**
   * @see aw.gui.chart.ITrace2D#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public final void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * <p>
   * Fires a property change event to the registered listeners.
   * </p>
   * 
   * @param property
   *          one of the <code>PROPERTY_XXX</code> constants defined in
   *          <code>{@link ITrace2D}</code>.
   * 
   * @param oldvalue
   *          the old value of the property.
   * 
   * @param newvalue
   *          the new value of the property.
   * 
   */
  protected final void firePropertyChange(final String property, final Object oldvalue,
      final Object newvalue) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("AbstractRangePolicy.firePropertyChange (" + property + "), 0 locks");
    }
    this.m_propertyChangeSupport.firePropertyChange(property, oldvalue, newvalue);
  }

  /**
   * @see aw.gui.chart.ITrace2D#getPropertyChangeListeners(String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String property) {
    return this.m_propertyChangeSupport.getPropertyChangeListeners(property);
  }

  /**
   * Returns the internal range that is used to decide about the policy of
   * displaying the chart.
   * <p>
   * 
   * @return the internal range that may be taken into account for returning
   *         bounds from {@link IRangePolicy#getMax(double, double)} and
   *         {@link IRangePolicy#getMax(double, double)}.
   * 
   */
  public final Range getRange() {
    return this.m_range;
  }

  /**
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String property,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(property, listener);
  }

  /**
   * Sets the internal range that is used to decide about the policy of
   * displaying the chart.
   * <p>
   * 
   * @param range
   *          the internal range that may be taken into account for returning
   *          bounds from {@link IRangePolicy#getMax(double, double)} and
   *          {@link IRangePolicy#getMax(double, double)}.
   */
  public void setRange(final Range range) {
    double oldMin = this.m_range.getMin();
    double oldMax = this.m_range.getMax();
    Range oldRange = this.m_range;
    boolean minchanged = range.getMin() != oldMin;
    boolean maxchanged = range.getMax() != oldMax;
    this.m_range = range;
    if (minchanged) {
      this.firePropertyChange(PROPERTY_RANGE_MIN, new Double(oldMin), new Double(range.getMin()));
    }
    if (minchanged) {
      this.firePropertyChange(PROPERTY_RANGE_MAX, new Double(oldMax), new Double(range.getMax()));
    }
    if (minchanged || maxchanged) {
      this.firePropertyChange(PROPERTY_RANGE, oldRange, this.m_range);
    }
  }

}
