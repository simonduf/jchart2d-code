/*
 * ILabelPainter.java,  <enter purpose here>.
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
package info.monitorenter.gui.chart;

import java.awt.Graphics2D;

/**
 * <p>
 * Interface to paint labels for a trace.
 * </p>
 * <b>Caution </b>
 * <p>
 * There is no guarantee that further manipulation on the given
 * {@link java.awt.Graphics2D} instance than painting just the label or tick
 * will not produce layout problems. E.g. changing the color or font is not
 * recommended as these should be assigned to the {@link info.monitorenter.gui.chart.ITrace2D}/
 * {@link info.monitorenter.gui.chart.Chart2D}.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.1 $
 *
 */
public interface ILabelPainter {

  /**
   * Paint the given label for the x axis.
   * <p>
   *
   * @param x
   *          the x coordinate of the baseline for the label.
   *
   * @param y
   *          the y coordinate of the baseline for the label.
   *
   * @param label
   *          the formatted label String.
   *
   * @param g
   *          the graphic context to draw on.
   */
  public void paintXLabel(final int x, final int y, String label, final Graphics2D g);

  /**
   * Paint the little marker for a label of the x axis.
   * <p>
   *
   * @param x
   *          the x coordinate of the baseline for the label.
   *
   * @param y
   *          the y coordinate of the baseline for the label.
   *
   * @param isMajorTick
   *          if true, this is a major tick.
   *
   * @param g
   *          the graphic context to draw on.
   */
  public void paintXTick(final int x, final int y, boolean isMajorTick, final Graphics2D g);

  /**
   * Paint the given label for the y axis.
   * <p>
   *
   * @param x
   *          the x coordinate of the baseline for the label.
   *
   * @param y
   *          the y coordinate of the baseline for the label.
   *
   * @param label
   *          the formatted label String.
   *
   * @param g
   *          the graphic context to draw on.
   */
  public void paintYLabel(final int x, final int y, String label, final Graphics2D g);

  /**
   * Paint the little marker for a label of the y axis.
   * <p>
   *
   * @param x
   *          the x coordinate of the baseline for the label.
   *
   * @param y
   *          the y coordinate of the baseline for the label.
   *
   * @param isMajorTick
   *          if true, this is a major tick.
   *
   * @param g
   *          the graphic context to draw on.
   */
  public void paintYTick(final int x, final int y, boolean isMajorTick, final Graphics2D g);

}
