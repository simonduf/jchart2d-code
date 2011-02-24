/*
 * ATracePainter.java,  base class for ITracePainter implementations.
 * Copyright (C) 2005  Achim Westermann, Achim.Westermann@gmx.de
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

import java.awt.Graphics2D;

/**
 * A trace painter that adds the service of knowing the previous point that had
 * to be painted.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.6 $
 * 
 */
public abstract class ATracePainter implements info.monitorenter.gui.chart.ITracePainter {

  /** Flag to remember if a paint iteration has ended. */
  private boolean m_isEnded = false;

  /**
   * The last x coordinate that was sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}.
   * <p>
   * It will be needed at {@link #endPaintIteration(Graphics2D)} as the former
   * method only uses the first set of coordinates to store in the internal list
   * to avoid duplicates.
   * <p>
   */

  protected int m_lastX;

  /**
   * The last � coordinate that was sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}.
   * <p>
   * It will be needed at {@link #endPaintIteration(Graphics2D)} as the former
   * method only uses the first set of coordinates to store in the internal list
   * to avoid duplicates.
   * <p>
   */

  protected int m_lastY;

  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(final Object o) {

    return this.getClass().getName().compareTo(o.getClass().getName());
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
    // nop
  }

  /**
   * Two instances are judged equal if they are of the same class.
   * <p>
   * This implies that any state of a {@link ATracePainter} is unimportant -
   * implementations that have a state (e.g. the radius for discs to paint in
   * {@link TracePainterDisc}) this method should be considered to be
   * overrriden (along with {@link #hashCode()}.
   * <p>
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(final Object obj) {
    return this.getClass() == obj.getClass();
  }

  /**
   * Returns the previous X value that had to be painted by
   * {@link #paintPoint(int, int, int, int, Graphics2D)}.
   * <p>
   * 
   * This value will be {@link Integer#MIN_VALUE} if no previous point had to be
   * painted.
   * <p>
   * 
   * @return the previous X value that had to be painted by
   *         {@link #paintPoint(int, int, int, int, Graphics2D)}.
   */
  public int getPreviousX() {
    int result = this.m_lastX;
    if (this.m_isEnded) {
      this.m_lastX = Integer.MIN_VALUE;
      if (this.m_lastY == Integer.MIN_VALUE) {
        this.m_isEnded = false;
      }

    }
    return result;
  }

  /**
   * Returns the previous Y value that had to be painted by
   * {@link #paintPoint(int, int, int, int, Graphics2D)}.
   * <p>
   * 
   * This value will be {@link Integer#MIN_VALUE} if no previous point had to be
   * painted.
   * <p>
   * 
   * @return the previous Y value that had to be painted by
   *         {@link #paintPoint(int, int, int, int, Graphics2D)}.
   */
  public int getPreviousY() {
    int result = this.m_lastY;
    if (this.m_isEnded) {
      this.m_lastY = Integer.MIN_VALUE;
      if (this.m_lastX == Integer.MIN_VALUE) {
        this.m_isEnded = false;
      }
    }
    return result;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.getClass().hashCode();
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#paintPoint(int, int, int,
   *      int, Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {
    this.m_lastX = nextX;
    this.m_lastY = nextY;
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#startPaintIteration(java.awt.Graphics2D)
   */
  public void startPaintIteration(final Graphics2D g2d) {
    // nop
  }
  
  
  
}
