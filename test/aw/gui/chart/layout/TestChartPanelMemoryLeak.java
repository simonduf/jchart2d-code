/*
 *  TestChartPanelMemoryLeak.java of project jchart2d, a 
 *  test for a memory leak detected by Pieter-Jan Busschaert.
 *  Copyright 2006 (C) Achim Westermann, created on 23:52:11.
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
package aw.gui.chart.layout;

import junit.framework.TestCase;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DLtd;

/**
 * 
 * TestChartPanelMemoryLeak.java of project jchart2d, a test for a memory leak
 * detected by Pieter-Jan Busschaert.
 * <p>
 * 
 * @author Pieter-Jan Busschaert
 * 
 * 
 * @version $Revision$
 */
public class TestChartPanelMemoryLeak extends TestCase {
  public void testMemoryLeak() {
    Chart2D chart = new Chart2D();
    ChartPanel chartPanel = new ChartPanel(chart);
    ITrace2D trace;
    for (int i = 0; i < 10000; i++) {
      trace = new Trace2DLtd(16383);
      if (i % 20 == 0) {
        System.out.println(i);
      }
      chart.addTrace(trace);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
      chart.removeTrace(trace);
    }
  }
}
