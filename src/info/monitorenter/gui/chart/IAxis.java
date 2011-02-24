/*
 *  IAxis.java of project jchart2d, interface for an axis of the 
 *  Chart2D.
 *  Copyright 2007 (C) Achim Westermann, created on 22:25:17.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor;
import info.monitorenter.util.Range;

import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;

/**
 * Interface for an axis of the {@link info.monitorenter.gui.chart.Chart2D}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.18 $
 */
public interface IAxis {
  /**
   * Constant for a {@link java.beans.PropertyChangeEvent} of the paint grid
   * flag.
   */
  public static final String PROPERTY_PAINTGRID = "axis.paintgrid";

  /** Constant for a {@link java.beans.PropertyChangeEvent} of the range policy. */
  public static final String PROPERTY_RANGEPOLICY = "axis.rangepolicy";

  /**
   * Constant for a {@link java.beans.PropertyChangeEvent} of the label
   * formatter.
   */
  public static final String PROPERTY_LABELFORMATTER = "axis.labelformatter";

  /**
   * Transforms the given pixel value (which has to be a awt value like
   * {@link java.awt.event.MouseEvent#getY()} into the chart value.
   * <p>
   * 
   * Internal use only, the interface does not guarantee that the pixel
   * corresponds to any valid awt pixel value within the chart component.
   * <p>
   * 
   * @param pixel
   *          a pixel value of the chart component as used by awt.
   * 
   * @return the awt pixel value transformed to the chart value.
   */
  public double translatePxToValue(final int pixel);

  /**
   * Transforms the given chart data value into the corresponding awt pixel
   * value for the chart.
   * <p>
   * 
   * @param value
   *          a chart data value.
   * 
   * @return the awt pixel value corresponding to the chart data value.
   */
  public int translateValueToPx(final double value);

  /**
   * Scales all <code>{@link ITrace2D}</code> instances in the dimension
   * represented by this axis.
   * <p>
   * This method is not deadlock - safe and should be called by the
   * <code>{@link Chart2D}</code> only!
   * <p>
   */
  public void scale();

  /**
   * Scales the given <code>{@link ITrace2D}</code> in the dimension
   * represented by this axis.
   * <p>
   * This method is not deadlock - safe and should be called by the
   * <code>{@link Chart2D}</code> only!
   * <p>
   * 
   * @param trace
   *          the trace to scale.
   */
  public void scaleTrace(final ITrace2D trace);

  /**
   * Allows the chart to register itself with the axix.
   * <p>
   * 
   * This is intended for <code>Chart2D</code> only!.
   * <p>
   * 
   * @param chart
   *          the chart to register itself with this axis.
   */
  //public void setChart(Chart2D chart);

  /**
   * Sets the title of this axis will be painted by the
   * <code>{IAxisTitlePainter}</code> of this instance.
   * <p>
   * 
   * @param title
   *          the title to set.
   * 
   * @return the previous Title or <code>null</code> if there was no title
   *         configured before.
   * 
   * @see #setTitlePainter(IAxisTitlePainter)
   */
  public String setTitle(String title);

  /**
   * Returns the title or <code>null</code> if there was no title configured
   * before.
   * <p>
   * 
   * @return the title or <code>null</code> if there was no title configured
   *         before.
   * 
   * @see #getTitlePainter()
   */
  public String getTitle();

  /**
   * Sets the title painter that will paint the title of this axis.
   * <p>
   * 
   * @param painter
   *          the instance that will paint the title of this axis.
   * 
   * @return the previous title painter of this axis or null if there was none
   *         configured before.
   */
  public IAxisTitlePainter setTitlePainter(IAxisTitlePainter painter);

  /**
   * Returns the instance that will paint the title of this axis.
   * <p>
   * 
   * @return the instance that will paint the title of this axis.
   */
  public IAxisTitlePainter getTitlePainter();

