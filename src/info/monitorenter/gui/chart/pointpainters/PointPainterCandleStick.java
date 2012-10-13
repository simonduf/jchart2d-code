/*
 *  PointPainterCandleStick.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2012, Achim Westermann, created on Oct 9, 2012
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
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
 *
 *
 * File   : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $
 * Date   : $Date: 2009/02/24 16:45:41 $
 * Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart.pointpainters;

import java.awt.Graphics;

import info.monitorenter.gui.chart.IPointPainter;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.tracepoints.CandleStick;

/**
 * A special point painter that will only be useable to render instances of
 * {@link CandleStick}.
 * <p>
 * 
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class PointPainterCandleStick extends APointPainter<PointPainterCandleStick> {

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -6708238540093878572L;

  /**
   * Constructor taking the width.
   * <p>
   * 
   * @param width
   *          the width of the {@link CandleStick}.
   **/
  public PointPainterCandleStick(final double width) {
    this.m_width = width;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(this.m_width);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PointPainterCandleStick other = (PointPainterCandleStick) obj;
    if (Double.doubleToLongBits(this.m_width) != Double.doubleToLongBits(other.m_width)) {
      return false;
    }
    return true;
  }

  /** The width of the candlestick. */
  private double m_width;


  /**
   * @see info.monitorenter.gui.chart.IPointPainter#endPaintIteration(java.awt.Graphics)
   */
  @Override
  public void endPaintIteration(Graphics g2d) {
   // nop
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#paintPoint(int, int, int,
   *      int, java.awt.Graphics, info.monitorenter.gui.chart.ITracePoint2D)
   */
  @Override
  public void paintPoint(int absoluteX, int absoluteY, int nextX, int nextY, Graphics g, ITracePoint2D original) {
    /*
     * 1. Paint the box marking space between start and stop y:
     */
    double X = absoluteX;
    double startY = absoluteY;
    /*
     * All other points have to be retrieved from original and transformed manually into 
     * absolute chart coordinates: 
     */

  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#calculateMaxX(double)
   */
  @Override
  public double calculateMaxX(double x) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#calculateMinX(double)
   */
  @Override
  public double calculateMinX(double x) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#calculateMaxY(double)
   */
  @Override
  public double calculateMaxY(double y) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#calculateMinY(double)
   */
  @Override
  public double calculateMinY(double y) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#startPaintIteration(java.awt.Graphics)
   */
  @Override
  public void startPaintIteration(Graphics g2d) {
    // nop

  }

}
