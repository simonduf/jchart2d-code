/*
 *  IteratorITracePoint2DUtil.java of project jchart2d, <enterpurposehere>. 
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

import java.util.Iterator;

import info.monitorenter.gui.chart.ITrace2DDataAccumulating;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.ITracePointProvider;

/**
 * Utility class helper, created for supporting data accumulation of
 * {@link ITrace2DDataAccumulating}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class IteratorITracePoint2DUtil {

  /**
   * Utility class constructor: hide as instance not needed.
   * <p>
   */
  private IteratorITracePoint2DUtil() {
    // nop
  }

  /**
   * Scrolls the given iterator to the first point that is above the given
   * x-coordinate and returns a synthetic interpolated point that exactly has
   * the given x-coordinate as x value (position 0) or null if the first point
   * was already above the x bound and the first trace point that was above the
   * bound (position 1).
   * <p>
   * Note: this only makes sense if the given iterator returns trace points in
   * ascending order of x values!
   * <p>
   * Note: the state of the given iterator is changed. After this call you may
   * continue iterating. You most probably will first consume the position 0 of
   * the result (interpolated point to the lower x bound) and position 1 of the
   * result (first trace point above given x point).
   * <p>
   * 
   * @param traceIt
   *          the source iterator to scroll.
   * 
   * @param tracePointProvider
   *          needed to create a point that interpolates to <code>startX</code>.
   * 
   * @param startX
   *          the lower x bound to scroll to.
   * 
   * @return a synthetic interpolated point that exactly has the given
   *         x-coordinate as x value (position 0) or null if the first point was
   *         already above the x bound and the first trace point that was above
   *         the bound (position 1).
   */
  public static ITracePoint2D[] scrollToFirstVisibleXValueWithInterpolation(final Iterator<ITracePoint2D> traceIt,
      final ITracePointProvider tracePointProvider, final int startX) {
    ITracePoint2D[] result = new ITracePoint2D[2];

    ITracePoint2D interpolated = null;
    ITracePoint2D previous = null;
    ITracePoint2D current;
    while (traceIt.hasNext()) {
      current = traceIt.next();
      if (current.getX() >= startX) {
        if(previous == null) {
          // no interpolation possible:
          result[0] = null;
        } else {
          // interpolate
        }
        result[1] = current;
        break;
      }
    }
    return result;
  }

}
