/*
 * TracePainterCompound.java,  <enter purpose here>.
 * Copyright (C) 2006  Achim Westermann, Achim.Westermann@gmx.de
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
 * <p>
 * A trace painter that contains several trace painters to have a trace being
 * painted by several painters.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 *
 */
public final class TracePainterCompound implements ITracePainter {

  /** The internal lists of the painters to delegate to. */
  private List m_tracePainters = new LinkedList();

  /**
   * Constructor with the painters to use.
   * <p>
   *
   * The trace will be rendered by the painters in the order they are in the
   * given argument.
   *
   * @param painters
   *          the painters to use.
   */
  public TracePainterCompound(final ITracePainter[] painters) {
    for (int i = 0; i < painters.length; i++) {
      this.m_tracePainters.add(painters[i]);
    }
  }

  /**
   * @see aw.gui.chart.ITracePainter#paintPoint(int, int, int, int,
   *      java.awt.Graphics2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g) {
    Iterator itPainter = this.m_tracePainters.iterator();
    ITracePainter painter;
    while (itPainter.hasNext()) {
      painter = (ITracePainter) itPainter.next();
      painter.paintPoint(absoluteX, absoluteY, nextX, nextY, g);
    }
  }

  /**
   * @see aw.gui.chart.ITracePainter#startPaintIteration()
   */
  public void startPaintIteration() {
    Iterator itPainter = this.m_tracePainters.iterator();
    ITracePainter painter;
    while (itPainter.hasNext()) {
      painter = (ITracePainter) itPainter.next();
      painter.startPaintIteration();
    }
  }

  /**
   * @see aw.gui.chart.ITracePainter#endPaintIteration()
   */
  public void endPaintIteration() {
    Iterator itPainter = this.m_tracePainters.iterator();
    ITracePainter painter;
    while (itPainter.hasNext()) {
      painter = (ITracePainter) itPainter.next();
      painter.endPaintIteration();
    }
  }



  /**
   * @see aw.gui.chart.ITracePainter#discontinue()
   */
  public void discontinue() {
    Iterator itPainter = this.m_tracePainters.iterator();
    ITracePainter painter;
    while (itPainter.hasNext()) {
      painter = (ITracePainter) itPainter.next();
      painter.discontinue();
    }
  }
}
