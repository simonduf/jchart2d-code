/*
 *  ErrorBarPainter.java of project jchart2d, base class for 
 *  an error bar painter that allows configuration of the way 
 *  the segment, start point and end point of an error bar is painted 
 *  by the use of info.monitorenter.gui.chart.IPointPainter.
 *  Copyright (c) 2007 - 2010 Achim Westermann, created on 03.09.2006 19:55:53.
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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IErrorBarPainter;
import info.monitorenter.gui.chart.IErrorBarPixel;
import info.monitorenter.gui.chart.IPointPainter;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * Implementation of an error bar painter that allows configuration of the way
 * the connection, start point and end point of an error bar is painted by the
 * use of {@link info.monitorenter.gui.chart.IPointPainter}.
 * <p>
 * 
 * Property change events are fired as described in method
 * 
 * <code>{@link info.monitorenter.gui.chart.IErrorBarPainter#addPropertyChangeListener(String, PropertyChangeListener) }</code>
 * . Note that adding property change listeners to the nested access facades of
 * type
 * <code>{@link info.monitorenter.gui.chart.IErrorBarPainter.ISegment}</code>
 * accessible via <code>getXXXSegment()</code> methods will fire the
 * corresponding events for listeners of this instance (as they delegate the
 * calls) while they fire events for properties defined in
 * <code>{@link info.monitorenter.gui.chart.IErrorBarPainter.ISegment}</code>
 * too. If you register for events of this instance and for the retrieved
 * segments you will receive two
 * <code>{@link PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)}</code>
 * for the same value changed.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * @version $Revision: 1.21 $
 */
public class ErrorBarPainter implements IErrorBarPainter {

  /**
   * Base class for <code>ISegment</code> implementations that covers common
   * features such as property change support.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
   * 
   * 
   * @version $Revision: 1.21 $
   */
  private abstract class ASegment implements ISegment {

    /** Generated <code>serialVersionUID</code>. **/
    private static final long serialVersionUID = 6620706884643200785L;

    /**
     * Defcon.
     * <p>
     */
    protected ASegment() {
      super();
    }

    /**
     * Properties supported are defined in <code>
     * {@link IErrorBarPainter#addPropertyChangeListener(String, PropertyChangeListener)}
     * </code>.
     * <p>
     * 
     * Note that adding property change listeners to the nested access facades
     * of type <code>{@link IErrorBarPainter.ISegment}</code> accessible via
     * <code>getXXXSegment()</code> methods will fire the corresponding events
     * for listeners of this instance (as they delegate the calls) while they
     * fire events for properties defined in
     * <code>{@link IErrorBarPainter.ISegment}</code> too. If you register for
     * events of this instance and for the retrieved segments you will receive
     * two
     * <code>{@link PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)}</code>
     * for the same value changed.
     * <p>
     * 
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(final String property,
        final PropertyChangeListener listener) {
      ErrorBarPainter.this.addPropertyChangeListener(property, listener);
    }

  }

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -4978322492200966266L;

  /** The color for the segment. */
  private Color m_connectionColor;

  /**
   * The renderer of the connection (distance between origin and end of error
   * bar) of error bars.
   */
  private IPointPainter< ? > m_connectionPainter;

  /** The color for the end point. */
  private Color m_endPointColor;

  /**
   * The renderer of the end point of error bars.
   */
  private IPointPainter< ? > m_endPointPainter;

  /**
   * The instance that add support for firing <code>PropertyChangeEvents</code>
   * and maintaining <code>PropertyChangeListeners</code>.
   * <p>
   */
  protected PropertyChangeSupport m_propertyChangeSupport = new SwingPropertyChangeSupport(this);

