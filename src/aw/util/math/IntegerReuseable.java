
package aw.util.math;

/**
 * This is a plain wrapper around an Integer. I needed an 
 * wrapper of an primitive int to share the same value between different instances and 
 * to have the changes made to the primitive value take effect on all owners of the 
 * same instance.<br>
 * What a pity that java.lang.Integer does not allow to change it's internal value at 
 * runtime. Every time a new Integer has to be constructed.
 **/
public class IntegerReuseable
{
    /**
     * The smallest value of type <code>int</code>. The constant 
     * value of this field is <tt>-2147483648</tt>.
     */
    public static final int   MIN_VALUE = 0x80000000;

    /**
     * The largest value of type <code>int</code>. The constant 
     * value of this field is <tt>2147483647</tt>.
     */
    public static final int   MAX_VALUE = 0x7fffffff;

	public static final ArithmeticException OVERFLOW = new ArithmeticException("Overflow detected. Value saved unchanged.");
	public static final ArithmeticException CARRY = new ArithmeticException("Carry detected. Value saved unchanged.");
    /**
     * The value of the Integer.
     *
     * @serial
     */
    private int value;

    /**
     * Constructs a newly allocated <code>Integer</code> object that
     * represents the primitive <code>int</code> argument.
     *
     * @param   value   the value to be represented by the <code>Integer</code>.
     */
    public IntegerReuseable(int value) {
		this.value = value;
    }



    /**
     * Returns a String object representing this Integer's value. The 
     * value is converted to signed decimal representation and returned 
     * as a string, exactly as if the integer value were given as an 
     * argument to the {@link java.lang.Integer#toString(int)} method.
     *
     * @return  a string representation of the value of this object in
     *          base&nbsp;10.
     */
    public String toString() {
		return String.valueOf(value);
    }



    
    public void setValue(int value){
    	this.value = value;
    }
    
    public int intValue(){
    	return this.value;
    }
    public int getValue(){
    	return this.value;
    }
    
    public void add(int i)throws ArithmeticException{
    	int oldval = this.value;
    	this.value+=i;
    	if(oldval>this.value){
    		this.value = oldval;
    		throw OVERFLOW;
    	}
    }
    
    public void add(Integer i)throws ArithmeticException{
    	this.add(i.intValue());
    }
    
    public void add(IntegerReuseable i)throws ArithmeticException{
    	this.add(i.getValue());
    }
    
    public void sub(int i)throws ArithmeticException{
    	int oldval = this.value;
    	this.value -= i;
    	if(oldval<this.value){
    		this.value = oldval;
    		throw CARRY;
    	}
    }
    
    public void sub(Integer i)throws ArithmeticException{
    	this.sub(i.intValue());
    }
    
    public void sub(IntegerReuseable i)throws ArithmeticException{
    	this.sub(i.intValue());
    }
}
