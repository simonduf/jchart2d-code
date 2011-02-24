/*
 *  BlankChart.java of project jchart2d
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

import javax.swing.JFrame;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contributed by zoola for bug report <a href=
 * "http://sourceforge.net/tracker/?func=detail&atid=459734&aid=1426461&group_id=50440"
 * target="_blank">#1426461 </a>.
 * <p>
 * 
 * @author zoola
 * 
 * @version $Revision: 1.5 $
 */
public final class AddRemoveTrace
    extends TestCase {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3689069560279873588L;

  /**
   * Creates an empty chart and displays it.
   * <p>
   * 
   */
  public AddRemoveTrace(String testname) {
    super(testname);
    this.m_frame = new JFrame(this.getClass().getName());
    Chart2D chart = new Chart2D();
    ITrace2D trace = new Trace2DLtd();
    chart.addTrace(trace);
    chart.removeTrace(trace);
    this.m_frame.getContentPane().add(chart);
    this.m_frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.m_frame.pack();
    this.m_frame.setVisible(true);
  }

  /** The display frame. */
  private JFrame m_frame;

  /**
   * Main entry.
   * <p>
   */
  public void testAddRemoveTrace() throws InterruptedException {
    while (this.isVisible()) {
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
    suite.setName(AddRemoveTrace.class.getName());

    suite.addTest(new AddRemoveTrace("testAddRemoveTrace"));

    return suite;
  }

  /**
   * @see java.awt.Component#isVisible()
   */
  public boolean isVisible() {
    return this.m_frame.isVisible();
  }
}
