/*
 *  Trace2DDebugger.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 04.03.2005, 12:33:48
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import aw.util.Range;

/**
 * <p>
 * A decorator for any ITrace2D implementation. Useful if your chart looks
 * unexpected and the problem may be related to the data that is added. It
 * prints every point added to the console.
 * </p>
 * <p>
 * <p>
 * Use it by decorating the ITrace2D you normally use:
 *
 * <pre>
 *
 *
 *   // Create a chart:
 *   Chart2D chart = new Chart2D();
 *   // Create an ITrace:
 *   &lt;b&gt;ITrace2D trace = new Trace2DDebugger(new Trace2DSimple());
 *   // add data...
 *   ...
 *   //
 *   chart.addTrace(trace);
 *
 *
 * </pre>
 *
 * </p>
 * <p>
 * One can use {@link #setXRange(Range)},{@link #setYRange(Range)}to let this
 * instance throw an Exception if bounds for legal data are exceeded.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.11 $
 */
public class Trace2DDebugger implements ITrace2D {

  /**
   * The instance to debug.
   */
  private ITrace2D m_delegate;

  /**
   * The valid range for x values. If a point breaks these bounds an
   * {@link IllegalArgumentException}will be thrown.
   */
  private Range m_xRange = new Range(-Double.MAX_VALUE, +Double.MAX_VALUE);

  /**
   * The valid range for y values. If a point breaks these bounds an
   * {@link IllegalArgumentException}will be thrown.
   */
  private Range m_yRange = new Range(-Double.MAX_VALUE, +Double.MAX_VALUE);

  /**
   * @param debug
   *          The ITrace to debug.
   */

  /**
   * Creates an inistance to debug the given trace for valid points added.
   * <p>
   *
   * @param debug
   *          the trace to debug.
   */
  public Trace2DDebugger(final ITrace2D debug) {
    if (debug == null) {
      throw new IllegalArgumentException("Argument must not be null.");
    }
    this.m_delegate = debug;
  }

  // /////////////////////////////////
  // Proxy methods

  /**
   * @see ITrace2D#addPoint(double, double)
   */
  public boolean addPoint(final double x, final double y) {
    TracePoint2D p = new TracePoint2D(x, y);
    return this.addPoint(p);
  }

  /**
   * @see ITrace2D#addPoint(TracePoint2D)
   */
  public boolean addPoint(final TracePoint2D p) {
    double x = p.getX();
    double y = p.getY();
    if (!this.m_xRange.isContained(x)) {
      throw new IllegalArgumentException(p.toString() + " is not within the valid x-range "
          + this.m_xRange.toString());
    }
    if (!this.m_yRange.isContained(y)) {
      throw new IllegalArgumentException(p.toString() + " is not within the valid x-range "
          + this.m_xRange.toString());
    }
    System.out.println(p);
    return this.m_delegate.addPoint(p);
  }

  /**
   * @see ITrace2D#addPropertyChangeListener(String, PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_delegate.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(final Object obj) {
    return this.m_delegate.equals(obj);
  }

  /**
   * @see ITrace2D#getColor()
   */
  public Color getColor() {
    return this.m_delegate.getColor();
  }

  /**
   * @see aw.util.collections.IComparableProperty#getComparableProperty()
   */
  public Number getComparableProperty() {
    return this.m_delegate.getComparableProperty();
  }

