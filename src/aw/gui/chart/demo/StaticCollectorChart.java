/*
 *  StaticCollectorChart.java, utility test class for jchart2d.
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DSimple;
import aw.gui.chart.io.AbstractStaticDataCollector;
import aw.gui.chart.io.PropertyFileStaticDataCollector;
import aw.gui.chart.layout.ChartPanel;

/**
 * <p>
 * A chart to test labels. This chart uses a
 * <code>{@link aw.gui.chart.LabelFormatterNumber}</code> that formats to
 * whole integer numbers. No same labels should appear.
 * </p>
 *
 * @author Achim Westermann
 *
 * @version $Revision: 1.2 $
 */
public class StaticCollectorChart extends JPanel {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3689069555917797688L;

  /**
   * Shows the chart in a frame and fills it with the data file that is
   * specified by the first command line argument.
   * <p>
   *
   * @param args
   *          arg[0] has to be a file name of a data file in
   *          java.util.Properties format.
   *
   * @throws IOException
   *           if sth. goes wrong.
   */
  public static void main(final String[] args) throws IOException {
    JFrame frame = new JFrame("SampleChart");
    InputStream stream = new FileInputStream(new File(args[0]));
    ITrace2D trace = new Trace2DSimple();
    AbstractStaticDataCollector collector = new PropertyFileStaticDataCollector(trace, stream);

    frame.getContentPane().add(new StaticCollectorChart(collector));
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setSize(600, 600);
    frame.setVisible(true);
  }

  /** The internal chart. */
  private Chart2D m_chart;

  /**
   * Creates a chart that collects it's data from the given collector.
   * <p>
   *
   * @param datacollector
   *          the data collector to use.
   *
   * @throws IOException
   *           if collecting data fails.
   */
  public StaticCollectorChart(final AbstractStaticDataCollector datacollector) throws IOException {
    this.setLayout(new BorderLayout());
    this.m_chart = new Chart2D();

    // Add all points, as it is static:
    datacollector.collectData();
    // Add the trace to the chart:
    this.m_chart.addTrace(datacollector.getTrace());

    // Make it visible:
    this.add(new ChartPanel(this.m_chart), BorderLayout.CENTER);

  }

  /**
   * Returns the chart.
   * <p>
   *
   * @return Returns the chart.
   */
  public final Chart2D getChart() {
    return this.m_chart;
  }
}
