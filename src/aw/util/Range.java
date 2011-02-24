/*
 * 
 *  Range.java, a simple data structure to express min and max.
 *  Copyright (C) Achim Westermann, created on 09.09.2004, 12:38:21  
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
package aw.util;

import java.io.Serializable;

/**
 * <p>
 * A simple data structure that defines a minimum and a maximum 
 * and knows, what lies within it and what not. 
 * </p>
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class Range implements Serializable {
  protected double min;
  protected double max;

  /**
   * <p>
   * Constructs a new Range that covers the given bounds. 
   * </p>
   */
  public Range(double min,double max) {
    if(min==Double.NaN){
      throw new IllegalArgumentException("Cannot work on Double.NaN for min.");
    }
    if(max==Double.NaN){
      throw new IllegalArgumentException("Cannot work on Double.NaN for min.");
    }
    if(min<max){
      this.min = min;
      this.max = max;
    }
    else{
      this.min = max;
      this.max = min;
    }
  }
  
  public double getMin(){
    return this.min;
  }
  
  public double getMax(){
    return this.max;
  }
  
  public double getExtent(){
    return this.max-this.min;
  }
  
  /**
   * <p>
   * Force this Range to cover the given value.
   * </p>
   * @return true, if an internal modification of one 
   * bound took place, false else.
   *
   */
  public boolean ensureContained(double contain){
    boolean ret = false;
    if(contain<this.min){
      ret = true;
      this.min = contain;
    }
    else if(contain>this.max){
      ret = true;
      this.max = contain;
    }
    return ret;
  }
  
  public boolean isContained(double contained){
    return ((this.min<=contained) && (this.max>= contained));
  }
}
