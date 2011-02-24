/**
 * RingBufferArrayFast, an array- based fast implementation of a RingBuffer.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know. 
 *  Achim.Westermann@gmx.de
 */
package aw.util.collections;

import java.util.*;
import aw.util.StringUtil;
/**
 *
 *  This implementation differs from the <code>RingBufferArray</code> in one
 *  point:<br>
 *  If <code>setBufferSize(int asize)</code> decreases the
 *  size of the buffer and it will get smaller than the actual amount of elements
 *  stored, they will get lost. This avoids the need for an internal List to
 *  store elements overhanging. Some tests may be left out that may speed up
 *  this <code>IRingBuffer</code>. Adding 5000000 elements was about 25 % faster
 *  compared to the <code>RingBufferArray</code> on an Athlon 1200, 256 MB RAM.
 *  <p><b>
 *  For allowing high performance single-threaded use this implementation and
 *  the implementations of the retrieveable <code>Iterator</code>- instances are
 *  not synchronized at all. <font size=+1>For heavens sake take care for synchronizing
 *  the access from outside if multiple Threads are using the same instace!!!</Font>
 *  </b></p>
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 */
public class RingBufferArrayFast implements Cloneable, IRingBuffer {
    /**
     *  Flip the switch and you will see how the compiler changes the
     *  size of the classfile.
     **/
    public static final boolean debug = false;
    protected Object[] buffer;
    /**
     *  For performance reasons the size of the buffer -1!
     **/
    protected int size;
    /*
     *  The internal index to buffer where the next element is going to be placed
     *  (not placed yet!).
     **/
    protected int headpointer = 0;
    /**
     *  The internal index to buffer where the next element is going to be read.
     **/
    protected int tailpointer = 0; // is increased before used as index!!
    /**
     *  <pre>
     *       headpointer
     *        |
     *  +---+---+---+---+
     *  | 0 | 1 | 2 | 3 |
     *  +---+---+---+---+
     *        |
     *       tailpointer
     *
     *  From where to where are the elements?
     *  Where is empty space?
     *  empty == true:  0 elements are contained: buffer empty
     *  empty == false:  4 elements are contained: buffer full
     *  remember:
     *      -the headpointer points to the space where the next element will be inserted.
     *      -the tailpointer points to the space to read the next element from.
     *  </pre>
     **/
    protected boolean empty = true;
    /**
     *	Constructs a RingBuffer with the given size.
     */
    public RingBufferArrayFast(int aSize) {
        this.buffer = new Object[aSize];
        this.size = aSize-1;
    }
    /**
     * Adds an element to the ring buffer, potentially removing the first element to make
     * more room.<P>
     *
     * @param anObject java.lang.Object
     * @return the oldes Object, if RingBuffer was filled with 'maxsize' elements before, or null;
     */
    public Object add(Object anObject) {
        Object ret = null;
        if(this.isFull()) {
            ret = this.buffer[tailpointer];
            this.incTail();
        }
        if(debug)System.out.println("add: tailpointer: "+tailpointer+" headpointer: "+headpointer);
        this.buffer[headpointer] = anObject;
        this.incHead();
        return ret;
    }
    
    /**
     * Removes the element which has been in the Buffer for the longes time.<P>
     *
     */
    public Object remove() {
        if(this.isEmpty())throw IRingBuffer.BUFFER_EMPTY;
        Object ret = null;
        ret = this.buffer[tailpointer];
        this.incTail();
        if(this.debug)System.out.println("Removing element: "+ret+" head: "+this.headpointer+" tail: "+this.tailpointer);
        return ret;
    }
    
    
    /**
     *  Clears the buffer. It will return all of it's stored elements.
     * The returned Object[] will be orderered by descending age (time they were in the buffer).
     *
     **/
    public Object[] removeAll(){
        Object[] ret = new Object[this.size()];
        for(int i=0;i<ret.length;i++){
            ret[i] = this.remove();
        }
        return ret;
    }
    
        public Object getYoungest() throws RingBufferException {
        if(isEmpty())throw BUFFER_EMPTY;
        int tmp = this.headpointer;
        if(tmp==0)tmp=this.size;
        else tmp--;
        return this.buffer[tmp];
        
    }
    
