/*
 * LinePainter.java,  <enter purpose here>.
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

/**
 * <p>
 * A trace painter that adds the service of knowing the previous point that had
 * to be painted.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.3 $
 *
 */
public abstract class AbstractTracePainter implements ITracePainter {

  /**
   * The last x coordinate that was sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}.
   * <p>
   * It will be needed at {@link #endPaintIteration()}as the former method only
   * uses the first set of coordinates to store in the internal list to avoid
   * duplicates.
   * <p>
   */

  private int m_lastX;

  /**
   * The last ý coordinate that was sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}.
   * <p>
   * It will be needed at {@link #endPaintIteration()}as the former method only
   * uses the first set of coordinates to store in the internal list to avoid
   * duplicates.
   * <p>
   */

  private int m_lastY;

  /** Flag to remember if a paint iteration has ended. */
  private boolean m_isEnded = false;

  /**
   * Stores the last grapcics sent to
   * {@link #paintPoint(int, int, int, int, Graphics2D)}to allow painting
   * polygon at the end of the paint iteration.
   */

  private Graphics2D m_graphics;

  /**
   * @see aw.gui.chart.ITracePainter#paintPoint(int, int, int, int, Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {
    this.m_lastX = nextX;
    this.m_lastY = nextY;
    this.m_graphics = g;
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
   * This value will be {@link Integer#MIN_VALUE}if no previous point had to be
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
   * @see aw.gui.chart.ITracePainter#discontinue()
   */
  public void discontinue() {
    this.endPaintIteration();
    this.startPaintIteration();
  }

  /**
   * @return Returns the m_graphics.
   */
  protected final Graphics2D getGraphics() {
    return this.m_graphics;
  }

  /**
   * @see aw.gui.chart.ITracePainter#endPaintIteration()
   */
  public void endPaintIteration() {
    // nop
  }

  /**
   * @see aw.gui.chart.ITracePainter#startPaintIteration()
   */
  public void startPaintIteration() {
    // nop
  }
}
