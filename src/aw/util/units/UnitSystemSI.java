/*
 * UnitSystemSI.java
 *
 * Created on 14. September 2001, 10:58
 */

package aw.util.units;

/**
 *  @see aw.util.units.Unit
 *  @see aw.util.units.UnitFactory
 *  @see aw.util.units.UnitSystem
 *
 * @author  <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * @version 1.0
 */
public class UnitSystemSI implements UnitSystem {
    
    private static Class[]units = new Class[]{
        UnitFemto.class,
        UnitNano.class,
        UnitMicro.class,
        UnitMilli.class,
        UnitUnchanged.class,
        UnitKilo.class,
        UnitMega.class,
        UnitGiga.class,
        UnitTera.class,
        UnitPeta.class
    };
    
    private static UnitSystem instance;
    private UnitSystemSI(){};
    
    public static UnitSystem getInstance(){
        if(instance == null)
            instance = new UnitSystemSI();
        return instance;
    }
    
    public Class[] getUnits(){
        return units;
    }
}
