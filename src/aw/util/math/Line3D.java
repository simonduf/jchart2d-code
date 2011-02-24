
package aw.util.math;

import java.util.Iterator;

public class Line3D implements Object3D {
	double gradeY;
	double gradeZ;
	double offsetY;
	double offsetZ;
	
	Point3D start;
	Point3D end;
	
	public Line3D(Point3D start,Point3D end){
		if(start.equals(end))throw new IllegalArgumentException("Given arguments are a Point not a line!");
		// finding gradeY and gradeZ. 		
		if(start.x < end.x){
			this.start = start;
			this.end = end;
		}
		else{
			this.end = start;
			this.start = end;
		}
		this.gradeY = (this.end.y-this.start.y)/(this.end.x-this.start.x);
		this.gradeZ = (this.end.z-this.start.z)/(this.end.x-this.start.x);
		this.offsetY = this.start.y - this.gradeY * this.start.x;
		this.offsetZ = this.start.z - this.gradeZ * this.start.x;
	}
	/**
	 * @see Object3D#isContained(Object3D)
	 */
	public boolean isContained(Object3D subspace) {
		if(!(subspace instanceof Point3D))return false;
		Point3D test = (Point3D)subspace;
		if(test.y != test.x*this.gradeY+this.offsetY)return false;
		if(test.z != test.x*this.gradeZ+this.offsetZ)return false;
		if(test.x > this.end.x) return false;		// The Point3D argument is out of range.
		return true;	// The Point3D argument is on the line!		
	}

	/**
	 * @see Object3D#isIntersect(Object3D)
	 */
	public boolean isIntersect(Object3D cutspace) {
		Object3D test = intersection(cutspace);
		if(test == Object3DVoid) return false;
		return true;
	}

	/**
	 * @see Object3D#isUnion(Object3D)
	 */
	public boolean isUnion(Object3D space) {
		return equals(space);
	}

	/**
	 * @see Object3D#union(Object3D)
	 */
	public Object3D union(Object3D space) {
		if(this.isContained(space)) return this;
		if(space.isContained(this)) return space;
		if(space instanceof Line3D){
			/* 
			 * Try to avoid construction of an Object3DCombined. 
			 * If grade and offset in xz and xy area are matching and both lines 
			 * have a common x-range just create a longer Line3D. 
			 */
			Line3D other = (Line3D) space;
			if(
				(other.gradeY == this.gradeY)
				&&(other.gradeZ == this.gradeZ)
				&&(other.offsetY == this.offsetY)
				&&(other.offsetZ == this.offsetZ)	
			){
				if(
					(this.start.x<=other.start.x)
					&&(this.end.x>=other.start.x)
				){
					return new Line3D(this.start,other.end);					
				}
				if(
					(this.start.x<=other.end.x)
					&&(this.end.x>= other.end.x)
				){
					return new Line3D(other.start,this.end);		
				}
			}
		}
		return new Object3DCombined(this).union(space);	
	}

