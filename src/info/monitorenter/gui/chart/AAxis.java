/*
 *  AAxis.java (bold as love), base class for an axis  of the Chart2D.
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

package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.labelformatters.ALabelFormatter;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterAutoUnits;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterSimple;
import info.monitorenter.gui.chart.rangepolicies.ARangePolicy;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyUnbounded;
import info.monitorenter.util.Range;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

/**
 * The base class for an axis of the <code>{@link Chart2D}</code>.
 * <p>
 * Normally - as the design and interaction of an <code>Axis</code> with the
 * <code>Chart2D</code> is very fine-grained - it is not instantiated by users
 * of jchart2d: It is automatically instantiated by the constructor of
 * <code>Chart2D</code>. It then may be retrieved from the
 * <code>Chart2D</code> by the methods {@link Chart2D#getAxisX()} and
 * {@link Chart2D#getAxisY()} for further configuration.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.12 $
 */
public abstract class AAxis implements IAxis {
  /**
   * An internal connector class that will connect the axis to the a Chart2D.
   * <p>
   * It is aggregated to the {@link AAxis} in order to access either y values or
   * x values of the Chart2D thus making the IAxis an Y Axis or X axis. This
   * strategy reduces redundant code for label creation. It avoids complex
   * inheritance / interface implements for different IAxis implementation that
   * would be necessary for y-axis / x-axis implementations.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public abstract class AChart2DDataAccessor {

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
    protected AChart2DDataAccessor(final Chart2D chart) {

      AAxis.this.setAccessor(this);
      this.m_chart = chart;
      this.m_rangePolicy.addPropertyChangeListener(ARangePolicy.PROPERTY_RANGE, chart);
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

    /**
     * Returns the constant for the dimension that is accessed on the chart.
     * <p>
     * 
     * @return {@link Chart2D#X}, {@link Chart2D#Y} or -1 if this axis is not
     *         assigned to a chart.
     */
    public abstract int getDimension();

    /**
     * Returns the maximum value from the Chart2D's axis (X or Y) this instance
     * is standing for with respect to the installed range policy.
     * <p>
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
     * Returns the amount of pixel avalable for displaying the values on the
     * chart in the dimension this accessor stands for.
     * <p>
     * This method must not be called within the first lines of a paint cycle
     * (neccessary underlying values then are computed new).
     * <p>
     * 
     * @return the amount of pixel avalable for displaying the values on the
     *         chart in the dimension this accessor stands for.
     */
    protected abstract int getPixelRange();

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
     * Sets the RangePolicy.
     * <p>
     * A new Range with minimum and maxium of the chart is set to it to ensure
     * that after the change all traces are shown.
     * <p>
     * 
     * @param rangePolicy
     *          The rangePolicy to set.
     */
    public void setRangePolicy(final IRangePolicy rangePolicy) {

      // initially configure the range to show all data (in case a fixed
      // viewport rp is used):
      rangePolicy.setRange(new Range(this.getMinFromAxis(), this.getMaxFromAxis()));
      this.m_rangePolicy.removePropertyChangeListener(this.m_chart);
      this.m_rangePolicy = rangePolicy;
      this.m_rangePolicy.addPropertyChangeListener(ARangePolicy.PROPERTY_RANGE, this.m_chart);
      this.m_rangePolicy.addPropertyChangeListener(ARangePolicy.PROPERTY_RANGE_MAX, this.m_chart);
      this.m_rangePolicy.addPropertyChangeListener(ARangePolicy.PROPERTY_RANGE_MIN, this.m_chart);
    }

