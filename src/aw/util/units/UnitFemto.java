
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
public class UnitFemto extends Unit {

    /** Creates new UnitFemto */
    public UnitFemto() {
        this.factor = 0.000000000000001;
        this.prefix = "f";
    }
}
