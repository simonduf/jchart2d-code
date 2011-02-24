/*
 *  LinePainter.java,  <enter purpose here>.
 *  Copyright (c) 2004 - 2010  Achim Westermann, Achim.Westermann@gmx.de
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

import info.monitorenter.gui.chart.IPointPainter;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;

import java.awt.Graphics;

/**
 * A trace painter that renders a trace by lines.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.20 $
 * 
 */
public class TracePainterLine extends ATracePainter {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -3310431930065989648L;

  /** The implementation for rendering the point as a line. */
  private IPointPainter< ? > m_pointPainter;

  /**
   * Defcon.
   * <p>
   */
  public TracePainterLine() {
    this.m_pointPainter = new PointPainterLine();
  }

  /**
   * @see info.monitorenter.gui.chart.traces.painters.ATracePainter#endPaintIteration(java.awt.Graphics)
   */
  @Override
  public void endPaintIteration(final Graphics g2d) {
    this.m_pointPainter.endPaintIteration(g2d);
  }

  /**
   * Paints a line from current to next point.
   * <p>
   * 
   * @see info.monitorenter.gui.chart.traces.painters.ATracePainter#paintPoint(int,
   *      int, int, int, java.awt.Graphics,
   *      info.monitorenter.gui.chart.ITracePoint2D)
   */
  @Override
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics g, final ITracePoint2D original) {
    super.paintPoint(absoluteX, absoluteY, nextX, nextY, g, original);
    this.m_pointPainter.paintPoint(absoluteX, absoluteY, nextX, nextY, g, original);
  }

  /**
   * @see info.monitorenter.gui.chart.traces.painters.ATracePainter#startPaintIteration(java.awt.Graphics)
   */
  @Override
  public void startPaintIteration(final Graphics g2d) {
    this.m_pointPainter.startPaintIteration(g2d);
  }
}
