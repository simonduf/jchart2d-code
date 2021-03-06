/*
 *  AccumulatingIteratorConsecutivePoints.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2013, Achim Westermann, created on Nov 20, 2011
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
 */

package info.monitorenter.gui.chart.traces.iterators.fsm;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.IAccumulationStrategy.AccumulationControlConsecutivePoints;
import info.monitorenter.gui.chart.IAccumulationStrategy.IAccumulationControl;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.accumulationfunctions.AccumulationFunctionArithmeticMeanXY;
import info.monitorenter.gui.chart.traces.iterators.AAccumulationIterator;
import info.monitorenter.util.Range;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestIteratorTracePointStateEngine {

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
    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 0);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
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

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 1);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
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

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 2);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
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
   * Assumption: iterator must return just three points. That three points must
   * be equal to the one points in source.
   * <p>
   */
  @Test
  public void testThreeValueTraceAccumulationFunctionArithmeticMeanXY() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();

    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2.0, 2.0);
    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 1);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
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

  /**
   * Test an instance with a trace containing three points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * two points into one (which is not possible with 3 points).
   * <p>
   * Assumption: iterator must return just three points. That three points must
   * be equal to the one points in source.
   * <p>
   */
  @Test
  public void testThreeValueTraceAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate2into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2.0, 2.0);
    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 1);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
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

  /**
   * Test an instance with a trace containing 4 points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * two points into one (which is possible with 4 points).
   * <p>
   * Assumption: iterator must return just three points. That three points must
   * be equal to the one points in source.
   * <p>
   */
  @Test
  public void testFourValueTraceAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate2into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2.0, 2.0);
    ITracePoint2D four = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);

    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);
    this.m_trace.addPoint(four);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 2);
    Assert.assertEquals(2,
        ((AccumulationControlConsecutivePoints) accumulationControl).getAmountToAccumulate());

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    // This is the arithmetic mean (x,y) of two and three!
    Assert.assertEquals(1.5, result.getX(), 0.0);
    Assert.assertEquals(1.5, result.getY(), 0.0);
    /*
     * Next point has to be the last point of the previous accumulation phase as it is not equal 
     * to the previous accumulated point. Policy is: When leaving accumulation (towards invisiblity or last point) the last visible point 
     * has to be returned bare in case it is different from the last accumulated point. 
     */
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(three, result);

    // last point
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(four, result);
    Assert.assertFalse(toTest.hasNext());
  }

  /**
   * Test an instance with a trace containing 4 points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * two points into one (which is possible with 4 points). However there is a
   * NaN value (discontinuation) on position 3.
   * <p>
   * Assumption: iterator must return just 4 points. The NaN point untouched.
   * <p>
   */
  @Test
  public void testFourValueTraceWith1NaNAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate2into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(Double.NaN, 2.0);
    ITracePoint2D four = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);

    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);
    this.m_trace.addPoint(four);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 2);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    // one
    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    // two
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(two, result);
    // three: this must be two unchanged as it is visible -> NaN!
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(three.getX(), result.getX(), 0.0);
    Assert.assertEquals(three.getY(), result.getY(), 0.0);
    // four: this must be three unchanged as it is a discontinuation!
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(four, result);
    Assert.assertFalse(toTest.hasNext());
  }

  /**
   * Test an instance with a trace containing 4 points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * two points into one (which is possible with 4 points). However there are
   * two NaN values (discontinuation) on position 2 and 3.
   * <p>
   * Assumption: iterator must return just 4 points. The NaN points untouched.
   * <p>
   */
  @Test
  public void testFourValueTraceWith2NaNAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate2into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, Double.NaN);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(Double.NaN, 2.0);
    ITracePoint2D four = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);

    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);
    this.m_trace.addPoint(four);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 2);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    // This must be two unchanged as it is a discontinuation!
    Assert.assertEquals(two.getX(), result.getX(), 0.0);
    Assert.assertEquals(two.getY(), result.getY(), 0.0);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    // This must be three unchanged as it is a discontinuation!
    Assert.assertEquals(three.getX(), result.getX(), 0.0);
    Assert.assertEquals(three.getY(), result.getY(), 0.0);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals(four, result);
    Assert.assertFalse(toTest.hasNext());
  }

  /**
   * Test an instance with a trace containing 4 points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * two points into one (which might be possible with 4 points). However there
   * is one NaN value (discontinuation) on position 1 which has to prevent
   * accumulation.
   * <p>
   * Assumption: iterator must return just 4 points. The NaN point, first
   * visible and last untouched.
   * <p>
   */
  @Test
  public void testFourValueTraceWithFirstNaNAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate2into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(Double.NaN, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2.0, 2.0);
    ITracePoint2D four = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);

    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);
    this.m_trace.addPoint(four);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 2);

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    // This must be one unchanged as it is a discontinuation!
    Assert.assertEquals(one.getX(), result.getX(), 0.0);
    Assert.assertEquals(one.getY(), result.getY(), 0.0);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    /*
     * This must be two as it is the first visible point (no accumulation must
     * be performed)!
     */
    Assert.assertEquals(two.getX(), result.getX(), 0.0);
    Assert.assertEquals(two.getY(), result.getY(), 0.0);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    /*
     * This must be three as it must not be accumulated with the last point!
     */
    Assert.assertEquals(three, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    /*
     * This must be four, the last point!
     */
    Assert.assertEquals(four, result);
    Assert.assertFalse(toTest.hasNext());

  }

  /**
   * Test an instance with a trace containing 102 points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * two points into one.
   * <p>
   * Assumption: iterator must return just 52 points. First and last have to be
   * returned as contained in the original. Remaining points must be the
   * arithmetic mean of two consecutive points.
   * <p>
   */
  @Test
  public void test102ValueTraceAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate2into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D first = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    this.m_trace.addPoint(first);
    for (int i = 1; i < 101; i++) {
      this.m_trace.addPoint(new info.monitorenter.gui.chart.tracepoints.TracePoint2D(i, i));
    }
    ITracePoint2D last = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(101.0, 101.0);
    this.m_trace.addPoint(last);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 50);
    Assert.assertEquals(2,
        ((AccumulationControlConsecutivePoints) accumulationControl).getAmountToAccumulate());

    AAccumulationIterator toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(first, result);

    /*
     * Loop them
     */
    double i = 1;
    double expected;
    while (toTest.hasNext()) {
      // arithmetic mean of two following points.
      expected = (i * 2 + 1) / 2;
      result = toTest.next();
      if (toTest.hasNext()) {
        Assert.assertEquals(expected, result.getX(), 0.0);
        Assert.assertEquals(expected, result.getY(), 0.0);
      } else {
        Assert.assertEquals(result, last);
      }
      // Two points were advanced.
      i += 2.0;
    }
  }

  /**
   * Test an instance with a trace containing 92 points and an
   * {@link AccumulationFunctionArithmeticMeanXY} and the request to accumulate
   * three points into one.
   * <p>
   * Assumption: iterator must return just 32 points. First and last have to be
   * returned as contained in the original. Remaining points must be the
   * arithmetic mean of two consecutive points.
   * <p>
   */
  @Test
  public void test92ValueTraceAccumulationFunctionArithmeticMeanXYWithRequestToAccumulate3into1() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D first = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    this.m_trace.addPoint(first);
    for (int i = 1; i < 91; i++) {
      this.m_trace.addPoint(new info.monitorenter.gui.chart.tracepoints.TracePoint2D(i, i));
    }
    ITracePoint2D last = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(91.0, 91.0);
    this.m_trace.addPoint(last);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 30);

    Iterator<ITracePoint2D> toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(first, result);

    /*
     * Loop them
     */
    double i = 1;
    double expected;
    while (toTest.hasNext()) {
      // arithmetic mean of two following points.
      expected = (i * 3 + 3) / 3;
      result = toTest.next();
      if (toTest.hasNext()) {
        Assert.assertEquals(expected, result.getX(), 0.0);
        Assert.assertEquals(expected, result.getY(), 0.0);
      } else {
        Assert.assertEquals(result, last);
      }
      // Two points were advanced.
      i += 3.0;
    }
  }

  /**
   * Adds a point outside the visible range (lower bound) then a point in the
   * visible range and then a point outside the visible range (upper bound) all
   * with ascending x value.
   * <p>
   * 
   * <ul>
   * <li>First point returned has to be the first invisible one (contract to
   * allow that interpolation towards visible bounds still works with
   * accumulation API).</li>
   * <li>2nd point has to be the original visible point.</li>
   * <li>Last point returned has to be the last invisible one (contract to allow
   * that interpolation towards visible bounds still works with accumulation
   * API).</li>
   * </ul>
   * <p>
   * 
   */
  @Test
  public void testReturnsFirstInvisible() {
    Chart2D chart = this.m_trace.getRenderer();
    Range visibleRange = new Range(0, 1.0);
    chart.getAxisX().setRangePolicy(new RangePolicyFixedViewport(visibleRange));
    chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(visibleRange));
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(-1.0, -1.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.5, 0.5);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, 1.0);
    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), this.m_trace.getSize());

    Iterator<ITracePoint2D> toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);
    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    Assert.assertEquals(two, toTest.next());
    Assert.assertEquals(three, toTest.next());
    Assert.assertFalse(toTest.hasNext());
  }

  /**
   * Adds a point outside the visible range (lower bound) then many points in
   * the visible range and then a point outside the visible range (upper bound)
   * all with ascending x value. Accumulation is desired to accumulate all
   * points into 1 point.
   * <p>
   * 
   * <ul>
   * <li>First point returned has to be the first invisible one (contract to
   * allow that interpolation towards visible bounds still works with
   * accumulation API).</li>
   * <li>2nd point has to be the original visible point.</li>
   * <li>Last point returned has to be the last invisible one (contract to allow
   * that interpolation towards visible bounds still works with accumulation
   * API).</li>
   * </ul>
   * <p>
   * 
   */
  @Test
  public void testReturnsFirstInvisibleManyPoints() {
    Chart2D chart = this.m_trace.getRenderer();
    Range visibleRange = new Range(0, 1.0);
    chart.getAxisX().setRangePolicy(new RangePolicyFixedViewport(visibleRange));
    chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(visibleRange));
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(-1.0, -1.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);
    this.m_trace.addPoint(one);
    double accumX = 0;
    double accumY = 0;
    double x = 0;
    double y = 0;
    int count = 0;

    for (int i = 100; i > 0; i--) {
      x = 1.0 / i;
      y = 1.0 / i;
      if (i != 100) {
        /*
         * First visible point will be returned without accumulation. 
         */
        accumX += x;
        accumY += y;
        count++;
      }
      this.m_trace.addPoint(new info.monitorenter.gui.chart.tracepoints.TracePoint2D(x, y));
    }
    this.m_trace.addPoint(three);
    accumX /= count;
    accumY /= count;

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 1);
    Assert.assertEquals(102,
        ((AccumulationControlConsecutivePoints) accumulationControl).getAmountToAccumulate());

    Iterator<ITracePoint2D> toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    /*
     * First invisible point!
     */
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    /*
     * First visible point!
     */
    result = toTest.next();
    Assert.assertEquals(0.01, result.getX(),0.0);
    Assert.assertEquals(0.01, result.getY(),0.0);
    /*
     * Whole lotta accumulated point!
     */
    result = toTest.next();
    Assert.assertEquals(accumX, result.getX(), 0.0);
    Assert.assertEquals(accumY, result.getY(), 0.0);
    /*
     * Last visible point!
     */
    result = toTest.next();
    /*
     * Last (invisible) point!
     */
    result = toTest.next();
    Assert.assertEquals(three, result);

    Assert.assertFalse("Should be empty", toTest.hasNext());
  }

  /**
   * Test if after a discontinuation the next point is returend without data
   * accumulation even if it would be possible to accumulate that
   * NaN-following-point with following visible points.
   * <p>
   * Contract: After NaN the next visible point must not be accumulated to
   * prevent discontinuation-gaps to show up bigger than they really are.
   * <p>
   */
  @Test
  public void testReturns1stVisiblePointAfterDiscontinuation() {
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(0.0, 0.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(1.0, Double.NaN);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(2.0, 2.0);
    ITracePoint2D four = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);
    ITracePoint2D five = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(4.0, 4.0);
    ITracePoint2D six = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(6.0, 6.0);

    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);
    this.m_trace.addPoint(four);
    this.m_trace.addPoint(five);
    this.m_trace.addPoint(six);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 2);

    Iterator<ITracePoint2D> toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals(one, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    // This must be two unchanged as it is a discontinuation!
    Assert.assertEquals(two.getX(), result.getX(), 0.0);
    Assert.assertEquals(two.getY(), result.getY(), 0.0);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    /*
     * This must be three unchanged as it is the first point after a
     * discontinuation!
     */
    Assert.assertEquals(three, result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    /*
     * This has to be the arithmetic mean of four and five (even if we want to
     * accumulate 3 points together) as the six is the last point!
     */
    Assert
        .assertEquals(
            "This has to be the arithmetic mean of four and five (even if we want to accumulate 3 points together) as the six is the last point!",
            (four.getX() + five.getX()) / 2, result.getX(), 0.0);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals("This should be the unchanged last point!", six, result);
  }

  /**
   * Two points are added both in the invisible area. But they have an
   * intersection in the visible range and have to show up.
   * <p>
   * Contract: If no points are within the visible area but two points have an
   * intersection in the visible area those points have to be returned unchanged
   * (to allow the chart painting that intersection).
   * <p>
   */
  @Test
  public void testIntersectionOfInvisiblePoints() {
    Chart2D chart = this.m_trace.getRenderer();
    Range visibleRange = new Range(0, 1.0);
    chart.getAxisX().setRangePolicy(new RangePolicyFixedViewport(visibleRange));
    chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(visibleRange));
    IAccumulationFunction accumulationFunction = new AccumulationFunctionArithmeticMeanXY();

    ITracePoint2D zero = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(-2.0, -2.0);
    ITracePoint2D one = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(-1.0, -1.0);
    ITracePoint2D two = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(3.0, 3.0);
    ITracePoint2D three = new info.monitorenter.gui.chart.tracepoints.TracePoint2D(4.0, 4.0);
    this.m_trace.addPoint(zero);
    this.m_trace.addPoint(one);
    this.m_trace.addPoint(two);
    this.m_trace.addPoint(three);

    IAccumulationControl accumulationControl = new AccumulationControlConsecutivePoints();
    accumulationControl.initializeControl(this.m_trace.getSize(), 1);

    Iterator<ITracePoint2D> toTest = new IteratorTracePointStateEngine(this.m_trace,
        accumulationFunction, accumulationControl);

    Assert.assertTrue(toTest.hasNext());
    ITracePoint2D result = toTest.next();
    Assert.assertEquals("Latest lower invisible point has to be returned for interpolation. ", one,
        result);
    Assert.assertTrue(toTest.hasNext());
    result = toTest.next();
    Assert.assertEquals("First upper invisible point has to be returned for interpolation. ", two,
        result);
  }

}
