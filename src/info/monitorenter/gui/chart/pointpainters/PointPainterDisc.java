/*
 *  PointPainterDisc.java of project jchart2d, paints round points. 
 *  Copyright (c) 2006 - 2010 Achim Westermann, created on 03.09.2006 20:27:06.
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

import info.monitorenter.gui.chart.ITracePoint2D;

import java.awt.Graphics;

/**
 * Renders points in form of a disc with configurable diameter.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.18 $
 */
public class PointPainterDisc extends APointPainter {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -6317473632026920774L;

  /** The diameter of the discs to paint. */
  private int m_discSize;

  /**
   * Cached m_discSize divided by two to save division for each point to render.
   */
  private int m_halfDiscSize;

  /**
   * Creates an instance with a default disc size of 4.
   * <p>
   */
  public PointPainterDisc() {
    this.setDiscSize(4);
  }

  /**
   * Creates an instance with the given disc diameter.
   * 
   * @param diameter
   *          the disc size in pixel to use.
   */
  public PointPainterDisc(final int diameter) {
    this.setDiscSize(diameter);
  }

  /**
   * Equality is judged if the object is also of the same type as this and the
   * disc size is the same.
   * <p>
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj instanceof PointPainterDisc) {
      PointPainterDisc other = (PointPainterDisc) obj;
      result = other.m_discSize == this.m_discSize;
    }
    return result;
  }

  /**
   * Returns the diameter of the discs to paint in pixel.
   * <p>
   * 
   * @return the diameter of the discs to paint in pixel.
   */
  public int getDiscSize() {
    return this.m_discSize;
  }

  /**
   * Uses the class hashcode with addition of the disc size: hashcode is the
   * same for all instances of the same class with the same disc size.
   * <p>
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int result = PointPainterDisc.class.hashCode();
    result += this.m_discSize;
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#paintPoint(int, int, int,
   *      int, java.awt.Graphics, info.monitorenter.gui.chart.ITracePoint2D)
   */
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics g, final ITracePoint2D original) {
    g.drawOval(absoluteX - this.m_halfDiscSize, absoluteY - this.m_halfDiscSize, this.m_discSize,
        this.m_discSize);
  }

  /**
   * Sets the diameter of the discs to paint in pixel.
   * <p>
   * 
   * @param discSize
   *          the diameter of the discs to paint in pixel.
   */
  public void setDiscSize(final int discSize) {
    this.m_discSize = discSize;
    this.m_halfDiscSize = this.m_discSize / 2;
  }

}
