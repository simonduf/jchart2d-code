/*
 * ITrace2D, the interface for all traces used by the Chart2D.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
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
package aw.gui.chart;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyChangeListener;
import java.util.Set;

import aw.util.collections.IComparableProperty;

/**
 * An interface used by <code>Chart2D</code>. ITrace2D contains the values to
 * display, the color for displaying and a suitable label. It may be seen as a
 * trace of the <code>Chart2D</code> that displays it. <br>
 * Implementations may be optimized for different use- cases: <br>
 * RingBuffers for fast changing data to keep the amount of tracepoints and
 * consumption of memory constant, internal Lists for allowing the sorting the
 * internal <code>TracePoint2D</code>- instances or Arrays for unordered Data
 * (in the order of adding) for fast performance. Even an <code>ITrace2D</code>
 * constructed by a "function- Object" may be thinkable.
 * <p>
 * There are various constraints for Traces: <br>- ordered by x-values <br>-
 * ordered by the order of <code>addPoint</code>- invocation (control form
 * outside) <br>- unique, single x- values <br>- limitation of tracepoints
 * <br>- time- critical (fast- changing tracepoints) <br>
 * <br>
 * Therefore there are various <code>ITrace2D</code>- implementations. Read
 * their description to find the one you need. Some may not have been written
 * yet.
 * </p>
 * <p>
 * {@link java.lang.Comparable} should be implemented by using the internal
 * property zIndex (see {@link #getZIndex()}, {@link #setZIndex(Integer)}).
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.15 $
 */
public interface ITrace2D extends IComparableProperty {

  /**
   * The property key defining the <code>color</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_COLOR = "color";

  /**
   * The property key defining the <code>maxX</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MAX_X = "maxX";

  /**
   * The property key defining the <code>maxY</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MAX_Y = "maxY";

  /**
   * The property key defining the <code>minX</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MIN_X = "minX";

  /**
   * The property key defining the <code>minY</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MIN_Y = "minY";

  /**
   * The property key defining the <code>name</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_NAME = "name";

  /**
   * The property key defining the <code>physicalUnits</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_PHYSICALUNITS = "physicalUnits";

  /**
   * The property key defining the <code>stroke</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_STROKE = "stroke";

  /**
   * The property key defining a change in the collection of
   * <code>{@link TracePoint2D}</code> instances within this trace. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_TRACEPOINT = "tracepoint";

  /**
   * The property key defining the <code>visible</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_VISIBLE = "visible";

  /**
   * <p>
   * The property key defining the <code>zIndex</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * </p>
   */
  public static final String PROPERTY_ZINDEX = "zIndex";

  /**
   * The minimum value for property zIndex: 0.
   *
   * @see #getZIndex()
   * @see #setZIndex(Integer)
   */
  public static final int Z_INDEX_MIN = 0;

  /**
   * The maximum value for property zIndex: 100.
   * <p>
   * the descriptive name for this trace.
   * <p>
   *
   * @see #getZIndex()
   * @see #setZIndex(Integer)
   */
  public static final int ZINDEX_MAX = 100;

  /**
   * <p>
   * Adds a tracepoint to the internal data.
   * </p>
   *
   * @see #addPoint(TracePoint2D p)
   * @param x
   *          the x-value of the point to add.
   * @param y
   *          the y-value of the point to add.
   * @return true if the operation was successful, false else.
   */
  boolean addPoint(double x, double y);

  /**
   * <p>
   * Adds the given <code>TracePoint2D </code> to the internal data.
   * </p>
   * <p>
   * Try to pass instances of <code>TracePoint2D</code> to this instance
   * instead of invoking <code>{@link #addPoint(double, double)}</code> to
   * increase performace. Else the given point has to be copied into such an
   * instance from the other method and delegated to this method.
   * </p>
   * <p>
   * Implementations decide wether the point will be accepted or not. So they
   * have to update the internal properties <code>minX</code>,
   * <code>maxX</code>,<code>maxY</code> and <code>minY</code> and also
   * care about firing property change events for those properties by method
   * <code>{@link java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)}</code>.
   * </p>
   *
   * @param p
   *          the point to add.
   * @return true if the operation was successful, false else.
   */
  boolean addPoint(TracePoint2D p);

  /**
   * <p>
   * Registers a property change listener that will be informed about changes of
   * the property identified by the given <code>propertyName</code>.
   * </p>
   *
   * @param propertyName
   *          the name of the property the listener is interested in
   *
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   *
   *
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Because the color is data common to a trace of a <code>Chart2D</code> it
   * is stored here. <br>
   * On the other hand only the corresponding <code>Chart2D </code> may detect
   * the same color chosen for different <code>IChart2D</code> instances to be
   * displayed. Therefore it is allowed to return null. This is a message to the
   * <code>Chart2D</code> to leave it the choice of the color. Then the
   * <code>Chart2D</code> will chose a color not owned by another
   * <code>ITrace2D</code> instance managed and assign it to the null-
   * returning instance.
   * <p>
   * The <code>Chart2D </code> will often call this method. So try to cache the
   * value in implementation and only check on modifications of
   * <code>TracePoint</code> instances or on <code>add</code>- invocations
   * for changes.
   * </p>
   *
   * @return The chosen java.awt.Color or null if the decision for the color
   *         should be made by the corresponding <code>Chart2D</code>.
   */
  Color getColor();

