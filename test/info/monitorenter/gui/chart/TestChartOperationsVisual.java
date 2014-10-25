/*
 *  TestChart2DHeadless.java of project jchart2d, junit tests for Chart2D
 *  in headless mode.
 *  Copyright (c) 2007 Achim Westermann.
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

import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.axistickpainters.AxisTickPainterInwards;
import info.monitorenter.gui.chart.errorbars.ErrorBarPainter;
import info.monitorenter.gui.chart.errorbars.ErrorBarPolicyRelative;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterDate;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterNumber;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.test.ATestChartOperations;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DLtdReplacing;
import info.monitorenter.gui.chart.traces.Trace2DLtdSorted;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.util.Range;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * Visual test of operations upon the chart with basic ui for human judgement of
 * success or failure.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.34 $
 */
public class TestChartOperationsVisual extends ATestChartOperations {

  /**
   * Constructor with the test name.
   * <p>
   * 
   * @param arg0
   *          the name of the test.
   */
  public TestChartOperationsVisual(final String arg0) {
    super(arg0);
  }

  /**
   * Inserts a new TracePoint with an y value <code>{@link Double#NaN}</code> to
   * a sorted trace to get a discontinued trace.
   * <p>
   */
  @org.junit.Test
  public void testAddDiscontinuation() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("Inserting a discontinuation: Trace should be split afterwards!") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.addPoint(5, Double.NaN);
        return null;
      }

      /**
       * Creates a <code>{@link Trace2DLtdReplacing}</code>.
       * <p>
       * 
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation
       *      #createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D sortedTrace = new Trace2DLtdSorted(500);
        return new ITrace2D[] {sortedTrace };
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#fillTrace(info.monitorenter.gui.chart.ITrace2D)
       */
      @Override
      public void fillTrace(ITrace2D trace) {
        for (int i = 0; i < 10; i++) {
          trace.addPoint(i, (Math.random() + 1.0) * i);
        }
        super.fillTrace(trace);
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Adds a point (50,110) and prompts for visual judgement.
   * <p>
   */
  @org.junit.Test
  public void testAddPoint() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.addPoint(50, 110)") {
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        assertNotNull(trace);
        trace.addPoint(50, 110);
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Adds three traces and removes the 3rd one again.
   * <p>
   * Bug report
   * http://sourceforge.net/tracker/index.php?func=detail&aid=2605832&
   * group_id=50440&atid=459734: <cite> The effect I can observe is that
   * traces[2] is _not_ removed from the graph (it changes its appearance
   * though, as if it would not be correctly scaled anymore). On the other hand,
   * removing traces[0] or traces[1] instead of traces[2] works as expected!
   * 
   * I tried to look inside to figure out what's going on, but I failed; I don't
   * really understand the TreeSetGreedy idea (why is it even there? I'm sure
   * there is a reason for it being more appropriate than a regular TreeSet, but
   * I cannot guess what it is).
   * 
   * Maybe this helps: the values for the ComparableProperty (that is, m_zIndex)
   * for the 3 traces are -1, 0, and 1. Are these plausible? </cite>
   */
  @org.junit.Test
  public void testAddRemoveTrace() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("add 3 traces, remove last one.") {

      /** Traces to add. */
      private final ITrace2D[] m_testTraces = new ITrace2D[3];

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        chart.removeTrace(this.m_testTraces[2]);

        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        return new ITrace2D[] {};
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(final Chart2D chart) throws Exception {
        super.preCondition(chart);
        Color[] colors = new Color[] {Color.green, Color.red, Color.blue };

        for (int i = 0; i < 3; i++) {
          this.m_testTraces[i] = new Trace2DLtd(300);
          this.m_testTraces[i].setStroke(new BasicStroke(8));
          this.m_testTraces[i].setColor(colors[i]);
          AAxis< ? > axisY = new AxisLinear<IAxisScalePolicy>();
          chart.addAxisYLeft(axisY);
          chart.addTrace(this.m_testTraces[i], chart.getAxisX(), axisY);
          for (int j = 0; j < 300; j++) {
            this.m_testTraces[i].addPoint(j, j + 100 * (i + Math.random()));
          }
        }

      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a new number format to a <code>{@link LabelFormatterNumber}</code> of
   * the y axis.
   * <p>
   */
  @org.junit.Test
  public void testAxisLabelFormatterNumberFormatSetNumberFormat() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("chart.getAxisY().getFormatter().setNumberFormat(new DecimalFormat(\"000.000\"))") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        NumberFormat nf = new DecimalFormat("000.000");
        LabelFormatterNumber labelFormatter = (LabelFormatterNumber) chart.getAxisY().getFormatter();
        labelFormatter.setNumberFormat(nf);
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
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
  @org.junit.Test
  public void testAxisSetLabelFormatter() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "chart.getAxisX().setFormatter(new LabelFormatterNumber(new SimpleDateFormat(\"HH:mm:ss SSS\")))") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss SSS");
        chart.getAxisX().setFormatter(new LabelFormatterDate(df));
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Turns off scale painting for axes prompts for visual judgement.
   * <p>
   */
  @org.junit.Test
  public void testAxisSetPaintScale() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("IAxis.setPaintScale(false)") {
      
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {

        for (IAxis< ? > axis : chart.getAxes()) {
          axis.setPaintScale(false);
        }
        return null;
      }
      
  
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(Chart2D chart) throws Exception {
        super.preCondition(chart);
        // y
        AxisTitle title = chart.getAxisY().getAxisTitle();
        Font font = title.getTitleFont();
        if(font == null){
          font = chart.getFont();
        }
        
        Font newFont = font.deriveFont(36f);
        title.setTitleFont(newFont);
        // x
        title = chart.getAxisX().getAxisTitle();
        title.setTitle("W");
        title.setTitleFont(newFont);
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Gets the IAxisTitle of axis y and set a new title String. 
   * <p>
   */
  @org.junit.Test
  public void testAxisSetTitle() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("YAxis.getAxisTitle().setTitle(\"Seconds\")") {
 
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {

        chart.getAxisX().getAxisTitle().setTitle("Seconds");
        chart.getAxisY().getAxisTitle().setTitle("Temperature");
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
  @org.junit.Test
  public void testDeadLockAddPointThread() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(

    "add points from another Thread") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
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

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createChartInstance()
       */
      @Override
      public Chart2D createChartInstance() {

        return new Chart2D();
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D trace = new Trace2DSimple();
        return new ITrace2D[] {trace };
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Inserts a new TracePoint with an y value <code>{@link Double#NaN}</code> to
   * a sorted trace to get a discontinued trace.
   * <p>
   */
  @org.junit.Test
  public void testDisablePaintScaleAndTitle() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("Setting paint scale to false and clearing titles.") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        chart.getAxisY().setPaintScale(false);
        chart.getAxisY().setAxisTitle(new AxisTitle(" "));
        
        chart.getAxisX().setPaintScale(false);
        chart.getAxisX().setAxisTitle(new AxisTitle(" "));
        return null;
      }

      /**
       * Creates a <code>{@link Trace2DLtdReplacing}</code>.
       * <p>
       * 
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation
       *      #createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D trace = new Trace2DLtd(500);
        return new ITrace2D[] {trace };
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#fillTrace(info.monitorenter.gui.chart.ITrace2D)
       */
      @Override
      public void fillTrace(ITrace2D trace) {
        for (int i = 0; i < 10; i++) {
          trace.addPoint(i, (Math.random() + 1.0) * i);
        }
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Hides axis titles and prompts for visual judgement.
   * <p>
   */
  @org.junit.Test
  public void testHideAxisTitles() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("Hide axis titles") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {

        AxisTitle voidTitle = new AxisTitle("");
        for (IAxis< ? > axis : chart.getAxes()) {
          axis.setAxisTitle(voidTitle);
        }
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(Chart2D chart) throws Exception {
        super.preCondition(chart);
        AxisTitle yTitle = chart.getAxisX().getAxisTitle();
        Font yFont = chart.getFont();
        Font yNewFont = yFont.deriveFont(36f);
        yTitle.setTitleFont(yNewFont);
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Invokes
   * <code>{@link IErrorBarPainter#setStartPointPainter(IPointPainterConfigurableUI)}
   * </code> with a <code>{@link PointPainterDisc}</code>.
   * <p>
   */
  @org.junit.Test
  public void testIErrorBarPainterSetStartPointPainter() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.getErrorbarPainter().setStartPointPainter(new PointPainterDisc())") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().iterator().next();
        IErrorBarPolicy< ? > errorBarPolicy = trace.getErrorBarPolicies().iterator().next();
        IErrorBarPainter errorBarPainter = errorBarPolicy.getErrorBarPainters().iterator().next();
        errorBarPainter.setStartPointPainter(new PointPainterDisc(12));
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(final Chart2D chart) throws Exception {
        super.preCondition(chart);
        ITrace2D trace = chart.getTraces().iterator().next();
        // create an error bar policy and configure it
        IErrorBarPolicy< ? > errorBarPolicy = new ErrorBarPolicyRelative(0.2, 0.2);
        errorBarPolicy.setShowNegativeYErrors(true);
        errorBarPolicy.setShowPositiveYErrors(true);
        // errorBarPolicy.setShowNegativeXErrors(true);
        // errorBarPolicy.setShowPositiveXErrors(true);
        // configure how error bars are rendered with an error bar
        // painter:
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
   * Removes all points of the trace and prompts for visual judgment.
   * <p>
   */
  @org.junit.Test
  public void testRemoveAllPoints() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.removeAllPoints()") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.removeAllPoints();
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
    ATestChartOperations.AChartOperation operation = new AChartOperation("chart.removeAllTraces()") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        chart.removeAllTraces();
        if (Chart2D.DEBUG_THREADING) {
          System.out.println(this.getClass().getName() + " removed all traces. ");
        }
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Test the following procedure from bug report:
   * https://sourceforge.net/tracker
   * /?func=detail&aid=2891801&group_id=50440&atid=459734.
   * <p>
   * <ol>
   * <li>Add a chart.</li>
   * <li>Add a first trace on a first Y axis.</li>
   * <li>Add a second trace on a second Y axis.</li>
   * <li>Add a third trace on a third Y axis.</li>
   * <li>Add a fourth trace on the first Y axis.</li>
   * <li>try to remove it.</li>
   * </ol>
   * <p>
   */
  @org.junit.Test
  public void testRemoveTraceForBug2891801() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("testRemoveTraceForBug2891801") {

      /** The trace to check remove on. **/
      private ITrace2D m_trace4;

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(Chart2D chart) throws Exception {
        chart.removeTrace(this.m_trace4);
        return null;
      }

      /**
       * Overridden to have an empty array returned and be in control to which
       * axes the traces are added in
       * 
       * <code>{@link info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(Chart2D)}</code>
       * <p>
       * 
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D[] result = new ITrace2D[] {};
        return result;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(Chart2D chart) throws Exception {
        super.preCondition(chart);
        AAxis< ? > axisY1 = new AxisLinear<IAxisScalePolicy>();
        axisY1.getAxisTitle().setTitle("asisY1");
        AAxis< ? > axisY2 = new AxisLinear<IAxisScalePolicy>();
        axisY2.getAxisTitle().setTitle("asisY2");
        AAxis< ? > axisY3 = new AxisLinear<IAxisScalePolicy>();
        axisY3.getAxisTitle().setTitle("asisY3");
        chart.addAxisYLeft(axisY1);
        chart.addAxisYLeft(axisY2);
        chart.addAxisYLeft(axisY3);

        // trace1
        IAxis< ? > xAxis = chart.getAxisX();
        ITrace2D trace1 = new Trace2DLtd();
        trace1.setColor(Color.BLUE);
        chart.addTrace(trace1, xAxis, axisY1);
        this.fillTrace(trace1);

        // trace2
        ITrace2D trace2 = new Trace2DLtd();
        trace2.setColor(Color.RED);
        chart.addTrace(trace2, xAxis, axisY2);
        this.fillTrace(trace2);

        // trace3
        ITrace2D trace3 = new Trace2DLtd();
        trace3.setColor(Color.BLACK);
        chart.addTrace(trace3, xAxis, axisY3);
        this.fillTrace(trace3);

        // trace4 to axis1
        ITrace2D trace4 = new Trace2DLtd();
        trace4.setColor(Color.MAGENTA);
        trace4.setStroke(new BasicStroke(3f));
        chart.addTrace(trace4, xAxis, axisY1);
        this.fillTrace(trace4);
        this.m_trace4 = trace4;
      }
    };
    this.setTestOperation(operation);
  }

  
  /**
   * Sets a new name to the trace and prompts for visual judgment.
   * <p>
   */
  @org.junit.Test
  public void testSetAxisTickPainterInwards() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(" chart.setAxisTickPainter(new AxisTickPainterInwards())") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        chart.setAxisTickPainter(new AxisTickPainterInwards());
        return null;
      }
    };
    this.setTestOperation(operation);
  }
  
  
  /**
   * Sets an empty String title to the trace: The label should disappear.
   * <p>
   */
  @org.junit.Test
  public void testSetEmptyTraceTitle() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setName(\"\")") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.setName("");
        return null;
      }
    };
    this.setTestOperation(operation);
  }
  
  /**
   * Sets null title to the trace: The label should disappear.
   * <p>
   */
  public void testSetNullTraceTitle() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setName(null)") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.setName(null);
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
  @org.junit.Test
  public void testSetRangePolicyX() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("chart.getAxisX().setRangePolicy( "
        + "new RangePolicyFixedViewport(new Range(20, 30)))") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        IRangePolicy rangePolicy = new RangePolicyFixedViewport(new Range(20, 30));
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
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setStroke(new BasicStroke(2f))") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.setStroke(new BasicStroke(2f));
        return null;
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a new name to the trace and prompts for visual judgment.
   * <p>
   */
  @org.junit.Test
  public void testSetTraceName() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setName(\"Iphigenie\")") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.setName("Iphigenie");
        return null;
      }
    };
    this.setTestOperation(operation);
  }
  
  /**
   * Sets a new name to an empty trace and prompts for visual judgement.
   * <p>
   */
  @org.junit.Test
  public void testSetTraceNameEmptyTrace() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setName(\"Tauris\") on empty trace") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.setName("Tauris");
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {

        ITrace2D result = new Trace2DLtdSorted();
        return new ITrace2D[] {result };
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Sets a String title to the trace that has an empty title initially: The
   * label should appear.
   * <p>
   */
  @org.junit.Test
  public void testSetTraceTitleOnEmptyTitleTrace() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "trace.setName(\"foobar\")") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        trace.setName("foobar");
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(final Chart2D chart) throws Exception {
        super.preCondition(chart);
        ITrace2D trace = chart.getTraces().first();
        trace.setName("");
      }

    };
    this.setTestOperation(operation);
  }

  /**
   * Adds two traces and sets the Z-Index of the one behind to top.
   * <p>
   */
  @org.junit.Test
  public void testSetZIndex() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setZIndex(100)") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        SortedSet<ITrace2D> traces = chart.getTraces();
        Iterator<ITrace2D> itTraces = traces.iterator();
        boolean first = true;
        ITrace2D firstTrace = null;
        ITrace2D trace;
        while (itTraces.hasNext()) {
          trace = itTraces.next();
          if (first) {
            first = false;
            firstTrace = trace;
            System.out.println("selected trace: " + firstTrace.getName());
          }
          System.out.println(trace.getName() + " " + trace.getColor() + " : " + trace.getZIndex());

        }
        if (firstTrace != null) {
          firstTrace.setZIndex(Integer.valueOf(100));
        }

        // order after change test:
        System.out.println("after change: ");
        traces = chart.getTraces();
        itTraces = traces.iterator();
        while (itTraces.hasNext()) {
          trace = itTraces.next();
          System.out.println(trace.getName() + " " + trace.getColor() + " : " + trace.getZIndex());
        }

        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D[] result = new ITrace2D[2];
        for (int i = 0; i < 2; i++) {
          result[i] = new Trace2DLtd(300);
        }
        return result;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#preCondition(info.monitorenter.gui.chart.Chart2D)
       */
      @Override
      public void preCondition(Chart2D chart) throws Exception {
        super.preCondition(chart);
        Color[] colors = new Color[] {Color.RED, Color.BLUE };
        int count = 0;
        for (ITrace2D trace : chart.getTraces()) {
          trace.setColor(colors[count % 2]);
          trace.setStroke(new BasicStroke(8));
          trace.setZIndex(new Integer(count));
          count++;
        }
      }
    };
    this.setTestOperation(operation);
  }

  /**
   * Invokes <code>{@link ITracePoint2D#setLocation(double, double)}
   * </code> on an extremum point to move away from the extremum.
   * <p>
   */
  @org.junit.Test
  public void testTracePointSetLocation() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("TracePoint2D.setLocation(4,-1)") {
      /** An extremum point that is shifted away from this extremum. */
      private ITracePoint2D m_extremum;

      /**
       * 
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        this.m_extremum.setLocation(4, -1);
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D result = new Trace2DLtdSorted();
        return new ITrace2D[] {result };

      }

      @Override
      public void fillTrace(ITrace2D trace) {
        this.m_extremum = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(10, 10);
        trace.addPoint(new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2, 3));
        trace.addPoint(new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3, 2));
        trace.addPoint(new info.monitorenter.gui.chart.tracepoints.TracePoint2D(5, 7));
        trace.addPoint(this.m_extremum);
      }

    };
    this.setTestOperation(operation);

  }

  /**
   * Invokes <code>{@link ITrace2D#setErrorBarPolicy(IErrorBarPolicy)}</code>
   * with a configuration that should be noticed as a visual change.
   * <p>
   */
  @org.junit.Test
  public void testTraceSetErrorBarPolicy() {
    ATestChartOperations.AChartOperation operation = new AChartOperation("trace.setErrorBarPolicy(...)") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().iterator().next();
        // create an error bar policy and configure it
        IErrorBarPolicy< ? > errorBarPolicy = new ErrorBarPolicyRelative(0.2, 0.2);
        errorBarPolicy.setShowNegativeYErrors(true);
        errorBarPolicy.setShowPositiveYErrors(true);
        // errorBarPolicy.setShowNegativeXErrors(true);
        // errorBarPolicy.setShowPositiveXErrors(true);
        // configure how error bars are rendered with an error bar
        // painter:
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
  @org.junit.Test
  public void testZoom() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(

    "zoomableChart.zoom()") {
      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ZoomableChart zoomChart = (ZoomableChart) chart;
        zoomChart.zoom(0.01, 20, 0, 20);
        return null;
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#createChartInstance()
       */
      @Override
      public Chart2D createChartInstance() {

        return new ZoomableChart();
      }

      @Override
      public ITrace2D[] createTraces() {
        ITrace2D trace = new Trace2DSimple();
        return new ITrace2D[] {trace };
      }
    };
    this.setTestOperation(operation);
  }

}
