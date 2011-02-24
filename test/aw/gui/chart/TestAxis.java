/*
 *
 *  TestAxisAutoUnit.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 16.07.2005, 10:52:43
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
package aw.gui.chart;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class TestAxis extends ATestAxis {

  /* (non-Javadoc)
   * @see aw.gui.chart.TestAxis#createAxisY()
   */
  protected Axis createAxisY() {
    // TODO Auto-generated method stub
    return this.createAxisX();
  }

  /* (non-Javadoc)
   * @see aw.gui.chart.TestAxis#createAxisX()
   */
  protected Axis createAxisX() {
    return new Axis();
  }

  /* (non-Javadoc)
   * @see aw.gui.chart.TestAxis#createTrace()
   */
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

}
