/*
 *  TracePainterConfigurable.java, an ITracePainter implementation 
 *  that works on a given IPointPainter.
 *  Copyright (c) 2004 - 2010 Achim Westermann, Achim.Westermann@gmx.de
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

import java.awt.Graphics;

/**
 * An <code>{@link info.monitorenter.gui.chart.ITracePainter}</code>
 * implementation that works on a given <code>{@link IPointPainter}</code>.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.12 $
 * 
 * @param <T>
 *          needed for comparable<T>.
 * 
 */
public class TracePainterConfigurable<T extends IPointPainter<T>> extends ATracePainter {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = 548540923475344855L;

  /** The implementation for rendering the point as a disc. */
  private IPointPainter<T> m_pointPainter;

  /**
   * Creates an instance that works with the given point painter.
   * <p>
   * 
   * @param pointPainter
   *          the point painter to use.
   */
  public TracePainterConfigurable(final IPointPainter<T> pointPainter) {
    this.m_pointPainter = pointPainter;
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
    this.m_pointPainter.endPaintIteration(g2d);
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
   * @see info.monitorenter.gui.chart.traces.painters.ATracePainter#startPaintIteration(java.awt.Graphics)
   */
  @Override
  public void startPaintIteration(final Graphics g2d) {
    this.m_pointPainter.startPaintIteration(g2d);
  }

}
