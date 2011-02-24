/*
 * Trace2DBijective, a list- based implementation of a ITrace2D that only
 *  allows a single occurance of a certain x- value.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 */

package aw.gui.chart;

import java.util.Iterator;

/**
 * A <code> Trace2D</code> who only allows a single occurance of a tracepoint
 * with a certain x- value xi. <br>
 * <p>
 * From <i>y1 = f(x1) = f(x2) </i> follows: <i>x1==x2 </i> (injective) <br>
 * For every y- value yi contained there exists at least one value xi
 * (surjective) <br>
 * Both qualities joined result in a bijective assignment between x and y
 * values.
 * </p>
 * <p>
 * The policy for both <code>addPoint</code>- methods is implemented as
 * follows: <br>
 * <ul>
 * <li>Every point whose x- value is not contained yet is appended at the end
 * of the internal list.</li>
 * <li>If the x- value is contained before, the tracepoint with that value is
 * removed and the new point is added to the end of the internal list. <b>In
 * that case the new tracepoint is not inserted at the location where the old
 * point used to be! </b></li>
 * </ul>
 * </p>
 *
 * @author Achim Westermann <a
 *         href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </a>
 *
 * @version $Revision: 1.6 $
 */
public class Trace2DBijective extends Trace2DSimple {

  /**
   * Defcon of this stateless instance.
   */
  public Trace2DBijective() {
  }

  /**
   * @see aw.gui.chart.AbstractTrace2D#addPointInternal(aw.gui.chart.TracePoint2D)
   */
  public boolean addPointInternal(final TracePoint2D p) {
    boolean changed = true;
    // System.out.println("addPoint(point)");
    double px = p.getX();
    synchronized (this) {
      Iterator it = this.m_points.iterator();
      TracePoint2D tmp = null, removed = null;
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
        return false;
      }
      return super.addPointInternal(p);
    }
  }
}
