/*
 *  TestChartOperationsVisual.java of project jchart2d, test case 
 *  for visually testing operations of the jchart2d API.
 *  Copyright 2007 (C) Achim Westermann, created on 04.03.2007 11:12:01.
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

import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.errorbars.ErrorBarPainter;
import info.monitorenter.gui.chart.errorbars.ErrorBarPolicyRelative;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterDate;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterNumber;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.test.ATestChartOperations;
import info.monitorenter.gui.chart.traces.Trace2DLtdSorted;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.util.Range;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Visual test of operations upon the chart with basic ui for human judgement of
 * success or failure.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.18.4.3 $
 */
public class TestChartOperationsVisual
    extends ATestChartOperations {

  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestChartOperationsVisual.class.getName());

    suite.addTest(new TestChartOperationsVisual("testRemoveAllPoints"));
    suite.addTest(new TestChartOperationsVisual("testRemoveAllTraces"));
    suite.addTest(new TestChartOperationsVisual("testAddPoint"));
    suite.addTest(new TestChartOperationsVisual("testSetStroke"));
    suite.addTest(new TestChartOperationsVisual("testSetTraceName"));
    suite.addTest(new TestChartOperationsVisual("testSetTraceNameEmptyTrace"));
    suite.addTest(new TestChartOperationsVisual("testSetRangePolicyX"));
    suite
        .addTest(new TestChartOperationsVisual("testAxisLabelFormatterNumberFormatSetNumberFormat"));
    suite.addTest(new TestChartOperationsVisual("testAxisSetLabelFormatter"));
    suite.addTest(new TestChartOperationsVisual("testTraceSetErrorBarPolicy"));
    suite.addTest(new TestChartOperationsVisual("testIErrorBarPainterSetStartPointPainter"));
    suite.addTest(new TestChartOperationsVisual("testZoom"));
    suite.addTest(new TestChartOperationsVisual("testDeadLockAddPointThread"));
    suite.addTest(new TestChartOperationsVisual("testTracePointSetLocation"));

    return suite;
  }

  /**
   * Constructor with the test name.
   * <p>
   * 
   * @param arg0
   *            the name of the test.
   */
  public TestChartOperationsVisual(final String arg0) {
    super(arg0);
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createAxisX()
   */
  protected AAxis createAxisX() {
    return new AxisLinear();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createAxisY()
   */
  protected AAxis createAxisY() {
    return this.createAxisX();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createTrace()
   */
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#fillTrace(info.monitorenter.gui.chart.ITrace2D)
   */
  protected void fillTrace(final ITrace2D trace2D) {
    long timeOffset = System.currentTimeMillis();
    for (int i = 0; i < 101; i++) {
      this.m_trace.addPoint(timeOffset + i, i);
    }
  }

  /**
   * Adds a point (50,110) and prompts for visual judgement.
   * <p>
   */
  public void testAddPoint() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.addPoint(50, 110)") {
      public Object action(final Chart2D chart) {
        TestChartOperationsVisual.this.m_trace.addPoint(50, 110);
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a new number format to a <code>{@link LabelFormatterNumber}</code>
   * of the y axis.
   * <p>
   */
  public void testAxisLabelFormatterNumberFormatSetNumberFormat() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "chart.getAxisY().getFormatter().setNumberFormat(new DecimalFormat(\"000.000\"))") {
      public Object action(final Chart2D chart) {
        NumberFormat nf = new DecimalFormat("000.000");
        LabelFormatterNumber labelFormatter = (LabelFormatterNumber) chart.getAxisY()
            .getFormatter();
        labelFormatter.setNumberFormat(nf);
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      public void preCondition(final Chart2D chart) throws Exception {
        super.preCondition(chart);
        chart.getAxisY().setFormatter(new LabelFormatterNumber());
        // force the precondition visual change to have time show.
        // else the repaint could occur after the number format has been
        // set to the LabelFormatterNumber!
        Thread.sleep(1000);
      }

    };
    this.setTestOperation(operation);

  }

  /**
   * Invokes <code>{@link IAxis#setFormatter(IAxisLabelFormatter)}</code> on
   * axis x with a <code>{@link LabelFormatterDate}</code>.
   */
  public void testAxisSetLabelFormatter() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "chart.getAxisX().setFormatter(new LabelFormatterNumber(new SimpleDateFormat(\"HH:mm:ss SSS\")))") {
      public Object action(final Chart2D chart) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss SSS");
        chart.getAxisX().setFormatter(new LabelFormatterDate(df));
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Invokes
   * <code>{@link IErrorBarPainter#setStartPointPainter(IPointPainter)}</code>
   * with a <code>{@link PointPainterDisc}</code>.
   * <p>
   */
  public void testIErrorBarPainterSetStartPointPainter() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "trace.getErrorbarPainter().setStartPointPainter(new PointPainterDisc())") {
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().iterator().next();
        IErrorBarPolicy errorBarPolicy = trace.getErrorBarPolicies().iterator().next();
        IErrorBarPainter errorBarPainter = errorBarPolicy.getErrorBarPainters().iterator().next();
        errorBarPainter.setStartPointPainter(new PointPainterDisc(12));
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      public void preCondition(final Chart2D chart) throws Exception {
        super.preCondition(chart);
        ITrace2D trace = chart.getTraces().iterator().next();
        // create an error bar policy and configure it
        IErrorBarPolicy errorBarPolicy = new ErrorBarPolicyRelative(0.2, 0.2);
        errorBarPolicy.setShowNegativeYErrors(true);
        errorBarPolicy.setShowPositiveYErrors(true);
        // errorBarPolicy.setShowNegativeXErrors(true);
        // errorBarPolicy.setShowPositiveXErrors(true);
        // configure how error bars are rendered with an error bar painter:
        IErrorBarPainter errorBarPainter = new ErrorBarPainter();
        errorBarPainter.setEndPointPainter(new PointPainterDisc());
        errorBarPainter.setEndPointColor(Color.GRAY);
        errorBarPainter.setConnectionPainter(new PointPainterLine());
        errorBarPainter.setConnectionColor(Color.LIGHT_GRAY);
        // add the painter to the policy
        errorBarPolicy.setErrorBarPainter(errorBarPainter);
        trace.setErrorBarPolicy(errorBarPolicy);
        // let the update be shown:
        Thread.sleep(1000);
      }

    };
    this.setTestOperation(operation);

  }

  /**
   * Invokes
   * <code>{@link TracePoint2D#setLocation(java.awt.geom.Point2D)}</code> on
   * an extremum point to move away from the extremum.
   * <p>
   */
  public void testTracePointSetLocation() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "TracePoint2D.setLocation(x,y)") {
      /** An extremum point that is shifted away from this extremum. */
      private TracePoint2D m_extremum;

      public Object action(final Chart2D chart) {
        this.m_extremum.setLocation(4, -1);
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTrace()
       */
      public ITrace2D createTrace() {
        ITrace2D result = new Trace2DLtdSorted();
        this.m_extremum = new TracePoint2D(10, 10);
        result.addPoint(new TracePoint2D(2, 3));
        result.addPoint(new TracePoint2D(3, 2));
        result.addPoint(new TracePoint2D(5, 7));
        result.addPoint(this.m_extremum);
        return result;
      }
    };
    this.setTestOperation(operation);

  }

  /**
   * Removes all points of the trace and prompts for visual judgment.
   * <p>
   */
  public void testRemoveAllPoints() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.removeAllPoints()") {
      public Object action(final Chart2D chart) {
        TestChartOperationsVisual.this.m_trace.removeAllPoints();
        if (Chart2D.DEBUG_THREADING) {
          System.out.println(this.getClass().getName() + " removed all points. ");
        }
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Removes all traces of the chart and prompts for visual judgment.
   * <p>
   */
  public void testRemoveAllTraces() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.removeAllTraces()") {
      public Object action(final Chart2D chart) {
        TestChartOperationsVisual.this.m_chart.removeAllTraces();
        if (Chart2D.DEBUG_THREADING) {
          System.out.println(this.getClass().getName() + " removed all traces. ");
        }
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a new name to the trace and prompts for visual judgment.
   * <p>
   */
  public void testSetTraceName() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "trace.setName(\"Iphigenie\")") {
      public Object action(final Chart2D chart) {
        TestChartOperationsVisual.this.m_trace.setName("Iphigenie");
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a new name to an empty trace and prompts for visual judgement.
   * <p>
   */
  public void testSetTraceNameEmptyTrace() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "trace.setName(\"Tauris\") on empty trace") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTrace()
       */
      public ITrace2D createTrace() {

        ITrace2D result = new Trace2DLtdSorted();
        result.setName("Lola");
        return result;
      }

      public Object action(final Chart2D chart) {
        TestChartOperationsVisual.this.m_trace.setName("Tauris");
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a new range policy (RangePolicyFixedViewport(new Range(20,30))) to the
   * x axis of the chart and prompts for visual judgement.
   * <p>
   */
  public void testSetRangePolicyX() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "chart.getAxisX().setRangePolicy( "
            + "new RangePolicyFixedViewport(new Range(System.currentTimeMillis()-8000, System.currentTimeMillis())))") {
      public Object action(final Chart2D chart) {
        IRangePolicy rangePolicy = new RangePolicyFixedViewport(new Range(System
            .currentTimeMillis() - 8000, System.currentTimeMillis()));
        chart.getAxisX().setRangePolicy(rangePolicy);
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a bold stroke to the trace and prompts for visual judgement.
   * <p>
   */
  public void testSetStroke() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "trace.setStroke(new BasicStroke(2f))") {
      public Object action(final Chart2D chart) {
        TestChartOperationsVisual.this.m_trace.setStroke(new BasicStroke(2f));
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Invokes <code>{@link ITrace2D#setErrorBarPolicy(IErrorBarPolicy)}</code>
   * with a configuration that should be noticed as a visual change.
   * <p>
   */
  public void testTraceSetErrorBarPolicy() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "trace.setErrorBarPolicy(...)") {
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().iterator().next();
        // create an error bar policy and configure it
        IErrorBarPolicy errorBarPolicy = new ErrorBarPolicyRelative(0.2, 0.2);
        errorBarPolicy.setShowNegativeYErrors(true);
        errorBarPolicy.setShowPositiveYErrors(true);
        // errorBarPolicy.setShowNegativeXErrors(true);
        // errorBarPolicy.setShowPositiveXErrors(true);
        // configure how error bars are rendered with an error bar painter:
        IErrorBarPainter errorBarPainter = new ErrorBarPainter();
        errorBarPainter.setEndPointPainter(new PointPainterDisc());
        errorBarPainter.setEndPointColor(Color.GRAY);
        errorBarPainter.setConnectionPainter(new PointPainterLine());
        errorBarPainter.setConnectionColor(Color.LIGHT_GRAY);
        // add the painter to the policy
        errorBarPolicy.setErrorBarPainter(errorBarPainter);
        trace.setErrorBarPolicy(errorBarPolicy);
        return null;
      }

    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a bold stroke to the trace and prompts for visual judgement.
   * <p>
   */
  public void testZoom() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(

    "zoomableChart.zoom()") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createChartInstance()
       */
      public Chart2D createChartInstance() {

        return new ZoomableChart();
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTrace()
       */
      public ITrace2D createTrace() {
        ITrace2D trace = new Trace2DSimple();
        for (int i = 0; i < 100; i++) {
          trace.addPoint(i, (1.0 + Math.random()) * i);
        }
        return trace;
      }

      public Object action(final Chart2D chart) {
        ZoomableChart zoomChart = (ZoomableChart) chart;
        zoomChart.zoom(0.01, 20, 0, 20);
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Tests if creating a chart with one Thread and adding points from another
   * results in deadlocks.
   * <p>
   * 
   */
  public void testDeadLockAddPointThread() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(

    "add points from another Thread") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createChartInstance()
       */
      public Chart2D createChartInstance() {

        return new Chart2D();
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTrace()
       */
      public ITrace2D createTrace() {
        ITrace2D trace = new Trace2DSimple();
        return trace;
      }

      public Object action(final Chart2D chart) {
        Thread t = new Thread(new Runnable() {
          public void run() {
            ITrace2D trace = chart.getTraces().iterator().next();
            for (int i = 100; i < 200; i++) {
              trace.addPoint(i, (Math.random() + 1.0) * -i);
              try {
                Thread.sleep(100);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        });
        t.setDaemon(true);
        t.start();
        // wait for termination:
        while (t.isAlive()) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        return null;
      }
    };
    this.setTestOperation(operation);
  }
}
