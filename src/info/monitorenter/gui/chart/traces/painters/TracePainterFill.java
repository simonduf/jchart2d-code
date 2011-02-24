/*
 *  TracePainterFill.java,  <enter purpose here>.
 *  Copyright (c) 2007  Achim Westermann, Achim.Westermann@gmx.de
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
package info.monitorenter.gui.chart.traces.painters;

import info.monitorenter.gui.chart.Chart2D;

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
 * a paint iteration (submitted by {@link #paintPoint(int, int, int, int, Graphics2D)} 
 * between {@link #startPaintIteration(Graphics2D)} and 
 * {@link #endPaintIteration(Graphics2D)}) and only invoking only one polygon paint for a
 * paint call of the corresponding {@link info.monitorenter.gui.chart.Chart2D}.
 * <p>
 * 
 * 
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.5 $
 * 
 */
public class TracePainterFill
    extends ATracePainter {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -7194158082574997539L;

  /**
   * Stores the corresponding chart to know the coordinate roots for closing the
   * polygon to fill.
   */
  private Chart2D m_chart;

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
   * @see info.monitorenter.gui.chart.ITracePainter#discontinue(java.awt.Graphics2D)
   */
  public void discontinue(final Graphics2D g2d) {
    this.endPaintIteration(g2d);
    this.startPaintIteration(g2d);
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#endPaintIteration(java.awt.Graphics2D)
   */
  public void endPaintIteration(final Graphics2D g2d) {
    if (g2d != null) {

      int[] x = new int[this.m_xPoints.size() + 4];
      x[0] = this.m_chart.getXChartStart();
      Iterator it = this.m_xPoints.iterator();
      int count = 1;
      while (it.hasNext()) {
        x[count] = ((Integer) it.next()).intValue();
        count++;
      }
      x[count] = this.m_lastX;
      // step down (or up) to the y=0 for the last value (in y)
      x[count + 1] = this.m_lastX;
      // step back to startx,starty (root)
      x[count + 2] = this.m_chart.getXChartStart();

      int[] y = new int[this.m_yPoints.size() + 4];
      y[0] = this.m_chart.getYChartStart();
      it = this.m_yPoints.iterator();
      count = 1;
      while (it.hasNext()) {
        y[count] = ((Integer) it.next()).intValue();
        count++;
      }
      y[count] = this.m_lastY;
      // step down (or up) to the y=0 for the last value (in y)
      y[count + 1] = this.m_chart.getYChartStart();
      // step back to startx,starty (root)
      y[count + 2] = this.m_chart.getYChartStart();

      g2d.fillPolygon(x, y, x.length);
    }
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#paintPoint(int, int, int,
   *      int, java.awt.Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {

    this.m_xPoints.add(new Integer(absoluteX));
    this.m_yPoints.add(new Integer(absoluteY));
    // don't loose the last point:
    this.m_lastX = nextX;
    this.m_lastY = nextY;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#startPaintIteration(java.awt.Graphics2D)
   */
  public void startPaintIteration(final Graphics2D g2d) {
    this.m_xPoints = new LinkedList();
    this.m_yPoints = new LinkedList();
  }
}
