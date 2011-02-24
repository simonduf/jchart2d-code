/**
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
 *         href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </A>
 * @version 1.0
 */
public class Trace2DBijective extends Trace2DSimple {

  /** Creates new Class */
  public Trace2DBijective() {
  }

  public void addPoint(TracePoint2D p) {
    synchronized (this.renderer) {
      boolean changed = true;
      //System.out.println("addPoint(point)");
      double px = p.getX();
      synchronized (this) {
        Iterator it = this.points.iterator();
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
          double tmpx, tmpy;
          tmpx = removed.getX();
          tmpy = removed.getY();
          if (tmpx >= this.maxX) {
            this.maxXSearch();
          } else if (tmpx <= minX) {
            this.minXSearch();
          }
          if (tmpy >= this.maxY) {
            this.maxYSearch();
          } else if (tmpy <= this.minY) {
            this.minYSearch();
          }
        }
        super.addPoint(p);
      }
    }
  }

  private void maxXSearch() {
    synchronized (this) {
      double ret = -Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.points.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) > ret)
          ret = tmp;
      }
      if (ret == -Double.MAX_VALUE)
        this.maxX = 0d;
      else
        this.maxX = ret;
    }
  }

  private void minXSearch() {
    synchronized (this) {
      double ret = Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.points.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) < ret)
          ret = tmp;
      }
      if (ret == Double.MAX_VALUE) {
        this.minX = 0d;
      } else
        this.minX = ret;
    }
  }

  private void maxYSearch() {
    synchronized (this) {
      double ret = -Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.points.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) > ret)
          ret = tmp;
      }
      if (ret == -Double.MAX_VALUE)
        this.maxY = 0d;
      else
        this.maxY = ret;
    }
  }

  private void minYSearch() {
    synchronized (this) {
      double ret = Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.points.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) < ret)
          ret = tmp;
      }
      if (ret == Double.MAX_VALUE) {
        this.minY = 0d;

      } else
        this.minY = ret;
    }
  }

}