  /**
   * Add a listener for the given property.
   * <p>
   * 
   * The following {@link java.beans.PropertyChangeEvent} types should be fired
   * to listeners:<br>
   * 
   * <table border="0">
   * <tr>
   * <th><code>getPropertyName()</code></th>
   * <th><code>getSource()</code></th>
   * <th><code>getOldValue()</code></th>
   * <th><code>getNewValue()</code></th>
   * </tr>
   * <tr>
   * <td><code>{@link info.monitorenter.gui.chart.IAxis#PROPERTY_RANGEPOLICY}</code></td>
   * <td><code>{@link IAxis}</code> that changed</td>
   * <td><code>{@link IRangePolicy}</code>, the old value</td>
   * <td><code>{@link IRangePolicy}</code>, the new value</td>
   * </tr>
   * <tr>
   * <td><code>{@link info.monitorenter.gui.chart.IAxis#PROPERTY_PAINTGRID}</code></td>
   * <td><code>{@link IAxis}</code> that changed</td>
   * <td><code>{@link Boolean}</code>, the old value</td>
   * <td><code>{@link Boolean}</code>, the new value</td>
   * </tr>
   * <tr>
   * <td><code>{@link info.monitorenter.gui.chart.IAxis#PROPERTY_LABELFORMATTER}</code></td>
   * <td><code>{@link IAxis}</code> that changed</td>
   * <td><code>{@link IAxisLabelFormatter}</code>, the old value or null if
   * there was no formatter before. </td>
   * <td><code>{@link IAxisLabelFormatter}</code>, the new value</td>
   * </tr>
   * </table>
   * 
   * @param propertyName
   *          the property to be informed about changes.
   * 
   * @param listener
   *          the listener that will be informed.
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Returns the accessor to the chart.
   * <p>
   * 
   * @return the accessor to the chart.
   */
  public abstract AChart2DDataAccessor getAccessor();

  /**
   * Returns the constant for the dimension this axis stands for in the chart.
   * <p>
   * 
   * @return {@link Chart2D#X}, {@link Chart2D#Y} or -1 if this axis is not
   *         assigned to a chart.
   */
  public int getDimension();

  /**
   * Returns the formatter for labels.
   * <p>
   * 
   * @return the formatter for labels.
   */
  public abstract IAxisLabelFormatter getFormatter();

  /**
   * Get the major tick spacing for label generation.
   * <p>
   * 
   * @return the major tick spacing for label generation.
   * 
   * @see #setMajorTickSpacing(double)
   * 
   */

  public abstract double getMajorTickSpacing();

  /**
   * Get the minor tick spacing for label generation.
   * <p>
   * 
   * @return the minor tick spacing for label generation.
   * 
   * @see #setMinorTickSpacing(double)
   * 
   */
  public abstract double getMinorTickSpacing();

  /**
   * Returns the width in pixel this axis needs to paint itself.
   * <p>
   * 
   * This includes the axis line, it's ticks and labels and it's title.
   * <p>
   * 
   * <b>Note:</b></br/> For an x axis the width only includes the overhang it
   * needs on the right edge for painting a complete lable, not the complete
   * space it needs for the complete line.
   * <p>
   * 
   * @param g2d
   *          needed for font metric information.
   * 
   * @return the width in pixel this axis needs to paint itself.
   */
  public int getWidth(Graphics2D g2d);

  /**
   * Returns the height in pixel this axis needs to paint itself.
   * <p>
   * 
   * This includes the axis line, it's ticks and labels and it's title.
   * <p>
   * 
   * <b>Note:</b></br/> For an y axis the hight only includes the overhang it
   * needs on the upper edge for painting a complete lable, not the complete
   * space it needs for the complete line.
   * <p>
   * 
   * @param g2d
   *          needed for font metric information.
   * 
   * @return the height in pixel this axis needs to paint itself.
   */
  public int getHeight(Graphics2D g2d);

