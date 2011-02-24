/*
 *  PointHighlighterDecoratedPointPainter.java of project jchart2d, a pointHighlighter 
 *  decorating IPointPainters. 
 *  Copyright (C) 2004 - 2010, Achim Westermann, created on Apr 4, 2010
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
 * File   : $Source: /cvsroot/jchart2d/jchart2d/src/info/monitorenter/gui/chart/pointhighlighters/Attic/PointHighlighterConfigurable.java,v $
 * Date   : $Date: 2010/06/01 21:34:31 $
 * Version: $Revision: 1.5 $
 */

package info.monitorenter.gui.chart.pointhighlighters;

import info.monitorenter.gui.chart.IPointPainter;
import info.monitorenter.gui.chart.ITracePoint2D;

import java.awt.Graphics;

/**
 * A point highlighter configurable with a <code>{@link IPointPainter}</code>
 * which will do the painting work and the flag if it is consumed by paint.
 * <p>
 * 
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * 
 * @version $Revision: 1.5 $
 **/
public class PointHighlighterConfigurable extends APointHighlighter {

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -7834401912294587443L;

  /** The decorated point painter. */
  private IPointPainter< ? > m_painter;

  /**
   * Flag to configure if paint consumes this instance for the point it is
   * assigned to.
   */
  private boolean m_consumedByPaint;

  /**
   * Constructor with configuration.
   * <p>
   * 
   * @param delegate
   *          does the paint highlighting work.
   * 
   * @param isConsumedByPaint
   *          controls the consumed by paint flag.
   */
  public PointHighlighterConfigurable(final IPointPainter< ? > delegate,
      final boolean isConsumedByPaint) {
    super(isConsumedByPaint);
    this.m_painter = delegate;

  }

  /**
   * @see info.monitorenter.gui.chart.IPointHighlighter#isConsumedByPaint()
   */
  @Override
  public final boolean isConsumedByPaint() {

    return this.m_consumedByPaint;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#paintPoint(int, int, int,
   *      int, java.awt.Graphics, info.monitorenter.gui.chart.ITracePoint2D)
   */
  public final void paintPoint(int absoluteX, int absoluteY, int nextX, int nextY, Graphics g,
      ITracePoint2D original) {
    this.m_painter.paintPoint(absoluteX, absoluteY, nextX, nextY, g, original);

  }

  /**
   * @see info.monitorenter.gui.chart.IPointHighlighter#setConsumedByPaint(boolean)
   */
  @Override
  public final void setConsumedByPaint(boolean isConsumedByPaint) {
    this.m_consumedByPaint = isConsumedByPaint;

  }

  @Override
  public boolean equals(Object obj) {
 
    boolean result = false; 
    if(obj instanceof PointHighlighterConfigurable) {
      PointHighlighterConfigurable other = (PointHighlighterConfigurable)obj;
      result = this.m_consumedByPaint == other.m_consumedByPaint && this.m_painter.equals(other.m_painter);
      
    }
    return result; 
  }
  
  

}
