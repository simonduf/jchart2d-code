/*
 * Trace2DBijective, a list- based implementation of a ITrace2D that only
 *  allows a single occurance of a certain x- value.
 * Copyright (c) 2007  Achim Westermann, Achim.Westermann@gmx.de
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
 */

package info.monitorenter.gui.chart.traces;

import info.monitorenter.gui.chart.TracePoint2D;

import java.util.Iterator;

/**
 * A <code> Trace2D</code> who only allows a single occurance of a tracepoint with a certain x-
 * value xi. <br>
 * <p>
 * From <i>y1 = f(x1) = f(x2) </i> follows: <i>x1==x2 </i> (injective) <br>
 * For every y- value yi contained there exists at least one value xi (surjective) <br>
 * Both qualities joined result in a bijective assignment between x and y values.
 * </p>
 * <p>
 * The policy for both <code>addPoint</code>- methods is implemented as follows: <br>
 * <ul>
 * <li>Every point whose x- value is not contained yet is appended at the end of the internal list.</li>
 * <li>If the x- value is contained before, the tracepoint with that value is removed and the new
 * point is added to the end of the internal list. <b>In that case the new tracepoint is not
 * inserted at the location where the old point used to be! </b></li>
 * </ul>
 * </p>
 * 
 * @author Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </a>
 * @version $Revision: 1.7 $
 */
public class Trace2DBijective
    extends Trace2DSimple {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = 2913093358404794473L;

  /**
   * Defcon of this stateless instance.
   */
  public Trace2DBijective() {
    // nop
  }

  /**
   * @see ATrace2D#addPointInternal(info.monitorenter.gui.chart.TracePoint2D)
   */
  public boolean addPointInternal(final TracePoint2D p) {
    boolean result;
    double px = p.getX();
    synchronized (this) {
      Iterator it = this.m_points.iterator();
      TracePoint2D tmp = null;
      TracePoint2D removed = null;
      while (it.hasNext()) {
        tmp = (TracePoint2D) it.next();
        if (tmp.getX() == px) {
          it.remove();
          removed = tmp;
          break;
        }
      }
      if (removed != null) {
        this.removePoint(removed);
        // dont use bound check routines of calling addPoint.
        result = false;
      } else {
        result = super.addPointInternal(p);
      }
    }
    return result;
  }
}
