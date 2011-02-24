/*
 *  AAxisTransformation.java of project jchart2d, 
 *  base class for Axis implementations that transform the scale 
 *  for changed display.  
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
import info.monitorenter.gui.chart.TracePoint2D;
import info.monitorenter.util.MathUtil;
import info.monitorenter.util.Range;

import java.awt.event.MouseEvent;
import java.util.Iterator;

/**
 * Base class for Axis implementations that transform the scale for changed
 * display.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * @version $Revision: 1.26 $
 */
public abstract class AAxisTransformation extends AAxis {

  /**
   * An accessor for the x axis of a chart.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de>Achim Westermann </a>
   * @see Chart2D#getAxisX()
   */
  public final class XDataAccessor extends AAxis.XDataAccessor {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 8775312615991487847L;
    /**
     * Creates an instance that accesses the given chart's x axis.
     * <p>
     * 
     * @param chart
     *            the chart to access.
     */
    public XDataAccessor(final Chart2D chart) {

      super(chart);
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.XDataAccessor#scaleTrace(info.monitorenter.gui.chart.ITrace2D,
     *      info.monitorenter.util.Range)
     */
    protected void scaleTrace(final ITrace2D trace, final Range range) {
      if (trace.isVisible()) {
        Iterator<TracePoint2D> itPoints = trace.iterator();
        TracePoint2D point;
        double result;
        double scaler = range.getExtent();
        itPoints = trace.iterator();
        while (itPoints.hasNext()) {
          point = itPoints.next();
          double absolute = point.getX();
          try {
            result = (AAxisTransformation.this.transform(absolute) - range.getMin());
            result = result / scaler;
            if (!MathUtil.isDouble(result)) {
              result = 0;
            }
          } catch (IllegalArgumentException e) {
            long tstamp = System.currentTimeMillis();
            if (tstamp - AAxisTransformation.this.m_outputErrorTstamp > AAxisTransformation.OUTPUT_ERROR_THRESHHOLD) {
              System.out.println(e.getLocalizedMessage());
              AAxisTransformation.this.m_outputErrorTstamp = tstamp;
            }
            result = 0;
          }
          point.setScaledX(result);
        }
      }
    }
    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.XDataAccessor#translateValueToPx(double)
     */
    public final int translateValueToPx(final double value) {
      /*
       * Note: This code (the math) is the combination of the scaleTrace code
       * above for normalization plus the deflate-into-pixelspace code in
       * Chart2D.paint.
       */
      int result;
      double normalizedValue;
      Range range = AAxisTransformation.this.getRange();
      double scaler = range.getExtent();
      double absolute = value;
      try {
        normalizedValue = (AAxisTransformation.this.transform(absolute) - range.getMin());
        normalizedValue = normalizedValue / scaler;
        if (!MathUtil.isDouble(normalizedValue)) {
          normalizedValue = 0;
        }
      } catch (IllegalArgumentException e) {
        long tstamp = System.currentTimeMillis();
        if (tstamp - AAxisTransformation.this.m_outputErrorTstamp > AAxisTransformation.OUTPUT_ERROR_THRESHHOLD) {
          System.out.println(e.getLocalizedMessage());
          AAxisTransformation.this.m_outputErrorTstamp = tstamp;
        }
        normalizedValue = 0;
      }
      // Now the value is normalized:
      Chart2D chart = this.getChart();
      int pixelRange = this.getPixelRange();
      result = (int) Math.round(chart.getXChartStart() + normalizedValue * pixelRange);
      return result;

    }

  }

  /**
   * Accesses the y axis of the {@link Chart2D}.
   * <p>
   * 
   * @see AAxis#setAccessor(info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor)
   * @see Chart2D#getAxisY()
   */

  protected final class YDataAccessor extends AAxis.YDataAccessor {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 3043923189624836455L;

    /**
     * Creates an instance that accesses the y axis of the given chart.
     * <p>
     * 
     * @param chart
     *            the chart to access.
     */
    public YDataAccessor(final Chart2D chart) {
      super(chart);
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.YDataAccessor#scaleTrace(info.monitorenter.gui.chart.ITrace2D,
     *      info.monitorenter.util.Range)
     */
    protected void scaleTrace(final ITrace2D trace, final Range range) {
      if (trace.isVisible()) {
        TracePoint2D point;
        double scaler = range.getExtent();
        double result;
        Iterator<TracePoint2D> itPoints = trace.iterator();
        while (itPoints.hasNext()) {
          point = itPoints.next();
          double absolute = point.getY();
          try {
            // range.getMin() is based upon the transformed minimum (see
            // getMin() of outer class)!
            result = (AAxisTransformation.this.transform(absolute) - range.getMin());
            result = result / scaler;
            if (!MathUtil.isDouble(result)) {
              result = 0;
            }
          } catch (IllegalArgumentException e) {
            long tstamp = System.currentTimeMillis();
            if (tstamp - AAxisTransformation.this.m_outputErrorTstamp > AAxisTransformation.OUTPUT_ERROR_THRESHHOLD) {
              System.out.println(e.getLocalizedMessage());
              AAxisTransformation.this.m_outputErrorTstamp = tstamp;
            }
            result = 0;
          }
          point.setScaledY(result);
        }
      }
    }

