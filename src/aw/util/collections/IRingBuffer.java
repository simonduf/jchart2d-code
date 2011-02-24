/**
 * IRingBuffer, an interface for implementations of a RingBuffer.
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

/**
 *  Interface for implementations of RingBuffers.
 * @author  Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de</A>
 * @version 1.1
 */
public interface IRingBuffer extends java.io.Serializable{
    /**
     *  Static marker- exception to show that the RingBuffer is full.
     *  This way of exception- throwing avoids creation of new Exception- instances
     *  at runtime. Your code can test the type of Exception like follows:<br>
     *  <pre>
     *  ...
     *  try{
     *      ... // do sth. with an IRingBuffer.
     *  }catch(RingBufferException e){
     *      if(e == IRingBuffer.BUFFER_FULL){
     *          ... // react by increasing maxsize, storing elsewhere,...
     *      }
     *  }
     *  </pre>
     *  Note that <code>RingBufferException</code> does not need to be caught
     *  because it is an inheritant of <code>java.lang.RuntimeException</code>.
     *  Especially for the <code>Object add(Object element)</code>- method there
     *  may be an implementation that never throws <code>BUFFER_FULL</code> but
     *  returns the oldest element in case the buffer is full.
     **/
    IRingBuffer.RingBufferException BUFFER_FULL = new IRingBuffer.RingBufferException("Buffer is full.");
    /**
     *  Static marker- exception to show that the RingBuffer is empty.
     *
     *  @see #BUFFER_FULL
     **/
    IRingBuffer.RingBufferException BUFFER_EMPTY = new IRingBuffer.RingBufferException("Buffer is empty. ");
    
    /**
     *  Adds element to the RingBuffer. If the buffer is full, <code>BUFFER_FULL </code> will
     *  be thrown.
     *  <p>
     *  Note that <code>RingBufferException</code> does not need to be caught
     *  because it is an inheritant of <code>java.lang.RuntimeException</code>.
     *  Especially for the <code>Object add(Object element)</code>- method there
     *  may be an implementation that never throws <code>BUFFER_FULL</code> but
     *  returns the oldest element in case the buffer is full.
     *  </p>
     **/
    Object add(Object element)throws IRingBuffer.RingBufferException;
    /**
     *  Removes the oldest element from the buffer.
     *  If the buffer is empty, <code>BUFFER_EMPTY</code> will
     *  be thrown.
     **/
    Object remove()throws IRingBuffer.RingBufferException;
    /**
     *  Returns the last element added.
     *  This method does not remove the element.
     **/
    Object getYoungest()throws IRingBuffer.RingBufferException;
    
    /**
     *  Returns the oldest element from the buffer. 
     *  This method does not remove the element.
     **/
    Object getOldest()throws IRingBuffer.RingBufferException;
    
    /**
     *  Clears the buffer. It will return all of it's stored elements.
     **/
    Object[] removeAll();
    /**
     *  Returns the actual amount of elements stored in the buffer.
     **/
    int size();
    /**
     *  Returns the absolute amount of space in the buffer.
     **/
    int getBufferSize();
    /**
     *  Tests wether no elements are stored in the buffer.
     **/
    boolean isEmpty();
    /**
     *  Tests wether <code>getSize()==getBufferSize()</code>.
     **/
    boolean isFull();
    /**
     *  Sets a new buffer- size. <br>
     *  Implementations may vary on handling the problem that the new size is
     *  smaller than the actual amount of elements in the buffer:<br>
     *  <p>
     *  The oldest elements may be thrown away.
     *  </p>
     *  <p>
     *  A new size is assigned but the elements "overhanging" are returned
     *  by the <code>Object remove()</code> - method first. This may take time
     *  until the buffer has its actual size again.
     **/
    void setBufferSize(int newSize);
    /**
     *  Returns an iterator starting from the first (youngest) to the 
     *  last (oldest) element.
     *
     **/
    java.util.Iterator iteratorF2L();
    /**
     *  Returns an iterator starting from the last (oldest) to the 
     *  first (youngest) element.
     **/
    java.util.Iterator iteratorL2F();
    
    class RingBufferException extends RuntimeException{
        RingBufferException(){
            super();
        }
        RingBufferException(String msg){
            super(msg);
        }
    }
    
}
