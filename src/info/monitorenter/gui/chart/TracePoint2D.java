/*
 * TracePoint2D, a tuned Point2D.Double for use with ITrace2D- implementations.
 * Copyright (c) 2007  Achim Westermann, Achim.Westermann@gmx.de
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

package info.monitorenter.gui.chart;

import java.awt.geom.Point2D;

/**
 * A specialized version of <code>java.awt.Point2D.Double </code> who carries two further values:
 * <code> double scaledX</code> and <code>double scaledY</code> which allow the
 * <code>Chart2D</code> to cache the scaled values (between 0.0 and 1.0) without having to keep a
 * copy of the aggregators (<code>ITrace2D</code>) complete tracepoints.
 * <p>
 * This avoids the necessity to care for the correct order of a set of scaled tracepoints copied for
 * caching purposes. Especially in the case of new <code>TracePoint2D</code> instances added to a
 * <code>ITrace2D</code> instance managed by a <code>Chart2D</code> there remains no
 * responsibility for sorting the cached copy. This allows that the managing <code>Chart2D</code>
 * may just rescale the newly added tracepoint instead of searching for the correct order of the new
 * tracepoint by value - comparisons of x and y: The <code>TracePoint2D</code> passed to the
 * method <code>traceChanged(Chart2DDataChangeEvent e)</code> coded in the argument is the
 * original. <br>
 * <p>
 * Why caching of scaled values for the coordinates? <br>
 * This takes more RAM but else for every <code>repaint()</code> invocation of the
 * <code>Chart2D</code> would force all tracepoints of all traces to be rescaled again.
 * <p>
 * A TracePoint2D will inform it's listener of type <code>ITrace</code> on changes of the internal
 * values.
 * <p>
 * 
 * @author Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </a>
 * @version $Revision: 1.17 $
 */
