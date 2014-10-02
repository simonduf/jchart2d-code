/*
 * TestTrace2DReplacing.java of project jchart2d, <enterpurposehere>. Copyright
 * (C) 2002 - 2014, Achim Westermann, created on Jun 20, 2014
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * If you modify or optimize the code in a useful way please let me know.
 * Achim.Westermann@gmx.de
 * 
 * 
 * File : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $ Date :
 * $Date: 2009/02/24 16:45:41 $ Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart.traces;

import static org.junit.Assert.fail;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.test.ATestChartOperations;

import org.junit.Test;

public class TestTrace2DReplacing extends ATestChartOperations {

  public TestTrace2DReplacing(String arg0) {
    super(arg0);
  }

  @Test
  public void testReplaceAllPoints() {
    ATestChartOperations.AChartOperation operation = new AChartOperation(
        "Inserting points with same x values but different y values to Trace2DReplacing: Trace should be inverted.") {

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.IChart2DOperation#action(info.monitorenter.gui.chart.Chart2D)
       */
      public Object action(final Chart2D chart) {
        ITrace2D trace = chart.getTraces().first();
        for (int i = 0; i < 10; i++) {
          trace.addPoint(i, 10 - i);
        }
        return null;
      }

      /**
       * Creates a <code>{@link Trace2DLtdReplacing}</code>.
       * <p>
       * 
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation
       *      #createTraces()
       */
      @Override
      public ITrace2D[] createTraces() {
        ITrace2D trace = new Trace2DReplacing();
        return new ITrace2D[] {trace };
      }

      /**
       * @see info.monitorenter.gui.chart.test.ATestChartOperations.AChartOperation#fillTrace(info.monitorenter.gui.chart.ITrace2D)
       */
      @Override
      public void fillTrace(ITrace2D trace) {
        for (int i = 0; i < 10; i++) {
          trace.addPoint(i, i);
        }
      }
    };
    this.setTestOperation(operation);

  }

}
