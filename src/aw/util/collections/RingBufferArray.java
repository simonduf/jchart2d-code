/**
 * RingBufferArray, an array- based implementation of a RingBuffer, which never  
 *  drops stored elements in case of decreasing the buffer size.
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
 * A RingBuffer can be used to store a limited number of entries of any type within a
 * buffer.<BR>
 * As soon as the maximum number of entries is reached, the next entry is added to
 * the end and the first
 * entry is removed from it.
 * In this case, all elements are stored in a Object[]. But don't worry there
 *  will not be a single call to <code>System.arraycopy</code> caused by invocation
 *  of the <code>add(Object element)</code> - method. Internal indexes into the
 *  array for the head and tail allow to reuse the same memory again and again.
 * <br>
 *  No element is lost: If <code>setBufferSize(int asize)</code> decreases the
 *  size of the buffer and it will get smaller than the actual amount of elements
 *  stored, they will get cached until removed.
 *  <p><b>
 *  For allowing high performance single-threaded use this implementation and
 *  the implementations of the retrieveable <code>Iterator</code>- instances are
 *  not synchronized at all. <font size=+1>For heavens sake take care for synchronizing
 *  the access from outside if multiple Threads are using the same instace!!!</Font>
 *  </b></p>
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a> 
 *  @version 1.1
 */
public class RingBufferArray extends RingBufferArrayFast{
    protected List pendingremove = new LinkedList();
    /**
     *	Constructs a RingBuffer with the given size.
     */
    public RingBufferArray(int aSize) {
        super(aSize);
    }
    
    /**
     * Removes the element which has been in the Buffer for the longes time.<P>
     *
     */
    public Object remove() {
        if(this.pendingremove.size()>0){
            if(this.debug)System.out.println("Removing pending element!!!");
            return this.pendingremove.remove(0);
        }
        return super.remove();
    }
    
    
    /**
     *  Clears the buffer. It will return all of it's stored elements.
     * The returned Object[] will be orderered by descending age (time they were in the buffer).
     *
     **/
    public Object[] removeAll(){
        Object[] ret = new Object[this.size()+this.pendingremove.size()];
        int stop = this.pendingremove.size();
        int i;
        for(i=0;i<stop;i++)
            ret[i]= this.pendingremove.remove(0);
        for(;i<ret.length;i++){
            ret[i] = this.remove();
        }
        return ret;
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
        List newpending = null;
        if(this.size()>newSize){
            newpending = new LinkedList();
            int stop = this.size();
            for(int i=newSize;i<stop;i++){
                Object add = this.remove();
                newpending.add(add);
            }
        }
        Object[] newbuffer = new Object[newSize];
        int i=0;
        if(debug)System.out.println("setBufferSize("+newSize+"): isEmpty(): "+this.isEmpty()+" tail: "+this.tailpointer+" head: "+this.headpointer);
        while(!isEmpty()){
            newbuffer[i] = remove();
            i++;
            //if(debug)System.out.println(this.toString());
        }
        this.tailpointer = 0;
        if(newSize == i)
            this.headpointer = 0;
        else
            this.headpointer = i;
        this.buffer = newbuffer;
        this.size = newSize-1;
        if(newpending!=null)this.pendingremove = newpending;
    }
    /**
     * Returns the actual amount of elements stored in the buffer.
     */
    public int size() {
        return super.size()+this.pendingremove.size();
    }
    
    public boolean isEmpty(){
        return super.isEmpty() && (this.pendingremove.size()==0);
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
        int stop = this.pendingremove.size();
        Iterator it = this.pendingremove.iterator();
        int i=0;
        for(;i<stop;i++)
            actualcontent[i]=it.next();
        for(;i<actualcontent.length;i++){
            actualcontent[i]=this.buffer[tmp];
            if(tmp == this.size) tmp =0;
            else tmp++;
            if(tmp == this.headpointer && this.empty)break;
        }
        return StringUtil.ArrayToString(actualcontent);
    }
    
    
    private abstract class RingBufferIterator extends RingBufferArrayFast.RingBufferIterator{
        int pendpos;
        RingBufferIterator(boolean empty){
            super(empty);
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
            int pendmax = RingBufferArray.this.pendingremove.size(); {
                this.pos = tailpointer;
                this.startpos = pos;
                this.pendpos = 0;
            }
            public Object next(){
                Object ret = null;
                if(pendpos<pendmax){
                    ret = RingBufferArray.this.pendingremove.get(pendpos);
                    pendpos++;
                    return ret;
                }
                if(!hasNext())throw new NoSuchElementException();
                ret = buffer[pos];
                if(pos==size) pos = 0;
                else pos++;
                //updating empty
                if(pos==startpos)this.empty = true;
                if(ret==null)System.out.println("RingBufferArray.iteratorL2F returns null: head:"+RingBufferArray.this.headpointer+" tail: "+RingBufferArray.this.tailpointer);
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
            boolean bufini = false; {//anonymous constructor: the method with unknown name...
                this.pos = (headpointer==0)?size:headpointer-1;
                this.startpos = pos;
                this.pendpos = RingBufferArray.this.pendingremove.size()-1;
            }
            public Object next(){
                Object ret = null;
                // Pending elements are the oldest
                if(bufini){
                    ret = RingBufferArray.this.pendingremove.get(pendpos);
                    pendpos--;
                    if(pendpos<0)this.empty = true;
                    if(ret==null)System.out.println("RingBufferArray.iteratorF2L returns null: head:"+RingBufferArray.this.headpointer+" tail: "+RingBufferArray.this.tailpointer);
                    return ret;
                }
                if(!hasNext()) throw new NoSuchElementException();
                ret = buffer[pos];
                if(pos==0) pos = size;
                else pos--;
                if(pos==startpos)this.bufini=true;
                return ret;
            }
        };
    }
    
}
