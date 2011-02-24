
package aw.util.math;

public class Point3D implements Object3D {
	protected double x;
	protected double y;
	protected double z;
	
	public Point3D(double x,double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}


	/**
	 * @see Space3D#intersection(Space3D)
	 */
	public Object3D intersection(Object3D space) {
		if(space instanceof Point3D){
			if(space.equals(this)){
				return (Object3D)this.clone();
			}
			else return Object3DVoid;
		}
		if(space.isContained(this))return (Object3D)this.clone();
		return Object3DVoid;
	}

	/**
	 * @see Space3D#union(Space3D)
	 */
	public Object3D union(Object3D space) {
		if(space instanceof Point3D){
			if(space.equals(this)){
				return (Object3D)this.clone();
			}
		}
		if(space.isContained(this)){
			try{
				return (Object3D)space.clone();
			}catch(CloneNotSupportedException e){
				return space;
			}
		}
		Object3DCombined ret = new Object3DCombined((Object3D)this.clone());
		ret.union(space);
		return ret;
	}

	/**
	 * @see Space3D#isContained(Space3D)
	 */
	public boolean isContained(Object3D subspace) {
		return subspace.equals(this);
	}

	/**
	 * @see Space3D#isIntersect(Space3D)
	 */
	public boolean isIntersect(Object3D cutspace) {
		if(cutspace instanceof Point3D){
			return cutspace.equals(this);
		}
		if(cutspace.isContained(this)){
			return true;
		}
		return false;
	}

	/**
	 * @see Space3D#isUnion(Space3D)
	 */
	public boolean isUnion(Object3D space) {
		if(space instanceof Point3D){
			return space.equals(this);
		}
		return false;
	}
	
	public Object3D getCenter(){
		return (Object3D)this.clone();
	}
	public java.util.Iterator iterator(){
		return new java.util.Iterator(){
			private boolean unfinished = true;
			public boolean hasNext(){
				return unfinished;
			}
			public Object next(){
				if(unfinished){
					unfinished = false;
					return Point3D.this;
				}
				else throw new java.util.NoSuchElementException("If hasNext returns false, you should not try to continue!");
			}
			public void remove(){
				throw new UnsupportedOperationException("This is a single Point3D. Nothing left to remove!");
			}
		};
	}
	
	public Object clone(){
		return new Point3D(this.x,this.y,this.z);
	}
	
	public String toString(){
		return new StringBuffer("(").append(this.x).append(", ").append(this.y).append(", ").append(this.z).append(")").toString();
	}

}
