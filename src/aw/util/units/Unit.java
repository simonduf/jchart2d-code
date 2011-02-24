/*
 * Unit.java
 *
 * Created on 3. September 2001, 18:40
 */

package aw.util.units;


/**
 *  @see aw.util.units.UnitFactory
 *  @see aw.util.units.UnitSystem
 *  @see aw.util.units.UnitSystemSI
 *
 * @author  <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * @version 1.0
 */
public abstract class Unit extends Object {

    public double factor;
    protected String prefix;
    protected int decimals = 2;
    
    /**
     *  Transforms the given absolute value into the represented unit value 
     *  by dividing by the specific factor;
     *  The result is rounded using the actual decimal setting.
     **/
    public double getValue(double value){
       return round(value/this.factor);
    }
    public String getLabel(double value){
        return new StringBuffer().append(round(value/this.factor)).append(" ").append(this.prefix).toString();
    }
    
    public String getLabel(){
        return new StringBuffer().append(this.prefix).toString();
    }
    
    public String toString(){
        return this.getLabel();
    }
    /**
     *  factor is public!
     **/
    public double getFactor(){
        return this.factor;
    }
    
    public String getPrefix(){
        return this.prefix;
    }
    
    
    public void setDecimals(int aftercomma){
        if(aftercomma>=0)
            this.decimals = aftercomma;
    }
    public int getDecimals(){
        return this.decimals;
    }
    
    public double round(double value){
        double tmp = Math.pow(10,this.decimals);
        return (Math.floor(value*tmp + 0.5d))/tmp;
    }
}
