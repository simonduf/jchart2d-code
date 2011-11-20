/*
 * TracePoint2D, a tuned Point2D.Double for use with ITrace2D- implementations. Copyright (c) 2004 - 2011 Achim Westermann,
 * Achim.Westermann@gmx.de
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * If you modify or optimize the code in a useful way please let me know. Achim.Westermann@gmx.de
 */

package info.monitorenter.gui.chart;

import java.awt.geom.Point2D;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A specialized version of <code>java.awt.Point2D.Double </code> who carries two further values: <code> double scaledX</code> and
 * <code>double scaledY</code> which allow the <code>Chart2D</code> to cache the scaled values (between 0.0 and 1.0) without having to keep
 * a copy of the aggregators (<code>ITrace2D</code>) complete tracepoints.
 * <p>
 * This avoids the necessity to care for the correct order of a set of scaled tracepoints copied for caching purposes. Especially in the
 * case of new <code>TracePoint2D</code> instances added to a <code>ITrace2D</code> instance managed by a <code>Chart2D</code> there remains
 * no responsibility for sorting the cached copy. This allows that the managing <code>Chart2D</code> may just rescale the newly added
 * tracepoint instead of searching for the correct order of the new tracepoint by value - comparisons of x and y: The
 * <code>TracePoint2D</code> passed to the method <code>traceChanged(Chart2DDataChangeEvent e)</code> coded in the argument is the original.
 * <br>
 * <p>
 * Why caching of scaled values for the coordinates? <br>
 * This takes more RAM but else for every <code>repaint()</code> invocation of the <code>Chart2D</code> would force all tracepoints of all
 * traces to be rescaled again.
 * <p>
 * A TracePoint2D will inform it's listener of type <code>ITrace</code> on changes of the internal values.
 * <p>
 * 
 * @deprecated this has been moved to another package: use {@link info.monitorenter.gui.chart.tracepoints.TracePoint2D} instead.
 * 
 * @author Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de </a>
 * 
 * @version $Revision: 1.34 $
 */
public class TracePoint2D extends info.monitorenter.gui.chart.tracepoints.TracePoint2D {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3618980079204512309L;
    /**
     * Intended for <code>{@link TracePointProviderDefault}</code> only.
     * <p>
     */
    protected TracePoint2D() {
      super();
    }

    /**
     * Construct a TracePoint2D whose coords are initalized to (x,y).
     * <p>
     * 
     * @param xValue
     *            the x value to use.
     * @param yValue
     *            the y value to use.
     */
    public TracePoint2D(final double xValue, final double yValue) {
      super(xValue, yValue);
    }
}
