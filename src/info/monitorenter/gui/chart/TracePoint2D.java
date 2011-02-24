/*
 * TracePoint2D, a tuned Point2D.Double for use with ITrace2D- implementations.
 * Copyright (c) 2004 - 2010  Achim Westermann, Achim.Westermann@gmx.de
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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A specialized version of <code>java.awt.Point2D.Double </code> who carries
 * two further values: <code> double scaledX</code> and
 * <code>double scaledY</code> which allow the <code>Chart2D</code> to cache the
 * scaled values (between 0.0 and 1.0) without having to keep a copy of the
 * aggregators (<code>ITrace2D</code>) complete tracepoints.
 * <p>
 * This avoids the necessity to care for the correct order of a set of scaled
 * tracepoints copied for caching purposes. Especially in the case of new
 * <code>TracePoint2D</code> instances added to a <code>ITrace2D</code> instance
 * managed by a <code>Chart2D</code> there remains no responsibility for sorting
 * the cached copy. This allows that the managing <code>Chart2D</code> may just
 * rescale the newly added tracepoint instead of searching for the correct order
 * of the new tracepoint by value - comparisons of x and y: The
 * <code>TracePoint2D</code> passed to the method
 * <code>traceChanged(Chart2DDataChangeEvent e)</code> coded in the argument is
 * the original. <br>
 * <p>
 * Why caching of scaled values for the coordinates? <br>
 * This takes more RAM but else for every <code>repaint()</code> invocation of
 * the <code>Chart2D</code> would force all tracepoints of all traces to be
 * rescaled again.
 * <p>
 * A TracePoint2D will inform it's listener of type <code>ITrace</code> on
 * changes of the internal values.
 * <p>
 * 
 * @author Achim Westermann <a
 *         href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </a>
 * @version $Revision: 1.26 $
 */
public class TracePoint2D extends Point2D.Double implements ITracePoint2D {
  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3618980079204512309L;

  /**
   * The reference to the listening <code>ITrace</code> who owns this point.
   * <p>
   * A trace point should be contained only in one trace!
   * <p>
   */
  private ITrace2D m_listener;

  /**
   * The list of additional point painters.
   */
  private Set<IPointHighlighter< ? >> m_additionalPointHighlighters = new LinkedHashSet<IPointHighlighter< ? >>();

  /**
   * Scaled x value.
   */
  private double m_scaledX;

  /**
   * Scaled y value.
   */
  private double m_scaledY;

  /**
   * The x coordinate, re-declared as the super class member will not be
   * serialized.
   */
  private double m_x;

  /**
   * The y coordinate, re-declared as the super class member will not be
   * serialized.
   */
  private double m_y;

  /**
   * Intended for <code>{@link TracePointProviderDefault}</code> only.
   * <p>
   */
  protected TracePoint2D() {
    // nop
  }

  /**
   * Construct a TracePoint2D whose coords are initalized to (x,y).
   * <p>
   * 
   * @param xValue
   *          the x value to use.
   * @param yValue
   *          the y value to use.
   */
  public TracePoint2D(final double xValue, final double yValue) {

    this.m_x = xValue;
    this.m_y = yValue;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#addAdditionalPointHighlighter(info.monitorenter.gui.chart.IPointHighlighter)
   */
  public final boolean addAdditionalPointHighlighter(
      final IPointHighlighter< ? > additionalPointPainter) {
    boolean result = this.m_additionalPointHighlighters.add(additionalPointPainter);
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#clone()
   */
  @Override
  public Object clone() {
    TracePoint2D result = (TracePoint2D) super.clone();
    result.m_x = this.m_x;
    result.m_y = this.m_y;
    result.m_scaledX = this.m_scaledX;
    result.m_scaledY = this.m_scaledY;
    result.m_additionalPointHighlighters = new LinkedHashSet<IPointHighlighter< ? >>(
        this.m_additionalPointHighlighters);
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#compareTo(info.monitorenter.gui.chart.ITracePoint2D)
   */
  public int compareTo(final ITracePoint2D obj) {

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
   * @see info.monitorenter.gui.chart.ITracePoint2D#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object o) {
    boolean result = false;
    if (o instanceof ITracePoint2D) {
      result = this.compareTo((ITracePoint2D) o) == 0;
    }
    return result;

  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getAdditionalPointHighlighters()
   */
  public final Set<IPointHighlighter< ? >> getAdditionalPointHighlighters() {
    return this.m_additionalPointHighlighters;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getListener()
   */
  public ITrace2D getListener() {
    return this.m_listener;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getManhattanDistance(double,
   *      double)
   */
  public double getManhattanDistance(final double xNormalized, final double yNormalized) {
    double result;
    result = Math.abs(this.m_scaledX - xNormalized) + Math.abs(this.m_scaledY - yNormalized);
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getManhattanDistance(info.monitorenter.gui.chart.ITracePoint2D)
   */
  public double getManhattanDistance(final ITracePoint2D point) {
    return this.getManhattanDistance(point.getX(), point.getY());
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getScaledX()
   */
  public final double getScaledX() {
    return this.m_scaledX;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getScaledY()
   */
  public final double getScaledY() {
    return this.m_scaledY;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getX()
   */
  @Override
  public double getX() {
    return this.m_x;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#getY()
   */
  @Override
  public double getY() {
    return this.m_y;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#setListener(info.monitorenter.gui.chart.ITrace2D)
   */
  public void setListener(final ITrace2D listener) {
    this.m_listener = listener;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#setLocation(double, double)
   */
  @Override
  public void setLocation(final double xValue, final double yValue) {

    this.m_x = xValue;
    this.m_y = yValue;
    if (this.m_listener != null) {
      this.m_listener.firePointChanged(this, ITracePoint2D.STATE_CHANGED);
    }
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#setScaledX(double)
   */
  public final void setScaledX(final double scaledX) {
    this.m_scaledX = scaledX;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#setScaledY(double)
   */
  public final void setScaledY(final double scaledY) {
    this.m_scaledY = scaledY;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePoint2D#toString()
   */
  @Override
  public String toString() {
    return "TracePoint2D[" + this.m_x + ", " + this.m_y + "]";
  }
}
