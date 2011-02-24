/*
 * LabelPainterDefault.java,  <enter purpose here>.
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
 * Default implementation for a label painter that uses all given arguments (no
 * proprietary behaviour).
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 *
 */
public class LabelPainterDefault implements ILabelPainter {

  /**
   * @see aw.gui.chart.ILabelPainter#paintXLabel(int, int, java.lang.String,
   *      java.awt.Graphics2D)
   */
  public void paintXLabel(final int x, final int y, final String label, final Graphics2D g) {
    g.drawString(label, x, y);
  }

  /**
   * @see aw.gui.chart.ILabelPainter#paintXTick(int, int, boolean,
   *      java.awt.Graphics2D)
   */
  public void paintXTick(final int x, final int y, final boolean isMajorTick, final Graphics2D g) {
    if (isMajorTick) {
      g.drawLine(x, y, x, y + 5);
    } else {
      g.drawLine(x, y, x, y + 2);
    }
  }

  /**
   * @see aw.gui.chart.ILabelPainter#paintYLabel(int, int, java.lang.String,
   *      java.awt.Graphics2D)
   */
  public void paintYLabel(final int x, final int y, final String label, final Graphics2D g) {
    g.drawString(label, x, y);
  }

  /**
   * @see aw.gui.chart.ILabelPainter#paintYTick(int, int, boolean,
   *      java.awt.Graphics2D)
   */
  public void paintYTick(final int x, final int y, final boolean isMajorTick, final Graphics2D g) {
    if (isMajorTick) {
      g.drawLine(x, y, x - 5, y);
    } else {
      g.drawLine(x, y, x - 2, y);
    }
  }
}