  /**
   * Returns an array of all the listeners that were added to the this instance
   * with {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * <p>
   * 
   * @return an array of all the listeners that were added to the this instance
   *         with
   *         {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * 
   * @param propertyName
   *          The name of the property being listened to.
   * 
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

  /**
   * This method is used by the Chart2D to scale it's values during painting.
   * <p>
   * Caution: This method does not necessarily return the Range configured with
   * {@link #setRange(Range)}. The internal {@link IRangePolicy} is taken into
   * account.
   * <p>
   * 
   * @return the range corresponding to the upper and lower bound of the values
   *         that will be displayable on this Axis of the Chart2D.
   * 
   * @see #setRangePolicy(IRangePolicy)
   * 
   */
  public abstract Range getRange();

  /**
   * Returns the range policy used by this axis.
   * <p>
   * 
   * @return the range policy used by this axis
   * 
   * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#getRangePolicy()
   * 
   */
  public abstract IRangePolicy getRangePolicy();

  /**
   * Scales the given absolute value into a value between 0 and 1.0 (if it is in
   * the range of the data).
   * <p>
   * If the given absolute value is not in the display- range of the
   * <code>Chart2D</code>, negative values or values greater than 1.0 may
   * result.
   * <p>
   * 
   * @param absolute
   *          a value in the real value range of the corresponding chart.
   * 
   * @return a value between 0.0 and 1.0 that is mapped to a position within the
   *         chart.
   * 
   */
  public abstract double getScaledValue(final double absolute);

  /**
   * Returns wether the x grid is painted or not.
   * <p>
   * 
   * @return wether the x grid is painted or not.
   */
  public abstract boolean isPaintGrid();

  /**
   * Returns whether the scale for this axis should be painted or not.
   * <p>
   * 
   * @return whether the scale for this axis should be painted or not.
   */
  public abstract boolean isPaintScale();

  /**
   * Check wether scale values are started from major ticks.
   * <p>
   * 
   * @return true if scale values start from major ticks.
   * 
   * @see info.monitorenter.gui.chart.axis.AAxis#setMajorTickSpacing(double)
   */
  public abstract boolean isStartMajorTick();

  /**
   * Remove a PropertyChangeListener for a specific property. If
   * <code>listener</code> was added more than once to the same event source
   * for the specified property, it will be notified one less time after being
   * removed. If <code>propertyName</code> is null, no exception is thrown and
   * no action is taken. If <code>listener</code> is null, or was never added
   * for the specified property, no exception is thrown and no action is taken.
   * 
   * @param property
   *          The name of the property that was listened on.
   * 
   * @param listener
   *          The PropertyChangeListener to be removed.
   * 
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(String property, PropertyChangeListener listener);

  /**
   * Copies the complete state (flat, references are overtaken) of the given
   * axis to this instance and overtakes any property change listeners.
   * <p>
   * 
   * This is used when a new axis is set to a chart.
   * <p>
   * 
   * @see Chart2D#setAxisX(info.monitorenter.gui.chart.axis.AAxis)
   * 
   * @see Chart2D#setAxisY(info.monitorenter.gui.chart.axis.AAxis)
   * 
   * @param axis
   *          the axis to replace by this instance.
   */
  public abstract void replace(final IAxis axis);

  /**
   * Sets the formatter to use for labels.
   * <p>
   * 
   * @param formatter
   *          The formatter to set.
   */
  public abstract void setFormatter(final IAxisLabelFormatter formatter);

  /**
   * This method sets the major tick spacing for label generation.
   * <p>
   * 
   * Only values between 0.0 and 100.0 are allowed.
   * <p>
   * 
   * The number that is passed in represents the distance, measured in values,
   * between each major tick mark. If you have a trace with a range from 0 to 50
   * and the major tick spacing is set to 10, you will get major ticks next to
   * the following values: 0, 10, 20, 30, 40, 50.
   * <p>
   * 
   * <b>Note: </b> <br>
   * Ticks are free of any multiples of 1000. If the chart contains values
   * between 0 an 1000 and configured a tick of 2 the values 0, 200, 400, 600,
   * 800 and 1000 will highly probable to be displayed. This depends on the size
   * (in pixels) of the <code>Chart2D<</code>. Of course there is a
   * difference: ticks are used in divisions and multiplications: If the
   * internal values are very low and the ticks are very high, huge rounding
   * errors might occur (division by ticks results in very low values a double
   * cannot hit exactly. So prefer setting ticks between 0 an 10 or - if you
   * know your values are very small (e.g. in nano range [10 <sup>-9 </sup>])
   * use a small value (e.g. 2*10 <sup>-9 </sup>).
   * <p>
   * 
   * @param majorTickSpacing
   *          the major tick spacing for label generation.
   */
  public abstract void setMajorTickSpacing(final double majorTickSpacing);

