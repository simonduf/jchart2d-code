/*
 * DefaultDisplayTest.java,  <enter purpose here>.
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
package aw.gui.chart.demo;

import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DSimple;

/**
 * <p>
 * TODO Write a comment ending with '.'
 * </p>
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 * @version  $Revision: 1.2 $
 *
 */
public class DefaultDisplayTest extends AbstractDisplayTest {

  /* (non-Javadoc)
   * @see aw.gui.chart.demo.AbstractDisplayTest#createTrace()
   */
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

  /* (non-Javadoc)
   * @see aw.gui.chart.demo.AbstractDisplayTest#configure(aw.gui.chart.demo.StaticCollectorChart)
   */
  protected void configure(StaticCollectorChart chart) {
    // nop
  }

//  public void defaultDisplayTest() throws IOException {
//    displayTest();
//  }

}
