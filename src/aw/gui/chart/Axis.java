/*
 *  Axis.java (bold as love), represents an axis  of the Chart2D.
 *  Copyright (C) Achim Westermann, created on 21.03.2005, 16:41:06
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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.LinkedList;

import aw.util.Range;
import aw.util.units.Unit;

/**
 * <p>
 * The representation of an axis of the <code>{@link Chart2D}</code>.
 * </p>
 * <p>
 * Normally - as the design and interaction of an <code>Axis</code> with the
 * <code>Chart2D</code> is very fine-grained - it is not instantiated by users
 * of jchart2d: It is automatically instantiated by the constructor of
 * <code>Chart2D</code>. It then may be retrieved from the
 * <code>Chart2D</code> by the methods {@link Chart2D#getAxisX()} and
 * {@link Chart2D#getAxisY()} for further configuration.
 * </p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.22 $
 */
public class Axis {

  /**
   * An internal connector class that will connect the IAxis to the a Chart2D.
   * It is aggregated to the IAxis in order to access either y values or x
   * values of the Chart2D thus making the IAxis an Y Axis or X axis. This
   * strategy reduces redundant code for label creation. It avoids complex
   * inheritance / interface implements for different IAxis implementation that
   * would be necessary for y-axis / x-axis implementations.
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public abstract class Chart2DDataAccessor {

    /** The chart that is acessed. */
    protected Chart2D m_chart;

    /**
     * A pluggable range policy.
     */
    protected IRangePolicy m_rangePolicy = new RangePolicyUnbounded(Range.RANGE_UNBOUNDED);

    /**
     * Constructor with the chart that is acessed.
     * <p>
     * 
     * @param chart
     *          the chart that is acessed.
     */
    protected Chart2DDataAccessor(final Chart2D chart) {

      Axis.this.setAccessor(this);
      this.m_chart = chart;
      this.m_rangePolicy.addPropertyChangeListener(AbstractRangePolicy.PROPERTY_RANGE, chart);
    }

    /**
     * 
     * @return the maximum value from the Chart2D's axis (X or Y) this instance
     *         is standing for with respect to the installed range policy.
     */
    protected abstract double getMax();

    /**
     * @return the maximum value from the Chart2D's "axis" (X or Y) this
     *         instance is standing for.
     * 
     */
    protected abstract double getMaxFromAxis();

    /**
     * Returns the maximum pixels that will be needed to paint a label.
     * <p>
     * 
     * @return the maximum pixels that will be needed to paint a label.
     */
    protected abstract double getMaximumPixelForLable();

    /**
     * @return the minimum value from the Chart2D's "axis" (X or Y) this
     *         instance is standing for with respect to the installed range
     *         policy.
     * 
     */
    protected abstract double getMin();

    /**
     * @return the minimum value from the Chart2D's "axis" (X or Y) this
     *         instance is standing for.
     * 
     */
    protected abstract double getMinFromAxis();

    /**
     * Returns the minimum amount of increase in the value that will be needed
     * to display all labels without overwriting each others.
     * <p>
     * 
     * This procedure needs the amount of pixels needed by the largest possible
     * label and relies on the implementation of
     * {@link #getMaximumPixelForLable()}, whose result is multiplied with the
     * "value per pixel" quantifier.
     * <p>
     * 
     * @return the minimum amount of increase in the value that will be needed
     *         to display all labels without overwriting each others.
     * 
     */
    protected abstract double getMinimumValueDistanceForLables();

    /**
     * @return Returns the rangePolicy.
     */
    public final IRangePolicy getRangePolicy() {

      return this.m_rangePolicy;
    }

    /**
     * <p>
     * Returns the value distance on the current chart that exists for the given
     * amount of pixel distance in the given direction of this <code>Axis</code>.
     * </p>
     * <p>
     * Depending on the width of the actual Chart2D and the contained values,
     * the relation between displayed distances (pixel) and value distances (the
     * values of the addes <code>{@link ITrace2D}</code> instances changes.
     * </p>
     * <p>
     * This method calculates depending on the actual painting area of the
     * Chart2D, the shift in value between two points that have a screen
     * distance of the given pixel. <br>
     * This method is not used by the chart itself but a helper for outside use.
     * </p>
     * 
     * @param pixel
     *          The desired distance between to scalepoints of the x- axis in
     *          pixel.
     * @return a scaled (from pixel to internal value-range) and normed (to the
     *         factor of the current unit of the axis) value usable to calculate
     *         the coords for the scalepoints of the axis.
     */
    protected abstract double getValueDistanceForPixel(int pixel);

