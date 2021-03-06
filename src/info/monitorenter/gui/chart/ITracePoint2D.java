/*
 * ITracePoint2d.java, interface for trace points.
 * Copyright (c) 2004 - 2013  Achim Westermann, Achim.Westermann@gmx.de
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

import info.monitorenter.gui.chart.tracepoints.CandleStick;

import java.util.Set;

/**
 * An interface for trace points.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.13 $
 */
public interface ITracePoint2D extends Comparable<ITracePoint2D>, java.io.Serializable, Cloneable {

  public static enum STATE {
    /**
     * The state flag used to inform the <code>{@link ITrace2D}</code> listener
     * via
     * <code>{@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}</code>
     * in case a point was added.
     * <p>
     */
    ADDED,
    /**
     * The state flag used to inform the <code>{@link ITrace2D}</code> listener
     * via
     * <code>{@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}</code>
     * in case {@link ITracePoint2D#addAdditionalPointPainter(IPointPainter)}
     * was invoked.
     * <p>
     */
    ADDITIONAL_POINT_PAINTER_ADDED,
    /**
     * The state flag used to inform the <code>{@link ITrace2D}</code> listener
     * via
     * <code>{@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}</code>
     * in case {@link ITracePoint2D#removeAdditionalPointPainter(IPointPainter)}
     * was invoked.
     * <p>
     */
    ADDITIONAL_POINT_PAINTER_REMOVED,
    /**
     * The state flag used to inform the <code>{@link ITrace2D}</code> listener
     * via
     * <code>{@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}</code>
     * in case a point was changed.
     * <p>
     * Will be used by <code>{@link #setLocation(double, double)}</code> and
     * <code>{@link java.awt.geom.Point2D#setLocation(java.awt.geom.Point2D)}</code>.
     * <p>
     */
    CHANGED,
    /**
     * The state flag used to inform the <code>{@link ITrace2D}</code> listener
     * via
     * <code>{@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}</code>
     * in case a point was removed.
     * <p>
     */

    REMOVED
  }

  /**
   * Adds a point painter that additionally (to the pointer painters of the
   * trace (<code>{@link ITrace2D#getTracePainters()} </code>)) paint this
   * point.
   * <p>
   * No clone will be taken. Outside modifications of the argument later on will
   * also affect this instances state!
   * <p>
   * <b>Caution!</b> This is a low level mechanism that is also used by the
   * highlighting mechanism. It is being utilized by the
   * <code>{@link Chart2D#enablePointHighlighting(boolean)}</code> which will
   * use some mouse motion listener to remove outdated highlighters and add
   * highlighters to the new point in focus by taking the highlighter configured
   * in the trace. <br/>
   * So to use point highlighting for traces you should not re-program it at
   * this level but just use
   * <code>{@link ITrace2D#addPointHighlighter(IPointPainter)}</code> and
   * </code>{@link Chart2D#enablePointHighlighting(boolean)}</code>.
   * <p>
   * 
   * @param pointPainter
   *          a point painter that will additionally (to the trace painter of
   *          the chart) paint this point.
   * 
   * @return true if this point painter was accepted (not contained before by
   *         the means of <code> {@link IPointPainter#compareTo(Object) } </code>
   *         .
   */
  public boolean addAdditionalPointPainter(IPointPainter< ? > pointPainter);

  /**
   * Returns a cloned instance (deep copy).
   * <p>
   * 
   * @return a cloned instance (deep copy)
   */
  public Object clone();

  /**
   * Returns the point painter that additionally (to the trace painter of the
   * chart) paint this point.
   * <p>
   * The original list is returned so painters may be added or removed (even all
   * painters may be cleared).
   * <p>
   * 
   * @return the point painters that additionally (to the trace painter of the
   *         chart) paint this point.
   */
  public Set<IPointPainter< ? >> getAdditionalPointPainters();

  /**
   * Returns the Euclid distance of this point's normalized values (
   * <code>{@link #getScaledX()}, {@link #getScaledY()}</code>) to the given
   * normalized coordinates.
   * <p>
   * 
   * @param xNormalized
   *          the normalized x coordinate between 0 and 1.0 to measure the
   *          Euclid distance to.
   * 
   * @param yNormalized
   *          the normalized y coordinate between 0 and 1.0 to measure the
   *          Euclid distance to.
   * 
   * @return the Euclid distance of this point's normalized values (<code>
   *         {@link #getScaledX()}, {@link #getScaledY()}</code>) to the given
   *         normalized coordinates.
   */
  public abstract double getEuclidDistance(final double xNormalized, final double yNormalized);

  /**
   * Returns the listener trace connected to this trace point.
   * <p>
   * 
   * @return the listener trace connected to this trace point.
   */
  public abstract ITrace2D getListener();

