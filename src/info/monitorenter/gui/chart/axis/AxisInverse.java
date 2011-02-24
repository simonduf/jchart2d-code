/*
 *  AAxisInverse.java of project jchart2d, 
 *  an IAxis implementations that inverts the values and shows 
 *  decreasing values (10, 9,... 1).  
 *  Copyright (c) 2007 Achim Westermann, created on 20:33:13.
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
package info.monitorenter.gui.chart.axis;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxisLabelFormatter;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.LabeledValue;
import info.monitorenter.gui.chart.TracePoint2D;
import info.monitorenter.util.Range;

import java.util.Iterator;

/**
 * An {@link AAxis} with inverse display of values.
 * <p>
 * 
 * Labels and values are starting from the highest value and go down to the
 * lowest one.
 * <p>
 * 
 * 
 * @author Andrea Plotegher (initial contribution)
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *         (adaption for core)
 * 
 * @version $Revision: 1.9 $
 */

public class AxisInverse
    extends AAxis {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -1688970969107347292L;

  /**
   * 
   * An accessor for the x axis of a chart.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de>Achim Westermann </a>
   * 
   * @see Chart2D#getAxisX()
   */
  public class XDataInverseAccessor
      extends AAxis.XDataAccessor {

    /**
     * Creates an instance that accesses the given chart's x axis.
     * <p>
     * 
     * @param chart
     *          the chart to access.
     */
    public XDataInverseAccessor(final Chart2D chart) {

      super(chart);
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#scaleTrace(info.monitorenter.gui.chart.ITrace2D,
     *      info.monitorenter.util.Range)
     */
    protected void scaleTrace(final ITrace2D trace, final Range range) {
      Iterator itPoints;
      final double scaler = range.getExtent();
      if (trace.isVisible()) {
        itPoints = trace.iterator();
        TracePoint2D point;
        while (itPoints.hasNext()) {
          point = (TracePoint2D) itPoints.next();
          double absolute = point.getX();
          double result = 1 - ((absolute - range.getMin()) / scaler);
          if (Double.isNaN(result) || Double.isInfinite(result)) {
            result = 0;
          }
          point.m_scaledX = result;
        }
      }
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#translatePxToValue(int)
     */
    public double translatePxToValue(final int pixel) {
      double result = 0;

      // relate to the offset:
      double px = pixel - this.m_chart.getXChartStart();

      int rangeX = this.m_chart.getXChartEnd() - this.m_chart.getXChartStart();
      if (rangeX == 0) {
        // return 0
      } else {
        double scaledX = 1 - (px / (double) rangeX);
        Range valueRangeX = AxisInverse.this.getRange();
        result = scaledX * valueRangeX.getExtent() + valueRangeX.getMin();
      }
      return result;
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#translateValueToPx(double)
     */
    public int translateValueToPx(final double value) {

      int result = 0;
      // first normalize to [00.0..1.0]
      double valueNormalized;
      // the same as AAxis.this.getRange().getExtend()
      double valueRange = this.getMax() - this.getMin();
      valueNormalized = 1 - ((value - this.getMin()) / valueRange);
      // no expand into the pixelspace:
      int rangeX = this.m_chart.getXChartEnd() - this.m_chart.getXChartStart();
      if (rangeX == 0) {
        // return null
      } else {
        double tmpResult = (valueNormalized * rangeX + this.m_chart.getXChartStart());
        result = (int) Math.round(tmpResult);
      }
      return result;
    }

  }

  /**
   * Accesses the y axis of the {@link Chart2D}.
   * <p>
   * 
   * @see AAxis#setAccessor(info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor)
   * 
   * @see Chart2D#getAxisY()
   */
  public class YDataInverseAccessor
      extends AAxis.YDataAccessor {

    /**
     * Creates an instance that accesses the y axis of the given chart.
     * <p>
     * 
     * @param chart
     *          the chart to access.
     */
    public YDataInverseAccessor(final Chart2D chart) {

      super(chart);
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#scaleTrace(info.monitorenter.gui.chart.ITrace2D,
     *      info.monitorenter.util.Range)
     */
    protected void scaleTrace(final ITrace2D trace, final Range range) {
      if (trace.isVisible()) {
        double scaler = range.getExtent();
        Iterator itPoints = trace.iterator();
        TracePoint2D point;
        while (itPoints.hasNext()) {
          point = (TracePoint2D) itPoints.next();
          double absolute = point.getY();
          double result = 1 - ((absolute - range.getMin()) / scaler);
          if (Double.isNaN(result) || Double.isInfinite(result)) {
            result = 0;
          }
          point.m_scaledY = result;
        }
      }
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#translatePxToValue(int)
     */
    public double translatePxToValue(final int pixel) {
      double result = 0;
      // invert, as awt px are higher the lower the chart value is:
      double px = this.m_chart.getYChartStart() - pixel;

      int rangeY = this.m_chart.getYChartStart() - this.m_chart.getYChartEnd();
      if (rangeY == 0) {
        // return null
      } else {
        double scaledY = 1 - (px / (double) rangeY);
        Range valueRangeY = AxisInverse.this.getRange();
        result = scaledY * valueRangeY.getExtent() + valueRangeY.getMin();
      }
      return result;
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#translateValueToPx(double)
     */
    public int translateValueToPx(final double value) {
      int result = 0;
      // first normalize to [00.0..1.0]
      double valueNormalized;
      // the same as AAxis.this.getRange().getExtend()
      double valueRange = this.getMax() - this.getMin();
      valueNormalized = 1 - ((value - this.getMin()) / valueRange);
      // no expand into the pixelspace:
      int rangeY = this.m_chart.getYChartStart() - this.m_chart.getYChartEnd();
      if (rangeY == 0) {
        // return null
      } else {
        result = (int) Math.round(this.m_chart.getYChartStart() - valueNormalized * rangeY);
      }
      return result;
    }
  }

  /**
   * Defcon.
   * <p>
   */
  public AxisInverse() {
    // nop
  }

  /**
   * Constructor that uses the given label formatter for formatting labels.
   * <p>
   * 
   * @param formatter
   *          needed for formatting labels of this axis.
   * 
   */
  public AxisInverse(final IAxisLabelFormatter formatter) {
    super(formatter);
  }

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#createAccessor(info.monitorenter.gui.chart.Chart2D,
   *      int)
   */
  protected AChart2DDataAccessor createAccessor(final Chart2D chart, final int dimension) {
    if (dimension == Chart2D.X) {
      return new AxisInverse.XDataInverseAccessor(chart);
    } else if (dimension == Chart2D.Y) {
      return new AxisInverse.YDataInverseAccessor(chart);
    } else {
      throw new IllegalArgumentException("Dimension has to be Chart2D.X or Chart2D.Y!");
    }
  }

  /**
   * Returns the labels for this axis.
   * <p>
   * The labels will have at least the given argument <code>resolution</code>
   * as distance in the value domain of the chart.
   * <p>
   * 
   * @param resolution
   *          the distance in the value domain of the chart that has to be at
   *          least between to labels.
   * 
   * @return the labels for this axis.
   */

  protected LabeledValue[] getLabels(final double resolution) {

    LabeledValue[] ret = super.getLabels(resolution);
    LabeledValue label;
    int stop = ret.length;
    for (int i = 0; i < stop; i++) {
      label = ret[i];
      label.setValue(1 - label.getValue());
    }
    return ret;
  }

  /**
   * @see info.monitorenter.gui.chart.IAxis#getScaledValue(double)
   */
  public double getScaledValue(final double absolute) {
    Range range = this.getRange();
    double scalerX = range.getExtent();
    double result = 1 - ((absolute - range.getMin()) / scalerX);
    if (Double.isNaN(result) || Double.isInfinite(result)) {
      result = 0;
    }
    return result;
  }
}
