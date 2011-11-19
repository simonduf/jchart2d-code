/*
 *  AccumulationFunctionArithmeticMeanXY.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2011, Achim Westermann, created on Oct 9, 2011
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

import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.ITracePointProvider;

/**
 * Returns the arithmetic mean (x and y) of all points being accumulated by the
 * calls to <code>{@link #addPointToAccumulate(ITracePoint2D)}</code> since the
 * call to <code>{@link #getAccumulatedPoint()}</code>.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class AccumulationFunctionArithmeticMeanXY extends AAccumulationFunction {

  /**
   * Needed as a divisor when {@link #getAccumulatedPoint()} is called to divide
   * the summation of all previous accumulated point which is tricky kept in
   * {@link #getAccumulatedPointCurrent()}.
   */
  private int m_accumulatedPointsCount = 0;

  // /**
  // * Creates an instance that uses the given trace point provider to create
  // * accumulation points.
  // * <p>
  // *
  // * @param tracePointProvider
  // * see {@link #setTracePointProvider(ITracePointProvider)}.
  // */
  // public AccumulationFunctionArithmeticMeanXY(ITracePointProvider
  // tracePointProvider) {
  // super(tracePointProvider);
  // }

  /**
   * @see info.monitorenter.gui.chart.IAccumulationFunction#addPointToAccumulate(info.monitorenter.gui.chart.ITracePoint2D)
   */
  public void addPointToAccumulate(ITracePoint2D point) throws IllegalArgumentException {
    
    if(point.isDiscontinuation()) {
      throw new IllegalArgumentException("Do not attemp to consume a discontinuation by accumulation - preserve them for the chart!");
    }
    ITracePoint2D accumulatedPointCurrent = this.getAccumulatedPointCurrent();
    if (accumulatedPointCurrent == null) {
      ITracePointProvider tracePointProvider = this.acquireTracePointProvider(point);

      accumulatedPointCurrent = tracePointProvider.createTracePoint(point.getX(), point.getY());
      this.setAccumulatedPointCurrent(accumulatedPointCurrent);
    } else {
      /*
       * Caution this is not the correct arithmetic mean calculation but just
       * collection of the sum of all added points. Calculation is done in
       * getAccumulatedPoint()!
       */
      accumulatedPointCurrent.setLocation(accumulatedPointCurrent.getX() + point.getX(), accumulatedPointCurrent.getY() + point.getY());
    }
    this.m_accumulatedPointsCount++;
  }

  /**
   * @see info.monitorenter.gui.chart.traces.accumulationfunctions.AAccumulationFunction#getAccumulatedPoint()
   */
  @Override
  public ITracePoint2D getAccumulatedPoint() {
    ITracePoint2D accumulatedPointCurrent = this.getAccumulatedPointCurrent();
    if (accumulatedPointCurrent != null) {
      accumulatedPointCurrent.setLocation(accumulatedPointCurrent.getX() / this.m_accumulatedPointsCount, accumulatedPointCurrent.getY()
          / this.m_accumulatedPointsCount);
      this.m_accumulatedPointsCount = 0;
    }
    return super.getAccumulatedPoint();
  }
}
