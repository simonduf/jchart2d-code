/*
 *
 *  TestStaticCollectorChart.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 23.04.2005, 08:21:12
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

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import junit.framework.TestCase;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DSimple;
import aw.gui.chart.io.AbstractStaticDataCollector;
import aw.gui.chart.io.PropertyFileStaticDataCollector;

/**
 * @author Achim Westermann
 */
public class TestStaticCollectorChart extends TestCase {

  /**
   * Tests {@link StaticCollectorChart}with data from test1.properties.
   * <p>
   *
   * @throws IOException
   *           if sth. goes wrong.
   */
  public final void testStaticCollectorChart1() throws IOException {
    InputStream stream = this.getClass().getResourceAsStream("test1.properties");
    ITrace2D trace = new Trace2DSimple();
    AbstractStaticDataCollector collector = new PropertyFileStaticDataCollector(trace, stream);
    StaticCollectorChart chart = new StaticCollectorChart(collector);
    show(chart);
  }

  /**
   * Tests {@link StaticCollectorChart}with data from test2.properties.
   * <p>
   *
   * @throws IOException
   *           if sth. goes wrong.
   */
  public final void testStaticCollectorChart2() throws IOException {
    InputStream stream = this.getClass().getResourceAsStream("test2.properties");
    ITrace2D trace = new Trace2DSimple();
    AbstractStaticDataCollector collector = new PropertyFileStaticDataCollector(trace, stream);
    StaticCollectorChart chart = new StaticCollectorChart(collector);
    // Adapt the decimal formatting for this data set.
    // chart.getChart().getAxisY().setFormatter(
    // new LabelFormatterNumber(new DecimalFormat("#.#####")));
    show(chart);
  }

  /**
   * Tests {@link StaticCollectorChart}with data from test2.properties.
   * <p>
   *
   * @throws IOException
   *           if sth. goes wrong.
   */
  public final void testStaticCollectorChart3() throws IOException {
    InputStream stream = this.getClass().getResourceAsStream("test3.properties");
    ITrace2D trace = new Trace2DSimple();
    trace.setColor(Color.RED);
    AbstractStaticDataCollector collector = new PropertyFileStaticDataCollector(trace, stream);
    StaticCollectorChart chart = new StaticCollectorChart(collector);
    // Adapt the decimal formatting for this data set.
    show(chart);
  }

  /**
   * Internal helper that shows the chart in a frame.
   * <p>
   *
   * @param chart
   *          the chart to display.
   */
  private void show(final StaticCollectorChart chart) {
    final JFrame frame = new JFrame("StaticCollectorChart");
    frame.getContentPane().add(chart);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        frame.setVisible(false);
        frame.dispose();
      }
    });
    frame.setSize(600, 600);
    frame.setVisible(true);
    while (frame.isVisible()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }
  }


  public static void main(String[]args) throws IOException{
    TestStaticCollectorChart test = new TestStaticCollectorChart();
    test.testStaticCollectorChart2();
  }
}
