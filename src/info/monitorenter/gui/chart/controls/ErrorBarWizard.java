/*
 *  ErrorBarWizard.java of project jchart2d, a wizard for 
 *  configuring error bars for a trace. 
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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IErrorBarPainter;
import info.monitorenter.gui.chart.IErrorBarPolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.controls.errorbarwizard.ErrorBarPolicyPanel;
import info.monitorenter.gui.chart.demos.StaticChartErrorBarLineDisc;
import info.monitorenter.gui.chart.errorbars.ErrorBarPainterConfigureable;
import info.monitorenter.gui.chart.errorbars.ErrorBarPolicyRelative;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.util.FileUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * A wizard to manage error bars for a trace.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.2 $
 */
public class ErrorBarWizard
    extends JPanel {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 6973894101328190445L;

  /**
   * Creates a wizard for controlling the error bars of the given trace.
   * 
   * @param trace
   *          the trace to control the error bars of.
   */
  public ErrorBarWizard(final ITrace2D trace) {

    super();
    JTabbedPane tabPolicies = new JTabbedPane();

    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(2, 2, 2, 2);

    Iterator it = trace.getErrorBarPolicies().iterator();
    IErrorBarPolicy policy;
    String tabName;
    while (it.hasNext()) {
      policy = (IErrorBarPolicy) it.next();
      // maybe unstable: we use a file suffix semantics method to find the
      // class file name without package information:
      tabName = (String) FileUtil.cutExtension(policy.getClass().getName()).getValue();
      tabPolicies.addTab(tabName, null, new ErrorBarPolicyPanel(policy), "Does nothing");
    }

    this.setLayout(new BorderLayout());
    this.add(tabPolicies);
  }

  /**
   * Main method for testing.
   * <p>
   * 
   * @param args
   *          ignored.
   */

  public static void main(String[] args) {
    Chart2D chart = new Chart2D();

    // Create an ITrace:
    // Note that dynamic charts need limited amount of values!!!
    ITrace2D trace = new Trace2DSimple();
    trace.setColor(Color.RED);
    // create an error bar policy and configure it
    IErrorBarPolicy errorBarPolicy = new ErrorBarPolicyRelative(0.2);
    errorBarPolicy.setShowNegativeYErrors(true);
    errorBarPolicy.setShowPositiveYErrors(true);
    // errorBarPolicy.setShowNegativeXErrors(true);
    // errorBarPolicy.setShowPositiveXErrors(true);
    trace.setErrorBarPolicy(errorBarPolicy);
    // configure how error bars are rendered with an error bar painter:
    IErrorBarPainter errorBarPainter = new ErrorBarPainterConfigureable();
    errorBarPainter.setEndPointPainter(new PointPainterDisc());
    errorBarPainter.setEndPointColor(Color.GRAY);
    errorBarPainter.setSegmentPainter(new PointPainterLine());
    errorBarPainter.setSegmentColor(Color.LIGHT_GRAY);
    // add the painter to the policy
    errorBarPolicy.setErrorBarPainter(errorBarPainter);

    // Fini: now we have configured how error bars look,
    // which parts they render (end point, start point, segment), in which
    // direction they should extend and what kind of error is calculated.

    // Add all points, as it is static:
    for (double i = 2; i < 40; i++) {
      trace.addPoint(i, Math.random() * i + i * 10);
    }
    // Add the trace to the chart:
    chart.addTrace(trace);
    ErrorBarWizard wizard = new ErrorBarWizard(trace);

    // Show it:
    JFrame frame = new JFrame(ErrorBarWizard.class.getName());
    frame.getContentPane().add(wizard);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setSize(600, 600);
    frame.setVisible(true);

  }

}
