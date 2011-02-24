/*
 * UnitFactory.java
 *
 * Created on 13. September 2001, 00:15
 */

package aw.util.units;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
/**
 *  Singleton that caches instances whole unit- systems and provides you with 
 *  the matching unit for a maximum value. 
 *  @see aw.util.units.UnitSystem
 * @author  <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * @version 1.1
 */
public class UnitFactory extends Object {
    static Map unitsystems = new HashMap();
    static Unit unchanged = new UnitUnchanged();
    private static UnitFactory instance;
    
    private UnitFactory(){
    }
    
    public static UnitFactory getInstance(){
        if(instance == null)
            instance = new UnitFactory();
        return instance;
    }
    
    public Unit getUnit(double absoluteMax,UnitSystem units){
        List choice;
        if((choice = (List)this.unitsystems.get(units.getClass().getName()))==null){
            choice = new LinkedList();
            Class[] clazzs = units.getUnits();
            for(int i=0;i<clazzs.length;i++){
                if(!Unit.class.isAssignableFrom(clazzs[i])){
                    System.err.println("UnitFactory: wrong class "+clazzs[i].getName()+" delivered by "+units.getClass().getName());
                    continue;
                }
                else{
                    try{
                        choice.add(clazzs[i].newInstance());
                    }catch(InstantiationException e){
                        System.err.println("UnitFactory encountered problems by instantiation of "+clazzs[i].getName());
                    }catch(IllegalAccessException f){
                        System.err.println("UnitFactory has no access to "+clazzs[i].getName());
                    }
                }
            }
            // hardcoded minsearch sort:
            double tmpfactori,tmpfactorj;
            int min;
            int stop = choice.size();
            for(int i=0;i<stop-1;i++){
                min = i;
                tmpfactori = ((Unit)choice.get(i)).getFactor();
                for(int j =i+1;j<stop;j++){
                    if((tmpfactorj=((Unit)choice.get(j)).getFactor())<tmpfactori){
                        tmpfactori = tmpfactorj;
                        min=j;
                    }
                }
                choice.add(i,choice.remove(min));
            }
            this.unitsystems.put(units.getClass().getName(),choice);
        }
        //Now to find the right unit.
        Iterator it = choice.iterator();
        Unit ret = null,old = null;
        if(it.hasNext()){
            old = (Unit)it.next();
            while(it.hasNext()){
                ret = (Unit)it.next();
                if(absoluteMax<(ret.getFactor()))
                    return old;
                old = ret;
            }
            return old; // highes unit available
        }
        return unchanged;   // no clue
    }
}
