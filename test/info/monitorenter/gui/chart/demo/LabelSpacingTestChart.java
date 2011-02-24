/*
 *  LabelSpacingTestChart.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 22:22:26.
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
package info.monitorenter.gui.chart.demo;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Contributed by zoola for bug report <a href=
 * "http://sourceforge.net/tracker/index.php?func=detail&aid=1427330&group_id=50440&atid=459734"
 * target="_blank">#1427330 </a>.
 * <p>
 *
 * @author zoola
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.3 $
 */
public final class LabelSpacingTestChart extends TestCase {

  /**
   * Generated <code>serialVersionUID</code>.
   */
 private static final long serialVersionUID = 3689069560279873588L;

 /** The display frame. */
 private JFrame m_frame;
 
  /**
   * Creates a chart with three traces and different labels and displays it.
   * <p>
   *
   */
  public LabelSpacingTestChart(String testName) {
    super(testName);
    this.m_frame = new JFrame(testName);
    Chart2D chart = new Chart2D();
    ITrace2D trace1 = new Trace2DLtd();
    trace1.setName("lottolotto");
    trace1.setColor(Color.RED);
    chart.addTrace(trace1);

    ITrace2D trace2 = new Trace2DLtd();
    trace2.setName("bbb");
    trace2.setColor(Color.BLUE);
    chart.addTrace(trace2);

    ITrace2D trace3 = new Trace2DLtd();
    trace3.setColor(Color.BLACK);
    trace3.setName("ffollejolle");
    chart.addTrace(trace3);
    this.m_frame.getContentPane().add(chart);
    this.m_frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.m_frame.setSize(new Dimension(400, 300));
    this.m_frame.setVisible(true);
  }

  public void testLabelSpacing() throws InterruptedException {
    while(this.isVisible()) {
      Thread.sleep(1000);
    }
  }
  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(LabelSpacingTestChart.class.getName());

    suite.addTest(new LabelSpacingTestChart("testLabelSpacing"));

    return suite;
  }

  /**
   * @see java.awt.Component#isVisible()
   */
  public boolean isVisible() {
    return this.m_frame.isVisible();
  }
}
