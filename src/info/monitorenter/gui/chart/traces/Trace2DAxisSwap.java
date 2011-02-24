/*
 *  Trace2DAxisSwap.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 09:06:33
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
package info.monitorenter.gui.chart.traces;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePainter;
import info.monitorenter.gui.chart.TracePoint2D;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Set;

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
 * @version $Revision: 1.3 $
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
   * @see info.monitorenter.gui.chart.ITrace2D#addPoint(double, double)
   */
  public boolean addPoint(final double x, final double y) {
    return this.addPoint(new TracePoint2D(x, y));
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#addPoint(info.monitorenter.gui.chart.TracePoint2D)
   */
  public boolean addPoint(final TracePoint2D p) {
    return this.m_delegate.addPoint(new TracePoint2D(p.getY(), p.getX()));
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_delegate.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#addTracePainter(info.monitorenter.gui.chart.ITracePainter)
   */
  public boolean addTracePainter(final ITracePainter painter) {
    return this.m_delegate.addTracePainter(painter);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#containsTracePainter(info.monitorenter.gui.chart.ITracePainter)
   */
  public boolean containsTracePainter(final ITracePainter painter) {
    return this.m_delegate.containsTracePainter(painter);
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(final Object obj) {
    return this.m_delegate.equals(obj);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getColor()
   */
  public Color getColor() {
    return this.m_delegate.getColor();
  }

  /**
   * @see info.monitorenter.util.collections.IComparableProperty#getComparableProperty()
   */
  public Number getComparableProperty() {
    return this.m_delegate.getComparableProperty();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getLable()
   */
  public String getLable() {
    return this.m_delegate.getLable();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getMaxSize()
   */
  public int getMaxSize() {
    return this.m_delegate.getMaxSize();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getMaxX()
   */
  public double getMaxX() {
    return this.m_delegate.getMaxX();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getMaxY()
   */
  public double getMaxY() {
    return this.m_delegate.getMaxY();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getMinX()
   */
  public double getMinX() {
    return this.m_delegate.getMinX();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getMinY()
   */
  public double getMinY() {
    return this.m_delegate.getMinY();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getName()
   */
  public String getName() {
    return this.m_delegate.getName();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getPhysicalUnits()
   */
  public String getPhysicalUnits() {
    return this.m_delegate.getPhysicalUnits();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String property) {
    return this.m_delegate.getPropertyChangeListeners(property);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getRenderer()
   */
  public Chart2D getRenderer() {
    return this.m_delegate.getRenderer();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getSize()
   */
  public int getSize() {
    return this.m_delegate.getSize();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getStroke()
   */
  public Stroke getStroke() {
    return this.m_delegate.getStroke();
  }

  /**
   * 
   * @see info.monitorenter.gui.chart.ITrace2D#getTracePainters()
   */
  public Set getTracePainters() {
    return this.m_delegate.getTracePainters();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#isVisible()
   */
  public boolean isVisible() {
    return this.m_delegate.isVisible();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getZIndex()
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
   * @see info.monitorenter.gui.chart.ITrace2D#isEmpty()
   */
  public boolean isEmpty() {
    return this.m_delegate.isEmpty();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#iterator()
   */
  public Iterator iterator() {
    return this.m_delegate.iterator();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#removeAllPoints()
   */
  public void removeAllPoints() {
    this.m_delegate.removeAllPoints();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#removePoint(info.monitorenter.gui.chart.TracePoint2D)
   */
  public boolean removePoint(final TracePoint2D point) {
    return this.m_delegate.removePoint(point);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.m_delegate.removePropertyChangeListener(listener);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String property,
      final PropertyChangeListener listener) {
    this.m_delegate.removePropertyChangeListener(property, listener);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#removeTracePainter(info.monitorenter.gui.chart.ITracePainter)
   */
  public boolean removeTracePainter(final ITracePainter painter) {
    return this.m_delegate.removeTracePainter(painter);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setColor(java.awt.Color)
   */
  public void setColor(final Color color) {
    this.m_delegate.setColor(color);
  }

  /**
   * @see info.monitorenter.util.collections.IComparableProperty#setComparableProperty(java.lang.Number)
   */
  public void setComparableProperty(final Number n) {
    this.m_delegate.setComparableProperty(n);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setName(java.lang.String)
   */
  public void setName(final String name) {
    this.m_delegate.setName(name);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setPhysicalUnits(java.lang.String,
   *      java.lang.String)
   */
  public void setPhysicalUnits(final String xunit, final String yunit) {
    this.m_delegate.setPhysicalUnits(xunit, yunit);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setRenderer(info.monitorenter.gui.chart.Chart2D)
   */
  public void setRenderer(final Chart2D renderer) {
    this.m_delegate.setRenderer(renderer);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setStroke(java.awt.Stroke)
   */
  public void setStroke(final Stroke stroke) {
    this.m_delegate.setStroke(stroke);
  }

  /**
   * 
   * @see info.monitorenter.gui.chart.ITrace2D#setTracePainter(info.monitorenter.gui.chart.ITracePainter)
   */
  public Set setTracePainter(final ITracePainter painter) {
    return this.m_delegate.setTracePainter(painter);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setVisible(boolean)
   */
  public void setVisible(final boolean visible) {
    this.m_delegate.setVisible(visible);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#setZIndex(java.lang.Integer)
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