  /**
   * This method sets the minor tick spacing for label generation.
   * <p>
   * 
   * The number that is passed-in represents the distance, measured in values,
   * between each minor tick mark. If you have a trace with a range from 0 to 10
   * and the minor tick spacing is set to 2, you will get major ticks next to
   * the following values: 0, 2, 4, 6, 8, 10. If a major tick hits the same
   * values the tick will be a major ticks. For this example: if a major tick
   * spacing is set to 5 you will only get minor ticks for: 2, 4, 6, 8.
   * <p>
   * 
   * <b>Note: </b> <br>
   * Ticks are free of any powers of 10. There is no difference between setting
   * a tick to 2, 200 or 20000 because ticks cannot break the rule that every
   * scale label has to be visible. If the chart contains values between 0 an
   * 1000 and configured a tick of 2 the values 0, 200, 400, 600, 800 and 1000
   * will highly probable to be displayed. This depends on the size (in pixels)
   * of the <code>Chart2D<</code>. Of course there is a difference: ticks
   * are used in divisions and multiplications: If the internal values are very
   * low and the ticks are very high, huge rounding errors might occur (division
   * by ticks results in very low values a double cannot hit exactly. So prefer
   * setting ticks between 0 an 10 or - if you know your values are very small
   * (e.g. in nano range [10 <sup>-9 </sup>]) use a small value (e.g. 2*10
   * <sup>-9 </sup>).
   * <p>
   * 
   * @param minorTickSpacing
   *          the minor tick spacing to set.
   * 
   */
  public abstract void setMinorTickSpacing(final double minorTickSpacing);

  /**
   * Set wether the grid in this dimension should be painted or not.
   * <p>
   * A repaint operation for the chart is triggered.
   * <p>
   * 
   * @param grid
   *          true if the grid should be painted or false if not.
   */

  public abstract void setPaintGrid(boolean grid);

  /**
   * Set if the scale for this axis should be shown.
   * <p>
   * 
   * @param show
   *          true if the scale on this axis should be shown, false else.
   */
  public abstract void setPaintScale(final boolean show);

  /**
   * Sets a Range to use for filtering the view to the the connected Axis. Note
   * that it's effect will be affected by the internal {@link IRangePolicy}.
   * <p>
   * To get full control use: <br>
   * <code> setRangePolicy(new &lt;AnARangePolicy&gt;(range);</code>
   * <p>
   * 
   * @param range
   *          Range to use for filtering the view to the the connected Axis.
   * 
   * @see #getRangePolicy()
   * 
   * @see IRangePolicy#setRange(Range)
   */
  public abstract void setRange(final Range range);

  /**
   * Sets the RangePolicy.
   * <p>
   * If the given RangePolicy has an unconfigured internal Range (
   * {@link Range#RANGE_UNBOUNDED}) the old internal RangePolicy is taken into
   * account: <br>
   * If the old RangePolicy has a configured Range this is transferred to the
   * new RangePolicy.
   * <p>
   * 
   * @param rangePolicy
   *          The rangePolicy to set.
   */
  public abstract void setRangePolicy(final IRangePolicy rangePolicy);

  /**
   * Set wether scale values are started from major ticks.
   * <p>
   * 
   * @param majorTick
   *          true if scale values shall start with a major tick.
   * 
   * @see info.monitorenter.gui.chart.axis.AAxis#setMajorTickSpacing(double)
   */
  public abstract void setStartMajorTick(final boolean majorTick);

}
