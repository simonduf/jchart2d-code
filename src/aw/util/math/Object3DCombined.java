package aw.util.math;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
/**
 * A wrapper around multiple <code>Object3D</code> - instances. 
 * all operations performed on instances of this class will be routed to the 
 * contained client - instances. 
 **/
public class Object3DCombined implements Object3D {
	private List clients = new LinkedList();
	/**
	 * Creates a new <code>Object3DCombined</code> with no contained space. 
	 **/
	public Object3DCombined(){
	}
	/**
	 * Creates a new <code>Object3DCombined</code> with argument first as the first 
	 * contained client. 
	 **/
	public Object3DCombined(Object3D first){
		clients.add(first);
	}
	
	/**
	 * @see Object3D#isContained(Object3D)
	 */
	public boolean isContained(Object3D subspace) {
		java.util.Iterator it = this.clients.iterator();
		while(it.hasNext()){
			if(((Object3D)it.next()).isContained(subspace))return true;
		}
		return false;
	}

	/**
	 * @see Object3D#isIntersect(Object3D)
	 */
	public boolean isIntersect(Object3D cutspace) {
		java.util.Iterator it = this.clients.iterator();
		while(it.hasNext()){
			if(((Object3D)it.next()).isIntersect(cutspace))return true;
		}
		return false;
	}

	/**
	 * @see Object3D#isUnion(Object3D)
	 */
	public boolean isUnion(Object3D space) {
		java.util.Iterator it = this.clients.iterator();
		while(it.hasNext()){
			if(((Object3D)it.next()).isUnion(space))return true;
		}
		return false;
	}

	/**
	 * @see Object3D#union(Object3D)
	 */
	public Object3D union(Object3D space) {
		/*
		 *	First try to avoid adding new Object3D - instances to this wrapper. 
		 * Maybe the given space is contained within an Object3D already contained? 
		 */
		java.util.Iterator it = this.clients.iterator();
		Object3D tmp,tmpnew;
		while(it.hasNext()){
			tmp = (Object3D)it.next();
			if(tmp.isContained(space)){
					return this;
			}
		}
		/* 
		 * Maybe the newly added space is big enough to take up some other 
		 * Object3D's space already contained? 
		 */
		if(!((space instanceof Line3D)||(space instanceof Point3D))){
		 	it = this.clients.iterator();
		 	while(it.hasNext()){
		 		tmp = (Object3D)it.next();
		 		if(space.isContained(tmp)){
		 			it.remove();
		 		}
			}
		}
		if(space instanceof Object3DCombined){
		 		it = ((Object3DCombined)space).clients.iterator();
		 		while(it.hasNext()){
		 			this.clients.add(it.next());
		 		}
		}
		else this.clients.add(space);
		return this;
	}

	/**
	 * @see Object3D#intersection(Object3D)
	 */
	public Object3D intersection(Object3D space) {
		Object3D combined = new Object3DCombined();
		Object3D tmp;
		java.util.Iterator it = this.clients.iterator();
		while(it.hasNext()){
			tmp = ((Object3D)it.next()).intersection(space);
			if(tmp != Object3DVoid)combined.union(tmp);
		}
		return combined;
	}


	/**
	 * I can not recommend using this method on instances of <code>Object3DCombined</code>. 
	 * Instances of this class are wrapping several instances of type <code>Object3D</code>. 
	 * I have no clue how to find a center of such a cluster of several 3D - objects. <br>
	 * What is done here is to get the center - points of all clients contained and calculate 
	 * the arithmetical average of the x,y and z - values. <br>
	 * What is wrong about it.?<br>
	 * - The center of a spreaded 3D - object has no definition known to me. <br>
	 * - If it should be the weighted center of 3D - objects the fact is left out that the 
	 *   center of a small 3D - object such as a simple <code>Point3D</code> (which has no volume!) weighs 
	 *   just as much as the center of a big tetrahedron.
	 * @see Object3D#getCenter()
	 */
	public Object3D getCenter() {
		Point3D tmp,ret = new Point3D(0.0,0.0,0.0);
		Object3D tmpcast;
		int cnt = 0;
		java.util.Iterator it = this.iterator();
		while(it.hasNext()){
			tmpcast = ((Object3D)it.next()).getCenter();
			if(tmpcast!=Object3DVoid){
				tmp = (Point3D)tmpcast;
				ret.x += tmp.x;
				ret.y += tmp.y;
				ret.z += tmp.z;
				cnt++;
			}
		}
		if(cnt!=0){
			ret.x /= cnt;
			ret.y /= cnt;
			ret.z /= cnt;
			return ret;
		}
		return Object3DVoid;
	}

	/**
	 * @see Object3D#iterator()
	 */
	public java.util.Iterator iterator() {
		return new Iterator();
	}
	public Object clone(){
		java.util.Iterator it = this.clients.iterator();
		Object3DCombined ret = new Object3DCombined();
		while(it.hasNext()){
			ret.clients.add(it.next());
		}
		return ret;
	}
	
	public String toString(){
		StringBuffer ret = new StringBuffer("combined space:\n");
		java.util.Iterator it = this.clients.iterator();
		while(it.hasNext()){
			ret.append("   ").append(it.next().toString()).append("\n");
		}
		return ret.toString();
	}
	
	private class Iterator implements java.util.Iterator{
		private java.util.Iterator clientsit = Object3DCombined.this.clients.iterator();
		private java.util.Iterator pointer  = null;
		private Iterator(){
			if(clientsit.hasNext()){
				pointer = ((Object3D)clientsit.next()).iterator();
			}
		}
		public boolean hasNext(){
			if(pointer.hasNext()) return true;
			if(clientsit.hasNext()) return true;
			return false;			
		}
		
		public Object next(){
			if(pointer.hasNext())return pointer.next();
			if(clientsit.hasNext()){
				pointer = ((Object3D)clientsit.next()).iterator();
			}
			if(pointer.hasNext())return pointer.next();
			throw new java.util.NoSuchElementException("Read a tutorial about usage of java.util.Iterator!");
		}		
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
}