  /**
   * Returns the Manhattan distance of this point's normalized values (
   * <code>{@link #getScaledX()}, {@link #getScaledY()}</code>) to the given
   * normalized coordinates.
   * <p>
   * 
   * @param xNormalized
   *          the normalized x coordinate between 0 and 1.0 to measure the
   *          Manhattan distance to.
   * @param yNormalized
   *          the normalized y coordinate between 0 and 1.0 to measure the
   *          Manhattan distance to.
   * @return the Manhattan distance of this point's normalized values (<code>
   *         {@link #getScaledX()}, {@link #getScaledY()}</code>) to the given
   *         normalized coordinates.
   */
  public abstract double getManhattanDistance(final double xNormalized, final double yNormalized);

  /**
   * Returns the normalized coordinates of this point that should be highlighted
   * or null if <code>
   * new double[]{this.getX(), this.getY()};
   * </code> is the position to highlight.
   * <p>
   * Trace point implementations (like {@link CandleStick}) render larger spaces
   * than their center coordinate. Those may return the area of interest that
   * should be highlighted.
   * <p>
   * 
   * @return the normalized coordinates of this point that should be highlighted
   *         or null if not different from {@link #getX()} and {@link #getY()}.
   * 
   */
  public abstract double[] getNormalizedHighlightSweetSpotCoordinates();

  /**
   * @return the scaledX.
   */
  public abstract double getScaledX();

  /**
   * @return the scaledY.
   */
  public abstract double getScaledY();

  /**
   * Returns the tool tip text that should be displayed in case this trace point
   * is selected to show a tool tip.
   * 
   * @return the tool tip text that should be displayed in case this trace point
   *         is selected to show a tool tip.
   */
  public abstract String getTooltipText();

  /**
   * Returns the x value.
   * <p>
   * 
   * @return the x value.
   * 
   * @see java.awt.geom.Point2D#getX()
   */
  public abstract double getX();

  /**
   * Returns the y value.
   * <p>
   * 
   * @return the y value.
   * 
   * @see java.awt.geom.Point2D#getY()
   */
  public abstract double getY();

  /**
   * Returns true if {@link #getX()} and/or {@link #getY()} is
   * {@link Double#NaN} which signals a discontinuation.
   * <p>
   * 
   * @return true if {@link #getX()} and/or {@link #getY()} is
   *         {@link Double#NaN} which signals a discontinuation.
   */
  public boolean isDiscontinuation();

  /**
   * Returns true if {@link #getScaledX()} AND {@link #getScaledY()} is in the
   * range [0.0..1.0] and {@link #isDiscontinuation()} returns false.
   * <p>
   * This call only makes sense after the point has been added to a trace.
   * <p>
   * 
   * @return true if {@link #getScaledX()} AND {@link #getScaledY()} is in the
   *         range [0.0..1.0] and {@link #isDiscontinuation()} returns false.
   */
  public boolean isVisble();

  /**
   * Removes a point painter that additionally (to the pointer painters of the
   * trace (<code>{@link ITrace2D#getTracePainters()} </code>)) paint this
   * point.
   * <p>
   * 
   * @param pointPainter
   *          a point painter that currently is used to additionally (to the
   *          trace painter of the chart) paint this point.
   * 
   * @return true if this point painter was removed (contained before by the
   *         means of <code> {@link IPointPainter#compareTo(Object) } </code>.
   */
  public boolean removeAdditionalPointPainter(IPointPainter< ? > pointPainter);

  /**
   * Removes all point painters that additionally (to the pointer painters of
   * the trace (<code>{@link ITrace2D#getTracePainters()} </code>)) paint this
   * point.
   * <p>
   * 
   * @return all instances that were used before this call.
   */
  public Set<IPointPainter< ? >> removeAllAdditionalPointPainters();

  /**
   * Allows <code>ITrace2D</code> instances to register (or de-register)
   * themselves with this point to receive (or stop receiving) change
   * information via
   * {@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}
   * events.
   * <p>
   * 
   * @param listener
   *          The instance that will be informed about changes or null to
   *          deregister.
   */
  public abstract void setListener(final ITrace2D listener);

  /**
   * This method overloads the method of
   * <code>java.awt.geom.Point2D.Double</code> to fire a property change event
   * to listeners of the corresponding <code>{@link ITrace2D}</code> instances
   * via their method
   * <code>{@link ITrace2D#firePointChanged(ITracePoint2D, STATE, Object, Object)}</code>
   * (with int argument set to <code>{@link STATE#CHANGED}</code>).
   * <p>
   * 
   * @param xValue
   *          the new x-coordinate for this point.
   * @param yValue
   *          the new y-coordinate for this point.
   */
  public abstract void setLocation(final double xValue, final double yValue);

  /**
   * Only intended for Chart2D!!!.
   * <p>
   * 
   * @param scaledX
   *          the scaledX to set
   */
  public abstract void setScaledX(final double scaledX);

  /**
   * Only intended for Chart2D!!!.
   * <p>
   * 
   * @param scaledY
   *          the scaledY to set
   */
  public abstract void setScaledY(final double scaledY);

}
