/*
 *  TracePainterDisc.java,  <enter purpose here>.
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
package info.monitorenter.gui.chart.traces.painters;

import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;

import java.awt.Graphics2D;

/**
 * Renders traces by painting a disc (hollow circle) with choosable diameter for
 * each {@link info.monitorenter.gui.chart.TracePoint2D} to show.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.3 $
 * 
 */
public class TracePainterDisc
    extends ATracePainter {

  /** The implementation for rendering the point as a disc. */
  private PointPainterDisc m_pointPainter;

  /**
   * Creates an instance with a default disc size of 4.
   * <p>
   */
  public TracePainterDisc() {
    this.m_pointPainter = new PointPainterDisc(4);
  }

  /**
   * Creates an instance with the given disc size.
   * 
   * @param discSize
   *          the disc size in pixel to use.
   */
  public TracePainterDisc(final int discSize) {
    this.m_pointPainter = new PointPainterDisc(discSize);
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#endPaintIteration(java.awt.Graphics2D)
   */
  public void endPaintIteration(final Graphics2D g2d) {
    if (g2d != null) {
      this.m_pointPainter.paintPoint(this.getPreviousX(), this.getPreviousY(), 0, 0, g2d);
    }
  }

  /**
   * Returns the diameter of the discs to paint in pixel.
   * <p>
   * 
   * @return the diameter of the discs to paint in pixel.
   */
  public int getDiscSize() {
    return this.m_pointPainter.getDiscSize();
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#paintPoint(int, int, int,
   *      int, java.awt.Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {
    super.paintPoint(absoluteX, absoluteY, nextX, nextY, g);
    this.m_pointPainter.paintPoint(absoluteX, absoluteY, nextX, nextY, g);
  }

  /**
   * Sets the diameter of the discs to paint in pixel.
   * <p>
   * 
   * @param discSize
   *          the diameter of the discs to paint in pixel.
   */
  public void setDiscSize(final int discSize) {
    this.m_pointPainter.setDiscSize(discSize);
  }
}