    /**
     * Returns the translation of the mouse event coordinates of the given mouse
     * event to the value within the chart for the dimension (x,y) covered by
     * this axis.
     * <p>
     * Note that the mouse event has to be an event fired on this component!
     * <p>
     * 
     * @param mouseEvent
     *          a mouse event that has been fired on this component.
     * @return the translation of the mouse event coordinates of the given mouse
     *         event to the value within the chart for the dimension covered by
     *         this axis (x or y) or null if no calculations could be performed
     *         as the chart was not painted before.
     */
    public abstract double translateMousePosition(final MouseEvent mouseEvent);
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
  public final class XDataAccessor
      extends AAxis.AChart2DDataAccessor {

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
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getDimension()
     */
    public int getDimension() {
      return Chart2D.X;
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMax()
     */
    protected double getMax() {
      return this.m_rangePolicy.getMax(this.m_chart.getMinX(), this.m_chart.getMaxX());
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMaxFromAxis()
     */
    protected final double getMaxFromAxis() {

      return m_chart.getMaxX();
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMaximumPixelForLable()
     */
    protected double getMaximumPixelForLable() {

      FontMetrics fontdim = m_chart.getGraphics().getFontMetrics();
      int fontwidth = fontdim.charWidth('0');
      // multiply with longest possible number.
      // longest possible number is the non-fraction part of
      // the highest number plus the maximum amount of fraction digits
      // plus one for the fraction separator dot.

      int len = AAxis.this.getFormatter().getMaxAmountChars();
      return fontwidth * (len + 2);
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMin()
     */
    protected final double getMin() {
      return this.m_rangePolicy.getMin(this.m_chart.getMinX(), this.m_chart.getMaxX());
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMinFromAxis()
     */
    protected final double getMinFromAxis() {

      return m_chart.getMinX();
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMinimumValueDistanceForLables()
     */
    protected final double getMinimumValueDistanceForLables() {

      Dimension d = this.m_chart.getSize();
      int pxrange = (int) d.getWidth() - 60;
      if (pxrange <= 0) {
        return 1;
      }
      double valuerange = AAxis.this.getMax() - AAxis.this.getMin();
      if (valuerange == 0) {
        valuerange = 10;
      }
      double pxToValue = valuerange / pxrange;
      double ret = pxToValue * this.getMaximumPixelForLable();
      return ret;
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getPixelRange()
     */
    protected int getPixelRange() {
      return this.m_chart.getXChartEnd() - this.m_chart.getXChartStart();
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getValueDistanceForPixel(int)
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
     * @see AAxis.AChart2DDataAccessor#setRangePolicy(info.monitorenter.gui.chart.IRangePolicy)
     */
    public void setRangePolicy(final IRangePolicy rangePolicy) {

      double xmax = this.getMax();
      double xmin = this.getMin();
      super.setRangePolicy(rangePolicy);
      // check for scaling changes:
      if (xmax != this.getMax() || xmin != this.getMin()) {
        this.m_chart.propertyChange(new PropertyChangeEvent(rangePolicy,
            ARangePolicy.PROPERTY_RANGE, new Range(xmin, xmax), this.m_rangePolicy.getRange()));
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

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#translateMousePosition(java.awt.event.MouseEvent)
     */
    public double translateMousePosition(final MouseEvent mouseEvent) {
      double result = 0;
      // relate to the offset:
      double mouseX = mouseEvent.getX() - this.m_chart.getXChartStart();

      int rangeX = this.m_chart.getXChartEnd() - this.m_chart.getXChartStart();
      if (rangeX == 0) {
        // return null
      } else {
        double scaledX = mouseX / (double) rangeX;
        Range valueRangeX = AAxis.this.getRange();
        result = scaledX * valueRangeX.getExtent() + valueRangeX.getMin();
      }
      return result;
    }
  }

  /**
   * Accesses the y axis of the {@link Chart2D}.
   * <p>
   * 
   * @see AAxis#setAccessor(AChart2DDataAccessor)
   * 
   * @see Chart2D#getAxisY()
   */

  public final class YDataAccessor
      extends AAxis.AChart2DDataAccessor {
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
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getDimension()
     */
    public int getDimension() {
      return Chart2D.Y;
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMax()
     */
    protected final double getMax() {
      return this.m_rangePolicy.getMax(this.m_chart.getMinY(), this.m_chart.getMaxY());
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMaxFromAxis()
     */
    protected final double getMaxFromAxis() {

      return m_chart.getMaxY();
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMaximumPixelForLable()
     */
    protected double getMaximumPixelForLable() {

      FontMetrics fontdim = m_chart.getGraphics().getFontMetrics();
      int fontheight = fontdim.getHeight();
      return fontheight + 10;
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMin()
     */
    protected final double getMin() {
      return this.m_rangePolicy.getMin(this.m_chart.getMinY(), this.m_chart.getMaxY());
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMinFromAxis()
     */
    protected final double getMinFromAxis() {

      return m_chart.getMinY();
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getMinimumValueDistanceForLables()
     */
    protected final double getMinimumValueDistanceForLables() {

      Dimension d = this.m_chart.getSize();
      int pxrange = (int) d.getHeight() - 40;
      if (pxrange <= 0) {
        return 1;
      }
      double valuerange = AAxis.this.getMax() - AAxis.this.getMin();
      if (valuerange == 0) {
        valuerange = 10;
      }
      double pxToValue = valuerange / pxrange;
      double ret = pxToValue * this.getMaximumPixelForLable();
      return ret;
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getPixelRange()
     */
    protected int getPixelRange() {
      return this.m_chart.getYChartStart() - this.m_chart.getYChartEnd();
    }

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#getValueDistanceForPixel(int)
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
     * @see AAxis.AChart2DDataAccessor#setRangePolicy(IRangePolicy)
     */
    public void setRangePolicy(final IRangePolicy rangePolicy) {
      double ymax = this.getMax();
      double ymin = this.getMin();
      super.setRangePolicy(rangePolicy);
      // check for scaling changes:
      if (ymax != this.getMax() || ymin != this.getMin()) {
        this.m_chart.propertyChange(new PropertyChangeEvent(rangePolicy,
            ARangePolicy.PROPERTY_RANGE, new Range(ymin, ymax), this.m_rangePolicy.getRange()));
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

    /**
     * @see info.monitorenter.gui.chart.AAxis.AChart2DDataAccessor#translateMousePosition(java.awt.event.MouseEvent)
     */
    public double translateMousePosition(final MouseEvent mouseEvent) {
      double result = 0;
      // relate to the offset:
      double mouseY = this.m_chart.getYChartStart() - mouseEvent.getY();

      int rangeY = this.m_chart.getYChartStart() - this.m_chart.getYChartEnd();
      if (rangeY == 0) {
        // return null
      } else {
        double scaledY = mouseY / (double) rangeY;
        Range valueRangeY = AAxis.this.getRange();
        result = scaledY * valueRangeY.getExtent() + valueRangeY.getMin();
      }
      return result;
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
  protected AChart2DDataAccessor m_accessor;

  /**
   * Formatting of the labels.
   */
  protected ILabelFormatter m_formatter;

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

  /** Boolean switch for painting x gridlines. * */
  private boolean m_paintGrid = false;

  /** Boolean switch for painting the scale in this dimension. */
  private boolean m_paintScale = true;

  /** Internally used for rouding to ticks, calculated once per paint iteration. */
  protected double m_power;

  /** Support for acting as a property change event producer for listeners. */
  private PropertyChangeSupport m_propertyChangeSupport;

  /**
   * Controls wether scale values are started from major ticks.
   * <p>
   * 
   * Default is false.
   * <p>
   */
  private boolean m_startMajorTick = false;

  /**
   * Default constructor that uses a {@link LabelFormatterAutoUnits} for
   * formatting labels.
   * <p>
   * 
   */
  public AAxis() {
    this(new LabelFormatterAutoUnits(new LabelFormatterSimple()));
  }

  /**
   * Constructor that uses the given label formatter for formatting labels.
   * <p>
   * 
   */
  public AAxis(final ALabelFormatter formatter) {
    this.setFormatter(formatter);
    this.m_propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * @see info.monitorenter.gui.chart.IAxis#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Returns the accessor to the chart.
   * <p>
   * 
   * @return the accessor to the chart.
   */
  public AChart2DDataAccessor getAccessor() {
    return this.m_accessor;
  }

  /**
   * @see info.monitorenter.gui.chart.IAxis#getDimension()
   */
  public int getDimension() {
    int result = -1;
    if (this.m_accessor != null) {
      result = this.m_accessor.getDimension();
    }
    return result;
  }

  /**
   * @return Returns the formatter.
   */
  public final ILabelFormatter getFormatter() {

    return this.m_formatter;
  }

  /**
   * @see AAxis#getLabels(double)
   */
  protected LabeledValue[] getLabels(final double resolution) {
    if (resolution <= 0) {
      return new LabeledValue[] {};
    }
    LinkedList collect = new LinkedList();
    Range domain = this.getRange();
    double min = domain.getMin();
    double max = domain.getMax();
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
        if (AAxis.DEBUG) {
          System.out.println(this.m_accessor.toString() + " axis: loop to high");
        }
      }
      if (oldLabelName.equals(labelName)) {
        if (AAxis.DEBUG) {
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
   * to.
   * <p>
   * It supports the retrieval of data from the corrcet dimension of the
   * connected Chart2 (X or Y) as well as the respect to the configured
   * {@link IRangePolicy}.
   * <p>
   * 
   * @return the maximum value access method for the Axis this instance is
   *         aggregated to.
   */
  public double getMax() {
    return this.m_accessor.getMax();
  }

  /**
   * Returns the minimum value access method for the Axis this instance is
   * aggregated to.
   * <p>
   * It supports the retrieval of data from the corrcet dimension of the
   * connected Chart2 (X or Y) as well as the respect to the configured
   * {@link IRangePolicy}.
   * <p>
   * 
   * @return the minimum value access method for the Axis this instance is
   *         aggregated to.
   */
  public double getMin() {

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
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
    return this.m_propertyChangeSupport.getPropertyChangeListeners();
  }

  /**
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String propertyName) {
    return this.m_propertyChangeSupport.getPropertyChangeListeners(propertyName);
  }

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
   * @see Chart2DDataAccessor#getRange()
   * 
   */
  public final Range getRange() {
    Range result;
    double min = this.getMin();
    double max = this.getMax();
    if (min == max) {
      max += 10;
    }
    result = new Range(min, max);
    return result;
  }

  /**
   * See!
   * <p>
   * 
   * @see Chart2DDataAccessor#getRangePolicy()
   * 
   */
  public IRangePolicy getRangePolicy() {

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
   * "transparent" method that may perform this caching over a paint period
   * without knowledge from outside. The first method called in a paint
   * iteration is called several further times in the iteration. So this is the
   * common hook to invoke before painting a chart.
   * <p>
   * 
   */
  protected void initPaintIteration() {

    // get the powers of ten of the range, a minor Tick of 1.0 has to be
    // able to
    // be 100 times in a range of 100
    // (match 1,2,3,... instead of 10,20,30,....
    double range = this.getMax() - this.getMin();
    if (range == 0 || range == Double.NEGATIVE_INFINITY || range == Double.POSITIVE_INFINITY) {
      range = 1;
    }
    double tmpPower = 0;
    if (range > 1) {
      while (range > 10) {
        range /= 10;
        tmpPower++;
      }
      tmpPower = Math.pow(10, tmpPower - 1);
    } else {
      while (range < 1) {
        range *= 10;
        tmpPower++;
      }

      tmpPower = 1 / Math.pow(10, tmpPower);
    }
    this.m_power = tmpPower;

    // This is needed e.g. for LabelFormatterAutoUnits to choose the unit
    // according to the actual range of this paint iteration.
    this.m_formatter.initPaintIteration();
  }

  /**
   * Returns wether the x grid is painted or not.
   * <p>
   * 
   * @return wether the x grid is painted or not.
   */
  public final boolean isPaintGrid() {
    return this.m_paintGrid;
  }

  /**
   * Returns whether the scale for this axis should be painted or not.
   * <p>
   * 
   * @return whether the scale for this axis should be painted or not.
   */
  public final boolean isPaintScale() {
    return this.m_paintScale;
  }

  /**
   * Check wether scale values are started from major ticks.
   * <p>
   * 
   * @return true if scale values start from major ticks.
   * 
   * @see AAxis#setMajorTickSpacing(double)
   */
  public boolean isStartMajorTick() {
    return this.m_startMajorTick;
  }

  /**
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * @see info.monitorenter.gui.chart.IAxis#replace(info.monitorenter.gui.chart.IAxis)
   */
  public void replace(final IAxis axis) {
    if (axis != null) {
      this.setAccessor(axis.getAccessor());
      this.setFormatter(axis.getFormatter());
      this.setMajorTickSpacing(axis.getMajorTickSpacing());
      this.setMinorTickSpacing(axis.getMinorTickSpacing());
      this.setPaintGrid(axis.isPaintGrid());
      this.setPaintScale(axis.isPaintScale());
      // This must not be done! See javadoc: it delegates to the accessor which
      // already has been moved.
      // axisX.setRange(old.getRange());
      // axisX.setRangePolicy(old.getRangePolicy());
      this.setStartMajorTick(axis.isStartMajorTick());

      // No go for listeners:
      // TODO: keep in track with evolving IAxis properties!
      PropertyChangeListener[] propertyChangeListeners = axis
          .getPropertyChangeListeners(IAxis.PROPERTY_PAINTGRID);
      for (int i = propertyChangeListeners.length - 1; i >= 0; i--) {
        axis.removePropertyChangeListener(propertyChangeListeners[i]);
        this.addPropertyChangeListener(IAxis.PROPERTY_PAINTGRID, propertyChangeListeners[i]);
      }
      propertyChangeListeners = axis.getPropertyChangeListeners(IAxis.PROPERTY_RANGEPOLICY);
      for (int i = propertyChangeListeners.length - 1; i >= 0; i--) {
        axis.removePropertyChangeListener(propertyChangeListeners[i]);
        this.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, propertyChangeListeners[i]);
      }
    }
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
      if (AAxis.DEBUG) {
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
  protected final void setAccessor(final AChart2DDataAccessor accessor) {

    this.m_accessor = accessor;
  }

  /**
   * Sets the formatter to use for labels.
   * <p>
   * 
   * @param formatter
   *          The formatter to set.
   */
  public void setFormatter(final ILabelFormatter formatter) {

    this.m_formatter = formatter;
    this.m_formatter.setAxis(this);
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
   * Set wether the grid in this dimension should be painted or not.
   * <p>
   * A repaint operation for the chart is triggered.
   * <p>
   * 
   * @param grid
   *          true if the grid should be painted or false if not.
   */
  public final void setPaintGrid(final boolean grid) {
    boolean oldValue = this.m_paintGrid;
    this.m_paintGrid = grid;
    if (oldValue != grid) {

      Chart2D chart2D = this.getAccessor().getChart();
      if (grid) {
        // TODO: this is hardcoded behaviour that is not explained!
        this.setPaintScale(true);
      }
      this.m_propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(this,
          IAxis.PROPERTY_PAINTGRID, new Boolean(oldValue), new Boolean(this.m_paintGrid)));
      chart2D.repaint(200);
    }
  }

  /**
   * Set if the scale for this axis should be shown.
   * <p>
   * 
   * @param show
   *          true if the scale on this axis should be shown, false else.
   */
  public final void setPaintScale(final boolean show) {
    this.m_paintScale = show;
  }

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
   * A property change event for {@link IAxis#PROPERTY_RANGEPOLICY} is fired and
   * receives listeners if a change took place.
   * <p>
   * 
   * @param rangePolicy
   *          The rangePolicy to set.
   */
  public void setRangePolicy(final IRangePolicy rangePolicy) {

    IRangePolicy old = this.m_accessor.getRangePolicy();
    this.m_accessor.setRangePolicy(rangePolicy);
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_RANGEPOLICY, old, rangePolicy);
  }

  /**
   * Set wether scale values are started from major ticks.
   * <p>
   * 
   * @param majorTick
   *          true if scale values shall start with a major tick.
   * 
   * @see AAxis#setMajorTickSpacing(double)
   */
  public void setStartMajorTick(final boolean majorTick) {
    this.m_startMajorTick = majorTick;
  }

  /**
   * Returns the translation of the mouse event coordinates of the given mouse
   * event to the value within the chart for the dimension (x,y) covered by this
   * axis.
   * <p>
   * Note that the mouse event has to be an event fired on the correspondinig
   * chart component!
   * <p>
   * 
   * @param mouseEvent
   *          a mouse event that has been fired on this component.
   * @return the translation of the mouse event coordinates of the given mouse
   *         event to the value within the chart for the dimension covered by
   *         this axis (x or y) or null if no calculations could be performed as
   *         the chart was not painted before.
   */
  protected abstract double translateMousePosition(final MouseEvent mouseEvent);
}
