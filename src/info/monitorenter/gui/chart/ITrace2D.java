/*
 * ITrace2D, the interface for all traces used by the Chart2D.
 * Copyright (c) 2007  Achim Westermann, Achim.Westermann@gmx.de
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

import info.monitorenter.util.collections.IComparableProperty;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Set;

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
 * <p>
 * {@link java.lang.Comparable} should be implemented by using the internal
 * property zIndex (see {@link #getZIndex()}, {@link #setZIndex(Integer)}).
 * <p>
 * <h3>Property Change events</h3>
 * The following <code>{@link java.beans.PropertyChangeEvent}</code> may be fired to 
 * <code>{@link PropertyChangeListener}</code> instances that register themselves 
 * with <code>{@link #addPropertyChangeListener(String, PropertyChangeListener)}</code>.
 * 
 * <table border="0">
 * <tr>
 * <th><code>getPropertyName()</code></th>
 * <th><code>getSource()</code></th>
 * <th><code>getOldValue()</code></th>
 * <th><code>getNewValue()</code></th>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_ZINDEX}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Number}</code>, the old value</td>
 * <td><code>{@link java.lang.Number}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_MAX_X}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_MIN_X}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_MAX_Y}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_MIN_Y}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Double}</code>, the old value</td>
 * <td><code>{@link java.lang.Double}</code>, the new value</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_TRACEPOINT}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link info.monitorenter.gui.chart.TracePoint2D}</code>, the
 * instance that was removed</td>
 * <td><code>null</code>, indication that an instance was removed</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_TRACEPOINT}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>null</code>, indication that a value was added</td>
 * <td><code>{@link info.monitorenter.gui.chart.TracePoint2D}</code>, the
 * new instance that was added, identifying that an instance was removed</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_VISIBLE}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.lang.Boolean}</code>, the old state.</td>
 * <td><code>{@link java.lang.Boolean}</code>, the new state.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_PAINTERS}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>null</code>, indicating that a painter was added.</td>
 * <td><code>{@link info.monitorenter.gui.chart.ITracePainter}</code>, the
 * new painter.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_PAINTERS}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link info.monitorenter.gui.chart.ITracePainter}</code>, the
 * old painter.</td>
 * <td><code>null</code>, indicating that a painter was removed.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_STROKE}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.awt.Stroke}</code>, the old stroke.</td>
 * <td><code>{@link java.awt.Stroke}</code>, the new stroke.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_COLOR}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link java.awt.Color}</code>, the new color.</td>
 * <td><code>{@link java.awt.Color}</code>, the new color.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_ERRORBARPOLICY}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>null</code>, indicating that an error bar policy was added.</td>
 * <td><code>{@link info.monitorenter.gui.chart.IErrorBarPolicy}</code>, the
 * new error bar policy.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_ERRORBARPOLICY}</code></td>
 * <td><code>{@link ITrace2D}</code> that changed</td>
 * <td><code>{@link info.monitorenter.gui.chart.IErrorBarPolicy}</code>, the
 * old error bar policy.</td>
 * <td><code>null</code>, indicating that an error bar policy was removed.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_ERRORBARPOLICY_CONFIGURATION}</code></td>
 * <td><code>{@link ITrace2D}</code> that notifies the change 
 * of the configured error bar policy.</td>
 * <td>null</td>
 * <td><code>{@link info.monitorenter.gui.chart.IErrorBarPolicy}</code>, the
 * instance with the configuration change.</td>
 * </tr>
 * <tr>
 * <td><code>{@link info.monitorenter.gui.chart.ITrace2D#PROPERTY_POINT_CHANGED}</code></td>
 * <td><code>{@link ITrace2D}</code> that notifies the change 
 * of the contained trace point.</td>
 * <td><code>null</code> because it would be too much overhead to store the old point in a additional instance as the 
 * original is modified in <code>{@link TracePoint2D#setLocation(java.awt.geom.Point2D)}</code>.</td>
 * <td><code>{@link TracePoint2D}</code> the point whose location was modified.</td>
 * </tr>
 * </table>
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.20 $
 */
public interface ITrace2D extends IComparableProperty, PropertyChangeListener {

