/*
 * UnitPico.java
 *
 * Created on 6. September 2001, 19:33
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
public class UnitPico extends Unit {
    public UnitPico() {
        this.factor = 0.000000000001;
        this.prefix = "p";
    }
}
