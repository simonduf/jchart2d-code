/*
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import aw.util.StringUtil;

/**
 * Fast ringbuffer implementation.
 * <p>
 *
 * This implementation differs from the <code>RingBufferArray</code> in one
 * point: <br>
 * If <code>setBufferSize(int asize)</code> decreases the size of the buffer
 * and it will get smaller than the actual amount of elements stored, they will
 * get lost. This avoids the need for an internal List to store elements
 * overhanging. Some tests may be left out that may speed up this
 * <code>IRingBuffer</code>. Adding 5000000 elements was about 25 % faster
 * compared to the <code>RingBufferArray</code> on an Athlon 1200, 256 MB RAM.
 * <p>
 *
 * For allowing high performance single-threaded use this implementation and the
 * implementations of the retrieveable <code>Iterator</code>- instances are
 * not synchronized at all. <font size=+1>For heavens sake take care for
 * synchronizing the access from outside if multiple Threads are using the same
 * instace!!! </Font>
 * <p>
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 */
public class RingBufferArrayFast implements Cloneable, IRingBuffer {
  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3834590997991404595L;

  /**
   * Flip the switch and you will see how the compiler changes the size of the
   * classfile.
   */
  public static final boolean DEBUG = false;

  /** The internal array used as buffer. */
  protected Object[] m_buffer;

  /**
   * The internal size of the buffer.
   * <p>
   *
   * For performance reasons the size of the buffer -1!
   * <p>
   */
  protected int m_size;

  /**
   * The internal index to buffer where the next element is going to be placed
   * (not placed yet!).
   */
  protected int m_headpointer = 0;

  /**
   * The internal index to buffer where the next element is going to be read.
   */
  protected int m_tailpointer = 0; // is increased before used as index!!

  /**
   * Flag that marks wether this buffer is empty or not.
   * <p>
   *
   * <pre>
   *
   *         headpointer
   *          |
   *    +---+---+---+---+
   *    | 0 | 1 | 2 | 3 |
   *    +---+---+---+---+
   *          |
   *         tailpointer
   *
   *    From where to where are the elements?
   *    Where is empty space?
   *    empty == true:  0 elements are contained: buffer empty
   *    empty == false:  4 elements are contained: buffer full
   *    remember:
   *        -the headpointer points to the space where the next element will be inserted.
   *        -the tailpointer points to the space to read the next element from.
   *
   *
   * </pre>
   *
   * <p>
   */
  protected boolean m_empty = true;

  /**
   * Constructs a RingBuffer with the given size.
   *
   * @param aSize
   *          the size of the buffer.
   */
  public RingBufferArrayFast(final int aSize) {
    this.m_buffer = new Object[aSize];
    this.m_size = aSize - 1;
  }

  /**
   * Adds an element to the ring buffer, potentially removing the first element
   * to make more room.
   * <P>
   *
   * @param anObject
   *          the instance to add.
   *
   * @return the oldes Object, if RingBuffer was filled with 'maxsize' elements
   *         before, or null.
   */
  public Object add(final Object anObject) {
    Object ret = null;
    if (this.isFull()) {
      ret = this.m_buffer[this.m_tailpointer];
      this.incTail();
    }
    if (RingBufferArray.DEBUG) {
      System.out.println("add: tailpointer: " + this.m_tailpointer + " headpointer: "
          + this.m_headpointer + " size: " + this.size());
    }
    this.m_buffer[this.m_headpointer] = anObject;
    this.incHead();
    return ret;
  }

  /**
   * @see aw.util.collections.IRingBuffer#remove()
   */
  public Object remove() {
    if (this.isEmpty()) {
      throw new IRingBuffer.RingBufferException("Buffer is empty.");
    }
    Object ret = null;
    ret = this.m_buffer[this.m_tailpointer];
    this.incTail();
    if (RingBufferArray.DEBUG) {
      System.out.println("Removing element: " + ret + " head: " + this.m_headpointer + " tail: "
          + this.m_tailpointer + " size: " + this.size());
    }
    return ret;
  }

  /**
   * @see aw.util.collections.IRingBuffer#removeAll()
   */
  public Object[] removeAll() {
    Object[] ret = new Object[this.size()];
    if (RingBufferArray.DEBUG) {
      System.out.println("removeAll()");
    }
    for (int i = 0; i < ret.length; i++) {
      ret[i] = this.remove();
    }
    return ret;
  }

  /**
   * Fast method to clear the buffer - only needs to set three primitive
   * members.
   * <p>
   *
   * @see aw.util.collections.IRingBuffer#clear()
   */
  public void clear() {
    this.m_headpointer = 0;
    this.m_tailpointer = 0;
    this.m_empty = true;
  }

