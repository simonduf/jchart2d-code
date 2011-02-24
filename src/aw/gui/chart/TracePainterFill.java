/*
 * TracePainterFill.java,  <enter purpose here>.
 * Copyright (C) 2005  Achim Westermann, Achim.Westermann@gmx.de
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

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A trace painter that fills the area between trace to render and the x axis
 * baseline with it's color.
 * <p>
 *
 * Additionally it increases performance by summing up all points to render for
 * a paint iteration ({@link #startPaintIteration()},
 * {@link #endPaintIteration()}) and only invoking only one polygon paint for a
 * paint call of the corresponding {@link aw.gui.chart.Chart2D}.
 * <p>
 *
 *
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.5 $
 *
 */
public class TracePainterFill implements ITracePainter {

  /**
   * Stores the corresponding chart to know the coordinate roots for closing the
   * polygon to fill.
   */
  private Chart2D m_chart;

  /**
   * Stores the last grapcics sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}to allow painting
   * polygon at the end of the paint iteration.
   */

  private Graphics2D m_graphics;

  /**
   * Stores the last x coordinate that was sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}to allow closing the
   * polygon.
   */
  private int m_lastX;

  /**
   * Stores the last y coordinate that was sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}to allow closing the
   * polygon.
   */
  private int m_lastY;

  /** The list of x coordinates collected in one paint iteration. */
  private List m_xPoints;

  /** The list of y coordinates collected in one paint iteration. */
  private List m_yPoints;

  /**
   * Constructor with the corresponding chart.
   * <p>
   *
   * @param chart
   *          needed to get the start pixel coordinates of traces.
   */
  public TracePainterFill(final Chart2D chart) {
    this.m_chart = chart;
  }

  /**
   * @see aw.gui.chart.ITracePainter#discontinue()
   */
  public void discontinue() {
    this.endPaintIteration();
    this.startPaintIteration();
  }

  /**
   * @see aw.gui.chart.ITracePainter#endPaintIteration()
   */
  public void endPaintIteration() {
    if (this.m_graphics != null) {

      int[] x = new int[this.m_xPoints.size() + 2];
      Iterator it = this.m_xPoints.iterator();
      int count = 0;
      while (it.hasNext()) {
        x[count] = ((Integer) it.next()).intValue();
        count++;
      }
      x[count] = this.m_lastX; //
      // step down (or up) to the y=0 for the last value (in y)
      x[count + 1] = this.m_lastX;

      int[] y = new int[this.m_yPoints.size() + 2];
      it = this.m_yPoints.iterator();
      count = 0;
      while (it.hasNext()) {
        y[count] = ((Integer) it.next()).intValue();
        count++;
      }
      y[count] = this.m_lastY;
      // step down (or up) to the y=0 for the last value (in y)
      y[count + 1] = this.m_chart.getYChartStart();

      this.m_graphics.fillPolygon(x, y, x.length);
    }
  }

  /**
   * @see aw.gui.chart.ITracePainter#paintPoint(int, int, int, int,
   *      java.awt.Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {

    this.m_xPoints.add(new Integer(absoluteX));
    this.m_yPoints.add(new Integer(absoluteY));
    // don't loose the last point:
    this.m_lastX = nextX;
    this.m_lastY = nextY;
    if (this.m_graphics == null) {
      this.m_graphics = g;
    }
  }

  /**
   * @see aw.gui.chart.ITracePainter#startPaintIteration()
   */
  public void startPaintIteration() {
    this.m_xPoints = new LinkedList();
    this.m_yPoints = new LinkedList();
    this.m_graphics = null;
  }
}