  /**
   * The facade instance for accessing the connection segment of this
   * configurable error bar painter.
   * <p>
   */
  private final ISegment m_segmentConnection = new ASegment() {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 2582262217019921050L;

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getColor()
     */
    public Color getColor() {
      return ErrorBarPainter.this.getConnectionColor();
    }

    /**
     * 
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getName()
     */
    public String getName() {
      return "Connection segment";
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPointPainter()
     */
    public IPointPainter< ? > getPointPainter() {
      return ErrorBarPainter.this.getConnectionPainter();
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPropertySegmentColor()
     */
    public final String getPropertySegmentColor() {
      return IErrorBarPainter.PROPERTY_CONNECTION_COLOR;
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPropertySegmentPointPainter()
     */
    public final String getPropertySegmentPointPainter() {
      return IErrorBarPainter.PROPERTY_CONNECTION;
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#setColor(java.awt.Color)
     */
    public void setColor(final Color color) {
      // fires the property change event:
      ErrorBarPainter.this.setConnectionColor(color);
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#setPointPainter(info.monitorenter.gui.chart.IPointPainter)
     */
    public void setPointPainter(final IPointPainter< ? > pointPainter) {
      // fires the property change event:
      ErrorBarPainter.this.setConnectionPainter(pointPainter);
    }

  };

  /**
   * The facade instance for accessing the end segment of this configurable
   * error bar painter.
   * <p>
   */
  private final ISegment m_segmentEnd = new ASegment() {
    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -5655272957651523988L;

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getColor()
     */
    public Color getColor() {
      return ErrorBarPainter.this.getEndPointColor();
    }

    public String getName() {
      return "End segment";
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPointPainter()
     */
    public IPointPainter< ? > getPointPainter() {
      return ErrorBarPainter.this.getEndPointPainter();
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPropertySegmentColor()
     */
    public String getPropertySegmentColor() {
      return IErrorBarPainter.PROPERTY_ENDPOINT_COLOR;
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPropertySegmentPointPainter()
     */
    public String getPropertySegmentPointPainter() {
      return IErrorBarPainter.PROPERTY_ENDPOINT;
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#setColor(java.awt.Color)
     */
    public void setColor(final Color color) {
      // fires the property change event:
      ErrorBarPainter.this.setEndPointColor(color);
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#setPointPainter(info.monitorenter.gui.chart.IPointPainter)
     */
    public void setPointPainter(final IPointPainter< ? > pointPainter) {
      // fires the property change event:
      ErrorBarPainter.this.setEndPointPainter(pointPainter);
    }
  };

  /**
   * The facade instance for accessing the start segment of this configurable
   * error bar painter.
   * <p>
   */
  private final ISegment m_segmentStart = new ASegment() {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -1547300597027982211L;

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getColor()
     */
    public Color getColor() {
      return ErrorBarPainter.this.getStartPointColor();
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getName()
     */
    public String getName() {
      return "Start segment";
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPointPainter()
     */
    public IPointPainter< ? > getPointPainter() {
      return ErrorBarPainter.this.getStartPointPainter();
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPropertySegmentColor()
     */
    public String getPropertySegmentColor() {
      return IErrorBarPainter.PROPERTY_STARTPOINT_COLOR;
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#getPropertySegmentPointPainter()
     */
    public String getPropertySegmentPointPainter() {
      return IErrorBarPainter.PROPERTY_STARTPOINT;
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#setColor(java.awt.Color)
     */
    public void setColor(final Color color) {
      // fires the property change event:
      ErrorBarPainter.this.setStartPointColor(color);
    }

    /**
     * @see info.monitorenter.gui.chart.IErrorBarPainter.ISegment#setPointPainter(info.monitorenter.gui.chart.IPointPainter)
     */
    public void setPointPainter(final IPointPainter< ? > pointPainter) {
      // fires the property change event:
      ErrorBarPainter.this.setStartPointPainter(pointPainter);
    }
  };

  /** The color for the start point. */
  private Color m_startPointColor;

  /**
   * The renderer of the start point of error bars.
   */
  private IPointPainter< ? > m_startPointPainter;

  /**
   * Creates an instance that by default will not render any error bar.
   * <p>
   * 
   * It then has to be configured with the remaining methods as desired.
   * <p>
   * 
   * @see #setEndPointColor(Color)
   * @see #setEndPointPainter(IPointPainter)
   * @see #setConnectionColor(Color)
   * @see #setConnectionPainter(IPointPainter)
   * @see #setStartPointColor(Color)
   * @see #setStartPointPainter(IPointPainter)
   */
  public ErrorBarPainter() {
    super();
    // set default values:
    this.m_startPointColor = Color.GRAY;
    this.m_startPointPainter = null;
    this.m_connectionColor = Color.GRAY;
    this.m_connectionPainter = new PointPainterLine();
    this.m_endPointColor = Color.GRAY;
    this.m_endPointPainter = new PointPainterDisc(4);

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
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getConnectionColor()
   */
  public final Color getConnectionColor() {
    return this.m_connectionColor;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getConnectionPainter()
   */
  public final IPointPainter< ? > getConnectionPainter() {
    return this.m_connectionPainter;
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
  public final IPointPainter< ? > getEndPointPainter() {
    return this.m_endPointPainter;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getPropertyChangeListeners(java.lang.String)
   */
  public PropertyChangeListener[] getPropertyChangeListeners(final String property) {
    return this.m_propertyChangeSupport.getPropertyChangeListeners(property);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getSegmentConnection()
   */
  public ISegment getSegmentConnection() {
    return this.m_segmentConnection;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getSegmentEnd()
   */
  public ISegment getSegmentEnd() {
    return this.m_segmentEnd;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#getSegmentStart()
   */
  public ISegment getSegmentStart() {
    return this.m_segmentStart;
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
  public final IPointPainter< ? > getStartPointPainter() {
    return this.m_startPointPainter;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#paintErrorBar(int, int, ITracePoint2D, Graphics, IErrorBarPixel)
   */
  public void paintErrorBar(final int absoluteX, final int absoluteY, final ITracePoint2D original,
      final Graphics g, final IErrorBarPixel errorBar) {

    Chart2D chart = errorBar.getTrace().getRenderer();
    // If some range policy is used that restricts the viewport ensure,
    // that we don't paint offscreen:
    int xStart = chart.getXChartStart();
    int xEnd = chart.getXChartEnd();
    int yStart = chart.getYChartStart();
    int yEnd = chart.getYChartEnd();
    int x1;
    int y1;
    int x2;
    int y2;
    // x1
    if (absoluteX < xStart) {
      x1 = xStart;
    } else {
      x1 = absoluteX;
    }
    if (absoluteX > xEnd) {
      x1 = xEnd;
    } else {
      x1 = absoluteX;
    }

    // y1
    if (absoluteY > yStart) {
      y1 = yStart;
    } else {
      y1 = absoluteY;
    }
    if (absoluteY < yEnd) {
      y1 = yEnd;
    } else {
      y1 = absoluteY;
    }

    // negative x error:
    int error = errorBar.getNegativeXErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      y2 = y1;
      if (error < xStart) {
        x2 = xStart;
      } else {
        x2 = error;
      }
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_connectionPainter,
          this.m_connectionColor, g);
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_startPointPainter,
          this.m_startPointColor, g);
      // don't paint end point if bounds were exceeded:
      if (x2 == error) {
        this.paintErrorBarPart(x2, y1, x1, y1, original, this.m_endPointPainter,
            this.m_endPointColor, g);
      }
    }
    // positive x error:
    error = errorBar.getPositiveXErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      y2 = y1;
      if (error > xEnd) {
        x2 = xEnd;
      } else {
        x2 = error;
      }

      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_connectionPainter,
          this.m_connectionColor, g);
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_startPointPainter,
          this.m_startPointColor, g);
      // don't paint end point if bounds were exceeded:
      if (x2 == error) {
        this.paintErrorBarPart(x2, y1, x1, y1, original, this.m_endPointPainter,
            this.m_endPointColor, g);
      }
    }

    // negative y error:
    error = errorBar.getNegativeYErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      x2 = x1;
      if (error > yStart) {
        y2 = yStart;
      } else {
        y2 = error;
      }
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_connectionPainter,
          this.m_connectionColor, g);
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_startPointPainter,
          this.m_startPointColor, g);
      // don't paint end point if bounds were exceeded:
      if (y2 == error) {
        this.paintErrorBarPart(x1, y2, x1, y1, original, this.m_endPointPainter,
            this.m_endPointColor, g);
      }
    }
    // positive y error:
    error = errorBar.getPositiveYErrorPixel();
    if (error != IErrorBarPixel.ERROR_PIXEL_NONE) {
      x2 = x1;
      if (error < yEnd) {
        y2 = yEnd;
      } else {
        y2 = error;
      }
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_connectionPainter,
          this.m_connectionColor, g);
      this.paintErrorBarPart(x1, y1, x2, y2, original, this.m_startPointPainter,
          this.m_startPointColor, g);
      // don't paint end point if bounds were exceeded:
      if (y2 == error) {
        this.paintErrorBarPart(x1, y2, x1, y1, original, this.m_endPointPainter,
            this.m_endPointColor, g);
      }
    }
  }

  /**
   * Internally renders the given part of the error bar with support for color
   * and invisibility (null painter) management.
   * <p>
   * Factored out code to keep calling method
   * {@link #paintErrorBar(int, int, ITracePoint2D, Graphics, IErrorBarPixel)} smaller.
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
   *          for connection).
   * 
   * @param nextY
   *          the next y coordinate in px of the error bar part (only relevant
   *          for connection).
   * 
   * @param pointPainter
   *          the painter to use.
   * 
   * @param color
   *          the color or null.
   * 
   * @param g2d
   *          needed for painting.
   * 
   * @param original
   *          the original trace point this error bar is painted for.
   */
  private final void paintErrorBarPart(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final ITracePoint2D original, final IPointPainter< ? > pointPainter,
      final Color color, final Graphics g2d) {
    if (pointPainter != null) {
      boolean colorChange = false;
      Color backupColor = g2d.getColor();
      if (color != null) {
        colorChange = true;
        g2d.setColor(color);
      }
      pointPainter.paintPoint(absoluteX, absoluteY, nextX, nextY, g2d, original);
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
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setConnectionColor(java.awt.Color)
   */
  public final void setConnectionColor(final Color connectionColor) {
    Color old = this.m_connectionColor;
    this.m_connectionColor = connectionColor;
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_CONNECTION_COLOR, old,
        this.m_connectionColor);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setConnectionPainter(info.monitorenter.gui.chart.IPointPainter)
   */
  public final void setConnectionPainter(final IPointPainter< ? > connectionPainter) {
    IPointPainter< ? > old = this.m_connectionPainter;
    this.m_connectionPainter = connectionPainter;
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_CONNECTION, old, connectionPainter);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setEndPointColor(java.awt.Color)
   */
  public final void setEndPointColor(final Color endPointColor) {
    Color old = this.m_endPointColor;
    this.m_endPointColor = endPointColor;
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_ENDPOINT_COLOR, old,
        this.m_endPointColor);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setEndPointPainter(info.monitorenter.gui.chart.IPointPainter)
   */
  public final void setEndPointPainter(final IPointPainter< ? > endPointPainter) {
    IPointPainter< ? > old = this.m_endPointPainter;
    this.m_endPointPainter = endPointPainter;
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_ENDPOINT, old, endPointPainter);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setStartPointColor(java.awt.Color)
   */
  public final void setStartPointColor(final Color startPointColor) {
    Color old = this.m_startPointColor;
    this.m_startPointColor = startPointColor;
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_STARTPOINT_COLOR, old,
        this.m_startPointColor);
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPainter#setStartPointPainter(info.monitorenter.gui.chart.IPointPainter)
   */
  public final void setStartPointPainter(final IPointPainter< ? > startPointPainter) {
    IPointPainter< ? > old = this.m_startPointPainter;
    this.m_startPointPainter = startPointPainter;
    this.m_propertyChangeSupport.firePropertyChange(PROPERTY_STARTPOINT, old,
        this.m_startPointPainter);
  }

}
