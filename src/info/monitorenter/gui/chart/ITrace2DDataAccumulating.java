/*
 * ITrace2DDataAccumulating, the interface for all traces used by the Chart2D.
 * Copyright (c) 2004 - 2011  Achim Westermann, Achim.Westermann@gmx.de
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
 */
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.traces.accumulationfunctions.AccumulationFunctionArithmeticMeanXY;
import info.monitorenter.gui.chart.traces.accumulationfunctions.AccumulationFunctionBypass;
import info.monitorenter.gui.chart.traces.iterators.AccumulatingIteratorConseCutivePointsOrderedXValues;
import info.monitorenter.gui.chart.traces.iterators.AccumulatingIteratorConsecutivePoints;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Iterator;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * An <code>{@link IAxis}</code> sub interface intended for implementations that
 * are able to accumulate several points into a single one.
 * <p>
 * 
 * This is needed for performance reasons. Consider a trace containing 1.000.000
 * trace points. It would be very slow to move the chart window or in any case
 * various repaints are caused (e.g. by tooltips or spanning a rectangle to
 * zoom).
 * <p>
 * Therefore this sub-interface gives more information to the trace in order to
 * be able to drop certain points:
 * <ul>
 * <li>How many points should it's iterator return?</li>
 * <li>In which range should the points be (visibility)?</li>
 * </ul>
 * As a result only few points are really returned to the renderer (
 * <code>{@link Chart2D} </code>) and painting will become much faster but also
 * able to adapt to be more detailed in value subdomains (in the case of
 * zooming).
 * <p>
 * 
 * <h3>Property Change events</h3>
 * 
 * Please see the class documentation of {@link ITrace2D} for basic supported
 * events. The following <code>{@link java.beans.PropertyChangeEvent}</code> may
 * be additionally fired to <code>{@link PropertyChangeListener}</code>
 * instances that register themselves with
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
 * <code>{@link info.monitorenter.gui.chart.ITrace2DDataAccumulating#PROPERTY_ACCUMULATION_STRATEGY}</code>
 * </td>
 * <td><code>{@link ITrace2DDataAccumulating}</code> that changed</td>
 * <td><code>{@link AccumulationStrategy}</code>, the old value</td>
 * <td><code>{@link AccumulationStrategy}</code>, the new value</td>
 * <td>
 * <code>{@link ITrace2DDataAccumulating#setAccumulationStrategy(AccumulationStrategy)}</code>
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
 * <code>{@link AccumulationStrategy#setAccumulationFunction(IAccumulationFunction)}</code>
 * was called on the current accumulation strategy.</td>
 * </tr>
 * </table>
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.41 $
 */
public interface ITrace2DDataAccumulating extends ITrace2D, PropertyChangeListener, Serializable {

  /**
   * Design helper to deal with the following: If a trace is unsorted then you
   * have to accumulated all n consecutive points into one regardless whether
   * they jump large distances in the value domain. The output might look like
   * something that has nothing in common with the original trace without
   * accumulations. Accumulation then is done just by deflating n consecutive
   * points (see {@link AccumulationStrategy#ACCUMULATE_AMOUNT_OF_POINTS}).
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
   * {@link AccumulationStrategy#ACCUMULATE_X_RANGE_WITH_RESPECT_TO_DENSITY}).
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
   * <code>{@link info.monitorenter.gui.chart.ITrace2DDataAccumulating.AccumulationStrategy#PROPERTY_ACCUMULATION_FUNCTION}</code>
   * </td>
   * <td><code>{@link AccumulationStrategy}</code> that changed</td>
   * <td><code>{@link IAccumulationFunction}</code>, the old value</td>
   * <td><code>{@link IAccumulationFunction}</code>, the new value</td>
   * <td>
   * <code>{@link ITrace2DDataAccumulating.AccumulationStrategy#setAccumulationFunction(IAccumulationFunction)}</code>
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
   * <code>{@link AccumulationStrategy#setAccumulationFunction(IAccumulationFunction)}</code>
   * was called on the current accumulation strategy.</td>
   * </tr>
   * </table>
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public static enum AccumulationStrategy {
    /**
     * This strategy will just accumulate <code>amountOfPoints</code>
     * consecutive points without caring for data density. Best use this
     * whenever you have unordered (by x value) traces.
     * <p>
     */
    ACCUMULATE_AMOUNT_OF_POINTS(new AccumulationFunctionArithmeticMeanXY()) {
      public Iterator<ITracePoint2D> iterator(final ITrace2D source, final int amountOfPoints) {
        return new AccumulatingIteratorConsecutivePoints(source, this.getAccumulationFunction(), amountOfPoints);
      }
    },
    /**
     * This strategy will just accumulate <code>amountOfPoints</code>
     * consecutive points without caring for data density. Best use this
     * whenever you have ordered (by x value) traces and want to cut off
     * invisible points at the beginning (zoom mode).
     */
    ACCUMULATE_AMOUNT_OF_POINTS_ASCENDING_X_VALUES(new AccumulationFunctionArithmeticMeanXY()) {
      public Iterator<ITracePoint2D> iterator(final ITrace2D source, final int amountOfPoints) {
        return new AccumulatingIteratorConseCutivePointsOrderedXValues(source, this.getAccumulationFunction(), amountOfPoints);
      }
    },

    /**
     * Bypass for accumulation: Just the given original <code>source</code>
     * argument is returned from it's method
     * <code>{@link #iterator(ITrace2D, int)}</code>.
     */
    ACCUMULATE_BYPASS(new AccumulationFunctionBypass()) {
      public Iterator<ITracePoint2D> iterator(final ITrace2D source, final int amountOfPoints) {
        return source.iterator();
      }
    },
    /**
     * This strategy will use the x-range and take all following points out of
     * the <code>source</code> (under the assumption trace is sorted by
     * x-values) that are within the range and then accumulate them to a single
     * trace point. Best use this whenever you have a trace with ordered x
     * values to get accumulation based on density of x-values and thereby avoid
     * loss of important points in areas with low x-value densities.
     * <p>
     */
    ACCUMULATE_X_RANGE_WITH_RESPECT_TO_DENSITY(new AccumulationFunctionArithmeticMeanXY()) {
      /**
       * Accumulates all points that come from <code>source</code> and are in
       * the given <code>xRange</code>. Once a point comes from
       * <code>source</code> outside the given <code>xRange</code> accumulation
       * is done and result is returned.
       * <p>
       * In this case <code>xRange</code> is not the total visible range given
       * to {@link ITrace2DDataAccumulating#iterator(int)} but a segment within
       * that range.
       * <p>
       * 
       * Note that this only makes sense for traces with points ordered
       * ascending by x-value.
       * <p>
       * 
       */
      public Iterator<ITracePoint2D> iterator(final ITrace2D source, final int amountOfPoints) {
        return null;
      }
    };

    /**
     * The property key defining the
     * <code>{@link #getAccumulationFunction()}</code> property. Use in
     * combination with
     * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
     */
    public static String PROPERTY_ACCUMULATION_FUNCTION = "AccumulationStrategy.PROPERTY_ACCUMULATION_FUNCTION";

    /**
     * The accumulation function used.
     */
    private IAccumulationFunction m_accumulationFunction;

    /**
     * The instance that add support for firing
     * <code>PropertyChangeEvents</code> and maintaining
     * <code>PropertyChangeListeners</code>.
     * <p>
     */
    protected PropertyChangeSupport m_propertyChangeSupport = new SwingPropertyChangeSupport(this);

    /**
     * Constructor taking the accumulation function to use.
     * <p>
     * Point != null)
     * 
     * @param accumulationFunction
     *          the accumulation function to use.
     *          <p>
     */
    private AccumulationStrategy(IAccumulationFunction accumulationFunction) {
      this.setAccumulationFunction(accumulationFunction);
    }

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
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
      this.m_propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Fires a property change event to the registered listeners.
     * <p>
     * 
     * @param property
     *          one of the <code>PROPERTY_XXX</code> constants defined in <code>
     *          {@link AccumulationStrategy}</code>.
     * 
     * @param oldvalue
     *          the old value of the property.
     * 
     * @param newvalue
     *          the new value of the property.
     */
    protected final void firePropertyChange(final String property, final Object oldvalue, final Object newvalue) {
      this.m_propertyChangeSupport.firePropertyChange(property, oldvalue, newvalue);
    }

    /**
     * Returns the accumulationFunction.
     * <p>
     * 
     * @return the accumulationFunction
     */
    public IAccumulationFunction getAccumulationFunction() {
      return this.m_accumulationFunction;
    }

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
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
      this.m_propertyChangeSupport.removePropertyChangeListener(listener);
    }

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
    public void removePropertyChangeListener(final String property, final PropertyChangeListener listener) {
      this.m_propertyChangeSupport.removePropertyChangeListener(property, listener);
    }

    /**
     * Sets the accumulationFunction to use for this strategy.
     * <p>
     * 
     * @param accumulationFunction
     *          the accumulationFunction to set.
     * 
     * @return the previous accumulation function used.
     */
    public IAccumulationFunction setAccumulationFunction(IAccumulationFunction accumulationFunction) {
      IAccumulationFunction result = this.m_accumulationFunction;
      this.m_accumulationFunction = accumulationFunction;
      this.firePropertyChange(PROPERTY_ACCUMULATION_FUNCTION, result, accumulationFunction);
      return result;
    }
  }

  /**
   * The property key defining the
   * <code>{@link #setAccumulationStrategy(AccumulationStrategy)}</code>
   * property. Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_ACCUMULATION_STRATEGY = "ITrace2DDataAccumulating.PROPERTY_ACCUMULATION_STRATEGY";

  /**
   * The property key defining a change of the
   * <code>{@link #setAccumulationStrategy(AccumulationStrategy)}</code>
   * property: Namely
   * {@link AccumulationStrategy#setAccumulationFunction(IAccumulationFunction)}
   * was called.
   * <p>
   * 
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_ACCUMULATION_STRATEGY_ACCUMULATION_FUNCTION_CHANGED = "ITrace2DDataAccumulating.PROPERTY_ACCUMULATION_STRATEGY_ACCUMULATION_FUNCTION_CHANGED";

  /**
   * Returns the current accumulation strategy.
   * <p>
   * 
   * @return the current accumulation strategy.
   */
  public AccumulationStrategy getAccumulationStrategy();

  /**
   * Returns an <code>Iterator</code> over the internal <code>
   * {@link ITracePoint2D}</code> instances that might accumulate internal
   * {@link ITracePoint2D} instances into one.
   * <p>
   * Implementations should be synchronized. As this method may return
   * "synthetic" points created at runtime that are made up by accumulation of
   * several real internal points modifications of the instances returned are
   * lossy!
   * <p>
   * There is no guarantee that changes made to the contained tracepoints will
   * be reflected in the display immediately. The order the iterator returns the
   * <code>TracePoint2D</code> instances decides how the <code>Chart2D</code>
   * will paint the trace.
   * <p>
   * <b>Important contract</b><br/>
   * No bounds of traces in x dimension should ever be changed for sorted traces
   * (by x value). This means the lowest point in x-dimension and the highest
   * point in x-dimension have to be returned unchanged in order not to change
   * the x value domain.
   * <p>
   * 
   * @param amountOfVisiblePoints
   *          The amount of points that should at least be returned to the
   *          caller. Note that implementation may return twice as much points
   *          depending on the accumulation function that is used (e.g. an
   *          arithmetic mean function would need at least two points to fold
   *          them into one so if the internal amount of points is not twice as
   *          much as the requested amount accumulation has to be skipped).
   * 
   * @return an <code>Iterator</code> over the internal <code>
   *         {@link ITracePoint2D}</code> instances that might accumulate
   *         internal {@link ITracePoint2D} instances into one by taking visible
   *         range and desired amount of points into account.
   */
  public Iterator<ITracePoint2D> iterator(final int amountOfVisiblePoints);;

  /**
   * Installs the given accumulation strategy.
   * <p>
   * 
   * @param accumulationStrategy
   *          the accumulation strategy to use.
   * 
   * @return the accumulation strategy used previously.
   */
  public AccumulationStrategy setAccumulationStrategy(final AccumulationStrategy accumulationStrategy);
}
