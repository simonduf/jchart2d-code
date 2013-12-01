/*
 *  ScrollablePanel.java of project jchart2d, a panel that offers scrollbars when chart is zoomed in.
 *  Copyright (C) 2002 - 2013, Achim Westermann, created on Oct 17, 2012
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
 */
package info.monitorenter.gui.chart.views;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.util.Range;

import java.awt.Adjustable;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 * Panel that offers scrollbars when chart is zoomed in. Use like:
 * 
 * <code>
 *  // Within a JComponent: 
 *  Chart2d chart = new ZoomableChart();
 *  ...
 *  this.this.getContentPane().add(chart);
 *  </code>
 * <p>
 * 
 * @author Ramon Zambelli.
 * 
 */
public class ScrollablePanel extends JPanel {

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -2293615518310543613L;

  private final int SCROLL_RANGE = 1000;

  private final Chart2D m_Chart;

  private JScrollBar m_scrollBarXaxis;

  private JScrollBar m_scrollBarYaxis;

  private PropertyChangeListener m_changeListenerXaxis;

  private PropertyChangeListener m_changeListenerYaxis;

  private AdjustmentListener m_adjustmentListenerXaxis;

  private AdjustmentListener m_adjustmentListenerYaxis;

  private IAxis< ? > m_xAxis;

  private IAxis< ? > m_yAxis;

  /**
   * Constructor taking a chart panel.
   * <p>
   * 
   * @param chartPanel
   *          adds controls to the chart.
   */
  public ScrollablePanel(final ChartPanel chartPanel) {
    this(chartPanel.getChart(), chartPanel);
  }

  /**
   * Constructor taking the chart.
   * <p>
   * 
   * @param chart
   *          the chart to allow scrolling when zoomed in.
   */
  public ScrollablePanel(final Chart2D chart) {
    this(chart, null);
  }

  /**
   * Constructor taking the chart and also an optional (nullable) wrapper the
   * chart is already contained in.
   * <p>
   * 
   * @param chart
   *          the chart itself.
   * 
   * @param wrapper
   *          optional (nullable) wrapper the chart is already contained in.
   */
  private ScrollablePanel(final Chart2D chart, final JComponent wrapper) {
    this.m_Chart = chart;

    this.m_adjustmentListenerXaxis = new AdjustmentListener() {

      @Override
      public void adjustmentValueChanged(final AdjustmentEvent arg0) {
        ScrollablePanel.this.onScrollBarAdjustment(ScrollablePanel.this.m_scrollBarXaxis, ScrollablePanel.this.m_xAxis);
      }
    };
    this.m_adjustmentListenerYaxis = new AdjustmentListener() {

      @Override
      public void adjustmentValueChanged(final AdjustmentEvent arg0) {
        ScrollablePanel.this.onScrollBarAdjustment(ScrollablePanel.this.m_scrollBarYaxis, ScrollablePanel.this.m_yAxis);
      }
    };

    final GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0 };
    gridBagLayout.rowHeights = new int[] {0, 0, 0 };
    gridBagLayout.columnWeights = new double[] {1.0, 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, Double.MIN_VALUE };
    this.setLayout(gridBagLayout);

    this.m_scrollBarXaxis = new JScrollBar();
    this.m_scrollBarXaxis.setVisible(false);
    this.m_scrollBarXaxis.setOrientation(Adjustable.HORIZONTAL);
    final GridBagConstraints gbc_scrollBarHorizontal = new GridBagConstraints();
    gbc_scrollBarHorizontal.fill = GridBagConstraints.BOTH;
    gbc_scrollBarHorizontal.gridx = 0;
    gbc_scrollBarHorizontal.gridy = 0;
    this.add(this.m_scrollBarXaxis, gbc_scrollBarHorizontal);

    this.m_scrollBarYaxis = new JScrollBar();
    this.m_scrollBarYaxis.setVisible(false);
    final GridBagConstraints gbc_scrollBarVertical = new GridBagConstraints();
    gbc_scrollBarVertical.fill = GridBagConstraints.VERTICAL;
    gbc_scrollBarVertical.gridx = 1;
    gbc_scrollBarVertical.gridy = 1;
    this.add(this.m_scrollBarYaxis, gbc_scrollBarVertical);

    final GridBagConstraints gbc_chart = new GridBagConstraints();
    gbc_chart.fill = GridBagConstraints.BOTH;
    gbc_chart.gridx = 0;
    gbc_chart.gridy = 1;
    if (wrapper != null) {
      this.add(wrapper, gbc_chart);
    } else {
      this.add(chart, gbc_chart);
    }

