/*
 *  IAxis.java of project jchart2d, interface for an axis of the 
 *  Chart2D.
 *  Copyright 2006 (C) Achim Westermann, created on 22:25:17.
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

import info.monitorenter.gui.chart.AAxis.Chart2DDataAccessor;
import info.monitorenter.gui.chart.labelformatters.ALabelFormatter;
import info.monitorenter.util.Range;

import java.beans.PropertyChangeListener;

/**
 * Interface for an axis of the {@link info.monitorenter.gui.chart.Chart2D}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.4 $
 */
public interface IAxis {

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
   * </table>
   * 
   * @param propertyName
   *          the property to be informed about changes.
   * 
   * @param listener
   *          the listener that will be informed.
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /** Constant for a {@link java.beans.PropertyChangeEvent} of the range policy. */
  public static final String PROPERTY_RANGEPOLICY = "axis.rangepolicy";

  /** Constant for a {@link java.beans.PropertyChangeEvent} of the paint grid flag. */
  public static final String PROPERTY_PAINTGRID = "axis.paintgrid";

  /**
   * Returns the accessor to the chart.
   * <p>
   * 
   * @return the accessor to the chart.
   */
  public abstract Chart2DDataAccessor getAccessor();

  /**
   * Returns the formatter for labels.
   * <p>
   * 
   * @return the formatter for labels.
   */
  public abstract ILabelFormatter getFormatter();

  /**
   * Get the major tick spacing for label generation.
   * <p>
   * 
   * @see #setMajorTickSpacing(double)
   * 
   */

  public abstract double getMajorTickSpacing();

  /**
   * Get the minor tick spacing for label generation.
   * <p>
   * 
   * @return he minor tick spacing for label generation.
   * 
   * @see #setMinorTickSpacing(double)
   * 
   */
  public abstract double getMinorTickSpacing();

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
   * @see info.monitorenter.gui.chart.AAxis.Chart2DDataAccessor#getRange()
   * 
   */
  public abstract Range getRange();

  /**
   * See!
   * <p>
   * 
   * @see info.monitorenter.gui.chart.AAxis.Chart2DDataAccessor#getRangePolicy()
   * 
   */
  public abstract IRangePolicy getRangePolicy();

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
   * @see AAxis#setMajorTickSpacing(double)
   */
  public abstract boolean isStartMajorTick();

  /**
   * Sets the formatter to use for labels.
   * <p>
   * 
   * @param formatter
   *          The formatter to set.
   */
  public abstract void setFormatter(final ALabelFormatter formatter);

  /**
   * This method sets the major tick spacing for label generation.
   * <p>
   * 
   * Only values between 0.0 and 100.0 are allowed.
   * <p>
   * 
   * The number that is passed-in represents the distance, measured in values,
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
   * between each major tick mark. If you have a trace with a range from 0 to 50
   * and the major tick spacing is set to 10, you will get major ticks next to
   * the following values: 0, 10, 20, 30, 40, 50.
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
   * <p>
   * Sets a Range to use for filtering the view to the the connected Axis. Note
   * that it's effect will be affected by the internal {@link IRangePolicy}.
   * </p>
   * <p>
   * To get full control use: <br>
   * <code> setRangePolicy(new &lt;AnARangePolicy&gt;(range);</code>
   * </p>
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
   * <p>
   * Sets the RangePolicy.
   * </p>
   * <p>
   * If the given RangePolicy has an unconfigured internal Range (
   * {@link Range#RANGE_UNBOUNDED}) the old internal RangePolicy is taken into
   * account: <br>
   * If the old RangePolicy has a configured Range this is transferred to the
   * new RangePolicy.
   * </p>
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
   * @see AAxis#setMajorTickSpacing(double)
   */
  public abstract void setStartMajorTick(final boolean majorTick);

}
