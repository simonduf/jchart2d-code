/*
 *
 *  AbstractTrace2D.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 22.05.2005, 19:28:36
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
package aw.gui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.SwingPropertyChangeSupport;

import aw.util.StringUtil;

/**
 * <p>
 * The abstract basic implementation of
 * <code>{@link aw.gui.chart.ITrace2D}</code> that provides the major amount
 * of aspects needed in order to work correctly together with
 * <code>{@link aw.gui.chart.Chart2D}</code>.
 * </p>
 * <p>
 * Caching of minimum and maximum bounds, property change support, the
 * complexity of z-Index handling (incorporates calls to internals of
 * <code>Chart2D</code>, default naming, bound management and event handling
 * are covered here.
 * </p>
 * <h3>Property Change events</h3>
 * <p>
 * <table border="0">
 * <tr>
 * <th><code>property</code></th>
 * <th><code>oldValue</code></th>
 * <th><code>newValue</code></th>
 * <th>occurance</th>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_ZINDEX}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Number}</code>, the old value</td>
 * <td><code>{@link java.lang.Number}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_MAX_X}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_MIN_X}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_MAX_Y}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_MIN_Y}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_TRACEPOINT}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link aw.gui.chart.TracePoint2D}</code>, the instance that was
 * removed</td>
 * <td><code>null</code>, indication that an instance was removed</td>
 * </tr>
 * <tr>
 * <td><code>{@link aw.gui.chart.ITrace2D#PROPERTY_TRACEPOINT}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>null</code>, indication that a value was added</td>
 * <td><code>{@link aw.gui.chart.TracePoint2D}</code>, the new instance that
 * was added, identifying that an instance was removed</td>
 * </tr>
 *
 * </table>
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.15 $
 */
public abstract class AbstractTrace2D implements ITrace2D {

  /**
   * Instance counter for read-access in subclasses.
   */
  private static int instanceCount = 0;

  /**
   * Returns the instanceCount for all <code>AbstractTrace2D</code>
   * subclasses.
   *
   * @return Returns the instanceCount for all <code>AbstractTrace2D</code>
   *         subclasses.
   */
  public static int getInstanceCount() {
    return AbstractTrace2D.instanceCount;
  }

  /**
   * {@link Trace2DListener} instances (mainly <code>Char2D</code> instances
   * that are interested in changes of internal <code>ITracePoint2D</code>
   * instances.
   */
  private List m_changeListeners = new LinkedList();

  /**
   * The color property.
   */
  private Color m_color = Color.black;

  /**
   * Needed for special treatment of cached values in case of empty state (no
   * points).
   */
  private boolean m_firsttime = true;

  /**
   * Cached maximum x value for performance improvement.
   */
  protected double m_maxX;

  /**
   * Cached maximum y value for performance improvement.
   */
  protected double m_maxY;

  /**
   * Cached minimum x value for performance improvement.
   */
  protected double m_minX;

  /**
   * Cached minimum y value for performance improvement.
   */
  protected double m_minY;

  /**
   * The name property.
   */
  protected String m_name = "";

  /**
   * The physical unit property.
   */
  protected String m_physicalUnits = "";

  /**
   * The instance that add support for firing <code>PropertyChangeEvents</code>
   * and maintaining <code>PropertyChangeListeners</code>.
   *
   * {@link PropertyChangeListener} instances.
   */

  protected PropertyChangeSupport m_propertyChangeSupport = new SwingPropertyChangeSupport(this);

  /**
   * The <code>Chart2D</code> this trace is added to. Needed for
   * synchronization.
   */
  protected Object m_renderer = new Object();

  /**
   * The stroke property.
   */
  private Stroke m_stroke = new BasicStroke(1f);

  /**
   * The visible property.
   */
  private boolean m_visible = true;

  /**
   * The zIndex property.
   */
  private Integer m_zIndex = new Integer(ITrace2D.ZINDEX_MAX);

  /**
   * Default contstructor.
   * <p>
   *
   */
  public AbstractTrace2D() {
    super();
    AbstractTrace2D.instanceCount++;
    this.m_tracePainters = new TreeSet();
    this.m_tracePainters.add(new TracePainterPolyline());
  }

