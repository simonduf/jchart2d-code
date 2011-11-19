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
import info.monitorenter.util.Range;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Iterator;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * An <code>{@link IAxis}</code> subinterface intended for implementations that
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
   * {@link Iterator} that decorates an iterator by the feature of accumulation
   * of points.
   * <p>
   * Contract:
   * <ul>
   * <li>
   * No point with an x value of <code>NaN</code> must be accumulated. Those are
   * discontinuations and must be preserved.</li>
   * <li>
   * The first call to {@link #next()} has to return the same as the first point
   * in the given / wrapped iterator in case that the visible range does not
   * exclude it.</li>
   * <li>
   * The last call to {@link #next()} has to return the same as the last point
   * in the given / wrapped iterator in case that the visible range does not
   * exclude it.</li>
   * <li>
   * The visible range may be ignored.</li>
   * <li>
   * If the visible range is not ignored and excludes the first point(s) of the
   * wrapped iterator then the first point returned from {@link #next()} has to
   * be an interpolated point at the exact coordinate of the lower bound of the
   * range.</li>
   * <li>
   * If the visible range is not ignored and excludes the last point(s) of the
   * wrapped iterator then the last point returned from {@link #next()} has to
   * be an interpolated point at the exact coordinate of the upper bound of the
   * range.</li>
   * </ul>
   * <p>
   * 
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public abstract class AAccumulationIterator implements Iterator<ITracePoint2D> {

    /** The accumulation function being used. */
    private final IAccumulationFunction m_accumulationFunction;

    /**
     * The amount of visible points to accumulate. This will be used in case we
     * do accumulate <code>amountOfPoints</code> consecutive points without
     * caring for data density. In case of density - based accumulation this is
     * used to compute the range segments to accumulate values within. However
     * then the total amount of returned points may be smaller than this value
     * as some segments might not contain any point.
     */
    private final int m_amountOfVisiblePoints;

    /**
     * The original iterator that is decorated with the point accumulation
     * feature.
     */
    private final Iterator<ITracePoint2D> m_originalIterator;

    /**
     * The amount of point in the source iterator.
     */
    private int m_totalPointsInSource;

    /**
     * The visible x range which may be used to find out if a trace point is
     * visible. This may be of help when we know that the original iterator
     * returns ascending x values.
     */
    private final Range m_visibleRangeX;

    /**
     * The visible y range which may be used to find out if a trace point is
     * visible. This may be of help when we know that the original iterator
     * returns ascending y values.
     */
    private final Range m_visibleRangeY;

    /**
     * Constructor with all that is needed for accumulating points.
     * <p>
     * 
     * @param originalIterator
     *          the iterator to decorate with the feature of accumulating
     *          points.
     * 
     * @param accumulationFunction
     *          the function to use for point - accumulation.
     * 
     * @param visibleRangeX
     *          The visible x range which may be used to find out if a trace
     *          point is visible: This may be of help when we know that the
     *          original iterator returns ascending x values.
     * 
     * @param visibleRangeY
     *          The visible y range which may be used to find out if a trace
     *          point is visible: This may be of help when we know that the
     *          original iterator returns ascending y values.
     * 
     * @param amountOfVisiblePoints
     *          The amount of visible points to accumulate. This will be used in
     *          case we do accumulate <code>amountOfPoints</code> consecutive
     *          points without caring for data density. In case of density -
     *          based accumulation this is used to compute the range segments to
     *          accumulate values within. However then the total amount of
     *          returned points may be smaller than this value as some segments
     *          might not contain any point.
     * 
     * @param totalPointsInSource
     *          The amount of point in the source iterator.
     */
    public AAccumulationIterator(final Iterator<ITracePoint2D> originalIterator, final IAccumulationFunction accumulationFunction,
        final Range visibleRangeX, final Range visibleRangeY, final int amountOfVisiblePoints, final int totalPointsInSource) {
      this.m_originalIterator = originalIterator;
      this.m_accumulationFunction = accumulationFunction;
      this.m_visibleRangeX = visibleRangeX;
      this.m_visibleRangeY = visibleRangeY;
      this.m_amountOfVisiblePoints = amountOfVisiblePoints;
      this.m_totalPointsInSource = totalPointsInSource;
    }

    /**
     * Returns the accumulationFunction.
     * <p>
     * 
     * @return the accumulationFunction
     */
    protected IAccumulationFunction getAccumulationFunction() {
      return this.m_accumulationFunction;
    }

    /**
     * Returns the amountOfVisiblePoints.
     * <p>
     * 
     * @return the amountOfVisiblePoints
     */
    protected int getAmountOfVisiblePoints() {
      return this.m_amountOfVisiblePoints;
    }

    /**
     * Returns the originalIterator.
     * <p>
     * 
     * @return the originalIterator
     */
    protected Iterator<ITracePoint2D> getOriginalIterator() {
      return this.m_originalIterator;
    }

    /**
     * Returns the totalPointsInSource.
     * <p>
     * 
     * @return the totalPointsInSource
     */
    protected int getTotalPointsInSource() {
      return this.m_totalPointsInSource;
    }

    /**
     * Returns the visibleRangeX.
     * <p>
     * 
     * @return the visibleRangeX
     */
    protected Range getVisibleRangeX() {
      return this.m_visibleRangeX;
    }

    /**
     * Returns the visibleRangeY.
     * <p>
     * 
     * @return the visibleRangeY
     */
    protected Range getVisibleRangeY() {
      return this.m_visibleRangeY;
    }

  }

  /**
   * An implementation that - without any care for order of x or y values and
   * without any care for densitiy-based accumulation accumulates n consecutive
   * points.
   * <p>
   * Invisible points will still be accumulated allowing the {@link Chart2D}
   * rendering the data to interpolate into visibility bounds. See the super
   * class description for additional contracts.
   * <p>
   * Points having a {@link ITracePoint2D#getX()} value of {@link Double#NaN}
   * will not be accumulated. Those are discontinuations that have to be
   * preserved. However consecutive <code>NaN</code> x-values will be
   * accumulated into one which decreases the possibility that no points get
   * accumulated. However in the worst case no accumulations may be performed:
   * In the case that alternating <code>NaN</code> x-values and non-
   * <code>NaN</code> x values occur.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public class AccumulatingIteratorConsecutivePoints extends AAccumulationIterator {

    /**
     * Computed points to accumulate per {@link #next()}. Kept as a member to
     * save computations proportional O(n) (n are amount of source points).
     */
    private int m_countPerNext;

    /**
     * Return the first point in case this is true, then switch to false.
     */
    private boolean m_firstCall2Next = true;

    /**
     * Store the last available point in case we found it but first have to
     * return an accumulation result.
     */
    private ITracePoint2D m_lastPoint;

    /**
     * Internal needed to store if we had to return an accumulated point while
     * seeing NaN which we then have to return next time.
     */
    private ITracePoint2D m_previousNaN;

    /**
     * Constructor with all that is needed for accumulating points.
     * <p>
     * 
     * @param originalIterator
     *          the iterator to decorate with the feature of accumulating
     *          points.
     * 
     * @param accumulationFunction
     *          the function to use for point - accumulation.
     * 
     * @param visibleRangeX
     *          The visible x range which may be used to find out if a trace
     *          point is visible: This may be of help when we know that the
     *          original iterator returns ascending x values.
     * 
     * @param visibleRangeY
     *          The visible y range which may be used to find out if a trace
     *          point is visible: This may be of help when we know that the
     *          original iterator returns ascending y values.
     * 
     * @param amountOfVisiblePoints
     *          The amount of visible points to accumulate. This will be used in
     *          case we do accumulate <code>amountOfPoints</code> consecutive
     *          points without caring for data density. In case of density -
     *          based accumulation this is used to compute the range segments to
     *          accumulate values within. However then the total amount of
     *          returned points may be smaller than this value as some segments
     *          might not contain any point.
     * 
     * @param totalPointsInSource
     *          The amount of point in the source iterator. *
     */
    public AccumulatingIteratorConsecutivePoints(final Iterator<ITracePoint2D> originalIterator,
        final IAccumulationFunction accumulationFunction, final Range visibleRangeX, final Range visibleRangeY,
        final int amountOfVisiblePoints, final int totalPointsInSource) {
      super(originalIterator, accumulationFunction, visibleRangeX, visibleRangeY, amountOfVisiblePoints, totalPointsInSource);
      /*
       * Compute the amount of points per next() to accumulate:
       */
      int targetCount = this.getAmountOfVisiblePoints();
      int sourceCount = this.getTotalPointsInSource();
      this.m_countPerNext = (int) Math.ceil(sourceCount / targetCount);
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return (this.getOriginalIterator().hasNext()) || (this.m_lastPoint != null);
    }

    /**
     * @see java.util.Iterator#next()
     */
    public ITracePoint2D next() {
      /*
       * Work with the accumulation function here:
       */
      ITracePoint2D result = null;
      IAccumulationFunction accumulate = this.getAccumulationFunction();
      Iterator<ITracePoint2D> iterator = this.getOriginalIterator();
      ITracePoint2D point;
      int amountToAccumulate = this.m_countPerNext;
      if (this.m_firstCall2Next) {
        result = iterator.next();
        this.m_firstCall2Next = false;
      } else {

        if (!iterator.hasNext()) {
          /*
           * This is the case if in previous call [a] was hit. hasNext of this
           * implementation returns still true to have the stored last point be
           * returned unchanged.
           */
          result = this.m_lastPoint;
          this.m_lastPoint = null;
        } else {
          while ((amountToAccumulate > 0) && (iterator.hasNext())) {
            if (this.m_previousNaN != null) {
              /*
               * In case our previous call next returned an accumulated point
               * but kept in mind that NaN (discontinuation) was found:
               * immediately return the discontinuation.
               */
              result = this.m_previousNaN;
            } else {
              point = iterator.next();
              if (!point.isDiscontinuation()) {
                /*
                 * [a] We must not blindly add this point to the accumulation
                 * result: The contract is that the last point has to be
                 * returned unchanged (as this implementation does not make use
                 * of the given ranges). In case this is the last point: Store
                 * it for the next invocation of next.
                 */
                if (iterator.hasNext()) {
                  accumulate.addPointToAccumulate(point);
                } else {
                  this.m_lastPoint = point;
                }
                // wipe out potential previous NaN!
                this.m_previousNaN = null;
                amountToAccumulate--;
              } else {
                // point is NaN!
                if (this.m_previousNaN != null) {
                  /*
                   * Don't care: our previous point was also NaN. So skip that
                   * NaN discontinuation.
                   */
                } else {

                  result = accumulate.getAccumulatedPoint();
                  if (result == null) {
                    /*
                     * We're good as this NaN point was the first to accumulate:
                     * Just return it!
                     */
                    result = point;
                    break;
                  } else {
                    /*
                     * Bad luck: We found a NaN but also a previous accumulation
                     * result. We have to return the previous accumulation
                     * result but keep in mind that we next have a NaN to
                     * return.
                     */
                    this.m_previousNaN = point;
                    break;
                  }
                }

              }
            }
          }
        }

        /*
         * We left the loop for several reasons:
         * 
         * 1. amountToAccumulate == 0 or iterator empty: get accumulated point
         * and return it.
         * 
         * 2. NaN found with no current accumulation result: return NaN
         * 
         * 3. NaN found but with current accumulation results: Return the
         * accumulated point, store NaN result.
         */

        // case 1:
        if (result == null) {
          result = accumulate.getAccumulatedPoint();
          if (result == null) {
            throw new IllegalStateException("This seems a programming error or unexpected state!");
          }
        } else {
          // case 2 and 3.
        }
      }
      return result;

    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {

      throw new UnsupportedOperationException("This is not supported for " + this.getClass().getName()
          + ". Data is not contained but computed (non 1-1) from an underlying source. ");
    }

  }

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
      public Iterator<ITracePoint2D> iterator(final Range xRange, final Range yRange, final Iterator<ITracePoint2D> source,
          final int amountOfPoints, final int totalPointsInSource) {
        return new AccumulatingIteratorConsecutivePoints(source, this.getAccumulationFunction(), xRange, yRange, amountOfPoints,
            totalPointsInSource);
      } 
    },
    /**
     * This strategy will just accumulate <code>amountOfPoints</code>
     * consecutive points without caring for data density. Best use this
     * whenever you have unordered (by x value) traces.      
     */
    ACCUMULATE_AMOUNT_OF_POINTS_ASCENDING_X_VALUES(new AccumulationFunctionArithmeticMeanXY()) {
      public Iterator<ITracePoint2D> iterator(final Range xRange, final Range yRange, final Iterator<ITracePoint2D> source,
          final int amountOfPoints, final int totalPointsInSource) {
        return new AccumulatingIteratorConsecutivePoints(source, this.getAccumulationFunction(), xRange, yRange, amountOfPoints,
            totalPointsInSource);
      } 
    },
    
    /**
     * Bypass for accumulation: Just the given original <code>source</code>
     * argument is returned from it's method
     * <code>{@link #iterator(Range, Range, Iterator, int, int)}</code>.
     */
    ACCUMULATE_BYPASS(new AccumulationFunctionBypass()) {
      public Iterator<ITracePoint2D> iterator(final Range xRange, final Range yRange, final Iterator<ITracePoint2D> source,
          final int amountOfPoints, final int totalPointsInSource) {
        return source;
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
       * to {@link ITrace2DDataAccumulating#iterator(Range, Range, int)} but a
       * segment within that range.
       * <p>
       * 
       * Note that this only makes sense for traces with points ordered
       * ascending by x-value.
       * <p>
       * 
       */
      public Iterator<ITracePoint2D> iterator(final Range xRange, final Range yRange, final Iterator<ITracePoint2D> source,
          final int amountOfPointsk, final int totalPointsInSource) {
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
     * @param xRange
     *          would allow to filter out points outside the valid x-value
     *          bounds.
     * 
     * @param yRange
     *          would allow to filter out points outside the valid y-value
     *          bounds.
     * 
     * @param source
     *          iterator over the real points of this trace.
     * 
     * @param amountOfPoints
     *          would allow to filter out points to accumulate by just taking n
     *          consecutive trace points.
     * 
     * @param totalPointsInSource
     *          the amount of points in the source iterator.
     * 
     * @return an iterator over the accumulated points from the given iterator.
     */
    public abstract Iterator<ITracePoint2D> iterator(final Range xRange, final Range yRange, final Iterator<ITracePoint2D> source,
        final int amountOfPoints, final int totalPointsInSource);

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
   * {@link TracePoint2D}</code> instances that might accumulate internal
   * {@link ITracePoint2D} instances into one by taking visible range and
   * desired amount of points into account.
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
   * @param visibleRangeX
   *          All internal points with X values outside this range will be
   *          ignored.
   * 
   * @param visibleRangeY
   *          All internal points with Y values outside this range will be
   *          ignored.
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
   *         {@link TracePoint2D}</code> instances that might accumulate
   *         internal {@link ITracePoint2D} instances into one by taking visible
   *         range and desired amount of points into account.
   */
  public Iterator<ITracePoint2D> iterator(final Range visibleRangeX, final Range visibleRangeY, final int amountOfVisiblePoints);;

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
