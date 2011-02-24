/*
 *  RangePanel.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 09:50:20.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart.controls;

import info.monitorenter.util.Range;
import infovis.panel.DoubleBoundedRangeModel;
import infovis.panel.dqinter.DoubleRangeSlider;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A panel that allows to choose a range from a special
 * {@link javax.swing.JSlider} with two sliders (dual Slider).
 * <p>
 * Credits go to the infovis project of Jean Daniel Fekete for the dual slider
 * comopenent: <a href="http://ivtk.sourceforge.net/"
 * target="_blank">http://ivtk.sourceforge.net/</a>.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.1 $
 */
public class RangeChooserPanel extends JPanel {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3258413911148410931L;

  /** Used for formatting the numbers displayed in the text fields. */
  private NumberFormat m_nf = NumberFormat.getNumberInstance(Locale.getDefault());

  /** The bislider to choose a range. */
  private DoubleRangeSlider m_slider;

  /**
   * Creates an instance that works on the given range.
   * <p>
   * 
   * @param range
   *          defines the bounds of the current selection and the extension of
   *          these that is choosable.
   */
  public RangeChooserPanel(final Range range) {

    super();
    this.m_nf.setMinimumFractionDigits(2);
    this.m_nf.setMaximumFractionDigits(2);

    // TODO: refactor calling action class to reuse this instance.
    double min = range.getMin();
    double max = range.getMax();
    double minBound = min;
    double maxBound = max;
    if (minBound == Double.NEGATIVE_INFINITY || minBound == Double.NaN
        || minBound == Double.POSITIVE_INFINITY || minBound == -Double.MAX_VALUE) {
      min = -100;
    }
    minBound = min - (max - min) / 2;

    if (maxBound == Double.NEGATIVE_INFINITY || maxBound == Double.NaN
        || maxBound == Double.POSITIVE_INFINITY || maxBound == Double.MAX_VALUE) {
      max = 100;
    }
    maxBound = max + (max - min) / 2;

    this.m_slider = new DoubleRangeSlider(minBound, maxBound, min, max);

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.m_slider.setMaximumSize(new Dimension(300, 40));
    this.m_slider.setEnabled(true);
    this.add(Box.createVerticalStrut(10));
    this.add(this.m_slider);
    // text field views to show the range values;
    // minimum value view:
    final JTextField rangeMinView = new JTextField();
    rangeMinView.setText(this.m_nf.format(new Double(this.m_slider.getLowValue())));
    DoubleBoundedRangeModel rangeModel = this.m_slider.getModel();
    rangeMinView.setEditable(true);
    rangeModel.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        rangeMinView.setText(RangeChooserPanel.this.m_nf.format(new Double(
            RangeChooserPanel.this.m_slider.getLowValue())));
      }
    });
    rangeMinView.setPreferredSize(new Dimension(120, 20));
    rangeMinView.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent ae) {
        JTextField textField = (JTextField) ae.getSource();
        try {
          Number entered = RangeChooserPanel.this.m_nf.parse(textField.getText());
          double low = entered.doubleValue();
          double high = RangeChooserPanel.this.m_slider.getHighValue();
          double minSlider = low - (high - low) / 2;
          RangeChooserPanel.this.m_slider.setMinimum(minSlider);
          RangeChooserPanel.this.m_slider.setLowValue(low);
        } catch (ParseException e) {
          // TODO: maybe inform user of invalid input.
          textField.setText(RangeChooserPanel.this.m_nf.format(RangeChooserPanel.this.m_slider
              .getLowValue()));
        }
      }
    });
    rangeMinView.setToolTipText("Enter a number and hit Return.");

    // maximum value view:
    final JTextField rangeMaxView = new JTextField();
    rangeMaxView.setText(new Double(this.m_slider.getHighValue()).toString());
    rangeMaxView.setEditable(true);
    rangeModel.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        rangeMaxView.setText(RangeChooserPanel.this.m_nf.format(new Double(
            RangeChooserPanel.this.m_slider.getHighValue())));
      }
    });
    rangeMaxView.setPreferredSize(new Dimension(120, 20));
    rangeMaxView.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent ae) {
        JTextField textField = (JTextField) ae.getSource();
        try {
          Number entered = RangeChooserPanel.this.m_nf.parse(textField.getText());
          double low = RangeChooserPanel.this.m_slider.getLowValue();
          double high = entered.doubleValue();
          double maxSlider = high + (high - low) / 2;
          RangeChooserPanel.this.m_slider.setMaximum(maxSlider);
          RangeChooserPanel.this.m_slider.setHighValue(high);
        } catch (ParseException e) {
          // TODO: maybe inform user of invalid input.
          textField.setText(RangeChooserPanel.this.m_nf.format(RangeChooserPanel.this.m_slider
              .getHighValue()));
        }
      }
    });
    rangeMaxView.setToolTipText("Enter a number and hit Return.");

    // add range views to panel:
    JPanel rangeViewPanel = new JPanel();
    rangeViewPanel.setMaximumSize(new Dimension(300, 30));
    rangeViewPanel.setLayout(new BoxLayout(rangeViewPanel, BoxLayout.X_AXIS));
    rangeViewPanel.add(Box.createHorizontalGlue());
    rangeViewPanel.add(rangeMinView);
    rangeViewPanel.add(Box.createHorizontalStrut(10));
    rangeViewPanel.add(rangeMaxView);
    rangeViewPanel.add(Box.createHorizontalGlue());
    this.add(Box.createVerticalStrut(10));
    this.add(rangeViewPanel);
    this.add(Box.createVerticalGlue());
  }

  /**
   * Returns the current selected range.
   * <p>
   * 
   * @return the current selected range.
   */
  public Range getRange() {
    return new Range(this.m_slider.getLowValue(), this.m_slider.getHighValue());
  }
}
