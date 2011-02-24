/**
 * Trace2DReplacing, a list- based implementation of a ITrace2D.
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

/**
 * Has the behaviour of <code>Trace2DBijective</code> and an additional
 * features: <br>
 * <ul>
 * <li>All tracepoints that are added are stored unchanged in a LinkedList.
 * </li>
 * <li>All traceoints added whose x- values are not already contained are added
 * to the end.</li>
 * <li>If a tracepoint is inserted whose x - value already exists in the List,
 * the old tracepoint with that value will be replaced by the new tracepoint.
 * </li>
 * </UL>
 * 
 * @see Trace2DBijective
 * 
 * @author Achim Westermann <a
 *         href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </A>
 * @version 1.0
 */
public class Trace2DReplacing extends Trace2DSimple {

  /** Creates new Trace2DOrdered */
  public Trace2DReplacing() {
  }

  /**
   * In case p has an x- value already contained, the old tracepoint with that
   * value will be replaced by the new one. Else the new tracepoint will be
   * added to the end, not caring wether tracepoints with a higher x- value are
   * contained.
   */
  public void addPoint(TracePoint2D p) {
    boolean changed = true;
    //System.out.println("addPoint(point)");
    TracePoint2D old;
    synchronized (this.renderer) {
      synchronized (this.points) {
        int index = -1;
        double tmp = 0;
        if (firsttime) {
          this.maxX = p.getX();
          this.minX = this.maxX;
          this.maxY = p.getY();
          this.minY = this.maxY;
          firsttime = false;
          this.points.add(p);
        } else {
          if ((index = this.points.indexOf(p)) != -1) {
            old = (TracePoint2D) this.points.get(index);
            old.setLocation(old.getX(), p.getY());
            if ((tmp = p.getY()) > this.maxY)
              this.maxY = tmp;
            if ((tmp = p.getY()) < this.minY)
              this.minY = tmp;
            this.fireTraceChanged(old);
            return;
          } else {
            this.points.add(p);
            if ((tmp = p.getX()) > this.maxX)
              this.maxX = tmp;
            if ((tmp = p.getX()) < this.minX)
              this.minX = tmp;
            if ((tmp = p.getY()) > this.maxY)
              this.maxY = tmp;
            if ((tmp = p.getY()) < this.minY)
              this.minY = tmp;
            this.fireTraceChanged(p);
          }
        }
      }
    }
  }

}