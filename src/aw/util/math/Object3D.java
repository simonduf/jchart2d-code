
package aw.util.math;

public interface Object3D extends Cloneable{
	/**
	 * Determines wether the whole given Space3D is containe within the bounds 
	 * of this Space3D. <br>
	 * @returns true if subspace is a partial set of this Space3D. Else false is returned. 
	 * 
	 **/
	public boolean isContained(Object3D subspace);
	/**
	 * Determines wether the whole given Space3D is contained within the bounds 
	 * of this Space3D <br>. 
	 * @returns true if subspace is a partial set of this Space3D. Else false is returned. 
	 **/
	public boolean isIntersect(Object3D cutspace);
	/**
	 * Determines wether the given Space3D owns the same bounds as this Space3D.<br>
	 * This is the same as <code>public boolean equals(Object 0);</code>
	 * @returns true if  the union of subspace and this Space3D is contained in this Space3D. Else false is returned. 
	 **/
	public boolean isUnion(Object3D space);
	/**
	 * The same as isIntersect(Space3D space).
	 * @see #isIntersect(Space3D)
	 **/
	public boolean equals(Object o);	
	/**
	 * Returns the union of both spaces. 
	 **/
	public Object3D union(Object3D space);
	/**
	 * Returns the intersection of both spaces. 
	 **/
	public Object3D intersection(Object3D space);
	/**
	 * Returns an Object3D (a Point3D) that describes the center of this Object3D. 
	 * If this Object is a Point3D a clone of itself will be returned. If this Object is a 
	 * line, the middle of the line will be returned.  If it is a Tetrahedron, the center 
	 * of will be found by finding the intersection point of  the bisectors of the edges 
	 * in both areas: x-y and  x-z. 
	 **/ 
	public Object3D getCenter();
	/**
	 * @return an Iterator over the Point3D- instances who are the edges of this Object3D. 
	 * This method should only be invoked by a Space3D - instance. 
	 **/ 
	public java.util.Iterator iterator();
	
	public Object clone() throws CloneNotSupportedException;
	
	
	Object3D Object3DVoid = new Object3D(){
		CloneNotSupportedException notclone = new CloneNotSupportedException("Makes no sense to clone an empty space!");
		public boolean isContained(Object3D subspace){
			return false;
		}
		public boolean isIntersect(Object3D cutspace){
			return false;
		}
		public boolean isUnion(Object3D space){
			return space==this;
		}
		public boolean equals(Object o){
			return o==this;
		}
		public Object3D union(Object3D space){
			Object3D ret;
			try{
				ret = (Object3D)space.clone();
			}catch(CloneNotSupportedException e){
				return space;
			}
			return ret;
		}
		public Object3D intersection(Object3D space){
			return this;
		}
	
		public Object3D getCenter(){
			return this;
		}
		public java.util.Iterator iterator(){
			return new java.util.Iterator(){
				public boolean hasNext(){
					return false;
				}
				public Object next(){
					throw new java.util.NoSuchElementException("this is the Void-Space with no dimension!");
				}
				public void remove(){
					throw new UnsupportedOperationException("Nothing to remove in the Void- Space!");
				}
			};
		}
		public Object clone()throws CloneNotSupportedException{
			throw notclone;
		}
		public String toString(){
			return "Void Object3D";
		}
	};
}
