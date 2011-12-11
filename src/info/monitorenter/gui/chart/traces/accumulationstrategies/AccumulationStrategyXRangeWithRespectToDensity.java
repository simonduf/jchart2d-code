/*
 *  AccumulationStrategyAmountOfPoints.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2011, Achim Westermann, created on Dec 11, 2011
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

package info.monitorenter.gui.chart.traces.accumulationstrategies;

import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITrace2DDataAccumulating;
import info.monitorenter.gui.chart.ITracePoint2D;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
/**
 * This strategy will use the x-range and take all following points out of
 * the <code>source</code> (under the assumption trace is sorted by
 * x-values) that are within the range and then accumulate them to a single
 * trace point. Best use this whenever you have a trace with ordered x
 * values to get accumulation based on density of x-values and thereby avoid
 * loss of important points in areas with low x-value densities.
 * <p>
 */
public class AccumulationStrategyXRangeWithRespectToDensity extends AAccumulationStrategy {
  
  /**
   * {@inheritDoc}
   */
  public AccumulationStrategyXRangeWithRespectToDensity(IAccumulationFunction accumulationFunction) {
    super(accumulationFunction);
  }

  /**
     * Accumulates all points that come from <code>source</code> and are in
     * the given <code>xRange</code>. Once a point comes from
     * <code>source</code> outside the given <code>xRange</code> accumulation
     * is done and result is returned.
     * <p>
     * In this case <code>xRange</code> is not the total visible range given
     * to {@link ITrace2DDataAccumulating#iterator(int)} but a segment within
     * that range.
     * <p>
     * 
     * Note that this only makes sense for traces with points ordered
     * ascending by x-value.
     * <p>
     * 
     */
    public Iterator<ITracePoint2D> iterator(final ITrace2D source, final int amountOfPoints) {
      throw new NotImplementedException();
    }
    
}
