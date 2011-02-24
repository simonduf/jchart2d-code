/*
 *  AxisTitlePainterDafault.java of project jchart2d, an IAxisTitlePainter 
 *  that will render titles in a default way while adapting to x or y 
 *  axis use. 
 *  Copyright 2007 (C) Achim Westermann, created on 04.08.2007 19:40:52.
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
package info.monitorenter.gui.chart.axistitlepainters;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.IAxisTitlePainter;
import info.monitorenter.util.StringUtil;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * An <code>{@link IAxisTitlePainter}</code> implementation that will render
 * titles in a default way while adapting to x or y axis use
 * <p>
 * For x axis the title will be displayed centered below the axis. For y axis
 * the title will be displayed rotated by 90 degrees centered left of the axis.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.6 $
 */
public class AxisTitlePainterDefault implements IAxisTitlePainter {

  /** Internal support for property change management.*/
  private PropertyChangeSupport m_propertyChangeSupport = new PropertyChangeSupport(this);

  /** the font to use for painting the title. */
  private Font m_titleFont;

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String propertyName) {
    return this.m_propertyChangeSupport.getPropertyChangeListeners(propertyName);
  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String property,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(property, listener);
  }

  /**
   * Defcon.
   * <p>
   */
  public AxisTitlePainterDefault() {
    super();
  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#getHeight(info.monitorenter.gui.chart.IAxis,
   *      java.awt.Graphics2D)
   */
  public int getHeight(final IAxis axis, final Graphics2D g2d) {
    int result = 0;
    String title = axis.getTitle();
    if (!StringUtil.isEmpty(title)) {
      Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(title, g2d);
      int dimension = axis.getDimension();
      switch (dimension) {
        case Chart2D.X:
          result = (int) bounds.getHeight();
          break;
        case Chart2D.Y:
          // for y it's rotated by 90 degrees:
          result = (int) bounds.getWidth();
          break;
        default:
          throw new IllegalArgumentException(
              "Given axis.getDimension() is neither Chart2D.X nor Chart2D.Y!");
      }
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#getTitleFont()
   */
  public final Font getTitleFont() {
    return this.m_titleFont;
  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#getWidth(info.monitorenter.gui.chart.IAxis,
   *      java.awt.Graphics2D)
   */
  public int getWidth(final IAxis axis, final Graphics2D g2d) {
    int result = 0;
    String title = axis.getTitle();
    if (!StringUtil.isEmpty(title)) {
      // incorporation of our font if there:
      Font backUpFont = g2d.getFont();
      if (this.m_titleFont != null) {
        g2d.setFont(this.m_titleFont);
      }
      Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(title, g2d);
      int dimension = axis.getDimension();
      switch (dimension) {
        case Chart2D.X:
          result = (int) bounds.getWidth();
          break;
        case Chart2D.Y:
          // for y it's rotated by 90 degrees:
          result = (int) bounds.getHeight();
          break;
        default:
          throw new IllegalArgumentException(
              "Given axis.getDimension() is neither Chart2D.X nor Chart2D.Y!");
      }
      // resetting original font if it was changed:
      if (this.m_titleFont != null) {
        g2d.setFont(backUpFont);
      }
    }

    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#paintTitle(info.monitorenter.gui.chart.IAxis,
   *      java.awt.Graphics2D)
   */
  public void paintTitle(final IAxis axis, final Graphics2D g) {

    String title = axis.getTitle();
    Rectangle2D bounds;
    // manage the title font if there:
    Font backUpFont = g.getFont();
    if (this.m_titleFont != null) {
      g.setFont(this.m_titleFont);
    }

    bounds = g.getFontMetrics().getStringBounds(title, g);
    Chart2D chart = axis.getAccessor().getChart();

    int dimension = axis.getDimension();
    switch (dimension) {
      case Chart2D.X:
        int startX = chart.getXChartStart();
        int endX = chart.getXChartEnd();
        double xspace = bounds.getWidth();
        int titleStartX = (int) ((endX - startX) / 2.0 - xspace / 2.0);
        int xTickAndLabelHeight = chart.getAxisTickPainter().getMajorTickLength();
        if (chart.isPaintLabels()) {
          xTickAndLabelHeight += chart.getFontMetrics(chart.getFont()).getHeight();
        }
        g.drawString(title, titleStartX, chart.getHeight() - xTickAndLabelHeight);
        break;
      case Chart2D.Y:
        int startY = chart.getYChartStart();
        int endY = chart.getYChartEnd();
        double yspace = bounds.getWidth();
        int titleStartY = (int) ((startY - endY) / 2.0 + yspace / 2.0);
        AffineTransform tr = g.getTransform();
        AffineTransform at = AffineTransform.getTranslateInstance(10, titleStartY);
        at.rotate(-Math.PI / 2);
        g.setTransform(at);
        g.drawString(title, 0, 0);
        g.setTransform(tr);
        break;
      default:
        throw new IllegalArgumentException(
            "Given axis.getDimension() is neither Chart2D.X nor Chart2D.Y!");
    }
    // resetting original font if it was changed:
    if (this.m_titleFont != null) {
      g.setFont(backUpFont);
    }

  }

  /**
   * @see info.monitorenter.gui.chart.IAxisTitlePainter#setTitleFont(java.awt.Font)
   */
  public final void setTitleFont(final Font titleFont) {
    PropertyChangeEvent evt = new PropertyChangeEvent(this, IAxisTitlePainter.PROPERTY_TITLEFONT,
        this.m_titleFont, titleFont);
    this.m_titleFont = titleFont;
    this.m_propertyChangeSupport.firePropertyChange(evt);
  }
}
