/*
 *  ScrollablePanel.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2012, Achim Westermann, created on Oct 17, 2012
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
 * File   : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $
 * Date   : $Date: 2009/02/24 16:45:41 $
 * Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart.views;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.util.Range;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ScrollablePanel extends JPanel {

  private final int SCROLL_RANGE = 1000;

  private final Chart2D m_Chart;

  private JScrollBar m_scrollBarXaxis;

  private JScrollBar m_scrollBarYaxis;

  private PropertyChangeListener m_changeListenerXaxis;

  private PropertyChangeListener m_changeListenerYaxis;

  private AdjustmentListener m_adjustmentListenerXaxis;

  private AdjustmentListener m_adjustmentListenerYaxis;

  private AAxis m_xAxis;

  private AAxis m_yAxis;

  public ScrollablePanel(Chart2D chart) {
    this.m_Chart = chart;

    m_adjustmentListenerXaxis = new AdjustmentListener() {

      @Override
      public void adjustmentValueChanged(AdjustmentEvent arg0) {
        onScrollBarAdjustment(m_scrollBarXaxis, m_xAxis);
      }
    };
    m_adjustmentListenerYaxis = new AdjustmentListener() {

      @Override
      public void adjustmentValueChanged(AdjustmentEvent arg0) {
        onScrollBarAdjustment(m_scrollBarYaxis, m_yAxis);
      }
    };

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0 };
    gridBagLayout.rowHeights = new int[] {0, 0, 0 };
    gridBagLayout.columnWeights = new double[] {1.0, 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    m_scrollBarXaxis = new JScrollBar();
    m_scrollBarXaxis.setVisible(false);
    m_scrollBarXaxis.setOrientation(JScrollBar.HORIZONTAL);
    GridBagConstraints gbc_scrollBarHorizontal = new GridBagConstraints();
    gbc_scrollBarHorizontal.fill = GridBagConstraints.BOTH;
    gbc_scrollBarHorizontal.gridx = 0;
    gbc_scrollBarHorizontal.gridy = 0;
    add(m_scrollBarXaxis, gbc_scrollBarHorizontal);

    m_scrollBarYaxis = new JScrollBar();
    m_scrollBarYaxis.setVisible(false);
    GridBagConstraints gbc_scrollBarVertical = new GridBagConstraints();
    gbc_scrollBarVertical.fill = GridBagConstraints.VERTICAL;
    gbc_scrollBarVertical.gridx = 1;
    gbc_scrollBarVertical.gridy = 1;
    add(m_scrollBarYaxis, gbc_scrollBarVertical);

    GridBagConstraints gbc_chart = new GridBagConstraints();
    gbc_chart.fill = GridBagConstraints.BOTH;
    gbc_chart.gridx = 0;
    gbc_chart.gridy = 1;
    add(chart, gbc_chart);

    m_changeListenerXaxis = new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent event) {
        IRangePolicy rangePolicy = (IRangePolicy) event.getNewValue();
        if (rangePolicy == null) {
          return;
        }
        m_xAxis = (AAxis) event.getSource();
        if (m_xAxis == null) {
          return;
        }
        onAxisPropertyChanged(m_scrollBarXaxis, m_xAxis, rangePolicy, m_adjustmentListenerXaxis);
      }
    };

    m_changeListenerYaxis = new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent event) {
        IRangePolicy rangePolicy = (IRangePolicy) event.getNewValue();
        if (rangePolicy == null) {
          return;
        }
        m_yAxis = (AAxis) event.getSource();
        if (m_xAxis == null) {
          return;
        }
        onAxisPropertyChanged(m_scrollBarYaxis, m_yAxis, rangePolicy, m_adjustmentListenerYaxis);
      }
    };

    if (m_Chart.getAxisX() != null) {
      m_Chart.getAxisX().addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY,
          m_changeListenerXaxis);
    }

    if (m_Chart.getAxisY() != null) {
      m_Chart.getAxisY().addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY,
          m_changeListenerYaxis);
    }

    m_Chart.addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent event) {
        System.out.println("m_Chart propertyChange:" + event.getPropertyName() + ","
            + event.getNewValue());
        if (event.equals(Chart2D.PROPERTY_AXIS_X)) {
          AAxis axis = (AAxis) event.getNewValue();
          if (axis != null) {
            axis.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, m_changeListenerXaxis);
          }
        }
        if (event.equals(Chart2D.PROPERTY_AXIS_Y)) {
          AAxis axis = (AAxis) event.getNewValue();
          if (axis != null) {
            axis.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, m_changeListenerYaxis);
          }
        }
      }
    });
  }

  private void onScrollBarAdjustment(JScrollBar scrollBar, AAxis axis) {
    double axisMin = axis.getMinValue();
    double axisMax = axis.getMaxValue();
    double axisRange = axisMax - axisMin;
    IRangePolicy rangePolicy = axis.getRangePolicy();
    double rangeMin = rangePolicy.getRange().getMin();
    double rangeMax = rangePolicy.getRange().getMax();
    double range = rangeMax - rangeMin;
    double rangeCenter = axis.getMinValue() + (axisRange * scrollBar.getValue() / SCROLL_RANGE);
    rangePolicy.setRange(new Range(rangeCenter - range / 2, rangeCenter + range / 2));
  }

  private void onAxisPropertyChanged(JScrollBar scrollBar, AAxis axis, IRangePolicy rangePolicy, AdjustmentListener adjustmentListener) {
    System.out.println("onAxisPropertyChanged propertyChange:" + rangePolicy + "," + axis);
    if (rangePolicy instanceof RangePolicyFixedViewport) {
      System.out.println("RangePolicyFixedViewport");
      scrollBar.setVisible(true);
      scrollBar.setMinimum(0);
      scrollBar.setMaximum(SCROLL_RANGE);
      double axisMin = axis.getMinValue();
      double axisMax = axis.getMaxValue();
      double axisRange = axisMax - axisMin;
      double rangeMin = rangePolicy.getRange().getMin();
      double rangeMax = rangePolicy.getRange().getMax();
      double rangeCenter = (rangeMin + rangeMax) / 2;
      double scrollValue = scrollBar.getMinimum() + (SCROLL_RANGE * (rangeCenter - axisMin) / axisRange);
      scrollBar.setValue((int) scrollValue);
      scrollBar.addAdjustmentListener(adjustmentListener);
    } else {
      System.out.println("RangePolicyMinimumViewport");
      scrollBar.removeAdjustmentListener(adjustmentListener);
      scrollBar.setVisible(false);
    }
  }
}
