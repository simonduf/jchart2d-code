
package aw.util;
import java.util.Map;
import java.util.Date;
/**
 *  Simple wrapper around a time in ms and a value Object. 
 *  The key is the time in ms and may be used in a Map. 
 *  The <code>compareTo</code> -method compares the key. 
 * @author  <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * @version 1.0
 */
    public class TimeStampedValue implements Map.Entry,Comparable{
        long key;
        Object value;
        /**
         *  choosable value taken for timestamp.
         **/
        public TimeStampedValue(long key,Object value){
            this.key = key;
            this.value = value;
        }
        /**
         *  Takes the actual time (creation time) as timestamp.
         **/
        public TimeStampedValue(Object value){
            this(System.currentTimeMillis(),value);
        }
        
        public Object getKey(){
            return new Long(this.key);
        }
        public Object getValue(){
            return this.value;
        }
        public long getTime(){
            return this.key;
        }
        
        /**
         *  Returns true, if o!=null && this.key.equals(0) && o.insanceOf(TimeStampedValue).
         **/
        public boolean equals(Object o){
            if(o==null)return false;
            TimeStampedValue compare =null;
            try{
                compare = (TimeStampedValue)o;
            }catch(ClassCastException e){
                return false;
            }
            if(this.getTime()==compare.getTime())return true;
            return false;
        }
        
        
        public Object setValue(Object value){
            Object ret = this.value;
            this.value = value;
            return ret;
        }
        
        /*
         *  Compares the internal time- representing key due to the 
         *  specification of Interface Comparable. 
         *  @see java.lang.Comparable
         **/
        public int compareTo(java.lang.Object obj) {
            TimeStampedValue other = (TimeStampedValue)obj;
            if(this.key<other.key)return -1;
            if(this.key==other.key)return 0;
            return 1;
        }
        /**
         *  For normal a timestamp represents a value regarded at a time. 
         *  But it is also thinkable to mark a value for expiration in the 
         *  future. This method returns true if the internal time- representing 
         *  key is smaller than the actual time.
         **/
        public boolean isPast(){
            return this.key<System.currentTimeMillis();
        }
        
    }