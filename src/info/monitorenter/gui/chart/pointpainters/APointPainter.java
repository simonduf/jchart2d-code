/*
 *  APointPainter.java of project jchart2d, adapter class 
 *  that implements the optional methods of the interface 
 *  IPointPainter with "no operations". 
 *  Copyright (c) 2007 Achim Westermann,/*
 *  PointPainterColorStepper.java of project jchart2d, wrapper 
 *  for IPointPainter implementations that adds color stepping 
 *  of subsequent paint point invocations. 
 *  Copyright (c) 2007 Achim Westermann, created on 03.09.2006 20:27:06.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart.pointpainters;

import info.monitorenter.gui.chart.IPointPainter;

import java.awt.Graphics;

/**
 * Adapter class that implements optional methods of
 * <code>{@link IPointPainter}</code> as "no operation".
 * <p>
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 3.0.0
 * 
 */
public abstract class APointPainter implements IPointPainter {

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -8279972259015294590L;

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#endPaintIteration(java.awt.Graphics)
   */
  public void endPaintIteration(final Graphics g2d) {
    // nop
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#startPaintIteration(java.awt.Graphics)
   */
  public void startPaintIteration(final Graphics g2d) {
    // nop
  }

}
