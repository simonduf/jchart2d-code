/*
 *  IAccumulationStrategy.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2011, Achim Westermann, created on Dec 11, 2011
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
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
 *
 * File   : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $
 * Date   : $Date: 2009/02/24 16:45:41 $
 * Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart;


import info.monitorenter.gui.chart.traces.accumulationstrategies.AAccumulationStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
/**
 * Design helper to deal with the following: If a trace is unsorted then you
 * have to accumulated all n consecutive points into one regardless whether
 * they jump large distances in the value domain. The output might look like
 * something that has nothing in common with the original trace without
 * accumulations. Accumulation then is done just by deflating n consecutive
 * points (see {@link AAccumulationStrategy#ACCUMULATE_AMOUNT_OF_POINTS}).
 * <p>
 * If a trace is sorted then accumulation may be done based on value regions.
 * Consider you have sorted ascending x values that have a high density of
 * tracepoints in certain regions but a very low density in other regions. If
 * you would accumulated just n consecutive points you would thin out the
 * regions with very little data points (low density) to become even less
 * precision in those large regions while the regions with high density only
 * loose little information. If you decide to split up the visible range in to
 * parts with same value-span then you can just accumulate depending on the
 * density of x values. You will not loose data in value-ranges with low
 * density but are able to drop lots of unnecessary values in high-density
 * areas (see
 * {@link AAccumulationStrategy#ACCUMULATE_X_RANGE_WITH_RESPECT_TO_DENSITY}).
 * <p>
 * 
 * <h3>Property Change events</h3>
 * The following <code>{@link java.beans.PropertyChangeEvent}</code> may be
 * fired to <code>{@link PropertyChangeListener}</code> instances that
 * register themselves with
 * <code>{@link #addPropertyChangeListener(String, PropertyChangeListener)}</code>.
 * <table border="0">
 * <tr>
 * <th><code>{@link PropertyChangeEvent#getPropertyName()}</code></th>
 * <th><code>{@link PropertyChangeEvent#getSource()}</code></th>
 * <th><code>{@link PropertyChangeEvent#getOldValue()}</code></th>
 * <th><code>{@link PropertyChangeEvent#getNewValue()}</code></th>
 * <th><code>When fired</code></th>
 * </tr>
 * <tr>
 * <td>
 * <code>{@link info.monitorenter.gui.chart.traces.accumulationstrategies.AAccumulationStrategy#PROPERTY_ACCUMULATION_FUNCTION}</code>
 * </td>
 * <td><code>{@link AAccumulationStrategy}</code> that changed</td>
 * <td><code>{@link IAccumulationFunction}</code>, the old value</td>
 * <td><code>{@link IAccumulationFunction}</code>, the new value</td>
 * <td>
 * <code>{@link AAccumulationStrategy#setAccumulationFunction(IAccumulationFunction)}</code>
 * was called.</td>
 * </tr>
 * <tr>
 * <td>
 * <code>{@link info.monitorenter.gui.chart.ITrace2DDataAccumulating#PROPERTY_ACCUMULATION_STRATEGY_ACCUMULATION_FUNCTION_CHANGED}</code>
 * </td>
 * <td><code>{@link ITrace2DDataAccumulating}</code> that changed</td>
 * <td><code>{@link IAccumulationFunction}</code>, the old value</td>
 * <td><code>{@link IAccumulationFunction}</code>, the new value</td>
 * <td>
 * <code>{@link AAccumulationStrategy#setAccumulationFunction(IAccumulationFunction)}</code>
 * was called on the current accumulation strategy.</td>
 * </tr>
 * </table>
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public interface IAccumulationStrategy {

  /**
   * Registers a property change listener that will be informed about changes
   * of the property identified by the given <code>propertyName</code>.
   * <p>
   * 
   * @param propertyName
   *          the name of the property the listener is interested in
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   */
  public abstract void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

  /**
   * Returns the accumulationFunction.
   * <p>
   * 
   * @return the accumulationFunction
   */
  public abstract IAccumulationFunction getAccumulationFunction();

  /**
   * Template method to return an iterator over accumulated points.
   * <p>
   * 
   * @param source
   *          the real points of this trace.
   * 
   * @param amountOfPoints
   *          would allow to filter out points to accumulate by just taking n
   *          consecutive trace points.
   * 
   * 
   * @return an iterator over the accumulated points from the given iterator.
   */
  public abstract Iterator<ITracePoint2D> iterator(final ITrace2D source, final int amountOfPoints);

  /**
   * Unregisters a property change listener that has been registered for
   * listening on all properties.
   * <p>
   * 
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   */
  public abstract void removePropertyChangeListener(final PropertyChangeListener listener);

  /**
   * Removes a property change listener for listening on the given property.
   * <p>
   * 
   * @param property
   *          one of the constants with the <code>PROPERTY_</code> prefix
   *          defined in this class or subclasses.
   * 
   * @param listener
   *          the listener for this property change.
   */
  public abstract void removePropertyChangeListener(final String property, final PropertyChangeListener listener);

  /**
   * Sets the accumulationFunction to use for this strategy.
   * <p>
   * 
   * @param accumulationFunction
   *          the accumulationFunction to set.
   * 
   * @return the previous accumulation function used.
   */
  public abstract IAccumulationFunction setAccumulationFunction(IAccumulationFunction accumulationFunction);

}