/*
 *  TracePainterBar.java, a trace painter that renders a bar 
 *  for each point.
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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.pointpainters.PointPainterVerticalBar;

import java.awt.Graphics;

/**
 * Renders traces by painting a bar with choosable width for each
 * {@link info.monitorenter.gui.chart.TracePoint2D} to show.
 * <p>
 * Bars are placed around the x value to render: the middle of the bar in x
 * dimension is the exact x value.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.17 $
 * 
 */
public class TracePainterVerticalBar extends ATracePainter {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = 6151930248938945671L;

  /** The implementation for rendering the point as a vertical bar. */
  private PointPainterVerticalBar m_pointPainter;

  /**
   * Creates an instance with a default bar width size of 4.
   * <p>
   * 
   * @param chart
   *          needed for bound information.
   */
  public TracePainterVerticalBar(final Chart2D chart) {
    this(4, chart);
  }

  /**
   * Creates an instance with the bar width.
   * 
   * @param barWidth
   *          the bar width in pixel to use.
   * 
   * @param chart
   *          needed for bound information.
   */
  public TracePainterVerticalBar(final int barWidth, final Chart2D chart) {
    this.m_pointPainter = new PointPainterVerticalBar(barWidth, chart);
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#endPaintIteration(java.awt.Graphics)
   */
  @Override
  public void endPaintIteration(final Graphics g2d) {
    if (g2d != null) {
      this.m_pointPainter.paintPoint(this.getPreviousX(), this.getPreviousY(), 0, 0, g2d, this
          .getPreviousPoint());
    }
  }

  /**
   * Returns the diameter of the discs to paint in pixel.
   * <p>
   * 
   * @return the diameter of the discs to paint in pixel.
   */
  public int getBarWidth() {
    return this.m_pointPainter.getBarWidth();
  }

  /**
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
   * Sets the width of the bars to paint in pixel.
   * <p>
   * 
   * @param barWidth
   *          the width of the bars to paint in pixel.
   */
  public void setBarWidth(final int barWidth) {
    this.m_pointPainter.setBarWidth(barWidth);
  }
}
