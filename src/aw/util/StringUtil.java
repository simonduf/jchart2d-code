package aw.util;

import java.lang.reflect.Array;
import java.util.List;
/**
 * Nice static helpers for working with Strings. Maybe not always the fastest solution
 * to call in here, but working. Also usabel for seeing examples and cutting code for
 * manual inlining.
 * @author Achim.Westermann@gmx.de
 **/
public class StringUtil {
    
    private static StringUtil instance = null;
    
    public static StringUtil instance() {
        if(instance==null)instance=new StringUtil();
        return instance;
    }
    
    private StringUtil() {
    }
    
    
    /**
     * Appends the necessary amount of spaces to the string until it has the givn length.
     * No Exception is thrown, if the length of the String is shorter than the given
     * length, but nothing will happen and a message will be printed to the System.out.
     *
     *
     * @param   s the String to expand.
     * @param   length  the desired length of the String to be returned.
     * @return  A String that represents the content of the old String including extra whitespaces.
     */
    public static final String setSize(String s,int length) {
        int oldlen = s.length();
        if (oldlen>length) {
            System.err.println("greenpeace.util.setSize(String s,int length): length ("+length+") is smaller than s.length("+oldlen+") : "+s);
            return s;
        }
        int tofill = length-oldlen;
        s=appendSpaces(s,tofill);
        return s;
    }
    
    
    /**
     *	Appends the given amount of spaces to the String. Keep the param on the calling side. It's reference is modified.
     *
     *	Not ment for big append -operations because in a loop alway just one space is added.
     * @param   s
     * @param   count
     */
    public static final String appendSpaces(String s,int count) {
        StringBuffer tmp = new StringBuffer(s);
        for(int i=0;i<count;i++)tmp.append(" ");
        return tmp.toString();
    }
    
    
    /**
     *	If the given Object is no Array, it's toString - method is invoked.
     *	Primitive type - Arrays and Object - Arrays are introspected using java.lang.reflect.Array.
     * Convention for creation fo String - representation: <br>
     * <pre>
     * Primitive Arrays:	"["+isArr[0]+","+isArr[1]+.. ..+isArr[isArr.length-1]+"]"
     *
     *
     * Object Arrays : 		"["+isArr[0].toString()+","+isArr[1].toString()+.. ..+isArr[isArr.length-1].toString+"]"
     *						Two or three - dimensional Arrays are not supported (should be reflected in a special output method, e.g.as a field)
     * other Objects:		toString()
     * </pre>
     * @param   isArr  The Array to represent as String.
     * @return  A String-represetation of the Object
     */
    public static final String ArrayToString(Object isArr) {
        if(isArr==null)return "null";
        Object element;
        StringBuffer tmp = new StringBuffer();
        try {
            int length = Array.getLength(isArr);
            tmp.append("[");
            for(int i=0;i<length;i++) {
                element = Array.get(isArr,i);
                if(element==null)
                    tmp.append("null");
                else
                    tmp.append(element.toString());
                if(i<length-1)tmp.append(",");
            }
            tmp.append("]");
        }catch(ArrayIndexOutOfBoundsException bound) {
            tmp.append("]");	//programming mistake or bad Array.getLength(obj).
            return tmp.toString();
            
        }catch(IllegalArgumentException noarr) {
            return isArr.toString();
        }
        return tmp.toString();
    }
    
    /**
     * Little String output -  helper that modifies the given LinkedList by getting it's
     * Objects and replace them by their toString() - representation.
     * What is special? <br>
     * If an Object in the given List is an Array (of Objects or primitive datatypes)
     * reflection will be used to create a String - representation of them.
     * The changes are reflected in the Objects that are in the given List. So keep a reference
     * to it. If you are sure, that your List does not contain Arrays do not use this method
     * to avoid overhead.<br>
     * Avoid structural modifications (remove) of the list while using this method. This method
     * or better: the given List is only thread - safe if the list is synchronized.<br>
     * A clever VM  (hotspot) will be able to inline this function because of void return.
     **/
    public static final void ArraysToString(List objects) {
        if (objects==null)return;
        int stop = objects.size();
        for(int i=0;i<stop;i++) {
            objects.add(i,ArrayToString(objects.remove(i)));
        }
    }
    
    /**
     * Only use this method in following case: <br>
     * <ul>
     * <li>
     * You have got an AbstractList or subclass containing Objects you do not know.
     * </li>
     * <li>
     * You want to transform all Elements in the List to Strings.
     * </li>
     * <li>
     * There might be Array's in the Object (whose toString method will return only their hashcode).
     * </li>
     * </ul>
     * <br>
     * What happens?
     * <ul>
     * <li>
     * All Objects, even primitive Arrays in your List will be turned to String- representation.
     * </li>
     * <li>
     * The result will replace the old position of the Object in the given List. The old Objects will get lost!
     * </li>
     * <li>
     * All Strings will be filled with whitespaces until each String has the same length.
     * </li>
     * </ul>
     * At least this method will be speeded up by a hotspot VM by inlining this method.
     * <br><i>
     * An example:<br>
     * You just retrieved data from a db using jdbc and a generic class (resultset.getObject()).
     * You want to print the data to System.out or to save it to a file, but do not know, if
     * Arrays are contained. You want the output to be formatted (each line).
     * </i>
     **/
    public static final void toLongestString(List objects) {
        if (objects==null)return;
        int maxsize=0,tint=0;
        String tmp;
        int stop = objects.size();
        for(int i=0;i<stop;i++) {
            ArrayToString(objects.get(i));
            tmp=(String)objects.get(i);
            if((tint=tmp.length())>maxsize)maxsize=tint;
            objects.add(i,tmp);
        }
        //maxsize known.
        for(int i=0;i<stop;i++) {
            objects.add(i,setSize((String)objects.remove(i),maxsize));
        }
    }
    
    /**
     * No data is changed in the given List at all. But the String - representation of
     * all Objects, even Arrays is build to inspect.
     * <br>
     * Convention for creation fo String - representation: <br>
     * <pre>
     * Primitive Arrays:	as performed by this classes @see #ArrayToString .
     * Object Arrays : 	as performed by this classes @see #ArrayToString
     * other Objects:		toString()	(as performed by this classes @see #ArrayToString ).
     * </pre>
     * @return The length of the longest String - representation of an Object in the List <b>or 0 if objects was null or of size 0.</b>
     **/
    public static final int longestStringRepresentation(List objects) {
        if (objects==null)return 0;
        int maxsize=0,tint=0;
        String tmp;
        int stop = objects.size();
        for(int i=0;i<stop;i++) {
            tmp = ArrayToString(objects.get(i));
            if((tint=tmp.length())>maxsize)maxsize=tint;
        }
        //maxsize known.
        return maxsize;
    }
    
    /**
     * Only call this method once (not in a loop) and keep the result.
     **/
    public static String getNewLine() {
        return (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
    }
}