	/**
	 * @see Object3D#intersection(Object3D)
	 */
	public Object3D intersection(Object3D space) {
		double cutxyX=0.0,cutxzX=0.0;
		if(space instanceof Point3D){
			Point3D other = (Point3D)space;
			if((this.gradeY*other.x+this.offsetY == other.y)&&(this.gradeZ*other.x+this.offsetZ == other.z))
				return (Object3D)other.clone();
			else return Object3DVoid;
		}
		if(space instanceof Line3D){
			Line3D test = (Line3D)space;
			// Exclusion of parallel lines which never will intersect each others.
			if(test.gradeY == this.gradeY){
				if(this.offsetY!=test.offsetY) return Object3DVoid;	// parallel lines in x-y area (they will never touch in this dimension)
			}
			if(test.gradeZ == this.gradeZ){
				if(this.offsetZ!=test.offsetZ) return Object3DVoid;	// parallel lines in x-z area (they will never touch in this dimension)
			}
			// mathematical search for the intersection point: 
			// has to cut in x-y dimension: 
			cutxyX = (this.offsetY-test.offsetY)/(test.gradeY-this.gradeY) ; 
			cutxzX = (this.offsetZ-test.offsetZ)/(test.gradeZ-this.gradeZ);
			if(cutxyX == 0.0/0.0){
				// Parallel lines are caught above.
				// In xy-dimension the lines are matching. (see calculation of cutxyX)
				// Now there are two possibilities: 
				// 	- The lines are also matching in dimension xz (cutxzX is NaN too): If the x-ranges of both lines are overlapping "intersection" is true. 
				//	- The lines have to cross: Check wether cutxzX  is in x-range of both lines. 
				if(cutxzX == 0.0/0.0){
					if(!(
						(this.end.x<test.start.x)		// start.x is smaller end.x! (constructor)
						||(this.start.x>test.end.x)											
					)){
						Point3D startx = (this.start.x>test.start.x)?this.start:test.start;
						Point3D endx = (this.end.x<test.end.x)?this.end:test.end;
						return new Line3D(startx,endx);
					}
					else return Object3DVoid;
				}
				else{
					// on zx-area it's not matching, so we have to check wether the intersection of both 
					// lines of the zx-area is within the x-bounds of both lines.
					if(
						(cutxzX>=this.start.x)&&(cutxzX<=this.end.x)
						&&(cutxzX>=test.start.x)&&(cutxzX<=test.end.x)
						)
						return new Point3D(cutxzX,this.gradeY*cutxzX+this.offsetY,this.gradeZ*cutxzX+this.offsetZ);
					else return Object3DVoid;
				}
			}
			// The lines have an intersection in xy-dimension
			else{
				// this is the other way round:
				// either in xz - dimension the lines are on the same way: 
				if(cutxzX == 0.0/0.0){
					// in zx - area the lines are matching in grade and offset. 
					// Then we will only have to check, wether the crossing of the lines in 
					// xy-dimension is within the x-bounds of both lines.
					if(
						(cutxyX>=this.start.x)&&(cutxyX<=this.end.x)
						&&(cutxyX>=test.start.x)&&(cutxyX<=test.end.x)
						)
						return new Point3D(cutxyX,cutxyX*this.gradeY+this.offsetY,cutxyX*this.gradeZ+this.offsetZ);
					else return Object3DVoid;
				}
				// The lines are crossing in xy and xz dimension: 
				// only if both x values cutxyX and cutxzX are equal and within the 
				// x-ranges of both lines this is an intersection:
				else{
					if(
						(cutxyX == cutxzX)
						&& ((cutxyX >=this.start.x)&&(cutxyX<=this.end.x))
						&& ((cutxyX >=test.start.x)&&(cutxyX<=test.end.x))
					){
						return new Point3D(cutxyX,cutxyX*this.gradeY+this.offsetY,cutxyX*this.gradeZ+this.offsetZ);
					}
					else return Object3DVoid;
				}
			}
		}
		return space.intersection(this);
	}



	/**
	 * Returns the center of the line (which is the Point3D in the middle of the line).
	 * @see Object3D#getCenter()
	 */
	public Object3D getCenter() {
		return new Point3D((this.start.x+this.end.x)/2.0,(this.start.y+this.end.y)*2.0,(this.start.z+this.end.z)/2.0);
	}

	/**
	 * @see Object3D#iterator()
	 */
	public Iterator iterator() {
		return new Iterator(){
			private int cnt = 0;
			public boolean hasNext(){
				if(cnt<2)return true;
				else return false;
			}
			public Object next(){
				switch(cnt){
					case 0:	cnt++;return Line3D.this.start;
					case 1: cnt++;return Line3D.this.end;
					default: throw new java.util.NoSuchElementException("Read a tutorial about usage of java.util.Iterator!");
				}
			}
			public void remove(){
				throw new UnsupportedOperationException("This would force  to morph the type of this Object3D.");
			}	
		
		};
	}
	
	public Object clone(){
		return null;
	}
	public boolean equals(Object o){
		if(!(o instanceof Line3D))return false;
		Line3D test = (Line3D)o;
		return (test.start.equals(this.start))&&(test.end.equals(this.end));	
	}
	public String toString(){
		return new StringBuffer("Line3D from ").append(this.start.toString()).append(" to ").append(this.end.toString()).toString();	
	}
	
	public static void main(String[]args){
		Point3D p1 = new Point3D(0.0,0.0,0.0);
		Point3D p2 = new Point3D(4.0,4.0,4.0);	
		Line3D l1 = new Line3D(p1,p2);
		
		Point3D p3 = new Point3D(0.0,4.0,4.0);
		Point3D p4 = new Point3D(4.0,0.0,0.0);
		Line3D l2 = new Line3D(p3,p4);
		
		System.out.println("p1: "+p1);
		System.out.println("p2: "+p2);
		System.out.println("l1: "+l1);
		System.out.println("l1.intersection(p2): "+l1.intersection(p2));
		System.out.println("p2.intersection(l1): "+p2.intersection(l1));
		System.out.println("l2: "+l2);
		System.out.println("l2.intersection(l1): "+l2.intersection(l1));
		System.out.println("l1.intersection(l2): "+l1.intersection(l2));
		
		System.out.println("Object3D c1 = l1.union(l2)");
		Object3D c1 = l1.union(l2);
		System.out.println("c1: \n"+c1.toString());
		System.out.println("p1.union(p4): \n"+p1.union(p4));
		System.out.println("c1.intersection(p1.union(p4))");
		System.out.println(c1.intersection(p1.union(p4)));
		
		System.out.println("c1.union(p3):\n"+c1.union(p3));
		
	}
}
