/*
 *  Trace2DAxisSwap.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 09:06:33
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

/**
 * <p>
 * A delegator / proxy that delegates all calls to an internal constructor-given
 * ITrace2d and swaps the data of the added Point2D instances.
 * </p>
 * <p>
 * x values become y values and vice versa. Performance is bad, as unnecessary
 * instances are created (each TracePoint2D is instantiated twice) so this
 * instance is for debugging / testing purposes only.
 *
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.11 $
 */
public class Trace2DAxisSwap implements ITrace2D {
  /** The delagate instance to decorate with axis swapping. */
  private ITrace2D m_delegate;

  /**
   * Creates an instance that will swap the axis of the given delegate.
   * <p>
   *
   * @param trace
   *          the delagate instance to decorate with axis swapping.
   */
  public Trace2DAxisSwap(final ITrace2D trace) {
    if (trace == null) {
      throw new IllegalArgumentException("Argument must not be null.");
    }
    if (trace.getClass() == this.getClass()) {
      throw new IllegalArgumentException("Nesting of " + this.getClass().getName()
          + " is unnecessary and may be harmful.");
    }
    this.m_delegate = trace;
  }

  /**
   * @see aw.gui.chart.ITrace2D#addPoint(double, double)
   */
  public boolean addPoint(final double x, final double y) {
    return this.addPoint(new TracePoint2D(x, y));
  }

  /**
   * @see aw.gui.chart.ITrace2D#addPoint(aw.gui.chart.TracePoint2D)
   */
  public boolean addPoint(final TracePoint2D p) {
    return this.m_delegate.addPoint(new TracePoint2D(p.getY(), p.getX()));
  }

  /**
   * @see aw.gui.chart.ITrace2D#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
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
   * @see aw.gui.chart.ITrace2D#getColor()
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