    /**
     * @see info.monitorenter.gui.chart.axis.AAxis.AChart2DDataAccessor#translateValueToPx(double)
     */
    public final int translateValueToPx(final double value) {
      /*
       * Note: This code (the math) is the combination of the scaleTrace code
       * above for normalization plus the deflate-into-pixelspace code in
       * Chart2D.paint.
       */
      int result = 0;
      Range range = AAxisTransformation.this.getRange();
      double scaler = range.getExtent();
      double normalizedValue;
      double absolute = value;
      try {
        // range.getMin() is based upon the transformed minimum (see getMin() of
        // outer class)!
        normalizedValue = (AAxisTransformation.this.transform(absolute) - range.getMin());
        normalizedValue = normalizedValue / scaler;
        if (!MathUtil.isDouble(normalizedValue)) {
          normalizedValue = 0;
        }
      } catch (IllegalArgumentException e) {
        long tstamp = System.currentTimeMillis();
        if (tstamp - AAxisTransformation.this.m_outputErrorTstamp > AAxisTransformation.OUTPUT_ERROR_THRESHHOLD) {
          System.out.println(e.getLocalizedMessage());
          AAxisTransformation.this.m_outputErrorTstamp = tstamp;
        }
        normalizedValue = 0;
      }
      // Now the value is normalized:
      Chart2D chart = this.getChart();
      int pixelRange = this.getPixelRange();
      result = (int) Math.round(chart.getYChartStart() - normalizedValue * pixelRange);
      return result;
    }

  }

  /**
   * Internal flag that defines that only every n milliseconds a transformation
   * error (untransformable value was used in chart: this axis implementation of
   * axis is not recommended for the data used) should be reported on system
   * output.
   */
  private static final int OUTPUT_ERROR_THRESHHOLD = 30000;

  /**
   * Internal timestamp of the last transformation error reporting.
   */
  private long m_outputErrorTstamp = 0;

  /**
   * Creates a default instance that will use a
   * {@link info.monitorenter.gui.chart.labelformatters.LabelFormatterAutoUnits}
   * for formatting labels.
   * <p>
   */
  public AAxisTransformation() {
    super();
  }

  /**
   * Creates an instance that will the given label formatter for formatting
   * labels.
   * <p>
   * 
   * @param formatter
   *            needed for formatting labels of this axis.
   */
  public AAxisTransformation(final IAxisLabelFormatter formatter) {
    super(formatter);
  }

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#createAccessor(info.monitorenter.gui.chart.Chart2D,
   *      int, int)
   */
  protected AAxis.AChart2DDataAccessor createAccessor(final Chart2D chart, final int dimension,
      final int position) {
    AAxis.AChart2DDataAccessor result;
    if (dimension == Chart2D.X) {
      // Don't allow a combination of dimension and position that is not usable:
      if ((position & (Chart2D.CHART_POSITION_BOTTOM | Chart2D.CHART_POSITION_TOP)) == 0) {
        throw new IllegalArgumentException("X axis only valid with top or bottom position.");
      }
      this.setAxisPosition(position);
      result = new AAxisTransformation.XDataAccessor(chart);
    } else if (dimension == Chart2D.Y) {
      // Don't allow a combination of dimension and position that is not usable:
      if ((position & (Chart2D.CHART_POSITION_LEFT | Chart2D.CHART_POSITION_RIGHT)) == 0) {
        throw new IllegalArgumentException("Y axis only valid with left or right position.");
      }
      this.setAxisPosition(position);
      result = new AAxisTransformation.YDataAccessor(chart);
    } else {
      throw new IllegalArgumentException("Dimension has to be Chart2D.X or Chart2D.Y!");
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#getMax()
   */
  public double getMax() {
    double result = 1.0;
    try {
      result = this.transform(super.getMax());
    } catch (IllegalArgumentException e) {
      // nop
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#getMin()
   */
  public double getMin() {
    double result = 0.0;
    try {
      result = this.transform(super.getMin());
    } catch (IllegalArgumentException e) {
      // nop
    }
    return result;
  }

  /**
   * @deprecated replaced by {@link #scaleTrace(ITrace2D)}
   * @see info.monitorenter.gui.chart.IAxis#getScaledValue(double)
   */
  public final double getScaledValue(final double absolute) {
    double result;
    Range range = this.getRange();
    try {
      result = (AAxisTransformation.this.transform(absolute) - range.getMin());
      double scaler = range.getExtent();
      result = result / scaler;
      if (!MathUtil.isDouble(result)) {
        result = 0;
      }
    } catch (IllegalArgumentException e) {
      long tstamp = System.currentTimeMillis();
      if (tstamp - this.m_outputErrorTstamp > AAxisTransformation.OUTPUT_ERROR_THRESHHOLD) {
        System.out.println(e.getLocalizedMessage());
        this.m_outputErrorTstamp = tstamp;
      }
      result = 0;
    }
    return result;
  }

  /**
   * Template method for performing the axis transformation.
   * <p>
   * The argument should not be negative, so only normalized values (no chart
   * values but their scaled values or pixel values) should be given here.
   * <p>
   * 
   * @param in
   *            the value to transform.
   * @return the transformed value.
   * @throws IllegalArgumentException
   *             if scaling is impossible (due to some mathematical
   *             transformation in implementations like
   *             {@link info.monitorenter.gui.chart.axis.AxisLog10}
   */
  protected abstract double transform(final double in) throws IllegalArgumentException;

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#translateMousePosition(java.awt.event.MouseEvent)
   */
  public final double translateMousePosition(final MouseEvent mouseEvent)
      throws IllegalArgumentException {
    return this.untransform(this.getAccessor().translateMousePosition(mouseEvent));
  }

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#translatePxToValue(int)
   */
  public double translatePxToValue(final int pixel) {
    return this.untransform(this.m_accessor.translatePxToValue(pixel));
  }

  /**
   * Template method for performing the reverse axis transformation.
   * <p>
   * This is the counterpart to {@link #transform(double)}.
   * <p>
   * 
   * @param in
   *            the transformed value.
   * @return the normal value;
   */
  protected abstract double untransform(final double in);
}
