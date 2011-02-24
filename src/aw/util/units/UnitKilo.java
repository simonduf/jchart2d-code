/*
 * UnitKilo.java
 *
 * Created on 6. September 2001, 18:59
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

public class UnitKilo extends Unit{
    public UnitKilo() {
        this.factor = 1000;
        this.prefix = "K";
    }
}

