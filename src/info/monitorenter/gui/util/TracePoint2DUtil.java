/*
 *  TracePoint2DUtil.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2011, Achim Westermann, created on Nov 13, 2011
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

package info.monitorenter.gui.util;

import info.monitorenter.gui.chart.IPointPainter;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.ITracePointProvider;
import info.monitorenter.gui.chart.tracepoints.TracePoint2D;

/**
 * Utility helper class for basic computations on
 * <code>{@link ITracePoint2D}</code> instances.
 * <p>
 * 
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class TracePoint2DUtil {

  /**
   * Interpolates (linear) the two neighboring points.
   * <p>
   * 
   * 
   * Calling this method only makes sense if argument invisible is not null or
   * if argument visible is not null (if then invisible is null, the visible
   * point will be returned).
   * <p>
   * <b>Precondition<b/><br/>
   * Given points scaled values ({@link TracePoint2D#getScaledX()}, {@link TracePoint2D#getScaledY()}) 
   * are computed by division of the x and y rage. So the bounding box (0,0) (1,1) defines the visible 
   * area.
   * <p>
   * <b>Important</b><br/>
   * Visibility is determined only by their internally normalized coordinates
   * that are within [0.0,1.0] for visible points. The original x and y value is
   * not set to the resulting point, you have to scale this up (by
   * multiplication with your x and y value range) by yourself!
   * <p>
   * Interpolation is done by the two point form: <br/>
   * <nobr>(y - y1)/(x - x1) = (y2 - y1)/(x2 - x1) solved to the missing
   * value.</nobr> Several attempts to pick a bound are made for finding the
   * proper bound to interpolate to in that order:
   * <ul>
   * <li>x = 1.0</li>
   * <li>x = 0.0</li>
   * <li>y = 1.0</li>
   * <li>y = 0.0</li>
   * </ul>
   * <p>
   * 
   * @param visible
   *          the visible point.
   * 
   * @param invisible
   *          the invisible point.
   * 
   * @param tracePointProvider
   *          needed to create a new trace point of the desired subtype.
   * 
   * @return the interpolation towards the exceeded bound or the given visible
   *         point if a mathematical miracle or a programming error was
   *         encountered.
   */
  public static ITracePoint2D interpolateVisible(final ITracePoint2D invisible, final ITracePoint2D visible,
      final ITracePointProvider tracePointProvider) {

    ITracePoint2D result;
    /*
     * In the first call invisible is null because it is the previous point
     * (there was no previous point: just return the new point):
     */
    if (invisible == null) {
      result = visible;
    } else {
      /*
       * Interpolation is done by the two point form: (y - y1)/(x - x1) = (y2 -
       * y1)/(x2 - x1) solved to the missing value.
       */
      // interpolate
      double xInterpolate = Double.NaN;
      double yInterpolate = Double.NaN;
      // find the bounds that has been exceeded:
      // It is possible that two bound have been exceeded,
      // then only one interpolation will be valid:
      boolean interpolated = false;
      boolean interpolatedWrong = false;
      if (invisible.getScaledX() > 1.0) {
        // right x bound
        xInterpolate = 1.0;
        yInterpolate = (visible.getScaledY() - invisible.getScaledY()) / (visible.getScaledX() - invisible.getScaledX())
            * (1.0 - invisible.getScaledX()) + invisible.getScaledY();
        interpolated = true;
        interpolatedWrong = Double.isNaN(yInterpolate) || yInterpolate < 0.0 || yInterpolate > 1.0;
      }
      if ((invisible.getScaledX() < 0.0) && (!interpolated || interpolatedWrong)) {
        // left x bound
        xInterpolate = 0.0;
        yInterpolate = (visible.getScaledY() - invisible.getScaledY()) / (visible.getScaledX() - invisible.getScaledX())
            * -invisible.getScaledX() + invisible.getScaledY();
        interpolated = true;
        interpolatedWrong = Double.isNaN(yInterpolate) || yInterpolate < 0.0 || yInterpolate > 1.0;
      }
      if ((invisible.getScaledY() > 1.0) && (!interpolated || interpolatedWrong)) {
        // upper y bound, checked
        yInterpolate = 1.0;
        xInterpolate = (1.0 - invisible.getScaledY()) * (visible.getScaledX() - invisible.getScaledX())
            / (visible.getScaledY() - invisible.getScaledY()) + invisible.getScaledX();
        interpolated = true;
        interpolatedWrong = Double.isNaN(xInterpolate) || xInterpolate < 0.0 || xInterpolate > 1.0;

      }
      if ((invisible.getScaledY() < 0.0) && (!interpolated || interpolatedWrong)) {
        // lower y bound
        yInterpolate = 0.0;
        xInterpolate = -invisible.getScaledY() * (visible.getScaledX() - invisible.getScaledX())
            / (visible.getScaledY() - invisible.getScaledY()) + invisible.getScaledX();
        interpolated = true;
        interpolatedWrong = Double.isNaN(xInterpolate) || xInterpolate < 0.0 || xInterpolate > 1.0;
      }
      if (interpolatedWrong) {
        /*
         * TODO: Maybe throw an exception as this must not happen under the
         * assumption that the visible point was really visible (within bounds
         * (0..1,0..1).
         */
        result = visible;
      } else {
        result = tracePointProvider.createTracePoint(0, 0);
        // transfer potential point highlighters to the synthetic point:
        for (IPointPainter< ? > highlighter : invisible.getAdditionalPointPainters()) {
          result.addAdditionalPointPainter(highlighter);
        }
        result.setScaledX(xInterpolate);
        result.setScaledY(yInterpolate);
        result.setListener(invisible.getListener());
      }
    }
    return result;
  }
}
