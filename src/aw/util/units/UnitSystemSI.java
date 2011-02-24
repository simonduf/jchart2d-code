/*
 *  UnitSystemSI.java, unit system for the "International System of Units" (abbr. SI).
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
 * The unit system for the "International System of Units" (SI).
 * <p>
 *
 * @see aw.util.units.Unit
 *
 * @see aw.util.units.UnitFactory
 *
 * @see aw.util.units.UnitSystem
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.3 $
 */
public final class UnitSystemSI implements UnitSystem {

  /** Singleton instance. */
  private static UnitSystem instance;

  /** The unit classes of this system. */
  private static final Class[] UNITS = new Class[] {UnitFemto.class, UnitNano.class,
      UnitMicro.class, UnitMilli.class, UnitUnchanged.class, UnitKilo.class, UnitMega.class,
      UnitGiga.class, UnitTera.class, UnitPeta.class };

  /**
   * Singleton retrieval method.
   * <p>
   *
   * @return the unique instance within the current VM.
   */
  public static UnitSystem getInstance() {
    if (UnitSystemSI.instance == null) {
      UnitSystemSI.instance = new UnitSystemSI();
    }
    return UnitSystemSI.instance;
  }

  /**
   * Singleton constructor.
   * <p>
   *
   */
  private UnitSystemSI() {
  }

  /**
   * @see aw.util.units.UnitSystem#getUnits()
   */
  public Class[] getUnits() {
    return UnitSystemSI.UNITS;
  }
}