public class TracePoint2D
    extends Point2D.Double implements Comparable<TracePoint2D>, java.io.Serializable, Cloneable {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3618980079204512309L;

  /**
   * The state flag used to inform the <code>{@link ITrace2D}</code> listener via
   * <code>{@link ITrace2D#firePointChanged(TracePoint2D, int)}</code> in case a point was added.
   * <p>
   */
  public static final transient int STATE_ADDED = 1;

  /**
   * The state flag used to inform the <code>{@link ITrace2D}</code> listener via
   * <code>{@link ITrace2D#firePointChanged(TracePoint2D, int)}</code> in case a point was
   * changed.
   * <p>
   * Will be used by <code>{@link #setLocation(double, double)}</code> and
   * <code>{@link #setLocation(Point2D)}</code>.
   * <p>
   */
  public static final transient int STATE_CHANGED = 4;

  /**
   * The state flag used to inform the <code>{@link ITrace2D}</code> listener via
   * <code>{@link ITrace2D#firePointChanged(TracePoint2D, int)}</code> in case a point was
   * removed.
   * <p>
   */
  public static final transient int STATE_REMOVED = 2;

  /** Flag to highlight the point. * */
  private boolean m_highlight;

  /**
   * The reference to the listening <code>ITrace</code> who owns this point.
   * <p>
   * A trace point should be contained only in one trace!
   * <p>
   */
  private ITrace2D m_listener;

  /**
   * Scaled x value.
   */
  private double m_scaledX;

  /**
   * Scaled y value.
   */
  private double m_scaledY;

  /** The x coordinate, re-declared as the super class member will not be serialized. */
  private double m_x;

  /** The y coordinate, re-declared as the super class member will not be serialized. */
  private double m_y;

  /**
   * Construct a TracePoint2D whose coords are initalized to (x,y).
   * <p>
   * 
   * @param xValue
   *            the x value to use.
   * @param yValue
   *            the y value to use.
   */
  public TracePoint2D(final double xValue, final double yValue) {

    this.m_x = xValue;
    this.m_y = yValue;
  }

  /**
   * @see java.lang.Object#clone()
   */
  @Override
  public Object clone() {
    TracePoint2D result = (TracePoint2D) super.clone();
    result.m_x = this.m_x;
    result.m_y = this.m_y;
    result.m_scaledX = this.m_scaledX;
    result.m_scaledY = this.m_scaledY;
    result.m_highlight = this.m_highlight;
    return result;
  }

  /**
   * Compares to {@link TracePoint2D} instances by their x value in ascending order.
   * <p>
   * 
   * @param obj
   *            the point to compare to this instance.
   * @return -1 if the given point has a higher x value, 0 if it has the same value or 1 if it has a
   *         lower x value.
   * @see Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(final TracePoint2D obj) {

    int result;
    double othx = obj.getX();
    if (this.m_x < othx) {
      result = -1;
    } else {
      if (this.m_x == othx) {
        result = 0;
      } else {
        result = 1;
      }
    }
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object o) {
    return this.compareTo((TracePoint2D) o) == 0;

  }

  /**
   * Returns the listener trace connected to this trace point.
   * <p>
   * 
   * @return the listener trace connected to this trace point.
   */
  public ITrace2D getListener() {
    return this.m_listener;
  }

  /**
   * Returns the Manhattan distance of this point's normalized values (<code>{@link #getScaledX()}, {@link #getScaledY()}</code>)
   * to the given normalized coordinates.
   * <p>
   * 
   * @param xNormalized
   *            the normalized x coordinate between 0 and 1.0 to measure the Manhattan distance to.
   * @param yNormalized
   *            the normalized y coordinate between 0 and 1.0 to measure the Manhattan distance to.
   * @return the Manhattan distance of this point's normalized values (<code>{@link #getScaledX()}, {@link #getScaledY()}</code>)
   * to the given normalized coordinates.
   */
  public double getManhattanDistance(final double xNormalized, final double yNormalized) {
    double result;
    result = Math.abs(this.m_scaledX - xNormalized) + Math.abs(this.m_scaledY - yNormalized);
    return result;
  }

  /**
   * Returns the Manhattan distance of this point to the given one.
   * <p>
   * 
   * @param point
   *            the point to measure the Manhattan distance to.
   * @return the Manhattan distance of this point to the given one.
   */
  public double getManhattanDistance(final TracePoint2D point) {
    return this.getManhattanDistance(point.getX(), point.getY());
  }

  /**
   * @return the scaledX.
   */
  public final double getScaledX() {
    return this.m_scaledX;
  }

  /**
   * @return the scaledY.
   */
  public final double getScaledY() {
    return this.m_scaledY;
  }

  /**
   * @see java.awt.geom.Point2D.Double#getX()
   */
  @Override
  public double getX() {
    return this.m_x;
  }

  /**
   * @see java.awt.geom.Point2D.Double#getY()
   */
  @Override
  public double getY() {
    return this.m_y;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * @return the highlight.
   */
  public final boolean isHighlight() {
    return this.m_highlight;
  }

  /**
   * Sets if this point should be highlighted.<p>
   * 
   * If a point is highligted it will be additionally painted by <code>{@link Chart2D#getPointHighlighter()}</code> 
   * in the next paint cycle. After the paint cycle this will be reset to <code>false</code>.<p>
   *  
   * @param highlight
   *            the highlight to set
   */
  public final void setHighlight(final boolean highlight) {
    this.m_highlight = highlight;
  }

  /**
   * Allows <code>ITrace2D</code> instances to register (or deregister) themselves with this point
   * to receive (or stop receiving) change information via
   * {@link ITrace2D#firePointChanged(TracePoint2D, int)} events.
   * <p>
   * 
   * @param listener
   *            The instance that will be informed about changes or null to deregister.
   */
  public void setListener(final ITrace2D listener) {
    this.m_listener = listener;
  }

  /**
   * This method overloads the method of <code>java.awt.geom.Point2D.Double</code> to fire a
   * property change event to listeners of the corresponding <code>{@link ITrace2D}</code>
   * instances via their method <code>{@link ITrace2D#firePointChanged(TracePoint2D, int)}</code>
   * (with int argument set to <code>{@link #STATE_CHANGED}</code>).
   * <p>
   * 
   * @param xValue
   *            the new x-coordinate for this point.
   * @param yValue
   *            the new y-coordinate for this point.
   */
  @Override
  public void setLocation(final double xValue, final double yValue) {

    this.m_x = xValue;
    this.m_y = yValue;
    if (this.m_listener != null) {
      this.m_listener.firePointChanged(this, TracePoint2D.STATE_CHANGED);
    }
  }

  /**
   * Only intended for Chart2D!!!.
   * <p>
   * 
   * @param scaledX
   *            the scaledX to set
   */
  public final void setScaledX(final double scaledX) {
    this.m_scaledX = scaledX;
  }

  /**
   * Only intended for Chart2D!!!.
   * <p>
   * 
   * @param scaledY
   *            the scaledY to set
   */
  public final void setScaledY(final double scaledY) {
    this.m_scaledY = scaledY;
  }

  /**
   * @see java.awt.geom.Point2D.Double#toString()
   */
  @Override
  public String toString() {
    return "TracePoint2D[" + this.m_x + ", " + this.m_y + "]";
  }
}
