/*
 *  AccumulatingIteratorConsecutivePoints.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2011, Achim Westermann, created on Nov 20, 2011
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

package info.monitorenter.gui.chart.traces.iterators;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.accumulationfunctions.AccumulationFunctionArithmeticMeanXY;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAccumulatingIteratorConsecutivePoints {

  /** Trace needed for test. */
  private ITrace2D m_trace;

  /**
   * JUnit setup.
   */
  @Before
  public void setUp() {
    Chart2D chart = new Chart2D();
    this.m_trace = new Trace2DSimple();
    chart.addTrace(this.m_trace);
  }

  /**
   * Tests an instance with an empty trace and an
   * {@link AccumulationFunctionArithmeticMeanXY}.
   * <p>
   * Assumption: iterator must be empty from the beginning.
   * <p>
   */
  @Test
  public void testEmptyTraceAccumulationFunctionArithmeticMeanXY() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    AAccumulationIterator toTest = new AccumulatingIteratorConsecutivePoints(this.m_trace.iterator(), accumulationFunction, 0, 0);
    Assert.assertFalse(toTest.hasNext());
  }

  /**
   * Test an instance with a trace containing one point and an
   * {@link AccumulationFunctionArithmeticMeanXY}.
   * <p>
   * Assumption: iterator must return just one point. That one point must be
   * equal to the one point in source.
   * <p>
   */
  @Test
  public void testOneValueTraceAccumulationFunctionArithmeticMeanXY() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D point = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    this.m_trace.addPoint(point);

    AAccumulationIterator toTest = new AccumulatingIteratorConsecutivePoints(this.m_trace.iterator(), accumulationFunction,
        this.m_trace.getSize(), this.m_trace.getSize());
    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(point, result);
    Assert.assertFalse(toTest.hasNext());
  }

  /**
   * Test an instance with a trace containing two points and an
   * {@link AccumulationFunctionArithmeticMeanXY}.
   * <p>
   * Assumption: iterator must return just two points. That two points must be
   * equal to the one points in source.
   * <p>
   */
  @Test
  public void testTwoValueTraceAccumulationFunctionArithmeticMeanXY() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);

    AAccumulationIterator toTest = new AccumulatingIteratorConsecutivePoints(this.m_trace.iterator(), accumulationFunction,
        this.m_trace.getSize(), this.m_trace.getSize());
    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(two, result);
    Assert.assertFalse(toTest.hasNext());
  }
  
  /**
   * Test an instance with a trace containing three points and an
   * {@link AccumulationFunctionArithmeticMeanXY}.
   * <p>
   * Assumption: iterator must return just three points. That three points must be
   * equal to the one points in source.
   * <p>
   */
  @Test
  public void testThreeValueTraceAccumulationFunctionArithmeticMeanXY() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);

    AAccumulationIterator toTest = new AccumulatingIteratorConsecutivePoints(this.m_trace.iterator(), accumulationFunction,
        this.m_trace.getSize(), this.m_trace.getSize());
    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(two, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(three, result);
    Assert.assertFalse(toTest.hasNext());
  }
}
