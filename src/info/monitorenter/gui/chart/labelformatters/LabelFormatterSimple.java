/*
 *
 *  LableFormatterSimple.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 09:54:15
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
package info.monitorenter.gui.chart.labelformatters;

import info.monitorenter.gui.chart.ILabelFormatter;

import java.text.DecimalFormat;

/**
 * <p>
 * An IlabelFormatter implementation that just returns
 * <code>String.valueOf(value)</code>.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.1 $
 *
 */
public final class LabelFormatterSimple extends LabelFormatterNumber implements ILabelFormatter {
  /**
   * Default constructor that limits the internal maximum fraction digits to 2
   * and the maximum integer digits to 16.
   * <p>
   *
   */
  public LabelFormatterSimple() {
    super(new DecimalFormat("#"));
    this.m_nf.setMaximumFractionDigits(2);
    this.m_nf.setMaximumIntegerDigits(16);
  }

}