    /**
     * Returns the oldest element from the buffer.
     * This method does not remove the element.
     */
    public Object getOldest() throws RingBufferException {
        if(isEmpty())throw BUFFER_EMPTY;
        return this.buffer[this.tailpointer];
    }

    
    
    
    /**
     * Returns the absolute amount of space in the buffer.
     */
    public int getBufferSize() {
        return this.size+1;
    }
    /**
     * Sets a new buffer- size. <br>
     * <p>
     * A new size is assigned but the elements "overhanging" are returned
     * by the <code>Object remove()</code> - method first. This may take time
     * until the buffer has its actual size again.
     *  Don't pretend on calling this method for saving of memory very often
     *  as the whole buffer has to be copied into a new array every time- and
     *  if newSize< getSize() additional the overhanging elements references
     *  have to be moved to the internal <code>List pendingremove</code>.
     *
     */
    public void setBufferSize(int newSize) {
        Object[] newbuffer = new Object[newSize];
        int i=0;
        if(debug)System.out.println("setBufferSize("+newSize+"): isEmpty(): "+this.isEmpty()+" tail: "+this.tailpointer+" head: "+this.headpointer);
        for(;i<newSize;i++){
            if(this.isEmpty())break;
            newbuffer[i] = remove();
        }
        this.tailpointer = 0;
        if(newSize-1 <= i)
            this.headpointer = 0;
        else
            this.headpointer = i;
        this.buffer = newbuffer;
        this.size = newSize-1;
    }
    /**
     * Returns the actual amount of elements stored in the buffer.
     */
    public int size() {
        if(empty) return 0;
        else if(this.headpointer==this.tailpointer)
            return this.size+1;
        else if(this.headpointer>this.tailpointer){
            return this.headpointer-this.tailpointer;
        }
        else{
            return this.headpointer+this.size+1-this.tailpointer;
        }
    }
    
    public boolean isEmpty(){
        if(debug)System.out.println("isEmpty: "+this.empty+" head: "+headpointer+" tail: "+tailpointer);
        return this.empty;
    }
    
    /**
     * Tests wether <code>getSize()==getBufferSize()</code>.
     */
    public boolean isFull() {
        boolean ret = (this.headpointer==this.tailpointer)&& !this.empty;
        if(debug)System.out.println("isFull: "+ret+" head: "+headpointer+" tail: "+tailpointer);
        return ret;
    }
    
    /**
     * Returns a string representation of the RingBuffer and it's contents.
     *  Don't call this in your application too often: hard arraycopy - operation
     *  an memalloc are triggered.
     *
     *
     * @return java.langString
     * @author Achim Westermann
     */
    public String toString() {
        if(this.isEmpty()){
            if(debug)System.out.println("toString(): isEmpty: true");
            return "[]";
        }
        Object[] actualcontent = new Object[this.size()];
        int tmp = this.tailpointer;
        int i=0;
        for(;i<actualcontent.length;i++){
            actualcontent[i]=this.buffer[tmp];
            if(tmp == this.size) tmp =0;
            else tmp++;
            if(tmp == this.headpointer && this.empty)break;
        }
        return StringUtil.ArrayToString(actualcontent);
    }
    
    private void incHead(){
        if(this.headpointer==this.size){
            this.headpointer = 0;
        }
        else this.headpointer++;
        this.empty = false;
    }
    
    private void incTail(){
        if(this.tailpointer==this.size){
            this.tailpointer = 0;
        }
        else this.tailpointer++;
        if(this.tailpointer == this.headpointer)
            this.empty = true;
    }
    
    protected abstract class RingBufferIterator implements Iterator{
        int pos,startpos;
        int size = RingBufferArrayFast.this.size()-1;
        boolean empty;
        RingBufferIterator(boolean empty){
            this.empty = empty;
        }
        public boolean hasNext(){
            return (!empty);
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        
    }
    /**
     *  Returns an <code>Iterator</code> that will return the elements in exactly
     *  the order the subsequent call to <code>remove()</code> would do.
     *  The oldest elements are returned first.
     *  <b>
     *  The <code>Iterator</code> returned is not thread- safe!
     *  </b>
     **/
    public java.util.Iterator iteratorL2F() {
        return new RingBufferIterator(this.empty){
            {
                this.pos = tailpointer;
                this.startpos = pos;
            }
            public Object next(){
                Object ret = null;
                if(!hasNext())throw new NoSuchElementException();
                ret = buffer[pos];
                if(pos==this.size) pos = 0;
                else pos++;
                //updating empty
                if(pos==startpos)this.empty = true;
                if(ret==null)System.out.println("RingBufferArrayFast.iteratorL2F returns null: pos:"+pos+" startpos: "+startpos);

                return ret;
            }
        };
    }
    /**
     *  Returns an <code>Iterator</code> that will return the elements in exactly
     *  the inverse order the subsequent call to <code>remove()</code> would do.
     *  The youngest elements are returned first.
     *  <b>
     *  The <code>Iterator</code> returned is not thread- safe!
     *  </b>
     **/
    
    public java.util.Iterator iteratorF2L() {
        return new RingBufferIterator(this.empty){
            {//anonymous constructor: the method with unknown name...
                this.pos = (headpointer==0)?this.size:headpointer-1;
                this.startpos = pos;
            }
            public Object next(){
                Object ret = null;
                // Pending elements are the oldest
                if(!hasNext()) throw new NoSuchElementException();
                ret = buffer[pos];
                if(pos==0) pos = this.size;
                else pos--;
                if(pos==startpos)this.empty=true;
                if(ret==null)System.out.println("RingBufferArrayFast.iteratorF2L returns null: pos:"+pos+" startpos: "+startpos);
                return ret;
            }
        };
    }
    
}
