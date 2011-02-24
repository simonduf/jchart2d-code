/*
 *  APointPainter.java of project jchart2d, adapter class 
 *  that implements the optional methods of the interface 
 *  IPointPainter with "no operations". 
 *  Copyright (c) 2004 - 2010 Achim Westermann, 
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
package info.monitorenter.gui.chart.pointhighlighters;

import info.monitorenter.gui.chart.IPointHighlighter;
import info.monitorenter.gui.chart.IPointPainter;

import java.awt.Graphics;

/**
 * Adapter class that implements optional methods of
 * <code>{@link IPointPainter}</code> as "no operation".
 * <p>
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.3 $
 * 
 * @since 3.0.0
 */
public abstract class APointHighlighter implements IPointHighlighter<APointHighlighter> {
  /**
   * Flag to define if this point painter in function of a point highlighter
   * assigned to a trace point will be consumed (detachted from the trace point)
   * after the first paint work for that point.
   * <p>
   */
  private boolean m_consumedByPaint;

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -8279972259015294590L;

  /**
   * Constructor with the consumed by paint flag.
   * <p>
   * 
   * @param consumedByPaint
   *          controls if this point painter in function of a point highlighter
   *          assigned to a trace point will be consumed (detached from the
   *          trace point) after the first paint work for that point.
   * 
   * @see #setConsumedByPaint(boolean)
   * 
   */
  public APointHighlighter(final boolean consumedByPaint) {
    this.setConsumedByPaint(consumedByPaint);
  }

  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(IPointHighlighter<APointHighlighter> o) {

    return this.getClass().getName().compareTo(o.getClass().getName());
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#endPaintIteration(java.awt.Graphics)
   */
  public final void endPaintIteration(final Graphics g2d) {
    // wipeout
  }

  /**
   * @see info.monitorenter.gui.chart.IPointHighlighter#isConsumedByPaint()
   */
  public boolean isConsumedByPaint() {
    return this.m_consumedByPaint;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointHighlighter#setConsumedByPaint(boolean)
   */
  public void setConsumedByPaint(final boolean isConsumedByPaint) {
    this.m_consumedByPaint = isConsumedByPaint;
  }

  /**
   * @see info.monitorenter.gui.chart.IPointPainter#startPaintIteration(java.awt.Graphics)
   */
  public final void startPaintIteration(final Graphics g2d) {
    // wipeout
  }

}