  /**
   *
   * @see aw.util.collections.IRingBuffer#getYoungest()
   */
  public Object getYoungest() throws RingBufferException {
    if (isEmpty()) {
      throw new IRingBuffer.RingBufferException("Buffer is empty.");
    }
    int tmp = this.m_headpointer;
    if (tmp == 0) {
      tmp = this.m_size;
    } else {
      tmp--;
    }
    return this.m_buffer[tmp];
  }

  /**
   * @see aw.util.collections.IRingBuffer#getOldest()
   */
  public Object getOldest() throws RingBufferException {
    if (this.isEmpty()) {
      throw new IRingBuffer.RingBufferException("Buffer is empty.");
    }
    return this.m_buffer[this.m_tailpointer];
  }

  /**
   * @see aw.util.collections.IRingBuffer#getBufferSize()
   */
  public int getBufferSize() {
    return this.m_size + 1;
  }

  /**
   * Sets a new buffer- size. <br>
   * <p>
   * A new size is assigned but the elements "overhanging" are returned by the
   * <code>Object remove()</code>- method first. This may take time until the
   * buffer has its actual size again. Don't pretend on calling this method for
   * saving of memory very often as the whole buffer has to be copied into a new
   * array every time- and if newSize < getSize() additional the overhanging
   * elements references have to be moved to the internal
   * <code>List pendingremove</code>.
   *
   * @param newSize
   *          the new size of the buffer.
   */
  public void setBufferSize(final int newSize) {
    Object[] newbuffer = new Object[newSize];
    boolean emptyStore = this.m_empty;
    int i = 0, j = 0;
    if (RingBufferArray.DEBUG) {
      System.out.println("setBufferSize(" + newSize + "): isEmpty(): " + this.isEmpty() + " tail: "
          + this.m_tailpointer + " head: " + this.m_headpointer);
    }
    // skip the oldest ones that are discarded
    int oldSize = this.size();
    int stop = oldSize - newSize;
    for (; i < stop && !this.isEmpty(); i++) {
      this.remove();
    }
    // add the ones that are the youngest (if some remaining)
    for (j = 0; j < newSize && !this.isEmpty(); j++) {
      newbuffer[j] = this.remove();
    }
    this.m_tailpointer = 0;
    this.m_headpointer = j - 1;
    this.m_buffer = newbuffer;
    this.m_size = newSize - 1;
    this.m_empty = emptyStore || (newSize == 0);
  }

  /**
   * @see aw.util.collections.IRingBuffer#size()
   */
  public int size() {
    if (this.m_empty) {
      return 0;
    } else if (this.m_headpointer == this.m_tailpointer) {
      return this.m_size + 1;
    } else if (this.m_headpointer > this.m_tailpointer) {
      return this.m_headpointer - this.m_tailpointer;
    } else {
      return this.m_headpointer + this.m_size + 1 - this.m_tailpointer;
    }
  }

  /**
   * @see aw.util.collections.IRingBuffer#isEmpty()
   */
  public boolean isEmpty() {
    if (RingBufferArray.DEBUG) {
      System.out.println("isEmpty: " + this.m_empty + " head: " + this.m_headpointer + " tail: "
          + this.m_tailpointer);
    }
    return this.m_empty;
  }

  /**
   * @see aw.util.collections.IRingBuffer#isFull()
   */
  public boolean isFull() {
    boolean ret = (this.m_headpointer == this.m_tailpointer) && !this.m_empty;
    if (RingBufferArray.DEBUG) {
      System.out.println("isFull: " + ret + " head: " + this.m_headpointer + " tail: "
          + this.m_tailpointer);
    }
    return ret;
  }

  /**
   * Returns a string representation of the RingBuffer and it's contents.
   * <p>
   * Don't call this in your application too often: hard arraycopy - operation
   * an memalloc are triggered.
   * <p>
   *
   * @return a string representation of the RingBuffer and it's contents.
   */
  public String toString() {
    if (this.isEmpty()) {
      if (RingBufferArray.DEBUG) {
        System.out.println("toString(): isEmpty: true");
      }
      return "[]";
    }
    Object[] actualcontent = new Object[this.size()];
    int tmp = this.m_tailpointer;
    int i = 0;
    for (; i < actualcontent.length; i++) {
      actualcontent[i] = this.m_buffer[tmp];
      if (tmp == this.m_size) {
        tmp = 0;
      } else {
        tmp++;
      }
      if (tmp == this.m_headpointer && this.m_empty) {
        break;
      }
    }
    return StringUtil.arrayToString(actualcontent);
  }

