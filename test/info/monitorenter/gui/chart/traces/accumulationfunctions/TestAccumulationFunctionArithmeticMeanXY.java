/*
 *  TestAccumulationFunctionArithmeticMeanXY.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2013, Achim Westermann, created on Nov 18, 2011
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
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
 *
 * File   : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $
 * Date   : $Date: 2009/02/24 16:45:41 $
 * Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart.traces.accumulationfunctions;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AccumulationFunctionArithmeticMeanXY}
 * 
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class TestAccumulationFunctionArithmeticMeanXY {

  /**
   * Instance under test.
   */
  private IAccumulationFunction m_toTest;

  /**
   * Chart that has to contain the trace that contains the points to accumulate.
   */
  private Chart2D m_chart;

  /**
   * 
   */
  @Before
  public void setUp() {
    this.m_toTest = new AccumulationFunctionArithmeticMeanXY();
    this.m_chart = new Chart2D();
  }

  /**
   * Tests an empty instance.
   */
  @Test
  public void testEmpty() {
    ITracePoint2D result = this.m_toTest.getAccumulatedPoint();
    Assert.assertEquals(null, result);
  }

  /**
   * Adds one point and expects an equal result from accumulation.
   * <p>
   */
  @Test
  public void test1value() {
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITrace2D trace = new Trace2DSimple();
    this.m_chart.addTrace(trace);
    trace.addPoint(one);
    this.m_toTest.addPointToAccumulate(one);
    ITracePoint2D result = this.m_toTest.getAccumulatedPoint();
    Assert.assertEquals(one, result);
  }

  /**
   * Adds two points and expects an equal result from accumulation.
   * <p>
   */
  @Test
  public void test2values() {
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2.0, 2.0);
    ITrace2D trace = new Trace2DSimple();
    this.m_chart.addTrace(trace);
    trace.addPoint(one);
    trace.addPoint(two);
    this.m_toTest.addPointToAccumulate(one);
    this.m_toTest.addPointToAccumulate(two);
    ITracePoint2D result = this.m_toTest.getAccumulatedPoint();
    Assert.assertEquals(1.5d, result.getX(), 0.0);
    Assert.assertEquals(1.5d, result.getY(), 0.0);
  }

  /**
   * Adds one point with {@link ITracePoint2D#getX()} being {@link Double#NaN}
   * and expects an exception being thrown.
   * <p>
   * 
   * Assumption: An {@link ITracePoint2D#isDiscontinuation()} if x or y are
   * {@link Double#NaN} and discontinuations have to be preserved. Stuffing a
   * discontinuation into an accumulation would allow to consume a
   * discontinuation.
   * <p>
   */
  @Test
  public void testSingleNaNX() {
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(Double.NaN, 1.0);
    ITrace2D trace = new Trace2DSimple();
    this.m_chart.addTrace(trace);
    trace.addPoint(one);
    try {
      this.m_toTest.addPointToAccumulate(one);
      Assert.fail("It must not be possible to enter trace points containing NaN.");
    } catch (IllegalArgumentException iae) {
      // all good!
    } catch (Throwable f) {
      f.printStackTrace(System.err);
      Assert.fail("Wrong type of exception: " + f.getClass().getName());
    }
  }

  /**
   * Adds one point with {@link ITracePoint2D#getY()} being {@link Double#NaN}
   * and expects an exception being thrown.
   * <p>
   * 
   * Assumption: An {@link ITracePoint2D#isDiscontinuation()} if x or y are
   * {@link Double#NaN} and discontinuations have to be preserved. Stuffing a
   * discontinuation into an accumulation would allow to consume a
   * discontinuation.
   * <p>
   */
  @Test
  public void testSingleNaNY() {
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, Double.NaN);
    ITrace2D trace = new Trace2DSimple();
    this.m_chart.addTrace(trace);
    trace.addPoint(one);
    try {
      this.m_toTest.addPointToAccumulate(one);
      Assert.fail("It must not be possible to enter trace points containing NaN.");
    } catch (IllegalArgumentException iae) {
      // all good!
    } catch (Throwable f) {
      f.printStackTrace(System.err);
      Assert.fail("Wrong type of exception: " + f.getClass().getName());
    }
  }


}