  /**
   * <p>
   * Add a point to this trace.
   * </p>
   * <p>
   * Prefer calling <code>{@link #addPoint(TracePoint2D)}</code> for better
   * performance (avoid one invokevirtual for delegation).
   * </p>
   *
   * @param x
   *          the x coordinate of the new point.
   * @param y
   *          the y coordinate of the new point.
   *
   * @return true if the operation was successful, false else.
   */
  public final boolean addPoint(final double x, final double y) {
    TracePoint2D p = new TracePoint2D(x, y);
    return this.addPoint(p);
  }

  /**
   * <p>
   * Add the given point to this <code>ITrace2D</code>.
   * </p>
   * <p>
   * This implementation performs caching of minimum and maximum values for x
   * and y and the delegates to {@link #addPointInternal(TracePoint2D)} that has
   * to perform the "real" add operation.
   * </p>
   * <p>
   * Property change events are fired as described in method
   * <code>{@link #firePointAdded(TracePoint2D)}</code>.
   * </p>
   *
   * @see #firePointChanged(TracePoint2D, boolean)
   * @param p
   *          the <code>TracePoint2D</code> to add.
   * @return true if the operation was successful, false else.
   *
   */
  public final boolean addPoint(final TracePoint2D p) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("addPoint, 0 locks");
    }
    synchronized (this.m_renderer) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("addPoint, 1 lock");
      }
      synchronized (this) {
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("addPoint, 2 locks");
        }
        boolean accepted = this.addPointInternal(p);
        if (this.m_firsttime) {
          this.m_maxX = p.getX();
          this.m_minX = this.m_maxX;
          this.m_maxY = p.getY();
          this.m_minY = this.m_maxY;
          this.firePropertyChange(PROPERTY_MAX_X, new Double(0), new Double(this.m_maxX));
          this.firePropertyChange(PROPERTY_MIN_X, new Double(0), new Double(this.m_minX));
          this.firePropertyChange(PROPERTY_MAX_Y, new Double(0), new Double(this.m_maxY));
          this.firePropertyChange(PROPERTY_MIN_Y, new Double(0), new Double(this.m_minY));

          this.m_firsttime = false;
        }
        if (accepted) {
          this.firePointAdded(p);
          p.setListener(this);
        }
        // else{
        // System.err.println("Not accepted!");
        // }
        return accepted;
      }
    }
  }

  /**
   * <p>
   * Override this template method for the custom add operation that depends on
   * the policies of the implementation.
   * </p>
   * <p>
   * No property change events have to be fired by default. If this method
   * returns <code>true</code> the outer logic of the calling method
   * <code>{@link #addPoint(TracePoint2D)}</code> will perform bound checks
   * for the new point and fire property changes as described in method
   * <code>{@link #firePointChanged(TracePoint2D, boolean)}</code>.
   * </p>
   * <p>
   * In special cases - when additional modifications to the internal set of
   * points take place (e.g. a further point gets removed) this method should
   * return false (regardless wether the new point was accepted or not) and
   * perform bound checks and fire the property changes as mentioned above
   * "manually".
   * </p>
   *
   * @param p
   *          the point to add.
   * @return true if the given point was accepted or false if not.
   *
   */
  protected abstract boolean addPointInternal(TracePoint2D p);

  /**
   * @see aw.gui.chart.ITrace2D#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public final void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * @see aw.gui.chart.ITrace2D#addTracePainter(aw.gui.chart.ITracePainter)
   */
  public boolean addTracePainter(final ITracePainter painter) {
    return this.m_tracePainters.add(painter);
  }

  /**
   * Decreases internal instance count by one.
   * <p>
   *
   * @throws Throwable
   *           if sth. goes wrong.
   */
  public void finalize() throws Throwable {
    super.finalize();
    AbstractTrace2D.instanceCount--;
  }

  /**
   * <p>
   * Fire property change events related to an added point.
   * </p>
   * <p>
   * A property change event for property
   * <code>{@link ITrace2D#PROPERTY_TRACEPOINT}</code> with null as the old
   * value and the new point as the new value is fired. This allows e.g.
   * rescaling of those instances (instead of having to rescale a whole trace).
   * </p>
   * <p>
   * Additionally before this property change, property change events for bounds
   * are fired as described in method
   * <code>{@link #firePointChanged(TracePoint2D, boolean)}</code>.
   * </p>
   *
   * @param added
   *          the point that was added.
   */
  protected void firePointAdded(final TracePoint2D added) {
    this.firePointChanged(added, true);
    this.firePropertyChange(PROPERTY_TRACEPOINT, null, added);
  }

  /**
   * <p>
   * Method triggered by
   * <code>{@link TracePoint2D#setLocation(double, double)}</code>,
   * <code>{@link #addPoint(TracePoint2D)}</code> or
   * <code>{@link #removePoint(TracePoint2D)}</code>.
   * </p>
   * <p>
   * Bound checks are performed and property change events for the properties
   * <code>{@link ITrace2D#PROPERTY_MAX_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MIN_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code> and
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> are fired if the add bounds
   * have changed due to the modification of the point.
   * </p>
   *
   * @param changed
   *          the point that has been changed which may be a newly added point
   *          (from <code>{@link #addPoint(TracePoint2D)}</code>, a removed
   *          one or a modified one.
   * @param added
   *          if true the points values dominate old bounds, if false the bounds
   *          are rechecked against the removed points values.
   */
  protected void firePointChanged(final TracePoint2D changed, final boolean added) {
    double tmpx = changed.getX();
    double tmpy = changed.getY();
    if (added) {
      if (tmpx > this.m_maxX) {
        this.m_maxX = tmpx;
        this.firePropertyChange(PROPERTY_MAX_X, null, new Double(this.m_maxX));
      } else if (tmpx < this.m_minX) {
        this.m_minX = tmpx;
        this.firePropertyChange(PROPERTY_MIN_X, null, new Double(this.m_minX));
      }
      if (tmpy > this.m_maxY) {
        this.m_maxY = tmpy;
        this.firePropertyChange(PROPERTY_MAX_Y, null, new Double(this.m_maxY));
      } else if (tmpy < this.m_minY) {
        this.m_minY = tmpy;
        this.firePropertyChange(PROPERTY_MIN_Y, null, new Double(this.m_minY));
      }
    } else {
      if (tmpx > this.m_maxX) {
        tmpx = this.m_maxX;
        this.maxXSearch();
        this.firePropertyChange(PROPERTY_MAX_X, new Double(tmpx), new Double(this.m_maxX));

      } else if (tmpx < this.m_minX) {
        tmpx = this.m_minX;
        this.minXSearch();
        this.firePropertyChange(PROPERTY_MIN_X, new Double(tmpx), new Double(this.m_minX));
      }
      if (tmpy > this.m_maxY) {
        tmpy = this.m_maxY;
        this.maxYSearch();
        this.firePropertyChange(PROPERTY_MAX_Y, new Double(tmpy), new Double(this.m_maxY));
      } else if (tmpy < this.m_minY) {
        tmpy = this.m_minY;
        this.minYSearch();
        this.firePropertyChange(PROPERTY_MIN_Y, new Double(tmpy), new Double(this.m_minY));
      }
      if (this.getSize() == 0) {
        this.m_firsttime = true;
      }
    }
  }

  /**
   * <p>
   * Fire property change events related to a removed point.
   * </p>
   * <p>
   * A property change event for property
   * <code>{@link ITrace2D#PROPERTY_TRACEPOINT}</code> with a point as the old
   * value and null as the new value is fired. This allows e.g. rescaling of
   * those instances (instead of having to rescale a whole trace).
   * </p>
   * <p>
   * Additionally before this property change, property change events for bounds
   * are fired as described in method
   * <code>{@link #firePointChanged(TracePoint2D, boolean)}</code>.
   * </p>
   *
   * @param removed
   *          the point that was removed.
   */
  protected void firePointRemoved(final TracePoint2D removed) {
    this.firePointChanged(removed, false);
    this.firePropertyChange(PROPERTY_TRACEPOINT, removed, null);
  }

  /**
   * <p>
   * Fires a property change event to the registered listeners.
   * </p>
   *
   * @param property
   *          one of the <code>PROPERTY_XXX</code> constants defined in
   *          <code>{@link ITrace2D}</code>.
   * @param oldvalue
   *          the old value of the property.
   * @param newvalue
   *          the new value of the property.
   *
   */
  protected final void firePropertyChange(final String property, final Object oldvalue,
      final Object newvalue) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.firePropertyChange (" + property + "), 0 locks");
    }
    if (property.equals(PROPERTY_MAX_X) || property.equals(PROPERTY_MAX_Y)
        || property.equals(PROPERTY_MIN_X) || property.equals(PROPERTY_MIN_Y)
        || property.equals(PROPERTY_TRACEPOINT)) {
      if (!Thread.holdsLock(this.m_renderer)) {
        throw new RuntimeException("You dirty threading hacker!");
      }
    }

    this.m_propertyChangeSupport.firePropertyChange(property, oldvalue, newvalue);
  }

  /**
   * Returns the change listeners of this instance.
   * <p>
   *
   * @return the change listeners of this instance.
   */
  public List getChangeListeners() {
    return new LinkedList(this.m_changeListeners);
  }

  /**
   * <p>
   * Get the <code>Color</code> this trace will be painted with.
   * </p>
   *
   * @return the <code>Color</code> of this instance
   */
  public final Color getColor() {
    return this.m_color;
  }

  /**
   * @see aw.util.collections.IComparableProperty#getComparableProperty()
   */
  public Number getComparableProperty() {
    return this.m_zIndex;
  }

  /**
   * Returns a label for this trace.
   * <p>
   *
   * The label is constructed of
   * <ul>
   * <li>The name of this trace ({@link #getName()}).</li>
   * <li>The phyical unit of this trace ({@link #getPhysicalUnits()()}).
   * </li>
   * </ul>
   * <p>
   *
   * @return a label for this trace.
   *
   * @see ITrace2D#getLable()
   *
   * @see #getName()
   *
   * @see #getPhysicalUnits()
   */
  public final String getLable() {
    String name = this.getName();
    String physunit = this.getPhysicalUnits();
    if ((name != "") && (physunit != "")) {
      name = new StringBuffer(name).append(" ").append(physunit).toString();
    } else if (physunit != "") {
      name = new StringBuffer("unnamed").append(" ").append(physunit).toString();
    }
    return name.toString();
  }

  /**
   * Returns the original maximum x- value ignoring the offsetX.
   * <p>
   *
   * @return the original maximum x- value ignoring the offsetX.
   */
  public final double getMaxX() {
    return this.m_maxX;
  }

  /**
   * Returns the original maximum y- value ignoring the offsetY.
   * <p>
   *
   * @return the original maximum y- value ignoring the offsetY.
   */

  public final double getMaxY() {
    return this.m_maxY;
  }

  /**
   * Returns the original minimum x- value ignoring the offsetX.
   * <p>
   *
   * @return the original minimum x- value ignoring the offsetX.
   */
  public final double getMinX() {
    return this.m_minX;
  }

  /**
   * Returns the original minimum y- value ignoring the offsetY.
   * <p>
   *
   * @return the original minimum y- value ignoring the offsetY.
   */
  public final double getMinY() {
    return this.m_minY;
  }

  /**
   * Returns the name of this trace.
   * <p>
   *
   * @return the name of this trace.
   *
   * @see ITrace2D#getName()
   *
   * @see #setName(String s)
   */
  public final String getName() {
    return this.m_name;
  }

  /**
   * @see #setPhysicalUnits(String x,String y)
   */
  public final String getPhysicalUnits() {
    return this.m_physicalUnits;
  }

  /**
   * @see aw.gui.chart.ITrace2D#getPropertyChangeListeners(String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String property) {
    return this.m_propertyChangeSupport.getPropertyChangeListeners(property);
  }

  /**
   * Returns the chart that renders this instance or null, if this trace is not
   * added to a chart.
   * <p>
   *
   * The chart that renders this trace registers itself with this trace in
   * {@link Chart2D#addTrace(ITrace2D)}.
   * <p>
   *
   * @return Returns the renderer.
   *
   * @see Chart2D#addTrace(ITrace2D)
   */
  public final Chart2D getRenderer() {
    if (this.m_renderer instanceof Chart2D) {
      return (Chart2D) this.m_renderer;
    } else {
      return null;
    }
  }

  /**
   * <p>
   * Get the <code>Stroke</code> object this instance will be painted with.
   * </p>
   *
   * @return the <code>Stroke</code> object this <code>ITrace2D</code> will
   *         be painted with.
   *
   * @see aw.gui.chart.ITrace2D#getStroke()
   */
  public final Stroke getStroke() {
    return this.m_stroke;
  }

  /**
   * @see aw.gui.chart.ITrace2D#getVisible()
   */
  public final boolean getVisible() {
    return this.m_visible;
  }

  /**
   * @see aw.gui.chart.ITrace2D#getZIndex()
   */
  public final Integer getZIndex() {
    // More expensive but the contract of the order of values described in the
    // interfaceis inverted to the contract of TreeSetGreedy.
    // This is done here instead of get/set ComparableProperty
    // as those are invoked several times for each iteration
    // (and paint contains several iterations).
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.getZindex, 0 locks");
    }

    synchronized (this.m_renderer) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.getZindex, 1 locks");
      }

      synchronized (this) {
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("trace.getZindex, 2 locks");
        }

        return new Integer(ZINDEX_MAX - this.m_zIndex.intValue());
      }
    }
  }

  /**
   * Internal search for the maximum x value that is only invoked if no cached
   * value is at hand or bounds have changed by adding new points.
   * <p>
   *
   * The result is assigned to the property maxX.
   * <p>
   *
   * @see #getMaxX()
   */
  protected void maxXSearch() {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.maxXSearch, 0 locks");
    }

    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.maxXSearch, 1 locks");
      }
      double ret = -Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) > ret) {
          ret = tmp;
        }
      }
      this.m_maxX = ret;
    }
  }

  /**
   * Internal search for the maximum y value that is only invoked if no cached
   * value is at hand or bounds have changed by adding new points.
   * <p>
   *
   * The result is assigned to the property maxY.
   * <p>
   *
   * @see #getMaxY()
   */
  protected void maxYSearch() {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.maxYSearch, 0 locks");
    }

    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.maxYSearch, 1 lock");
      }

      double ret = -Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) > ret) {
          ret = tmp;
        }
      }
      this.m_maxY = ret;
    }
  }

  /**
   * Internal search for the minimum x value that is only invoked if no cached
   * value is at hand or bounds have changed by adding new points.
   * <p>
   *
   * The result is assigned to the property minX.
   * <p>
   *
   * @see #getMinX()
   */
  protected void minXSearch() {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.minXSearch, 0 locks");
    }

    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.minXSearch, 1 locks");
      }

      double ret = Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) < ret) {
          ret = tmp;
        }
      }
      this.m_minX = ret;
    }
  }

  /**
   * Internal search for the minimum y value that is only invoked if no cached
   * value is at hand or bounds have changed by adding new points.
   * <p>
   *
   * The result is assigned to the property minY.
   * <p>
   *
   * @see #getMinY()
   */
  protected void minYSearch() {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.minYSearch, 0 locks");
    }

    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.minYSearch, 1 locks");
      }

      double ret = Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) < ret) {
          ret = tmp;
        }
      }
      this.m_minY = ret;
    }

  }

  /**
   * Changes the internal state to empty to allow that the caching of bounds is
   * cleared and delegates the call to {@link  #removeAllPointsInternal()}.
   * <p>
   *
   * @see aw.gui.chart.ITrace2D#removeAllPoints()
   */
  public final void removeAllPoints() {
    synchronized (this.m_renderer) {
      synchronized (this) {

        this.m_firsttime = true;
        this.removeAllPointsInternal();
        this.m_minX = 0;
        this.m_maxX = 0;
        this.m_maxY = 0;
        this.m_minY = 0;
        this.firePropertyChange(PROPERTY_MAX_X, null, new Double(this.m_maxX));
        this.firePropertyChange(PROPERTY_MIN_X, null, new Double(this.m_minX));
        this.firePropertyChange(PROPERTY_MAX_Y, null, new Double(this.m_maxY));
        this.firePropertyChange(PROPERTY_MIN_Y, null, new Double(this.m_minY));
      }
    }
  }

  /**
   * <p>
   * Override this template method for the custom remove operation that depends
   * on the <code>Collection</code> used in the implementation.
   * </p>
   * <p>
   * No change events have to be fired, this is done by {@link AbstractTrace2D}.
   * </p>
   *
   */
  protected abstract void removeAllPointsInternal();

  /**
   * <p>
   * Remove the given point from this <code>ITrace2D</code>.
   * </p>
   * <p>
   * This implementation performs caching of minimum and maximum values for x
   * and y and the delegates to
   * <code>{@link #removePointInternal(TracePoint2D)}</code> that has to
   * perform the "real" add remove operation.
   * </p>
   * <p>
   * Property change events are fired as described in method
   * <code>{@link #firePointRemoved(TracePoint2D)}</code>.
   * </p>
   *
   * @param point
   *          the <code>TracePoint2D</code> to remove.
   *
   * @return true if the removal suceeded, false else: this could be that the
   *         given point was not contained.
   *
   * @see #firePointChanged(TracePoint2D, boolean)
   */
  public boolean removePoint(final TracePoint2D point) {
    synchronized (this.m_renderer) {
      if (Chart2D.THREAD_DEBUG) {

        System.out.println("removePoint, 0 locks");
      }
      synchronized (this) {
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("removePoint, 1 lock");
        }
        boolean success = this.removePointInternal(point);
        if (success) {
          this.firePointRemoved(point);
        }
        return success;

      }
    }
  }

  /**
   * <p>
   * Override this template method for the custom remove operation that depends
   * on the internal storage the implementation.
   * </p>
   * <p>
   * No property change events have to be fired by default. If this method
   * returns <code>true</code> the outer logic of the calling method
   * <code>{@link #removePoint(TracePoint2D)}</code> will perform bound checks
   * for the new point and fire property changes for the properties
   * <code>{@link ITrace2D#PROPERTY_MAX_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MIN_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code> and
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code>.
   * </p>
   * <p>
   * In special cases - when additional modifications to the internal set of
   * points take place (e.g. a further point get added) this method should
   * return false (regardless wether the old point was really removed or not)
   * and perform bound checks and fire the property changes as mentioned above
   * "manually".
   * </p>
   *
   * @param point
   *          the point to remove.
   *
   * @return true if the given point was removed or false if not.
   */
  protected abstract boolean removePointInternal(final TracePoint2D point);

  /**
   * @see aw.gui.chart.ITrace2D#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * @see aw.gui.chart.ITrace2D#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String property,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(property, listener);
  }

  /**
   * Removes the given trace painter if an instance of the same class is
   * contained and more painters are remaining.
   * <p>
   *
   * @param painter
   *          the painter to remove.
   *
   * @return true if a painter of the same class as the given painter was
   *         removed.
   */
  public boolean removeTracePainter(final ITracePainter painter) {
    boolean result = false;
    if (this.m_tracePainters.size() > 1) {
      result = this.m_tracePainters.remove(painter);
    } else {
      // nop: if not contained operation will not be successful, if contained
      // not allowed.
    }
    return result;
  }

  /**
   * <p>
   * Set the <code>Color</code> this trace will be painted with.
   * </p>
   *
   * @param color
   *          the <code>Color</code> this trace will be painted with.
   */
  public final void setColor(final Color color) {
    Color oldValue = this.m_color;
    this.m_color = color;
    if (!this.m_color.equals(oldValue)) {
      this.firePropertyChange(PROPERTY_COLOR, oldValue, this.m_color);
    }
  }

  /**
   * @see aw.util.collections.IComparableProperty#setComparableProperty(java.lang.Number)
   */
  public void setComparableProperty(final Number n) {
    this.m_zIndex = ((Integer) n);
  }

  /**
   * Sets the descriptive name for this trace.
   * <p>
   *
   * If the given argument is null or consists of whitespaces only nothing will
   * be changed.
   * <p>
   *
   * @param name
   *          the descriptive name for this trace.
   *
   * @see aw.gui.chart.ITrace2D#setName(java.lang.String)
   */
  public final void setName(final String name) {
    if (!StringUtil.isEmpty(name)) {
      String oldValue = this.m_name;
      this.m_name = name;
      if (!this.m_name.equals(oldValue)) {
        this.firePropertyChange(PROPERTY_NAME, oldValue, this.m_name);
      }
    }
  }

  /**
   * @see ITrace2D#setPhysicalUnits(String, String)
   */
  public final void setPhysicalUnits(final String xunit, final String yunit) {
    String oldValue = this.m_physicalUnits;
    if ((xunit == null) && (yunit == null)) {
      return;
    }
    if ((xunit == null) && (yunit != null)) {
      this.m_physicalUnits = new StringBuffer("[x: , y: ").append(yunit).append("]").toString();
      return;
    }
    if ((xunit != null) && (yunit == null)) {
      this.m_physicalUnits = new StringBuffer("[x: ").append(xunit).append(", y: ]").toString();
      return;
    }
    this.m_physicalUnits = new StringBuffer("[x: ").append(xunit).append(", y: ").append(yunit)
        .append("]").toString();
    if (!this.m_physicalUnits.equals(oldValue)) {
      this.firePropertyChange(PROPERTY_PHYSICALUNITS, oldValue, this.m_physicalUnits);
    }
  }

  /**
   * <p>
   * Allows the chart this instance is painted by to register itself.
   * </p>
   * <p>
   * This is internally required for synchronization and re-ordering due to
   * z-Index changes.
   * </p>
   *
   * @param renderer
   *          the chart that paints this instance.
   */
  public final void setRenderer(final Chart2D renderer) {
    this.m_renderer = renderer;
  }

  /**
   * <p>
   * Set the <code>Stroke</code> object this instance will be painted with.
   * </p>
   *
   * @see aw.gui.chart.ITrace2D#setStroke(java.awt.Stroke)
   */
  public final void setStroke(final Stroke stroke) {
    if (stroke == null) {
      throw new IllegalArgumentException("Argument must not be null.");
    }
    Stroke oldValue = this.m_stroke;
    this.m_stroke = stroke;
    if (!this.m_stroke.equals(oldValue)) {
      this.firePropertyChange(PROPERTY_STROKE, oldValue, this.m_stroke);
    }
  }

  /** The internal lists of the painters to use. */
  private Set m_tracePainters = new TreeSet();

  /**
   * Returns true if the given painter is contained in this compound painter.
   * <p>
   *
   * @param painter
   *          the painter to check wether it is contained.
   *
   * @return true if the given painter is contained in this compound painter.
   */
  public boolean containsTracePainter(final ITracePainter painter) {
    return this.m_tracePainters.contains(painter);
  }

  /**
   * <p>
   * Set the visible property of this instance.
   * </p>
   * <p>
   * Invisible <code>ITrace2D</code> instances (visible == false) will not be
   * painted.
   * </p>
   *
   * @param visible
   *          the visible property of this instance to set.
   *
   * @see aw.gui.chart.ITrace2D#setVisible(boolean)
   */
  public final void setVisible(final boolean visible) {
    boolean oldValue = this.m_visible;
    this.m_visible = visible;
    if (oldValue != this.m_visible) {
      this.firePropertyChange(PROPERTY_VISIBLE, new Boolean(oldValue), new Boolean(this.m_visible));
    }
  }

  /**
   * Replaces all internal trace painters by the new one.
   * <p>
   *
   * @param painter
   *          the new sole painter to use.
   *
   * @return the <code>Set&lt;{@link ITracePainter}&gt;</code> that was used
   *         before.
   */
  public Set setTracePainter(final ITracePainter painter) {
    Set result = this.m_tracePainters;
    this.m_tracePainters = new TreeSet();
    this.m_tracePainters.add(painter);
    return result;
  }

  /**
   * @see aw.gui.chart.ITrace2D#setZIndex(java.lang.Integer)
   */
  public final void setZIndex(final Integer zIndex) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.setZIndex, 0 locks");
    }

    if (!zIndex.equals(this.m_zIndex)) {

      synchronized (this.m_renderer) {
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("trace.setZIndex, 1 lock");
        }

        Integer oldValue = this.m_zIndex;
        synchronized (this) {
          if (Chart2D.THREAD_DEBUG) {
            System.out.println("trace.setZIndex, 2 locks");
          }
          /*
           * see javadoc of TreeSetGreedy: we have to remove before outside
           * modification of the IComparableProperty, then modify and finally
           * add again.
           */
          boolean rendered = this.m_renderer instanceof Chart2D;
          if (rendered) {
            ((Chart2D) this.m_renderer).removeTrace(this);
          }
          this.m_zIndex = new Integer(ZINDEX_MAX - zIndex.intValue());
          if (rendered) {
            ((Chart2D) this.m_renderer).addTrace(this);
          }
          this.firePropertyChange(PROPERTY_ZINDEX, oldValue, this.m_zIndex);

        }

      }
    }
  }

  /**
   * @see aw.gui.chart.ITrace2D#getTracePainters()
   */
  public final Set getTracePainters() {

    return this.m_tracePainters;
  }

  /**
   * <p>
   * Returns <code>{@link #getName()}.</code>
   * </p>
   *
   * @return <code>{@link #getName()}</code>.
   */
  public String toString() {
    return this.getName();
  }
}
