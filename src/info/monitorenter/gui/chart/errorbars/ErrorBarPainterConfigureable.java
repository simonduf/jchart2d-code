/*
 *  ErrorBarPainterConfigureable.java of project jchart2d, base class for 
 *  an error bar painter that allows configuration of the way 
 *  the segment, start point and end point of an error bar is painted 
 *  by the use of info.monitorenter.gui.chart.IPointPainter.
 *  Copyright 2006 (C) Achim Westermann, created on 03.09.2006 19:55:53.
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
package info.monitorenter.gui.chart.errorbars;

import info.monitorenter.gui.chart.IErrorBarPainter;
import info.monitorenter.gui.chart.IErrorBarPixel;
import info.monitorenter.gui.chart.IPointPainter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * Base class for an error bar painter that allows configuration of the way the
 * segment, start point and end point of an error bar is painted by the use of
 * {@link info.monitorenter.gui.chart.IPointPainter}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.3 $
 */
public class ErrorBarPainterConfigureable implements IErrorBarPainter {

  /** The color for the end point. */
  private Color m_endPointColor;

  /**
   * The renderer of the end point of error bars.
   */
  private IPointPainter m_endPointPainter;

  /**
   * The instance that add support for firing <code>PropertyChangeEvents</code>
   * and maintaining <code>PropertyChangeListeners</code>.
   * <p>
   */
  protected PropertyChangeSupport m_propertyChangeSupport = new SwingPropertyChangeSupport(this);

  /** The color for the segment. */
  private Color m_segmentColor;

  /**
   * The renderer of the segment (distance between origin and end of error bar)
   * of error bars.
   */
  private IPointPainter m_segmentPainter;

  /** The color for the start point. */
  private Color m_startPointColor;

  /**
   * The renderer of the start point of error bars.
   */
  private IPointPainter m_startPointPainter;

