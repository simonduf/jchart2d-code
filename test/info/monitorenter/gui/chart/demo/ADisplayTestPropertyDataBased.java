/*
 * ADisplayTest.java,  <enter purpose here>.
 * Copyright (C) 2006  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 */
package info.monitorenter.gui.chart.demo;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.demos.StaticCollectorChart;
import info.monitorenter.gui.chart.io.PropertyFileStaticDataCollector;

import java.io.IOException;
import java.io.InputStream;

/**
 * Basic junit test method hook that searches for files in this package with
 * name "textX.properties" (where "X" is a number that starts from 1) collects
 * the data in the files and shows a chart with a trace that contains this data
 * along with buttons for judging the display as right or wrong.
 * <p>
 * Note that the test files have to be named with ascending numbers to ensure
 * all are shown.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.2 $
 * 
 */
public abstract class ADisplayTestPropertyDataBased
    extends ADisplayTest {

  private int count = 1;

  /**
   * @see info.monitorenter.gui.chart.demo.ADisplayTest#getNextChart()
   */
  protected final StaticCollectorChart getNextChart() throws IOException {
    InputStream in;
    ITrace2D trace;
    StaticCollectorChart chart;
    in = ADisplayTestPropertyDataBased.class.getResourceAsStream("test" + count + ".properties");
    if (in == null) {
      chart = null;
    } else {
      trace = createTrace();
      chart = new StaticCollectorChart(new PropertyFileStaticDataCollector(trace, in));
    }
    this.count++;
    return chart;
  }

  /**
   * @param arg0
   */
  public ADisplayTestPropertyDataBased(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

}