  /**
   * The property key defining the <code>color</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_COLOR = "ITrace2D.PROPERTY_COLOR";

  /**
   * The property key defining any change of a localtion of a contained
   * <code>{@link TracePoint2D} </code>.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * 
   * Property change event with this property will be fired if method
   * <code>{@link TracePoint2D#setLocation(java.awt.geom.Point2D)}</code> or
   * <code>{@link TracePoint2D#setLocation(double, double)}</code> of any
   * contained point is invoked.<p>
   */
  public static final String PROPERTY_POINT_CHANGED = "ITrace2D.PROPERTY_POINT_CHANGED";

  /**
   * The property key defining a change of <code>{@link IErrorBarPolicy}</code>
   * instances contained.
   * <p>
   * This is fired from <code>{@link #addErrorBarPolicy(IErrorBarPolicy)}</code>,
   * <code>{@link #removeErrorBarPolicy(IErrorBarPolicy)}</code> and
   * <code>{@link #setErrorBarPolicy(IErrorBarPolicy)}</code>.
   * <p>
   * 
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_ERRORBARPOLICY = "ITrace2D.PROPERTY_ERRORBARPOLICY";

  /**
   * The property key defining a change of the configuration of a contained<code>{@link IErrorBarPolicy}</code>.
   * <p>
   * This is fired whenever an <code>IErrorBarPolicy</code> notifies this
   * instance of a configuration change via an event for
   * <code>{@link IErrorBarPolicy#PROPERTY_CONFIGURATION}</code>.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_ERRORBARPOLICY_CONFIGURATION = "ITrace2D.PROPERTY_ERRORBARPOLICY_CONFIGURATION";

  /**
   * The property key defining the <code>maxX</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MAX_X = "ITrace2D.PROPERTY_MAX_X";

  /**
   * The property key defining the <code>maxY</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MAX_Y = "ITrace2D.PROPERTY_MAX_Y";

  /**
   * The property key defining the <code>minX</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MIN_X = "ITrace2D.PROPERTY_MIN_X";

  /**
   * The property key defining the <code>minY</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_MIN_Y = "ITrace2D.PROPERTY_MIN_Y";

  /**
   * The property key defining the <code>name</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_NAME = "ITrace2D.PROPERTY_NAME";

  /**
   * The property key defining a change in the set of
   * <code>{@link ITracePainter}</code> instances. Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_PAINTERS = "ITrace2D.PROPERTY_PAINTERS";

  /**
   * The property key defining the <code>physicalUnits</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_PHYSICALUNITS = "ITrace2D.PROPERTY_PHYSICALUNITS";

  /**
   * The property key defining the <code>stroke</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_STROKE = "ITrace2D.PROPERTY_STROKE";

  /**
   * The property key defining a change in the collection of
   * <code>{@link TracePoint2D}</code> instances within this trace. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_TRACEPOINT = "ITrace2D.PROPERTY_TRACEPOINT";

  /**
   * The property key defining the <code>visible</code> property. Use in
   * combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   */
  public static final String PROPERTY_VISIBLE = "ITrace2D.PROPERTY_VISIBLE";