  /**
   * Creates an instance that by default will not render any error bar.
   * <p>
   * 
   * It then has to be configured with the remaining methods as desired.
   * <p>
   * 
   * @see #setEndPointColor(Color)
   * @see #setEndPointPainter(IPointPainter)
   * @see #setSegmentColor(Color)
   * @see #setSegmentPainter(IPointPainter)
   * @see #setStartPointColor(Color)
   * @see #setStartPointPainter(IPointPainter)
   */
  public ErrorBarPainterConfigureable() {
    super();
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#addPropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public final void addPropertyChangeListener(final String propertyName,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getEndPointColor()
   */
  public final Color getEndPointColor() {
    return this.m_endPointColor;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getEndPointPainter()
   */
  public final IPointPainter getEndPointPainter() {
    return this.m_endPointPainter;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String property) {
    return this.m_propertyChangeSupport.getPropertyChangeListeners(property);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getSegmentColor()
   */
  public final Color getSegmentColor() {
    return this.m_segmentColor;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getSegmentPainter()
   */
  public final IPointPainter getSegmentPainter() {
    return this.m_segmentPainter;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getStartPointColor()
   */
  public final Color getStartPointColor() {
    return this.m_startPointColor;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getStartPointPainter()
   */
  public final IPointPainter getStartPointPainter() {
    return this.m_startPointPainter;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#paintErrorBar(int, int,
   *      java.awt.Graphics2D, info.monitorenter.gui.chart.IErrorBarPixel)
   */
  public void paintErrorBar(final int absoluteX, final int absoluteY, final Graphics2D g,
      final IErrorBarPixel errorBar) {
    // negative x error:
    int error = errorBar.getNegativeXErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {

      this.paintErrorBarPart(absoluteX, absoluteY, (int) error, absoluteY, this.m_segmentPainter,
          this.m_segmentColor, g);
      this.paintErrorBarPart(absoluteX, absoluteY, (int) error, absoluteY,
          this.m_startPointPainter, this.m_startPointColor, g);
      this.paintErrorBarPart((int) error, absoluteY, absoluteX, absoluteY, this.m_endPointPainter,
          this.m_endPointColor, g);
    }
    // positive x error:
    error = errorBar.getPositiveXErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      this.paintErrorBarPart(absoluteX, absoluteY, error, absoluteY, this.m_segmentPainter,
          this.m_segmentColor, g);
      this.paintErrorBarPart(absoluteX, absoluteY, error, absoluteY, this.m_startPointPainter,
          this.m_startPointColor, g);
      this.paintErrorBarPart((int) error, absoluteY, absoluteX, absoluteX, this.m_endPointPainter,
          this.m_endPointColor, g);
    }

    // negative y error:
    error = errorBar.getNegativeYErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      this.paintErrorBarPart(absoluteX, absoluteY, absoluteX, error, this.m_segmentPainter,
          this.m_segmentColor, g);
      this.paintErrorBarPart(absoluteX, absoluteY, absoluteX, (int) error,
          this.m_startPointPainter, this.m_startPointColor, g);
      this.paintErrorBarPart(absoluteX, (int) error, absoluteX, absoluteY, this.m_endPointPainter,
          this.m_endPointColor, g);
    }
    // positive y error:
    error = errorBar.getPositiveYErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      this.paintErrorBarPart(absoluteX, absoluteY, absoluteX, error, this.m_segmentPainter,
          this.m_segmentColor, g);
      this.paintErrorBarPart(absoluteX, absoluteY, absoluteX, error, this.m_startPointPainter,
          this.m_startPointColor, g);
      this.paintErrorBarPart(absoluteX, (int) error, absoluteX, absoluteY, this.m_endPointPainter,
          this.m_endPointColor, g);
    }
  }

  /**
   * Internally renders the given part of the error bar with support for color
   * and invisibility (null painter) management.
   * <p>
   * Factored out code to keep calling method
   * {@link #paintErrorBar(int, int, Graphics2D, IErrorBarValue)} smaller.
   * <p>
   * 
   * @param absoluteX
   *          the x coordinate in px of the error bar part.
   * 
   * @param absoluteY
   *          the y coordinate in px of the error bar part.
   * 
   * @param nextX
   *          the next x coordinate in px of the error bar part (only relevant
   *          for segment).
   * 
   * @param nextY
   *          the next y coordinate in px of the error bar part (only relevant
   *          for segment).
   * 
   * @param pointPainter
   *          the painter to use.
   * 
   * @param color
   *          the color or null.
   * 
   * @param g2d
   *          needed for painting.
   */
  private final void paintErrorBarPart(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final IPointPainter pointPainter, final Color color, final Graphics2D g2d) {
    if (pointPainter != null) {
      boolean colorChange = false;
      Color backupColor = g2d.getColor();
      if (color != null) {
        colorChange = true;
        g2d.setColor(color);
      }
      pointPainter.paintPoint(absoluteX, absoluteY, nextX, nextY, g2d);
      if (colorChange) {
        g2d.setColor(backupColor);
      }
    }
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#removePropertyChangeListener(java.lang.String,
   *      java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String property,
      final PropertyChangeListener listener) {
    this.m_propertyChangeSupport.removePropertyChangeListener(property, listener);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setEndPointColor(java.awt.Color)
   */
  public final void setEndPointColor(final Color endPointColor) {
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_ENDPOINT_COLOR, this.m_endPointColor,
        endPointColor);
    this.m_endPointColor = endPointColor;
  }

  /**
   * @param endPointPainter
   *          The endPointPainter to set.
   */
  public final void setEndPointPainter(final IPointPainter endPointPainter) {
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_ENDPOINT, this.m_endPointPainter,
        endPointPainter);
    this.m_endPointPainter = endPointPainter;
  }

  /**
   * @param segmentColor
   *          The segmentColor to set.
   */
  public final void setSegmentColor(final Color segmentColor) {
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_SEGMENT_COLOR, this.m_endPointColor,
        segmentColor);
    this.m_segmentColor = segmentColor;
  }

  /**
   * @param segmentPainter
   *          The segmentPainter to set.
   */
  public final void setSegmentPainter(final IPointPainter segmentPainter) {
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_SEGMENT, this.m_segmentPainter,
        segmentPainter);
    this.m_segmentPainter = segmentPainter;
  }

  /**
   * @param startPointColor
   *          The startPointColor to set.
   */
  public final void setStartPointColor(final Color startPointColor) {
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_STARTPOINT_COLOR,
        this.m_startPointColor, startPointColor);
    this.m_startPointColor = startPointColor;
  }

  /**
   * @param startPointPainter
   *          The startPointPainter to set.
   */
  public final void setStartPointPainter(final IPointPainter startPointPainter) {
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_STARTPOINT, this.m_startPointPainter,
        startPointPainter);
    this.m_startPointPainter = startPointPainter;
  }

}
