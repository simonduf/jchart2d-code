/*
 *  UnitUnchanged.java, unit for no prefix.
 *  Copyright (C) Achim Westermann, created on 12.05.2005, 20:11:17
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
package aw.util.units;

/**
 * Unchanged unit, 10 <sup>0 </sup>.
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
 * @version $Revision: 1.3 $
 */
public final class UnitUnchanged extends Unit {
  /**
   * Defcon.
   * <p>
   *
   */
  public UnitUnchanged() {
    this.m_factor = 1;
    this.m_unitName = "";
  }
}
