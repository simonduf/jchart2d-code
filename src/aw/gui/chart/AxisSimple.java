/*
 *
 *  AxisSimple.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 24.03.2005, 01:54:39
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

package aw.gui.chart;

/**
 * <p>
 * A basic <code>{@link Axis}</code> implementation that does not change the
 * unit of the scale labels to display.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.7 $
 *
 * @deprecated this class has been deprecated with version 1.10 as
 *             <code>{@link aw.gui.chart.Axis}</code> has been made
 *             non-abstract because it uses the unit member for scale labels now
 *             and <code>{@link aw.gui.chart.Axis#getUnit()}</code> has been
 *             implemented there. It will not be supported past version 1.2:
 *             Replace it's usage by <code>{@link Axis}</code>.
 *
 */
public final class AxisSimple extends Axis {

}
