/*
 *
 *  IRangePolicy.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 10:23:32
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
package info.monitorenter.gui.chart;

import info.monitorenter.util.Range;

import java.beans.PropertyChangeListener;


/**
 * <p>
 * An interface that allows an axis to be plugged with a range policy.
 * </p>
 * <p>
 * Implementations may limit the range of the underlying Chart2D's data
 * (clipping / zooming), increase it (void space offset), guarantee a minimum
 * viewport... .
 * </p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.1 $
 * 
 * @see info.monitorenter.gui.chart.AAxis
 */
public interface IRangePolicy {
  /**
   * <p>
   * Registers a property change listener that will be informed about changes of
   * the property identified by the given <code>propertyName</code>.
   * </p>
   * 
   * @param propertyName
   *          the name of the property the listener is interested in
   * 
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   * 
   * 
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Define the upper bound of the Chart2D's value range. Depends on the
   * {@link AAxis} this instance is bound to.
   * 
   * @param chartMin
   *          the minimum value of the connected Chart2D that may / should be
   *          taken into account.
   * 
   * @param chartMax
   *          the maximum value of the connected Chart2D that may / should be
   *          taken into account.
   * 
   * @return the maximum value (upper bound) for the Chart2D to display.
   * 
   */
  public double getMax(double chartMin, double chartMax);

  /**
   * Define the lower bound of the Chart2D's value range. Depends on the
   * {@link AAxis} this instance is bound to.
   * 
   * @param chartMin
   *          the minimum value of the connected Chart2D that may / should be
   *          taken into account.
   * 
   * @param chartMax
   *          the maximum value of the connected Chart2D that may / should be
   *          taken into account.
   * 
   * @return the minimum value (lower bound) for the Chart2D to display.
   * 
   */
  public double getMin(double chartMin, double chartMax);

  /**
   * <p>
   * Returns all property change listeners for the given property.
   * </p>
   * 
   * @param property
   *          one of the constants with teh <code>PROPERTY_</code> prefix
   *          defined in this class or subclasses.
   * 
   * @return the property change listeners for the given property.
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String property);

  /**
   * Get the range of this range policy.
   * <p>
   * 
   * @return he range of this range policy
   */
  public Range getRange();

  /**
   * <p>
   * Deregisters a property change listener that has been registerd for
   * listening on all properties.
   * </p>
   * 
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   * 
   * 
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * <p>
   * Removes a property change listener for listening on the given property.
   * </p>
   * 
   * @param property
   *          one of the constants with teh <code>PROPERTY_</code> prefix
   *          defined in this class or subclasses.
   * 
   * @param listener
   *          the listener for this property change.
   */
  public void removePropertyChangeListener(String property, PropertyChangeListener listener);

  /**
   * Set the range of this RangePolicy.
   * <p>
   * 
   * @param range
   *          the Range for the range policy.
   */
  public void setRange(Range range);
}
