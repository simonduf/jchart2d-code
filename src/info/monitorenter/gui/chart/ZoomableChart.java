/*
 *  ZoomableChart.java of project jchart2d, a chart enriched 
 *  by zoom functionality in x dimension. 
 *  Copyright 2006 (C) Achim Westermann, created on 23:58:31.
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
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.util.Range;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * {@link info.monitorenter.gui.chart.Chart2D} enriched by a zoom-functionality
 * in the x dimension.
 * <p>
 * 
 * @author Alessio Sambarino (Contributor)
 * 
 * @version $Revision: 1.5 $
 * 
 * @author Klaus Pesendorfer (klaus.pesendorfer@fabalabs.org)
 * @version 1.3.2 zoom in both directions
 */
public class ZoomableChart
    extends Chart2D implements MouseListener, MouseMotionListener {

  /**
   * Generated <code>serial version UID</code>.<p>
   */
  private static final long serialVersionUID = 8799808716942023907L;

  /** Needed to set the initial zooming area to the data bounds. */
  private boolean m_firstTime = true;

  /** The starting point of the mouse drag operation (click, then move). */
  private Point2D m_startPoint;

  /** The area to zoom. */
  private Rectangle2D m_zoomArea;

  // modified by Fabalabs (KP) 20060914: zoom in both directions
  /**
   * The maximum value of the data bounds of this chart in x dimension that is
   * needed to reset the zooming.
   */
  private double m_zoomMaxX;

  /**
   * The minimum value of the data bounds of this chart in x dimension that is
   * needed to reset the zooming.
   */
  private double m_zoomMinX;
  
  /**
   * The maximum value of the data bounds of this chart in y dimension that is
   * needed to reset the zooming.
   */
  private double m_zoomMaxY;

  /**
   * The minimum value of the data bounds of this chart in y dimension that is
   * needed to reset the zooming.
   */
  private double m_zoomMinY;

  // modified by Fabalabs (KP) 20060802: only zoom graph with left mouse-button
  /**
   * Store the last mouse click and test in the mouseDragged-method which mouse-button was clicked.
   */
  private int m_lastPressedButton;
  
  /**
   * Defcon.
   * <p>
   */
  public ZoomableChart() {

    super();

    addMouseListener(this);
    addMouseMotionListener(this);
  }

  /**
   * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
   */
  public void mouseClicked(final MouseEvent e) {
    // nop.
      
  }

  /**
   * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
   */
  public void mouseDragged(final MouseEvent e) {
      // modified by Fabalabs (KP) 20060802: only zoom graph with left mouse-button
      if (this.m_lastPressedButton != MouseEvent.BUTTON1) {
          return;
      }

      // modified by Fabalabs (KP) 20060914: set exact reactangle for 2D-zoom
      if ((e.getY() < 20) || (e.getY() > this.getYChartStart()) 
              || (e.getX() < 20) || (e.getX() > this.getXChartEnd())) {
          return;
      }
      double startX;
      double endX;
      double dimX;
      double dimY;
      double startY;
      double endY;

      // x-coordinate
      if (e.getX() > this.m_startPoint.getX()) {
          startX = this.m_startPoint.getX();
          endX = e.getX();
      } else {
          startX = e.getX();
          endX = this.m_startPoint.getX();
      }

      if (startX < this.getXChartStart()) {
          startX = this.getXChartStart();
      }

      if (endX > (this.getWidth() - 20)) {
          endX = this.getWidth() - 20;
      }

      // y-coordinate
      if (e.getY() > this.m_startPoint.getY()) {
          startY = this.m_startPoint.getY();
          endY = e.getY();
      } else {
          startY = e.getY();
          endY = this.m_startPoint.getY();
      }

      if (startY > this.getYChartStart()) {
          startY = this.getYChartStart();
      }

      if (endY > (this.getHeight() - 20)) {
          endY = this.getHeight() - 20;
      }


      dimX = endX - startX;
      dimY = endY - startY;

      this.m_zoomArea = new Rectangle2D.Double(startX, startY, dimX, dimY);

      repaint();
  }

  /**
   * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
   */
  public void mouseEntered(final MouseEvent e) {
    // nop.
  }

  /**
   * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
   */
  public void mouseExited(final MouseEvent e) {
    // nop.
  }

  /**
   * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
   */
  public void mouseMoved(final MouseEvent e) {
    // nop.
  }

  /**
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
  public void mousePressed(final MouseEvent e) {
    // modified by Fabalabs (KP) 20060914: use exact start-point for 2D-zoom
    this.m_startPoint = new Point2D.Double(e.getX(), e.getY());
    // modified by Fabalabs (KP) 20060802: only zoom graph with left mouse-button
    this.m_lastPressedButton = e.getButton();
  }

  /**
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
  public void mouseReleased(final MouseEvent e) {

      if (this.m_zoomArea == null) {
          return;
      }

      // x-coordinate
      double startPx = this.m_zoomArea.getX();
      double endPx = this.m_zoomArea.getX() + this.m_zoomArea.getWidth();

      Range rangeX = getAxisX().getRangePolicy().getRange();

      double maxX = rangeX.getMax();
      double minX = rangeX.getMin();

      if (maxX == Double.MAX_VALUE) {
          rangeX = getAxisX().getRange();
          maxX = rangeX.getMax();
          minX = rangeX.getMin();
      }

      double xAxisDomain = maxX - minX;
      double xAxisLength = getWidth() - 20 - this.getXChartStart();

      double xAxisMin = minX + xAxisDomain / xAxisLength * (startPx - this.getXChartStart());
      double xAxisMax = minX + xAxisDomain / xAxisLength * (endPx - this.getXChartStart());

      // modified by Fabalabs (KP) 20060914: zoom also in y-direction
      // y-coordinate
      double startPy = this.m_zoomArea.getY();
      double endPy = this.m_zoomArea.getY() + this.m_zoomArea.getHeight();

      Range rangeY = getAxisY().getRangePolicy().getRange();

      double maxY = rangeY.getMax();
      double minY = rangeY.getMin();

      if (maxY == Double.MAX_VALUE) {
          rangeY = getAxisY().getRange();
          maxY = rangeY.getMax();
          minY = rangeY.getMin();
      }

      double yAxisDomain = maxY - minY;
      double yAxisLength = getHeight() - 20 - this.getYChartEnd();

      double yAxisMin = maxY - yAxisDomain / yAxisLength * (startPy - this.getYChartEnd());
      double yAxisMax = maxY - yAxisDomain / yAxisLength * (endPy - this.getYChartEnd());

      // do not zoom extremly small areas (does not work properly because of calculation)
      if ((endPx - startPx) < 20 || (endPy - startPy) < 20) {
          this.m_zoomArea = null;
          repaint();
          return;
      }

      zoom(xAxisMin, xAxisMax, yAxisMin, yAxisMax);
  }

  /**
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  public void paintComponent(final Graphics g) {

      super.paintComponent(g);

      Graphics2D g2 = (Graphics2D) g;

      if (this.m_firstTime) {
          Range rangeX = getAxisX().getRange();
          this.m_zoomMinX = rangeX.getMin();
          this.m_zoomMaxX = rangeX.getMax();
          // modified by Fabalabs (KP) 20060914: zoom also in y-direction
          Range rangeY = getAxisY().getRange();
          this.m_zoomMinY = rangeY.getMin();
          this.m_zoomMaxY = rangeY.getMax();
          this.m_firstTime = false;
      }

      if (this.m_zoomArea != null) {
          g2.draw(this.m_zoomArea);
          g2.setPaint(new Color(255, 255, 0, 100));
          g2.fill(this.m_zoomArea);
      }
  }

  /**
   * Zooms to the selected bounds in both directions.
   * modified by Fabalabs (KP) 20060914: zoom also in y-direction
   * <p>
   * 
   * @param xmin
   *          the lower x bound.
   * 
   * @param xmax
   *          the upper x bound.
   *          
   * @param ymin
   *          the lower y bound.
   * 
   * @param ymax
   *          the upper y bound.
   */
  public void zoom(final double xmin, final double xmax, final double ymin, final double ymax) {

    this.m_zoomArea = null;

    IAxis axisX = getAxisX();
    // IRangePolicy
    axisX.setRangePolicy(new RangePolicyFixedViewport());
    axisX.setRange(new Range(xmin, xmax));

    IAxis axisY = getAxisY();
    // IRangePolicy
    axisY.setRangePolicy(new RangePolicyFixedViewport());
    axisY.setRange(new Range(ymin, ymax));
  }
  
  /**
   * Zooms to the selected bounds in x-axis.
   * <p>
   * 
   * @param xmin
   *          the lower x bound.
   * 
   * @param xmax
   *          the upper x bound.
   */
  public void zoom(final double xmin, final double xmax) {

    this.m_zoomArea = null;

    IAxis axis = getAxisX();
    // IRangePolicy
    axis.setRangePolicy(new RangePolicyFixedViewport());
    axis.setRange(new Range(xmin, xmax));

  }

  /**
   * Resets the zooming area to a range that displays all data.
   * <p>
   */
  public void zoomAll() {

    this.getAxisX().setRangePolicy(new RangePolicyFixedViewport(new Range(this.m_zoomMinX, this.m_zoomMaxX)));
    // modified by Fabalabs (KP) 20060914: zoom also in y-direction
    this.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(this.m_zoomMinY, this.m_zoomMaxY)));
  }
}
