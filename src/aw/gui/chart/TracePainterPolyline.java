/*
 *  TracePainterFill.java,  <enter purpose here>.
 *  Copyright (C) 2005  Achim Westermann, Achim.Westermann@gmx.de
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
package aw.gui.chart;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A trace painter that increases performance by summing up all points to render
 * for a paint iteration ({@link #startPaintIteration()},
 * {@link #endPaintIteration()}) and only invoking only one polyliine paint for
 * a paint call of the corresponding {@link aw.gui.chart.Chart2D}.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 *
 */
public class TracePainterPolyline extends AbstractTracePainter {

  /** The list of x coordinates collected in one paint iteration. */
  private List m_xPoints;

  /** The list of y coordinates collected in one paint iteration. */
  private List m_yPoints;

  /**
   * Default Constructor.
   * <p>
   *
   */
  public TracePainterPolyline() {
  }

  /**
   * @see aw.gui.chart.ITracePainter#endPaintIteration()
   */
  public void endPaintIteration() {
    if (this.getGraphics() != null) {

      int[] x = new int[this.m_xPoints.size() + 1];
      Iterator it = this.m_xPoints.iterator();
      int count = 0;
      while (it.hasNext()) {
        x[count] = ((Integer) it.next()).intValue();
        count++;
      }
      x[count] = this.getPreviousX();

      int[] y = new int[this.m_yPoints.size() + 1];
      it = this.m_yPoints.iterator();
      count = 0;
      while (it.hasNext()) {
        y[count] = ((Integer) it.next()).intValue();
        count++;
      }
      y[count] = this.getPreviousY();

      this.getGraphics().drawPolyline(x, y, x.length);
    }
  }

  /**
   * @see aw.gui.chart.ITracePainter#paintPoint(int, int, int, int,
   *      java.awt.Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {
    super.paintPoint(absoluteX, absoluteY, nextX, nextY, g);
    this.m_xPoints.add(new Integer(absoluteX));
    this.m_yPoints.add(new Integer(absoluteY));

  }

  /**
   * @see aw.gui.chart.ITracePainter#startPaintIteration()
   */
  public void startPaintIteration() {
    super.startPaintIteration();
    this.m_xPoints = new LinkedList();
    this.m_yPoints = new LinkedList();
  }

}
