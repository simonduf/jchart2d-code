/*
 *  IPointHighlighter.java of project jchart2d, point painter that may be assigned 
 *  to single trace points and optionally be consumed by a paint operation. 
 *  Copyright (C) 2004 - 2010, Achim Westermann.
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
 * File   : $Source: /cvsroot/jchart2d/jchart2d/src/info/monitorenter/gui/chart/Attic/IPointHighlighter.java,v $
 * Date   : $Date: 2010/06/01 21:34:27 $
 * Version: $Revision: 1.4 $
 */

package info.monitorenter.gui.chart;

/**
 * <code>{@link IPointPainter}</code> that may be assigned to single
 * <code>{@link ITracePoint2D}</code> instances and optionally be consumed by a
 * paint operation.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @param <T> to pass down type-safe comparable<T>. 
 * 
 * @version $Revision: 1.4 $
 * 
 **/
public interface IPointHighlighter<T extends IPointHighlighter<T>> extends IPointPainter<IPointHighlighter<T>> {

  /**
   * If this returns true the <code>{@link Chart2D}</code> will remove this
   * point highlighter from the <code>{@link ITracePoint2D}</code> after it was
   * used the first and only time to paint that point.
   * <p>
   * 
   * This behavior may be used e.g. for point - highlighting based upon mouse
   * events: The mouse events highlight the point but those mouse event
   * listeners do not know the previous highlighted point to remove the
   * highlighting.
   * 
   * @return true if this highlighter should be removed after the first painting
   *         of the point it is assigned to.
   * 
   *         TODO: rename this method to isExclusive.
   */
  public boolean isConsumedByPaint();

  /**
   * Sets whether this point highlighter should be detached from the trace point
   * after its single painting of that point.
   * <p>
   * 
   * @param isConsumedByPaint
   *          decides whether this point highlighter should be detached from the
   *          trace point after its single painting of that point.
   */
  public void setConsumedByPaint(boolean isConsumedByPaint);

}