  /**
   * The property key defining the <code>zIndex</code> property.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   */
  public static final String PROPERTY_ZINDEX = "ITrace2D.PROPERTY_ZINDEX";

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
   * Adds a trace that "should" compute values from the points added via
   * <code>{@link #addPoint(TracePoint2D)}</code>.
   * <p>
   * 
   * The given trace will be informed in case an add operation on this trace
   * succeeds via <code>{@link #addPoint(TracePoint2D)}</code>.
   * 
   * @param trace
   *          the trace that will calculate it's points from the added points of
   *          this trace.
   */
  public void addComputingTrace(ITrace2D trace);

  /**
   * Adds the given error bar policy to the internal set of error bar policies.
   * <p>
   * 
   * It will be the last error bar policy to render (most forward on screen).
   * <p>
   * 
   * @param errorBarPolicy
   *          the error bar policy to add for rendering this trace's error bars.
   * 
   * @return true if the painter was added (same instance was not contained
   *         before).
   */
  public boolean addErrorBarPolicy(IErrorBarPolicy errorBarPolicy);

  /**
   * Adds a tracepoint to the internal data.
   * <p>
   * 
   * @see #addPoint(TracePoint2D p)
   * 
   * @param x
   *          the x-value of the point to add.
   * @param y
   *          the y-value of the point to add.
   * @return true if the operation was successful, false else.
   */
  public boolean addPoint(double x, double y);

  /**
   * Adds the given <code>TracePoint2D </code> to the internal data.
   * <p>
   * Try to pass instances of <code>TracePoint2D</code> to this instance
   * instead of invoking <code>{@link #addPoint(double, double)}</code> to
   * increase performace. Else the given point has to be copied into such an
   * instance from the other method and delegated to this method.
   * <p>
   * Implementations decide wether the point will be accepted or not. So they
   * have to update the internal properties <code>minX</code>,
   * <code>maxX</code>,<code>maxY</code> and <code>minY</code> and also
   * care about firing property change events for those properties by method
   * <code>{@link java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)}</code>.
   * <p>
   * 
   * @param p
   *          the point to add.
   * @return true if the operation was successful, false else.
   */
  public boolean addPoint(TracePoint2D p);

  /**
   * Method to trigger by
   * <code>{@link TracePoint2D#setLocation(double, double)}</code>,
   * <code>{@link #addPoint(TracePoint2D)}</code> or
   * <code>{@link #removePoint(TracePoint2D)}</code>.
   * <p>
   * Bound checks are performed and property change events for the properties
   * <code>{@link ITrace2D#PROPERTY_MAX_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MIN_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code> and
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> are fired if the add bounds
   * have changed due to the modification of the point.
   * <p>
   * 
   * @param changed
   *          the point that has been changed which may be a newly added point
   *          (from <code>{@link #addPoint(TracePoint2D)}</code>, a removed
   *          one or a modified one.
   * @param state
   *          one of {<code>{@link TracePoint2D#STATE_ADDED}, {@link TracePoint2D#STATE_CHANGED},
   *   {@link TracePoint2D#STATE_REMOVED}</code>}
   *          to inform about the type of change.
   */
  public void firePointChanged(final TracePoint2D changed, final int state);

  /**
   * Registers a property change listener that will be informed about changes of
   * the property identified by the given <code>propertyName</code>.
   * <p>
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

  /**
   * Returns true if the given painter is contained in this compound painter.
   * <p>
   * 
   * @param painter
   *          the painter to check wether it is contained.
   * 
   * @return true if the given painter is contained in this compound painter.
   */
  public boolean containsTracePainter(final ITracePainter painter);

  /**
   * Because the color is data common to a trace of a <code>Chart2D</code> it
   * is stored here.
   * <p>
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
   * <p>
   * 
   * @return The chosen java.awt.Color or null if the decision for the color
   *         should be made by the corresponding <code>Chart2D</code>.
   */
  Color getColor();

  /**
   * Returns the <code>Set&lt;{@link IErrorBarPolicy}&gt;</code> that will
   * be used to render error bars for this trace.
   * <p>
   * 
   * @return the <code>Set&lt;{@link IErrorBarPolicy}&gt;</code> that will
   *         be used to render error bars for this trace.
   */
  public Set getErrorBarPolicies();

  /**
   * Callback method for the <code>Chart2D</code> that returns a
   * <code>String</code> describing the label of the <code>ITrace2D</code>
   * that will be displayed below the drawing area of the <code>Chart2D</code>.
   * <p>
   * This method should be implemented and finalized ASAP in the inheritance
   * tree and rely on the property <code>name</code> and
   * <code>physicalUnits</code>.
   * <p>
   * 
   * @return a String describing the Axis being accessed.
   * 
   */
  public String getLabel();

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
   * <p>
   * 
   * @return the maximum value of the internal data for the x- dimension.
   */
  public double getMaxX();

  /**
   * Returns the maximum value to be displayed on the y- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * <p>
   * 
   * @return the maximum value of the internal data for the y- dimension.
   */
  public double getMaxY();

  /**
   * Returns the minimum value to be displayed on the x- axis of the Chart2D.
   * <p>
   * 
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * <p>
   * 
   * The <code>Chart2D </code> will often call this method. So try to cache the
   * value in implementation and only check on modifications of
   * <code>TracePoint</code> instances or on <code>add</code>- invocations
   * for changes.
   * <p>
   * 
   * @return the minimum value of the internal data for the x- dimension.
   */

  double getMinX();

  /**
   * Returns the minimum value to be displayed on the y- axis of the Chart2D.
   * <p>
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
   * Returns the name of this trace.
   * <p>
   * 
   * @return the name of this trace.
   * 
   * @see #setName(String s)
   */
  public String getName();

  /**
   * Returns the concatenation
   * <code>[x: "{@link #getPhysicalUnitsX()}", y: "{@link #getPhysicalUnitsY()}"]</code>.
   * <p>
   * 
   * @return the concatenation
   *         <code>[x: "{@link #getPhysicalUnitsX()}", y: "{@link #getPhysicalUnitsY()}"]</code>.
   * @see #setPhysicalUnits(String x,String y)
   */
  public String getPhysicalUnits();

  /**
   * Returns the physical unit string value for the x dimension.
   * <p>
   * 
   * @return the physical unit string value for the x dimension.
   * 
   * @see #setPhysicalUnits(String x,String y)
   */
  public String getPhysicalUnitsX();

  /**
   * Returns the physical unit string value for the y dimension.
   * <p>
   * 
   * @return the physical unit string value for the y dimension.
   * 
   * @see #setPhysicalUnits(String x,String y)
   */
  public String getPhysicalUnitsY();

  /**
   * Returns all property change listeners for the given property.
   * <p>
   * 
   * @param property
   *          one of the constants with the <code>PROPERTY_</code> prefix
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
   * Returns the amount of {@link TracePoint2D} instances currently contained.
   * <p>
   * 
   * @return The amount of <code>{@link TracePoint2D}</code> instances
   *         currently contained.
   */
  public int getSize();

  /**
   * Returns the Stroke that is used to render this instance.
   * <p>
   * 
   * @return the Stroke that is used to render this instance.
   * 
   * @see #setStroke(Stroke)
   * 
   */
  public Stroke getStroke();

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
   * The z-index defines the order in which this instance will be painted.
   * <p>
   * A lower value will bring it more "to the front".
   * <p>
   * 
   * @return the z-index that will define the order in which this instance will
   *         be painted.
   * 
   */
  public Integer getZIndex();

  /**
   * Returns false if internal <code>{@link TracePoint2D}</code> instances are
   * contained or true if not.
   * <p>
   * 
   * @return <tt>false</tt> if internal <code>{@link TracePoint2D}</code>
   *         instances are contained or <tt>true</tt> if not.
   * 
   */
  public boolean isEmpty();

  /**
   * Returns true if this instance should be rendered.
   * <p>
   * 
   * @return true if this instance should be rendered.
   * 
   */
  public boolean isVisible();

  /**
   * Returns an <code>Iterator</code> over the internal
   * <code>{@link TracePoint2D}</code> instances.
   * <p>
   * Implementations should be synchronized. This method is meant to allow
   * modifications of the intenal <code>TracePoint2D</code> instances, so the
   * original points should be returned.
   * <p>
   * There is no guarantee that changes made to the contained tracepoints will
   * be reflected in the display immediately. The order the iterator returns the
   * <code>TracePoint2D</code> instances decides how the <code>Chart2D</code>
   * will paint the trace.
   * <p>
   * 
   * @return an <code>Iterator</code> over the internal
   *         <code>{@link TracePoint2D}</code> instances.
   */
  public Iterator iterator();

  /**
   * Removes all internal <code>TracePoint2D</code>.{@link #isEmpty()} will
   * return true afterwards.
   * <p>
   * 
   */
  public void removeAllPoints();

  /**
   * Remove a trace that "should" compute values from the points added via
   * <code>{@link #addPoint(TracePoint2D)}</code>.
   * <p>
   * 
   * @param trace
   *          the trace that will calculate it's points from the added points of
   *          this trace.
   * 
   * @return true if the given trace was removed (recognized by the means of
   *         <code>{@link Object#equals(Object)}</code>).
   */
  public boolean removeComputingTrace(ITrace2D trace);

  /**
   * Removes the given error bar policy from the internal set of error bar
   * policies.
   * <p>
   * 
   * @param errorBarPolicy
   *          the error bar policy to remove.
   * 
   * @return true if the painter was removed (same instance contained before).
   */
  public boolean removeErrorBarPolicy(IErrorBarPolicy errorBarPolicy);

  /**
   * Removes the given point from this trace.
   * <p>
   * 
   * @param point
   *          the point to remove.
   * 
   * @return true if the remove opertation was successful, false else.
   * 
   */
  public boolean removePoint(TracePoint2D point);

  /**
   * Deregisters a property change listener that has been registerd for
   * listening on all properties.
   * <p>
   * 
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   * 
   * 
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Removes a property change listener for listening on the given property.
   * <p>
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
   * Set a <code>java.awt.Color</code> for this trace.
   * <p>
   * 
   * @param color
   *          the <tt>Color</tt> to set.
   */
  void setColor(Color color);

  /**
   * Replaces all internal error bar policies by the new one.
   * <p>
   * 
   * @param errorBarPolicy
   *          the new sole painter to use.
   * 
   * @return the <code>Set&lt;{@link IErrorBarPolicy}&gt;</code> that was
   *         used before.
   */
  public Set setErrorBarPolicy(IErrorBarPolicy errorBarPolicy);

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
   * invoked from elswhere (needed for synchronization).
   * <p>
   * Not the best design to put this to an interface, but Char2D should handle
   * this interface only.
   * <p>
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
  public void setStroke(Stroke stroke);

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
   * Sets the internal z-index property. This decides the order in which
   * different traces within the same <code>{@link Chart2D}</code> are
   * painted.
   * <p>
   * 
   * The lower the given value is the more this trace will be brought to front.
   * <p>
   * The value must not be lower than {@link #Z_INDEX_MIN}(0) and higher than
   * {@link #ZINDEX_MAX}(100).
   * <p>
   * This might not be tested for increased performance but ignoring these
   * bounds may result in wrong ordering of display.
   * <p>
   * 
   * @param zIndex
   *          the z index of this trace - the lower the value the more in front
   *          the trace will appear amongst other traces in the same chart.
   * 
   * @see #getZIndex()
   * 
   */
  public void setZIndex(Integer zIndex);

  /**
   * Tests whether error bars are painted by this trace.
   * <p>
   * 
   * Returns true if
   * <ul>
   * <li>this trace contains {@link IErrorBarPolicy} instances. </li>
   * <li>and at least one of these instances contains at least one
   * {@link IErrorBarPainter} instance.</li>
   * </ul>
   * <p>
   * 
   * @return true if this trace renders error bars.
   */
  public boolean showsErrorBars();

  /**
   * Tests whether error bars in negative x direction are painted by this trace.
   * <p>
   * 
   * Returns true if
   * <ul>
   * <li> this trace contains at leaste one {@link IErrorBarPolicy} instance
   * that {@link IErrorBarPolicy#isShowNegativeXErrors()}. </li>
   * <li> and at least one of these instances contains at least one
   * {@link IErrorBarPainter} instance. </li>
   * </ul>
   * <p>
   * 
   * @return true if this trace renders error bars in negative x direction.
   */
  public boolean showsNegativeXErrorBars();

  /**
   * Tests whether error bars in negative y direction are painted by this trace.
   * <p>
   * 
   * Returns true if
   * <ul>
   * <li> this trace contains at leaste one {@link IErrorBarPolicy} instance
   * that {@link IErrorBarPolicy#isShowNegativeYErrors()}. </li>
   * <li> and at least one of these instances contains at least one
   * {@link IErrorBarPainter} instance. </li>
   * </ul>
   * <p>
   * 
   * @return true if this trace renders error bars in negative y direction.
   */
  public boolean showsNegativeYErrorBars();

  /**
   * Tests whether error bars in positive x direction are painted by this trace.
   * <p>
   * 
   * Returns true if
   * <ul>
   * <li> this trace contains at leaste one {@link IErrorBarPolicy} instance
   * that {@link IErrorBarPolicy#isShowPositiveXErrors()}. </li>
   * <li> and at least one of these instances contains at least one
   * {@link IErrorBarPainter} instance. </li>
   * </ul>
   * <p>
   * 
   * @return true if this trace renders error bars in positive x direction.
   */
  public boolean showsPositiveXErrorBars();

  /**
   * Tests whether error bars in positive y direction are painted by this trace.
   * <p>
   * 
   * Returns true if
   * <ul>
   * <li> this trace contains at leaste one {@link IErrorBarPolicy} instance
   * that {@link IErrorBarPolicy#isShowPositiveYErrors()}. </li>
   * <li> and at least one of these instances contains at least one
   * {@link IErrorBarPainter} instance. </li>
   * </ul>
   * <p>
   * 
   * @return true if this trace renders error bars in positive y direction.
   */
  public boolean showsPositiveYErrorBars();

}
