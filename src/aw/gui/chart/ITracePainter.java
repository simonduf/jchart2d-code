/*
 * ITracePainter.java,  <enter purpose here>.
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
package aw.gui.chart;

import java.awt.Graphics2D;

/**
 * An interface that works at trace level and defines how it's points are
 * rendered.
 * <p>
 *
 * A contract for implementation is that
 * {@link java.lang.Object#equals(java.lang.Object)} has to be implemented to
 * return true if two instances are of the same class and
 * {@link java.lang.Comparable#compareTo(java.lang.Object)} is implemented
 * according to that. This is OK as trace painters are mostly to characterize by
 * their different implementation of rendering a trace.
 * <p>
 *
 * <h3>Caution</h3>
 * There is no guarantee that further manipulation on the given
 * {@link java.awt.Graphics2D} instance than painting just the label or tick
 * will not produce layout problems. E.g. changing the color or font is not
 * recommended as these should be assigned to the {@link aw.gui.chart.ITrace2D}/
 * {@link aw.gui.chart.Chart2D}.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.7 $
 *
 */
public interface ITracePainter extends Comparable {

  /**
   * Invoked to inform the painter that a discontinue in the trace to # paint
   * has occured.
   * <p>
   * This only has to be implemented by painters that collect several points of
   * {@link #paintPoint(int, int, int, int, Graphics2D)} to draw them as
   * polygons (e.g.: {@link java.awt.Graphics#drawPolyline(int[], int[], int)}).
   * <p>
   *
   */
  public void discontinue();

  /**
   * Invoked to inform implementations that a paint iteration ends for the
   * corresponding {@link ITrace2D}.
   * <p>
   *
   */
  public void endPaintIteration();

  /**
   * Paint the point given by absolute coordinates on the given graphic context.
   * <p>
   *
   * The next coordinates are also provided to allow to check how much distance
   * is available for the graphic representation of the current point.
   * <p>
   *
   * @param absoluteX
   *          the ready to use x value for the point to paint.
   *
   * @param absoluteY
   *          the ready to use y value for the point to paint.
   *
   * @param nextX
   *          the ready to use next x value for the point to paint.
   *
   * @param nextY
   *          the ready to use next < value for the point to paint.
   *
   * @param g
   *          the graphic context to paint on.
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics2D g);

  /**
   * Invoked to inform implementations that a paint iteration starts for the
   * corresponding {@link ITrace2D}.
   * <p>
   *
   */
  public void startPaintIteration();

}
