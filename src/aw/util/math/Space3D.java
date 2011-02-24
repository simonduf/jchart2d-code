
package aw.util.math;
/**
 * A Space3D is a container for instances of interface Object3D. 
 * These Object3D - instances may be added to the Space3D which will be 
 * able to perform all operations concerning about the relation of the contained 
 * Object3D - instances, e.g. their distance between the contained Object3D - instances. 
 * Furthermore it may be moved (offsets), scaled or turned in it or the whole Space3D's axes 
 * may be turned and scaled. 
 **/
public interface Space3D {
	/**
	 * The given Object3D is added to this Space3D. 
	 **/
	public void add(Object3D o);
	/**
	 * The given Object3D is removed from this Space3D. 
	 * @return true, if it was successfully remove, else false (was not contained before).
	 **/
	public boolean remove(Object3D o);
	/** 
	 * The given Object is scaled on the x-axis by percent. 
	 * If percent is lower than 1.0 it will be shortened, if percent is 
	 * higher than 1.0 it will be prolonged. 
	 * @return true, if the operation was successful. If false is returned, this may have 
	 * 	several reasons: <br>
	 * 	- The given Object3D is not contained in this Space3D (comparison by public boolean equals()).<br>
	 * 	- The given double percent is lower than 0.0. 
	 **/
	public boolean scaleX(Object3D contained,double percent);
	/** 
	 * The given Object is scaled on the y-axis by percent. 
	*	@see #public boolean scaleX(Object3D contained, double percent);
	 **/
	public boolean scaleY(Object3D contained,double percent);
	/** 
	 * The given Object is scaled on the z-axis by percent. 
	*	@see #public boolean scaleX(Object3D contained, double percent);
	 **/
	public boolean scaleZ(Object3D contained,double percent);
	/** 
	 * The given Object is scaled on  x-,y and z-axis by percent. 
	 * The relation between all dimensions will be kept. 
	*	@see #public boolean scaleX(Object3D contained, double percent);
	 **/
	public boolean scaleAll(Object3D contained,double percent);
	/** 
	 * The given Object3D is turned around the x-axis by int angle. 
	 * If angle is positive, the given Object3D will be turned clockwise. 
	 * Argument angle is in degrees and will be taken modulo 360 (which is a full turn). 
	 * @return true, if the operation was successful. If false is returned the given Object3D 
	 * is not contained in this Space3D (comparison by public boolean equals()).<br>
	 **/	
	public boolean turnX(Object3D contained,int angle);
	/** 
	 * @see #public boolean turnX(Object3D contained,double percent);
	 **/
	public boolean turnY(Object3D contained,int angle);
	/** 
	 * @see #public boolean turnX(Object3D contained,double percent);
	 **/
	public boolean turnZ(Object3D contained,int angle); 
	/**
	 * The distance in Euklid's norm (shortest line)
	 * between the center of the two given Object3D instances is returned. 
	 * Therefore their Method <code>public Object3D getCenter()</code> will be invoked. 
	 * @return NaN, if first or second is not contained in this Space3D.
	 **/
	public double distance(Object3D first, Object3D second);
}