    /**
     * <p>
     * Sets the RangePolicy.
     * </p>
     * <p>
     * If the given RangePolicy has an unconfigured internal Range (
     * {@link Range#RANGE_UNBOUNDED}) the old internal RangePolicy is taken
     * into account: <br>
     * If the old RangePolicy has a configured Range this is transferred to the
     * new RangePolicy.
     * </p>
     * 
     * @param rangePolicy
     *          The rangePolicy to set.
     */
    public void setRangePolicy(final IRangePolicy rangePolicy) {

      if (rangePolicy.getRange() == Range.RANGE_UNBOUNDED) {
        Range oldRange = this.m_rangePolicy.getRange();
        if (oldRange != Range.RANGE_UNBOUNDED) {
          rangePolicy.setRange(oldRange);
        }
      }
      // remove old property change listener:
      this.m_rangePolicy.removePropertyChangeListener(this.m_chart);
      this.m_rangePolicy = rangePolicy;
      this.m_rangePolicy
          .addPropertyChangeListener(AbstractRangePolicy.PROPERTY_RANGE, this.m_chart);
    }

    /**
     * Returns the chart that is accessed.
     * <p>
     * 
     * @return the chart that is accessed.
     */
    public final Chart2D getChart() {
      return this.m_chart;
    }
  }

  /**
   * 
   * An accessor for the x axis of a chart.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de>Achim Westermann </a>
   * 
   * @see Chart2D#getAxisX()
   */
  public final class XDataAccessor extends Axis.Chart2DDataAccessor {

    /**
     * Creates an instance that accesses the given chart's x axis.
     * <p>
     * 
     * @param chart
     *          the chart to access.
     */
    public XDataAccessor(final Chart2D chart) {

      super(chart);
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMax()
     */
    protected double getMax() {
      return this.m_rangePolicy.getMax(this.m_chart.getMinX(), this.m_chart.getMaxX());
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMaxFromAxis()
     */
    protected final double getMaxFromAxis() {

      return m_chart.getMaxX();
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMaximumPixelForLable()
     */
    protected double getMaximumPixelForLable() {

      FontMetrics fontdim = m_chart.getGraphics().getFontMetrics();
      int fontwidth = fontdim.charWidth('0');
      // multiply with longest possible number.
      // longest possible number is the non-fraction part of
      // the highest number plus the maximum amount of fraction digits
      // plus one for the fraction separator dot.

      int len = Axis.this.getFormatter().getMaxAmountChars();
      return fontwidth * (len + 2);
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMin()
     */
    protected final double getMin() {
      return this.m_rangePolicy.getMin(this.m_chart.getMinX(), this.m_chart.getMaxX());
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMinFromAxis()
     */
    protected final double getMinFromAxis() {

      return m_chart.getMinX();
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMinimumValueDistanceForLables()
     */
    protected final double getMinimumValueDistanceForLables() {

      Dimension d = this.m_chart.getSize();
      int pxrange = (int) d.getWidth() - 60;
      if (pxrange <= 0) {
        return 1;
      }
      double valuerange = Axis.this.getMax() - Axis.this.getMin();
      if (valuerange == 0) {
        valuerange = 10;
      }
      double pxToValue = valuerange / pxrange;
      double ret = pxToValue * this.getMaximumPixelForLable();
      return ret;
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getValueDistanceForPixel(int)
     */
    protected double getValueDistanceForPixel(final int pixel) {
      Dimension d = this.m_chart.getSize();
      int pxrangex = (int) d.getWidth() - 60;
      if (pxrangex <= 0) {
        return -1d;
      }
      double valuerangex = this.getMax() - this.getMin();
      double pxToValue = valuerangex / pxrangex;
      double ret = pxToValue * pixel;
      return ret;
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#setRangePolicy(aw.gui.chart.IRangePolicy)
     */
    public void setRangePolicy(final IRangePolicy rangePolicy) {
      double xmax = this.getMax();
      double xmin = this.getMin();
      super.setRangePolicy(rangePolicy);
      // check for scaling changes:
      if (xmax != this.getMax() || xmin != this.getMin()) {
        this.m_chart.scaleAll(Chart2D.X);
      }
    }

    /**
     * Returns "X".
     * <p>
     * 
     * @return "X"
     */
    public String toString() {
      return "X";
    }
  }

  /**
   * Accesses the y axis of the {@link Chart2D}.
   * <p>
   * 
   * @see Axis#setAccessor(Axis.Chart2DDataAccessor)
   * 
   * @see Chart2D#getAxisY()
   */

  public final class YDataAccessor extends Axis.Chart2DDataAccessor {

    /**
     * Creates an instance that accesses the y axis of the given chart.
     * <p>
     * 
     * @param chart
     *          the chart to access.
     */
    public YDataAccessor(final Chart2D chart) {

      super(chart);
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMax()
     */
    protected final double getMax() {
      return this.m_rangePolicy.getMax(this.m_chart.getMinY(), this.m_chart.getMaxY());
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMaxFromAxis()
     */
    protected final double getMaxFromAxis() {

      return m_chart.getMaxY();
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMaximumPixelForLable()
     */
    protected double getMaximumPixelForLable() {

      FontMetrics fontdim = m_chart.getGraphics().getFontMetrics();
      int fontheight = fontdim.getHeight();
      return fontheight + 10;
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMin()
     */
    protected final double getMin() {
      return this.m_rangePolicy.getMin(this.m_chart.getMinY(), this.m_chart.getMaxY());
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMinFromAxis()
     */
    protected final double getMinFromAxis() {

      return m_chart.getMinY();
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getMinimumValueDistanceForLables()
     */
    protected final double getMinimumValueDistanceForLables() {

      Dimension d = this.m_chart.getSize();
      int pxrange = (int) d.getHeight() - 40;
      if (pxrange <= 0) {
        return 1;
      }
      double valuerange = Axis.this.getMax() - Axis.this.getMin();
      if (valuerange == 0) {
        valuerange = 10;
      }
      double pxToValue = valuerange / pxrange;
      double ret = pxToValue * this.getMaximumPixelForLable();
      return ret;
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#getValueDistanceForPixel(int)
     */
    protected double getValueDistanceForPixel(final int pixel) {
      Dimension d = this.m_chart.getSize();
      int pxrangey = (int) d.getHeight() - 40;
      if (pxrangey <= 0) {
        return -1d;
      }
      double valuerangey = this.getMaxFromAxis() - this.getMinFromAxis();
      double pxToValue = valuerangey / pxrangey;
      double ret = pxToValue * pixel;
      return ret;
    }

    /**
     * @see aw.gui.chart.Axis.Chart2DDataAccessor#setRangePolicy(aw.gui.chart.IRangePolicy)
     */
    public void setRangePolicy(final IRangePolicy rangePolicy) {
      double ymax = this.getMax();
      double ymin = this.getMin();
      super.setRangePolicy(rangePolicy);
      // check for scaling changes:
      if (ymax != this.getMax() || ymin != this.getMin()) {
        this.m_chart.scaleAll(Chart2D.Y);
      }
    }

    /**
     * Returns "Y".
     * <p>
     * 
     * @return "Y"
     */
    public String toString() {
      return "Y";
    }
  }

  /** Debugging flag for sysouts. */
  private static final boolean DEBUG = false;

  /**
   * The accessor to the Chart2D.
   * <p>
   * 
   * It determines, which axis (x or y) this instance is representing.
   * <p>
   */
  protected Chart2DDataAccessor m_accessor;

  /**
   * Formatting of the labels.
   */
  protected AbstractLabelFormatter m_formatter;

  /**
   * The amount of fraction digits to display.
   * <p>
   * 
   * This value affects the internal rounding for label creation. If set to zero
   * rounding will round to whole numbers, if set to 1 rounding will stick to
   * values with one fraction digit,...
   * <p>
   * 
   * @deprecated this feature has been replaced by label formatters (
   *             {@link #setFormatter(AbstractLabelFormatter)}) and will vanish
   *             in the next release.
   */
  protected int m_fractionsDigits = 1;

  /**
   * The major tick spacing for label generations.
   * <p>
   * 
   * @see #setMajorTickSpacing(double)
   */

  protected double m_majorTickSpacing = 5;

  /**
   * The minor tick spacing for label generations.
   * <p>
   * 
   * @see #setMinorTickSpacing(double)
   */
  protected double m_minorTickSpacing = 1;

  /** Internally used for rouding to ticks, calculated once per paint iteration. */
  protected double m_power;

  /**
   * Controls wether scale values are started from major ticks.
   * <p>
   * 
   * Default is false.
   * <p>
   */
  private boolean m_startMajorTick = false;

  /**
   * Default constructor.
   * <p>
   * 
   */
  public Axis() {
    this.setFormatter(new LabelFormatterAutoUnits(new LabelFormatterSimple()));
  }

  /**
   * @return Returns the formatter.
   */
  public final ILabelFormatter getFormatter() {

    return this.m_formatter;
  }

  /**
   * @return Returns the fractionsDigits.
   */
  public int getFractionsDigits() {

    return this.m_fractionsDigits;
  }

  /**
   * @see Axis#getLabels(double)
   */
  protected LabeledValue[] getLabels(final double resolution) {
    // triggers chooseUnit()
    if (resolution <= 0) {
      return new LabeledValue[] {};
    }
    LinkedList collect = new LinkedList();
    double min = this.getMin();
    double max = this.getMax();
    String oldLabelName = "";
    LabeledValue label;
    double range = max - min;
    LabeledValue[] ret = null;
    double value = min;
    String labelName = "start";
    int loopStop = 0;
    boolean firstMajorFound = false;
    // first tick, manual init
    while (value <= max && loopStop < 100) {
      if (loopStop == 99) {
        if (Axis.DEBUG) {
          System.out.println(this.m_accessor.toString() + " axis: loop to high");
        }
      }
      if (oldLabelName.equals(labelName)) {
        if (Axis.DEBUG) {
          System.out.println("constant Label");
        }
      }
      label = this.roundToTicks(value, false, !firstMajorFound && this.m_startMajorTick);

      oldLabelName = labelName;
      labelName = label.m_label;
      value = label.m_value;

      loopStop++;
      if (firstMajorFound || !this.m_startMajorTick || label.isMajorTick()) {
        firstMajorFound = true;
        if (value <= max) {
          collect.add(label);
        } else {
          // System.out.println("Dropping label (too high) : (" + label + ")
          // [max: " + max + "]");
        }
      }
      value += resolution;
    }
    int stop = collect.size();

    ret = new LabeledValue[stop];
    for (int i = 0; i < stop; i++) {
      label = (LabeledValue) collect.get(i);
      label.m_value = (label.m_value - min) / range;
      ret[i] = label;
    }
    return ret;
  }

  /**
   * Get the major tick spacing for label generation.
   * <p>
   * 
   * @see #setMajorTickSpacing(double)
   * 
   */

  public double getMajorTickSpacing() {
    return this.m_majorTickSpacing;
  }

  /**
   * The maximum value access method for the Axis this instance is aggregated
   * to. It supports the retrieval of data from the corrcet dimension of the
   * connected Chart2 (X or Y) as well as the respect to the configured
   * {@link IRangePolicy}.
   * <p>
   * 
   * @return the maximum value access method for the Axis this instance is
   *         aggregated to.
   */
  double getMax() {
    return this.m_accessor.getMax();
  }

  /**
   * <p>
   * Returns the minimum value access method for the Axis this instance is
   * aggregated to.
   * </p>
   * <p>
   * It supports the retrieval of data from the corrcet dimension of the
   * connected Chart2 (X or Y) as well as the respect to the configured
   * {@link IRangePolicy}.
   * </p>
   * 
   * @return the minimum value access method for the Axis this instance is
   *         aggregated to.
   */
  double getMin() {

    return this.m_accessor.getMin();
  }

  /**
   * Get the minor tick spacing for label generation.
   * <p>
   * 
   * @return he minor tick spacing for label generation.
   * 
   * @see #setMinorTickSpacing(double)
   * 
   */
  public double getMinorTickSpacing() {
    return this.m_minorTickSpacing;
  }

  /**
   * <p>
   * This method is used by the Chart2D to scale it's values during painting.
   * </p>
   * <p>
   * Caution: This method does not necessarily return the Range configured with
   * {@link #setRange(Range)}. The internal {@link IRangePolicy} is taken into
   * account.
   * </p>
   * 
   * @return the range corresponding to the upper and lower bound of the values
   *         that will be displayable on this Axis of the Chart2D.
   * 
   * @see #setRangePolicy(IRangePolicy)
   * @see Chart2DDataAccessor#getRange()
   * 
   */
  public final Range getRange() {

    return new Range(this.getMin(), this.getMax());
  }

  /**
   * See!
   * 
   * @see Chart2DDataAccessor#getRangePolicy()
   * 
   */
  public final IRangePolicy getRangePolicy() {

    return this.m_accessor.getRangePolicy();
  }

  /**
   * Returns the array of labeled values that will be used by the
   * {@link Chart2D} to paint labels.
   * <p>
   * 
   * @return the array of labeled values that will be used by the
   *         {@link Chart2D} to paint labels.
   */
  protected LabeledValue[] getScaleValues() {
    double labelspacepx = this.m_accessor.getMinimumValueDistanceForLables();
    double formattingspace = this.m_formatter.getMinimumValueShiftForChange();
    double max = Math.max(labelspacepx, formattingspace);
    return this.getLabels(max);
  }

  /**
   * Returns {@link AbstractLabelFormatter#UNIT_UNCHANGED} always.
   * <p>
   * 
   * 
   * @return {@link AbstractLabelFormatter#UNIT_UNCHANGED} always.
   * 
   * @deprecated use {@link #getFormatter()} and on this instance
   *             {@link ILabelFormatter#getUnit()} on that instance.
   */
  public Unit getUnit() {
    return this.getFormatter().getUnit();
  }

  /**
   * <p>
   * Returns the value distance on the current chart that exists for the given
   * amount of pixel distance in the given direction of this <code>Axis</code>.
   * </p>
   * <p>
   * Depending on the width of the actual Chart2D and the contained values, the
   * relation between displayed distances (pixel) and value distances (the
   * values of the addes <code>{@link ITrace2D}</code> instances changes.
   * </p>
   * <p>
   * This method calculates depending on the actual painting area of the
   * Chart2D, the shift in value between two points that have a screen distance
   * of the given pixel. <br>
   * This method is not used by the chart itself but a helper for outside use.
   * </p>
   * 
   * @param pixel
   *          The desired distance between to scalepoints of the x- axis in
   *          pixel.
   * @return a scaled (from pixel to internal value-range) and normed (to the
   *         factor of the current unit of the axis) value usable to calculate
   *         the coords for the scalepoints of the axis.
   */
  protected final double getValueDistanceForPixel(final int pixel) {
    return this.m_accessor.getValueDistanceForPixel(pixel);
  }

  /**
   * Perfomres expensive calculations for various values that are used by many
   * calls throughout a paint iterations.
   * <p>
   * 
   * These values are constant throughout a paint iteration by the contract that
   * no point is added removed or changed in this period. Because these values
   * are used from many methods it is impossible to calculate them at a
   * "transparent" method. The first method called in a paint iteration is
   * called several further times in the iteration.
   * <p>
   * 
   */
  protected void initPaintIteration() {
    // get the powers of ten of the range, a minor Tick of 1.0 has to be
    // able to
    // be 100 times in a range of 100
    // (match 1,2,3,... instead of 10,20,30,....
    double fakeRange = this.m_accessor.getMax() - this.m_accessor.getMin();
    if (fakeRange == 0 || fakeRange == Double.NEGATIVE_INFINITY
        || fakeRange == Double.POSITIVE_INFINITY) {
      fakeRange = 1;
    }
    double tmpPower = 0;
    if (fakeRange > 1) {
      while (fakeRange > 10) {
        fakeRange /= 10;
        tmpPower++;
      }
      tmpPower = Math.pow(10, tmpPower - 1);
    } else {
      while (fakeRange < 1) {
        fakeRange *= 10;
        tmpPower++;
      }

      tmpPower = 1 / Math.pow(10, tmpPower);
    }
    this.m_power = tmpPower;

    // this.m_formatter.initPaintIteration();
  }

  /**
   * Check wether scale values are started from major ticks.
   * <p>
   * 
   * @return true if scale values start from major ticks.
   * 
   * @see Axis#setMajorTickSpacing(double)
   */
  public boolean isStartMajorTick() {
    return this.m_startMajorTick;
  }

  /**
   * Internal rounding routine.
   * <p>
   * 
   * Arguments are not chosen to be "understandable" or "usable" but optimized
   * for performance.
   * <p>
   * 
   * The <code> findMajorTick</code> argument may be used e.g. to force labels
   * to start from a major tick.
   * <p>
   * 
   * @param value
   *          the value to round.
   * 
   * @param floor
   *          if true, rounding goes to floor else to ceiling.
   * 
   * @param findMajorTick
   *          if true the returned value will be a major tick (which might be
   *          fare more away from the given value than the next major tick).
   * 
   * @return the value rounded to minor or major ticks.
   */
  protected LabeledValue roundToTicks(final double value, final boolean floor,
      final boolean findMajorTick) {
    LabeledValue ret = new LabeledValue();

    double minorTick = this.m_minorTickSpacing * this.m_power;
    double majorTick = this.m_majorTickSpacing * this.m_power;

    double majorRound;

    if (floor) {
      majorRound = Math.floor(value / majorTick);
    } else {
      majorRound = Math.ceil(value / majorTick);
    }
    boolean majorZeroHit = majorRound == 0 && value != 0;
    majorRound *= majorTick;
    double minorRound;
    if (floor) {
      minorRound = Math.floor(value / minorTick);
    } else {
      minorRound = Math.ceil(value / minorTick);
    }
    boolean minorZeroHit = minorRound == 0 && value != 0;
    minorRound *= minorTick;
    if (majorZeroHit || minorZeroHit) {
      if (Axis.DEBUG) {
        System.out.println("zeroHit");
      }
    }

    double minorDistance = Math.abs(value - minorRound);
    double majorDistance = Math.abs(value - majorRound);

    double majorMinorRelation = minorDistance / majorDistance;
    if (majorMinorRelation == Double.NaN) {
      majorMinorRelation = 1.0;
    }

    if (majorDistance <= minorDistance || findMajorTick) {
      ret.m_value = majorRound;
      ret.m_isMajorTick = true;
    } else {
      ret.m_value = minorRound;
      ret.m_isMajorTick = false;
    }

    // format label string.
    ret.m_label = this.getFormatter().format(ret.m_value);
    // as formatting rounds too, reparse value so that it is exactly at the
    // point the
    // label string describes.
    ret.m_value = this.getFormatter().parse(ret.m_label).doubleValue();
    return ret;
  }

  /**
   * Sets the accessor to the axis of the chart.
   * <p>
   * 
   * @param accessor
   *          the accessor to the axis of the chart.
   */
  protected final void setAccessor(final Chart2DDataAccessor accessor) {

    this.m_accessor = accessor;
  }

  /**
   * Sets the formatter to use for labels.
   * <p>
   * 
   * If the formatter is of type <code>{@link LabelFormatterNumber}</code>
   * it's maximum fraction digits is set to
   * <code>{@link #setFractionsDigits(int)}</code> to avoid strange choice of
   * minimum and maximum bounds (e.g. maximum label would not be rounded to a
   * whole number). This is a helper but no clean coding (depends on
   * implementation).
   * <p>
   * 
   * @param formatter
   *          The formatter to set.
   */
  public void setFormatter(final AbstractLabelFormatter formatter) {

    this.m_formatter = formatter;
    this.m_formatter.setAxis(this);
    if (this.m_formatter instanceof LabelFormatterNumber) {
      this.setFractionsDigits(((LabelFormatterNumber) this.m_formatter).getNumberFormat()
          .getMaximumFractionDigits());
    }
  }

  /**
   * Sets the unused fraction digits.
   * <p>
   * 
   * @param fractions
   *          The fractions to set.
   * 
   * @deprecated this feature has been replaced by label formatters (
   *             {@link #setFormatter(AbstractLabelFormatter)}) and will vanish
   *             in the next release.
   */
  public void setFractionsDigits(final int fractions) {

    this.m_fractionsDigits = fractions;
  }

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
  public void setMajorTickSpacing(final double majorTickSpacing) {
    this.m_majorTickSpacing = majorTickSpacing;
  }

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
  public void setMinorTickSpacing(final double minorTickSpacing) {
    this.m_minorTickSpacing = minorTickSpacing;
  }

  /**
   * <p>
   * Sets a Range to use for filtering the view to the the connected Axis. Note
   * that it's effect will be affected by the internal {@link IRangePolicy}.
   * </p>
   * <p>
   * To get full control use: <br>
   * <code> setRangePolicy(new &lt;AnAbstractRangePolicy&gt;(range);</code>
   * </p>
   * 
   * @param range
   *          Range to use for filtering the view to the the connected Axis.
   * 
   * @see #getRangePolicy()
   * 
   * @see IRangePolicy#setRange(Range)
   */
  public final void setRange(final Range range) {

    this.m_accessor.getRangePolicy().setRange(range);
  }

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
  public final void setRangePolicy(final IRangePolicy rangePolicy) {

    this.m_accessor.setRangePolicy(rangePolicy);
  }

  /**
   * Set wether scale values are started from major ticks.
   * <p>
   * 
   * @param majorTick
   *          true if scale values shall start with a major tick.
   * 
   * @see Axis#setMajorTickSpacing(double)
   */
  public void setStartMajorTick(final boolean majorTick) {
    this.m_startMajorTick = majorTick;
  }

  /**
   * Returns the accessor to the chart.
   * <p>
   * 
   * @return the accessor to the chart.
   */
  public Chart2DDataAccessor getAccessor() {
    return this.m_accessor;
  }
}
