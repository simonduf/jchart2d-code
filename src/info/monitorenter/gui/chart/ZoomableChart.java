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
 * @version $Revision: 1.1 $
 */
public class ZoomableChart extends Chart2D implements MouseListener, MouseMotionListener {

  /** Needed to set the initial zooming area to the data bounds. */
  private boolean m_firstTime = true;

  /** The starting point of the mouse drag operation (click, then move). */
  private Point2D m_startPoint;

  /** The area to zoom. */
  private Rectangle2D m_zoomArea;

  /**
   * The maximum value of the data bounds of this chart in x dimension that is
   * needed to reset the zooming.
   */
  private double m_zoomMax;

  /**
   * The minimum value of the data bounds of this chart in x dimension that is
   * needed to reset the zooming.
   */
  private double m_zoomMin;

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

    if ((e.getY() < 20) || (e.getY() > this.getYChartStart())) {
      return;
    }
    double startX;
    double endX;
    double dimX;
    double startY = this.m_startPoint.getY();
    double endY = getYChartStart();
    double dimY = endY - startY;

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

    dimX = endX - startX;

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
    this.m_startPoint = new Point2D.Double(e.getX(), 20);
  }

  /**
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
  public void mouseReleased(final MouseEvent e) {

    if (this.m_zoomArea == null) {
      return;
    }

    double startPx = this.m_zoomArea.getX();
    double endPx = this.m_zoomArea.getX() + this.m_zoomArea.getWidth();

    Range range = getAxisX().getRangePolicy().getRange();

    double max = range.getMax();
    double min = range.getMin();

    if (max == Double.MAX_VALUE) {
      range = getAxisX().getRange();
      max = range.getMax();
      min = range.getMin();
    }

    double xAxisDomain = max - min;
    double xAxisLength = getWidth() - 20 - this.getXChartStart();

    double xAxisMin = min + xAxisDomain / xAxisLength * (startPx - this.getXChartStart());
    double xAxisMax = min + xAxisDomain / xAxisLength * (endPx - this.getXChartStart());

    zoom(xAxisMin, xAxisMax);
  }

  /**
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  public void paintComponent(final Graphics g) {

    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    if (this.m_firstTime) {
      Range range = getAxisX().getRange();
      this.m_zoomMin = range.getMin();
      this.m_zoomMax = range.getMax();
      this.m_firstTime = false;
    }

    if (this.m_zoomArea != null) {
      g2.draw(this.m_zoomArea);
      g2.setPaint(new Color(255, 255, 0, 100));
      g2.fill(this.m_zoomArea);
    }
  }

  /**
   * Zooms to the selected bounds.
   * <p>
   * 
   * @param min
   *          the lower bound.
   * 
   * @param max
   *          the upper bound.
   */
  public void zoom(final double min, final double max) {

    this.m_zoomArea = null;

    IAxis axis = getAxisX();
    // IRangePolicy
    axis.setRangePolicy(new RangePolicyFixedViewport());
    axis.setRange(new Range(min, max));

  }

  /**
   * Resets the zooming area to a range that displays all data.
   * <p>
   */
  public void zoomAll() {

    this.getAxisX().setRangePolicy(
        new RangePolicyFixedViewport(new Range(this.m_zoomMin, this.m_zoomMax)));
  }
}
