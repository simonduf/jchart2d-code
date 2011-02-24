/*
 *  UnitPeta.java, unit for peta prefix.
 *  Copyright (C) Achim Westermann, created on 12.05.2005, 20:11:17
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
package aw.util.units;

/**
 * Peta unit, 10 <sup>15 </sup>.
 * <p>
 *
 * @see aw.util.units.Unit
 *
 * @see aw.util.units.UnitFactory
 *
 * @see aw.util.units.UnitSystem
 *
 * @see aw.util.units.UnitSystemSI
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 */
public final class UnitPeta extends Unit {
  /**
   * Defcon.
   * <p>
   *
   */
  public UnitPeta() {
    this.m_factor = 1000000000000000d;
    this.m_unitName = "P";
  }
}