  /**
   * <p>
   * Callback method for the <code>Chart2D</code> that returns a
   * <code>String</code> describing the label of the <code>ITrace2D</code>
   * that will be displayed below the drawing area of the <code>Chart2D</code>.
   * </p>
   * <p>
   * This method should be implemented and finalized ASAP in the inheritance
   * tree and rely on the property <code>name</code> and
   * <code>physicalUnits</code>.
   * </p>
   *
   * @return a String describing the Axis being accessed.
   *
   */
  String getLable();

  /**
   * <p>
   * Returns the maximum amount of {@link TracePoint2D} instances that may be
   * added. For implementations that limit the maximum amount this is a
   * reasonable amount. Non-limiting implementations should return
   * {@link Integer#MAX_VALUE}. This allows to detect the unlimitedness. Of
   * course no implementation could store that amount of points.
   * </p>
   *
   * @return The maximum amount of {@link TracePoint2D} instances that may be
   *         added.
   *
   */
  public int getMaxSize();

  /**
   * Returns the maximum value to be displayed on the x- axis of the
   * <code>Chart2D</code>. Implementations should be synchronized for
   * multithreaded use. No exception is thrown. In case of empty data (no
   * tracepoints) 0 should be returned, to let the Chart2D know.
   * <p>
   * The <code>Chart2D </code> will often call this method. So try to cache the
   * value in implementation and only check on modifications of
   * <code>TracePoint</code> instances or on <code>add</code>- invocations
   * for changes.
   * </p>
   *
   * @return the maximum value of the internal data for the x- dimension.
   */
  double getMaxX();

  /**
   * Returns the maximum value to be displayed on the y- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   *
   * @return the maximum value of the internal data for the y- dimension.
   */
  double getMaxY();

  /**
   * Returns the minimum value to be displayed on the x- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * <p>
   * The <code>Chart2D </code> will often call this method. So try to cache the
   * value in implementation and only check on modifications of
   * <code>TracePoint</code> instances or on <code>add</code>- invocations
   * for changes.
   * </p>
   *
   * @return the minimum value of the internal data for the x- dimension.
   */

  double getMinX();

  /**
   * Returns the minimum value to be displayed on the y- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * <p>
   * The <code>Chart2D </code> will often call this method. So try to cache the
   * value in implementation and only check on modifications of
   * <code>TracePoint</code> instances or on <code>add</code>- invocations
   * for changes.
   * </p>
   *
   * @return the minimum value of the internal data for the y- dimension.
   */

  double getMinY();

  /**
   * @see #setName(String s)
   */
  public String getName();

  /**
   *
   * @see #setPhysicalUnits(String x,String y)
   */
  public String getPhysicalUnits();

  /**
   * <p>
   * Returns all property change listeners for the given property.
   * </p>
   *
   * @param property
   *          one of the constants with teh <code>PROPERTY_</code> prefix
   *          defined in this class or subclasses.
   *
   * @return the property change listeners for the given property.
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String property);

  /**
   * @return Returns the renderer.
   */
  public Chart2D getRenderer();

  /**
   * @return The amount of {@link TracePoint2D} instances currently contained.
   */
  public int getSize();

  /**
   * @return the Stroke that is used to render this instance.
   * @see #setStroke(Stroke)
   *
   */
  Stroke getStroke();

  /**
   * Returns the <code>Set&lt;{@link ITracePainter}&gt;</code> that will be
   * used to paint this trace.
   * <p>
   *
   * @return the <code>Set&lt;{@link ITracePainter}&gt;</code> that will be
   *         used to paint this trace.
   */
  public Set getTracePainters();

  /**
   * @return true if this instance should be rendered.
   *
   */
  public boolean getVisible();

  /**
   * <p>
   * The z-index defines the order in which this instance will be painted. A
   * lower value will bring it more "to the front".
   * </p>
   *
   * @return the z-index that will define the order in which this instance will
   *         be painted.
   *
   */
  public Integer getZIndex();

  /**
   * <p>
   * Returns false if internal <code>{@link TracePoint2D}</code> instances are
   * contained or true if not.
   * </p>
   *
   * @return <tt>false</tt> if internal <code>{@link TracePoint2D}</code>
   *         instances are contained or <tt>true</tt> if not.
   *
   */
  boolean isEmpty();