  /**
   * @see ITrace2D#getVisible()
   */
  public boolean getisible() {
    return this.m_delegate.getVisible();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getLable()
   */
  public String getLable() {
    return this.m_delegate.getLable();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMaxSize()
   */
  public int getMaxSize() {
    return this.m_delegate.getMaxSize();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMaxX()
   */
  public double getMaxX() {
    return this.m_delegate.getMaxX();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMaxY()
   */
  public double getMaxY() {
    return this.m_delegate.getMaxY();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMinX()
   */
  public double getMinX() {
    return this.m_delegate.getMinX();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMinY()
   */
  public double getMinY() {
    return this.m_delegate.getMinY();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getName()
   */
  public String getName() {
    return this.m_delegate.getName();
  }

  /**
   *
   * @see aw.gui.chart.ITrace2D#getPhysicalUnits()
   */
  public String getPhysicalUnits() {
    return this.m_delegate.getPhysicalUnits();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String property) {
    return this.m_delegate.getPropertyChangeListeners(property);
  }

  /**
   * @see aw.gui.chart.ITrace2D#getRenderer()
   */
  public Chart2D getRenderer() {
    return this.m_delegate.getRenderer();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getSize()
   */
  public int getSize() {
    return this.m_delegate.getSize();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getStroke()
   */
  public Stroke getStroke() {
    return this.m_delegate.getStroke();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getTracePainter()
   */
  public ITracePainter getTracePainter() {
    return this.m_delegate.getTracePainter();
  }

  /**
   * @see aw.gui.chart.ITrace2D#getVisible()
   */
  public boolean getVisible() {
    return this.m_delegate.getVisible();
  }

  /**
   * Returns the range of valid points of the x axis.
   * <p>
   *
   * @return the range of valid points of the x axis.
   */
  public Range getXRange() {
    return this.m_xRange;
  }

  /**
   * Returns the range of valid points of the y axis.
   * <p>
   *
   * @return the range of valid points of the y axis.
   */
  public Range getYRange() {
    return this.m_yRange;
  }

  /**
   * @see aw.gui.chart.ITrace2D#getZIndex()
   */
  public Integer getZIndex() {
    return this.m_delegate.getZIndex();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.m_delegate.hashCode();
  }

  /**
   * @see aw.gui.chart.ITrace2D#isEmpty()
   */
  public boolean isEmpty() {
    return this.m_delegate.isEmpty();
  }

  /**
   * @see aw.gui.chart.ITrace2D#iterator()
   */
  public Iterator iterator() {
    return this.m_delegate.iterator();
  }

  /**
   * @see aw.gui.chart.ITrace2D#removeAllPoints()
   */
  public void removeAllPoints() {
    this.m_delegate.removeAllPoints();
  }

  /**
   * @see aw.gui.chart.ITrace2D#removePoint(aw.gui.chart.TracePoint2D)
   */
  public boolean removePoint(final TracePoint2D point) {
    return this.m_delegate.removePoint(point);
  }

  /**
   * @see aw.gui.chart.ITrace2D#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.m_delegate.removePropertyChangeListener(listener);
  }

  /**
   * @see aw.gui.chart.ITrace2D#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String property,
      final PropertyChangeListener listener) {
    this.m_delegate.removePropertyChangeListener(property, listener);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setColor(java.awt.Color)
   */
  public void setColor(final Color color) {
    this.m_delegate.setColor(color);
  }

  /**
   * @see aw.util.collections.IComparableProperty#setComparableProperty(java.lang.Number)
   */
  public void setComparableProperty(final Number n) {
    this.m_delegate.setComparableProperty(n);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setName(java.lang.String)
   */
  public void setName(final String name) {
    this.m_delegate.setName(name);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setPhysicalUnits(java.lang.String,
   *      java.lang.String)
   */
  public void setPhysicalUnits(final String xunit, final String yunit) {
    this.m_delegate.setPhysicalUnits(xunit, yunit);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setRenderer(aw.gui.chart.Chart2D)
   */
  public void setRenderer(final Chart2D renderer) {
    this.m_delegate.setRenderer(renderer);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setStroke(java.awt.Stroke)
   */
  public void setStroke(final Stroke stroke) {
    this.m_delegate.setStroke(stroke);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setTracePainter(aw.gui.chart.ITracePainter)
   */
  public void setTracePainter(final ITracePainter painter) {
    this.m_delegate.setTracePainter(painter);
  }

  /**
   * @see aw.gui.chart.ITrace2D#setVisible(boolean)
   */
  public void setVisible(final boolean visible) {
    this.m_delegate.setVisible(visible);
  }

  /**
   * Set the valid range for x values. If a point breaks these bounds an
   * {@link IllegalArgumentException}will be thrown.
   * <p>
   *
   * @param range
   *          The xRange to set.
   */
  public void setXRange(final Range range) {
    if (range == null) {
      throw new IllegalArgumentException("Argument must not be null.");
    }
    this.m_xRange = range;
  }

  /**
   * Set the valid range for y values. If a point breaks these bounds an
   * {@link IllegalArgumentException}will be thrown.
   * <p>
   *
   * @param range
   *          The yRange to set.
   */
  public void setYRange(final Range range) {
    if (range == null) {
      throw new IllegalArgumentException("Argument must not be null.");
    }
    this.m_yRange = range;
  }

  /**
   * @see aw.gui.chart.ITrace2D#setZIndex(java.lang.Integer)
   */
  public void setZIndex(final Integer zIndex) {
    this.m_delegate.setZIndex(zIndex);
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return this.m_delegate.toString();
  }
}