    this.m_changeListenerXaxis = new PropertyChangeListener() {

      @Override
      public void propertyChange(final PropertyChangeEvent event) {
        final IRangePolicy rangePolicy = (IRangePolicy) event.getNewValue();
        if (rangePolicy == null) {
          return;
        }
        ScrollablePanel.this.m_xAxis = (IAxis< ? >) event.getSource();
        if (ScrollablePanel.this.m_xAxis == null) {
          return;
        }
        ScrollablePanel.this.onAxisPropertyChanged(ScrollablePanel.this.m_scrollBarXaxis, ScrollablePanel.this.m_xAxis, rangePolicy,
            ScrollablePanel.this.m_adjustmentListenerXaxis);
      }
    };

    this.m_changeListenerYaxis = new PropertyChangeListener() {

      @Override
      public void propertyChange(final PropertyChangeEvent event) {
        final IRangePolicy rangePolicy = (IRangePolicy) event.getNewValue();
        if (rangePolicy == null) {
          return;
        }
        ScrollablePanel.this.m_yAxis = (IAxis< ? >) event.getSource();
        if (ScrollablePanel.this.m_xAxis == null) {
          return;
        }
        ScrollablePanel.this.onAxisPropertyChanged(ScrollablePanel.this.m_scrollBarYaxis, ScrollablePanel.this.m_yAxis, rangePolicy,
            ScrollablePanel.this.m_adjustmentListenerYaxis);
      }
    };

    if (this.m_Chart.getAxisX() != null) {
      this.m_Chart.getAxisX().addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, this.m_changeListenerXaxis);
    }

    if (this.m_Chart.getAxisY() != null) {
      this.m_Chart.getAxisY().addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, this.m_changeListenerYaxis);
    }

    this.m_Chart.addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(final PropertyChangeEvent event) {
        if (event.equals(Chart2D.PROPERTY_AXIS_X)) {
          final IAxis< ? > axis = (IAxis< ? >) event.getNewValue();
          if (axis != null) {
            axis.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, ScrollablePanel.this.m_changeListenerXaxis);
          }
        }
        if (event.equals(Chart2D.PROPERTY_AXIS_Y)) {
          final IAxis< ? > axis = (IAxis< ? >) event.getNewValue();
          if (axis != null) {
            axis.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, ScrollablePanel.this.m_changeListenerYaxis);
          }
        }
      }
    });
  }

  private void onAxisPropertyChanged(final JScrollBar scrollBar, final IAxis< ? > axis, final IRangePolicy rangePolicy,
      final AdjustmentListener adjustmentListener) {
    System.out.println("onAxisPropertyChanged propertyChange:" + rangePolicy + "," + axis);
    if (rangePolicy instanceof RangePolicyFixedViewport) {
      System.out.println("RangePolicyFixedViewport");
      scrollBar.setVisible(true);
      scrollBar.setMinimum(0);
      scrollBar.setMaximum(this.SCROLL_RANGE);
      final double axisMin = axis.getMinValue();
      final double axisMax = axis.getMaxValue();
      final double axisRange = axisMax - axisMin;
      final double rangeMin = rangePolicy.getRange().getMin();
      final double rangeMax = rangePolicy.getRange().getMax();
      final double rangeCenter = (rangeMin + rangeMax) / 2;
      final double scrollValue = scrollBar.getMinimum() + ((this.SCROLL_RANGE * (rangeCenter - axisMin)) / axisRange);
      scrollBar.setValue((int) scrollValue);
      scrollBar.addAdjustmentListener(adjustmentListener);
    } else {
      System.out.println("RangePolicyMinimumViewport");
      scrollBar.removeAdjustmentListener(adjustmentListener);
      scrollBar.setVisible(false);
    }
  }

  private void onScrollBarAdjustment(final JScrollBar scrollBar, final IAxis< ? > axis) {
    final double axisMin = axis.getMinValue();
    final double axisMax = axis.getMaxValue();
    final double axisRange = axisMax - axisMin;
    final IRangePolicy rangePolicy = axis.getRangePolicy();
    final double rangeMin = rangePolicy.getRange().getMin();
    final double rangeMax = rangePolicy.getRange().getMax();
    final double range = rangeMax - rangeMin;
    final double rangeCenter = axis.getMinValue() + ((axisRange * scrollBar.getValue()) / this.SCROLL_RANGE);
    rangePolicy.setRange(new Range(rangeCenter - (range / 2), rangeCenter + (range / 2)));
  }
}