  /**
   * <p>
   * Returns an <code>Iterator</code> over the internal
   * <code>{@link TracePoint2D}</code> instances.
   * </p>
   * <p>
   * Implementations should be synchronized. This method is meant to allow
   * modifications of the intenal <code>TracePoint2D</code> instances, so the
   * original points should be returned.
   * </p>
   * <p>
   * There is no guarantee that changes made to the contained tracepoints will
   * be reflected in the display immediately. The order the iterator returns the
   * <code>TracePoint2D</code> instances decides how the <code>Chart2D</code>
   * will paint the trace.
   * </p>
   *
   * @return an <code>Iterator</code> over the internal
   *         <code>{@link TracePoint2D}</code> instances.
   */
  java.util.Iterator iterator();

  /**
   * Removes all internal <code>TracePoint2D</code>.{@link #isEmpty()} will
   * return true afterwards.
   *
   */
  public void removeAllPoints();

  /**
   * <p>
   * Removes the given point from this trace.
   * </p>
   *
   * @param point
   *          the point to remove.
   *
   * @return true if the remove opertation was successful, false else.
   *
   */
  public boolean removePoint(TracePoint2D point);

  /**
   * <p>
   * Deregisters a property change listener that has been registerd for
   * listening on all properties.
   * </p>
   *
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   *
   *
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * <p>
   * Removes a property change listener for listening on the given property.
   * </p>
   *
   * @param property
   *          one of the constants with teh <code>PROPERTY_</code> prefix
   *          defined in this class or subclasses.
   *
   * @param listener
   *          the listener for this property change.
   */
  public void removePropertyChangeListener(String property, PropertyChangeListener listener);

  /**
   * <p>
   * Set a <code>java.awt.Color</code> for this trace.
   * </p>
   *
   * @param color
   *          the <tt>Color</tt> to set.
   */
  void setColor(Color color);

  /**
   * Assingns a specific name to the <code>ITrace2D</code> which will be
   * displayed by the <code>Chart2D</code>.
   * <p>
   *
   * @param name
   *          the name for this trace.
   */
  public void setName(String name);

  /**
   * Assings a specific String representing the physical unit to the <code>
   *  ITrace2D</code>
   * (e.g. Volt, Ohm, lux, ...) which will be displayed by the
   * <code>{@link Chart2D}</code>
   * <p>
   *
   * @param xunit
   *          the physical unit for the x axis.
   *
   * @param yunit
   *          the physical unit for the y axis.
   */
  public void setPhysicalUnits(final String xunit, final String yunit);

  /**
   * This is a callback from {@link Chart2D#addTrace(ITrace2D)} and must not be
   * invoked from elswhere (needed for synchronization). Not the best design to
   * put this to an interface, but Char2D should handle this interface only.
   *
   * @param renderer
   *          The renderer to set.
   */
  public void setRenderer(Chart2D renderer);

  /**
   * Allows to specify the rendering of the ITrace2D. This Stroke will be
   * assigned to the {@link java.awt.Graphics2D} by the rendering
   * {@link Chart2D} when painting this instance.
   * <p>
   *
   * @param stroke
   *          the stroke to use for painting this trace.
   *
   */
  void setStroke(Stroke stroke);

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
  public Set setTracePainter(ITracePainter painter);

  /**
   * Removes the given trace painter, if it's class is contained and if more
   * painters are remaining.
   * <p>
   *
   * @param painter
   *          the trace painter to remove.
   *
   * @return true if a trace painter of the class of the given argument was
   *         removed.
   */
  public boolean removeTracePainter(final ITracePainter painter);

  /**
   * Set the visibility. If argument is false, this instance will not be
   * rendered by a Chart2D.
   * <p>
   *
   * @param visible
   *          true if this trace should be painted, false else.
   *
   */
  public void setVisible(boolean visible);

  /**
   * <p>
   * Sets the internal z-index property. This decides the order in which
   * different traces within the same <code>{@link Chart2D}</code> are
   * painted. The lower the given value is the more this trace will be brought
   * to front.
   * <p>
   * The value must not be lower than {@link #Z_INDEX_MIN}(0) and higher than
   * {@link #ZINDEX_MAX}(100).
   * </p>
   * <p>
   * This might not be tested for increased performance but ignoring these
   * bounds may result in wrong ordering of display.
   * </p>
   *
   * @see #getZIndex()
   *
   */
  public void setZIndex(Integer zIndex);

  /**
   * Adds the given trace painter to the internal set of trace painters.
   * <p>
   *
   * It will be the last painter to paint (most forward).
   * <p>
   *
   * @param painter
   *          the painter to add for rendering this trace.
   *
   * @return true if the painter was added (class of instance not contained
   *         before).
   */
  public boolean addTracePainter(ITracePainter painter);

}