  /**
   * Internally increases the array index pointer to the head of the buffer.
   * <p>
   *
   */
  private void incHead() {
    if (this.m_headpointer == this.m_size) {
      this.m_headpointer = 0;
    } else {
      this.m_headpointer++;
    }
    this.m_empty = false;
  }

  /**
   * Internally increases the array index pointer to the taill of the buffer.
   * <p>
   *
   */
  private void incTail() {
    if (this.m_tailpointer == this.m_size) {
      this.m_tailpointer = 0;
    } else {
      this.m_tailpointer++;
    }
    if (this.m_tailpointer == this.m_headpointer) {
      this.m_empty = true;
    }
  }

  /**
   *
   * Base for ringbuffer iterators that has access to the ringbuffer by being an
   * non-static inner class.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   * @version $Revision: 1.9 $
   */
  protected abstract class RingBufferIterator implements Iterator {
    /** The index of the next instance to return. */
    protected int m_pos;

    /** The index of the first instance to return. */
    protected int m_startpos;

    /** Flag if this iterator is empty. */
    protected boolean m_innerEmpty;

    /**
     * Defcon.
     * <p>
     *
     */
    RingBufferIterator() {
      this.m_innerEmpty = RingBufferArrayFast.this.m_empty;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      // Recheck the startpos!
      // An external removal could have made it unreachable.
      int sz = RingBufferArrayFast.this.size();
      if (!(this.m_startpos < sz)) {
        this.m_innerEmpty = true;
      }
      return !(this.m_innerEmpty || RingBufferArrayFast.this.isEmpty());
    }

    /**
     * Not supported.
     * <p>
     *
     * @throws UnsupportedOperationException
     *           always as this is not supported.
     *
     * @see java.util.Iterator#remove()
     */
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * Returns an <code>Iterator</code> that will return the elements in exactly
   * the order the subsequent call to <code>remove()</code> would do.
   * <p>
   * The oldest elements are returned first. <b>The <code>Iterator</code>
   * returned is not thread- safe! </b>
   * <p>
   *
   * @return an <code>Iterator</code> that will return the elements in exactly
   *         the order the subsequent call to <code>remove()</code> would do.
   */
  public java.util.Iterator iteratorL2F() {
    return new RingBufferIterator() {
      {
        this.m_pos = RingBufferArrayFast.this.m_tailpointer;
        this.m_startpos = m_pos;
      }

      public Object next() {
        Object ret = null;
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        }
        ret = RingBufferArrayFast.this.m_buffer[m_pos];
        if (this.m_pos == RingBufferArrayFast.this.m_size - 1) {
          this.m_pos = 0;
        } else {
          this.m_pos++;
        }
        // updating empty
        if (this.m_pos == this.m_startpos) {
          this.m_innerEmpty = true;
        }
        if (ret == null) {
          System.out.println("RingBufferArrayFast.iteratorL2F returns null: pos:" + m_pos
              + " startpos: " + this.m_startpos);
        }
        return ret;
      }
    };
  }

  /**
   * Returns an <code>Iterator</code> that will return the elements in exactly
   * the inverse order the subsequent call to <code>remove()</code> would do.
   * <p>
   *
   * The youngest elements are returned first. <b>The <code>Iterator</code>
   * returned is not thread- safe! </b>
   * <p>
   *
   * @return an <code>Iterator</code> that will return the elements in exactly
   *         the inverse order the subsequent call to <code>remove()</code>
   *         would do.
   */

  public java.util.Iterator iteratorF2L() {
    return new RingBufferIterator() {
      { // anonymous constructor: the method with unknown name...
        this.m_pos = (RingBufferArrayFast.this.m_headpointer == 0) ? RingBufferArrayFast.this
            .size() - 1 : RingBufferArrayFast.this.m_headpointer - 1;
        this.m_startpos = this.m_pos;
      }

      /**
       * @see java.util.Iterator#next()
       */
      public Object next() {
        Object ret = null;
        // Pending elements are the oldest
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        }
        ret = RingBufferArrayFast.this.m_buffer[this.m_pos];
        if (this.m_pos == 0) {
          this.m_pos = RingBufferArrayFast.this.size() - 1;
        } else {
          this.m_pos--;
        }
        if (this.m_pos == this.m_startpos) {
          this.m_innerEmpty = true;
        }
        if (ret == null) {
          System.out.println("RingBufferArrayFast.iteratorF2L returns null: pos:" + this.m_pos
              + " startpos: " + m_startpos);
        }
        return ret;
      }
    };
  }
}
