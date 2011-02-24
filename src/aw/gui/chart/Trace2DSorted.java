/**
 * Trace2DSorted, a TreeSet- based implementation of a ITrace2D that performs 
 * insertion- sort of TracePoint2D - instances by their x- value.
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

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Has the behaviour of <code>Trace2DReplacing</code> and an additional
 * features: <br>
 * <ul>
 * <li>All traceoints added whose x- values are not already contained are added
 * to the internal Tree ordered by growing x-values. Therefore it is guaranteed
 * that the tracepoints will be sorted in ascending order of x- values at any
 * time.</li>
 * </UL>
 * 
 * Because sorted insertion of a List causes n! index- operations (
 * <code>get(int i)</code>) additional to the comparisons this class does not
 * extend <code>Trace2DSimple</code> which uses a List. Instead a
 * <code>TreeSet </code> is used.
 * 
 * @author Achim Westermann <a
 *         href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </A>
 * @version 1.0
 */
public class Trace2DSorted implements ITrace2D {
  protected TreeSet points = new TreeSet();

  protected Color color;

  protected List changeListeners = new LinkedList();

  protected String label = "";

  double maxX, minX, maxY, minY;

  protected boolean firsttime = true;

  protected String name = "";

  protected String physunit = "";

  Chart2D renderer;

  /** Creates new Trace2DOrdered */
  public Trace2DSorted() {
  }

  /**
   * In case p has an x- value already contained, the old tracepoint with that
   * value will be replaced by the new one. Else the new tracepoint will be
   * added at an index in order to keep the ascending order of tracepoints with
   * a higher x- value are contained.
   */
  public void addPoint(TracePoint2D p) {
    synchronized (this.renderer) {
      synchronized (this.points) {
        if (firsttime) {
          this.maxX = p.getX();
          this.minX = this.maxX;
          this.maxY = p.getY();
          this.minY = this.maxY;
          firsttime = false;
        } else {
          double tmp = 0;
          if ((tmp = p.getX()) > this.maxX)
            this.maxX = tmp;
          if ((tmp = p.getX()) < this.minX)
            this.minX = tmp;
          if ((tmp = p.getY()) > this.maxY)
            this.maxY = tmp;
          if ((tmp = p.getY()) < this.minY)
            this.minY = tmp;
        }
        this.points.remove(p); //remove eventually contained to allow adding of
                               // new one.
        this.points.add(p);
        this.fireTraceChanged(p);
      }
    }
  }

  public void addPoint(double x, double y) {
    TracePoint2D p = new TracePoint2D(this, x, y);
    this.addPoint(p);
  }

  /**
   * Returns the original maximum x- value ignoring the offsetX.
   */
  public double getMaxX() {
    return this.maxX;
  }

  /**
   * Returns the original maximum y- value ignoring the offsetY.
   */

  public double getMaxY() {
    return this.maxY;
  }

  /**
   * Returns the original minimum x- value ignoring the offsetX.
   */
  public double getMinX() {
    return this.minX;
  }

  /**
   * Returns the original minimum y- value ignoring the offsetY.
   */
  public double getMinY() {
    return this.minY;
  }

  public Color getColor() {
    return this.color;
  }

  private void maxXSearch() {
    synchronized (this) {
      double ret = Double.MIN_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.points.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) > ret)
          ret = tmp;
      }
      if (ret == Double.MIN_VALUE)
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
      double ret = Double.MIN_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.points.iterator();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) > ret)
          ret = tmp;
      }
      if (ret == Double.MIN_VALUE)
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

  public void setColor(Color color) {
    this.color = color;
  }

  public Iterator iterator() {
    return this.points.iterator();
  }

  public void addChangeListener(ITrace2D.Trace2DListener x) {
    changeListeners.add(x);
    x.traceChanged(new Trace2DChangeEvent(this, ALL_POINTS_CHANGED)); // Aufruf
                                                                      // des
                                                                      // neuen
                                                                      // ChangeListeners
                                                                      // um zu
                                                                      // aktualisieren.
  }

  public void removeChangeListener(ITrace2D.Trace2DListener x) {
    changeListeners.remove(x);
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public void fireTraceChanged(TracePoint2D d) {
    Trace2DChangeEvent fire = new Trace2DChangeEvent(this, d);
    synchronized (this.points) {
      Iterator it = this.changeListeners.iterator();
      while (it.hasNext()) {
        ((ITrace2D.Trace2DListener) it.next()).traceChanged(fire);
      }
    }
  }

  public void pointChanged(TracePoint2D d) {
    this.fireTraceChanged(d);
  }

  /**
   * Tell wether no tracepoints are avaiable.
   */
  public boolean isEmpty() {
    return this.points.size() == 0;
  }

  public String toString() {
    return this.points.toString();
  }

  public void setName(String s) {
    this.name = s;
  }

  public String getName() {
    return this.name;
  }

  /**
   * @see #setPhysicalUnits(String x,String y)
   */
  public String getPhysicalUnits() {
    return this.physunit;
  }

  public void setPhysicalUnits(String xunit, String yunit) {
    if ((xunit == null) && (yunit == null))
      return;
    if ((xunit == null) && (yunit != null)) {
      this.physunit = new StringBuffer("[x: , y: ").append(yunit).append("]").toString();
      return;
    }
    if ((xunit != null) && (yunit == null)) {
      this.physunit = new StringBuffer("[x: ").append(xunit).append(", y: ]").toString();
      return;
    }
    this.physunit = new StringBuffer("[x: ").append(xunit).append(", y: ").append(yunit).append("]").toString();
  }

  /**
   * @return Returns the renderer.
   */
  public Chart2D getRenderer() {
    return renderer;
  }

  /**
   * @param renderer
   *          The renderer to set.
   */
  public void setRenderer(Chart2D renderer) {
    this.renderer = renderer;
  }
}