/*
 * UnitPeta.java
 *
 * Created on 6. September 2001, 19:39
 */

package aw.util.units;

/**
 *  @see aw.util.units.Unit
 *  @see aw.util.units.UnitFactory
 *  @see aw.util.units.UnitSystem
 *  @see aw.util.units.UnitSystemSI
 *
 * @author  <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * @version 1.0
 */
public class UnitPeta extends Unit {
    public UnitPeta() {
        this.factor = 1000000000000000d;
        this.prefix = "P";
    }
}
