/*
 *  IteratorITracePointStateEnginge.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2012, Achim Westermann, created on Jan 22, 2012
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 *
 * File   : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $
 * Date   : $Date: 2009/02/24 16:45:41 $
 * Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart.traces.iterators.fsm;

import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.traces.iterators.AAccumulationIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An iterator over trace points that works as a Finite State Machine by
 * utilizing different state instances.
 * <p>
 * While this approach may look a bit over-designed I decided to use it after a
 * plain-forward implementation of accumulation - iterators grew into
 * sphaghetti-code.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public abstract class IteratorITracePointStateEnginge extends AAccumulationIterator {

  /**
   * Interface for a condition that folds the mealy input ({@link ITracePoint2D}
   * ) into a boolean.
   * <p>
   * Note: For every input trace point all conditions of a certain state must
   * exclude a result of {@link Boolean#TRUE} amongst each other. There must not
   * be two or more states related to transitions of a state that return
   * {@link Boolean#TRUE} for the same input trace point.
   * <p>
   * 
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public interface ICondition {
    public boolean isMet(final ITracePoint2D input);
  }

  /**
   * Interface for a state that knows how to compute the output given for the
   * input.
   * <p>
   * 
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public interface IState {

    /**
     * Compute the output from the given input (and potential owned state
     * information) and append it to the given list for output.
     * <p>
     * 
     * @param input
     *          the input trace point.
     * 
     * @param outputTarget
     *          append the output to this list. This design (signature) was
     *          chosen as a single input may cause multiple output trace points.
     * 
     * @return true if a follow state should be searched. False is a way to
     *         indicate that the state knows (from it's input) that no state
     *         change is required (allows performance boost).
     */
    public boolean computeOutput(final ITracePoint2D input, final List<ITracePoint2D> outputTarget);
  }

  /**
   * Implementation of a transition. It is a compound of an {@link ICondition}
   * and the {@link IState} that follows from the input (in case the condition
   * is true).
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public final class Transition {
    /**
     * The condition (computed from the input {@link ITracePoint2D}) that has to
     * be met to jump to the state of this transition.
     */
    private final ICondition m_condition;

    /**
     * The state that will be the next current state if the condition is met.
     */
    private final IState m_stateFollowing;

    /**
     * Creates a transition with a condition and the next state (in case
     * condition falls true).
     * <p>
     * 
     * @param condition
     *          the condition (computed from the input {@link ITracePoint2D})
     *          that has to be met to jump to the state of this transition.
     * 
     * @param stateFollowing
     *          the state that will be the next current state if the condition
     *          is met.
     */
    public Transition(final ICondition condition, final IState stateFollowing) {
      this.m_condition = condition;
      this.m_stateFollowing = stateFollowing;
    }

    /**
     * Returns the condition (computed from the input {@link ITracePoint2D})
     * that has to be met to jump to the state of this transition.
     * <p>
     * 
     * @return the condition (computed from the input {@link ITracePoint2D})
     *         that has to be met to jump to the state of this transition.
     */
    public ICondition getCondition() {
      return this.m_condition;
    }

    /**
     * Returns the state that will be the next current state if the condition is
     * met.
     * <p>
     * 
     * @return the state that will be the next current state if the condition is
     *         met.
     */
    public IState getStateFollowing() {
      return this.m_stateFollowing;
    }
  }

  /**
   * The central data structure to control the state transition flow.
   */
  private Map<IState, List<Transition>> m_transitionTable;

  /**
   * The current state.
   */
  private IState m_currentState;

  /**
   * The start state. Needed e.g. for resetting.
   */
  private IState m_startState;

  /**
   * Output buffer given to the states to allow them to return more than one
   * output for a single input.
   */
  private LinkedList<ITracePoint2D> m_outputBuffer = new LinkedList<ITracePoint2D>();

  /**
   * Constructor with all that is needed for accumulating points.
   * <p>
   * 
   * @param originalTrace
   *          the iterator to decorate with the feature of accumulating points.
   * 
   * @param accumulationFunction
   *          the function to use for point - accumulation.
   * 
   * 
   */
  public IteratorITracePointStateEnginge(ITrace2D originalTrace,
      IAccumulationFunction accumulationFunction) {
    super(originalTrace, accumulationFunction);
    this.initTransitionTable();
  }

  /**
   * Allows to reuse this instance (by resetting to the initial state) with the
   * given trace.
   * <p>
   * This skips setting up the internal transition table (see
   * {@link #initTransitionTable()} anew and therefore bypasses unneccessary
   * computations.
   * <p>
   * 
   * @param trace
   *          the new trace this iterator will work for.
   */
  public void reset(final ITrace2D trace) {
    this.setOriginalTrace(trace);
    this.m_currentState = this.m_startState;
  }

  /**
   * Template method to initialize your transition table here. Also set the
   * start state.
   * <p>
   * After this subsequent calls to {@link #next()} should be OK.
   * <p>
   */
  protected abstract void initTransitionTable();

  /**
   * @see java.util.Iterator#hasNext()
   */
  @Override
  public boolean hasNext() {

    boolean result = false; 
    result = !this.m_outputBuffer.isEmpty() || this.getOriginalIterator().hasNext();
    return result;
  }

  /**
   * @see java.util.Iterator#next()
   */
  @Override
  public ITracePoint2D next() {

    ITracePoint2D result;
    /*
     * 1. Clean the output buffer.
     */
    if (!this.m_outputBuffer.isEmpty()) {
      // remove HEAD / FIRST
      result = this.m_outputBuffer.remove();
    } else {

      /*
       * 2. If output buffer was empty fill it again and return the first filled
       * point.
       */
      Iterator<ITracePoint2D> iterator = this.getOriginalIterator();
      ITracePoint2D point = iterator.next();
      boolean searchFollowState = false;
      while (this.m_outputBuffer.isEmpty()) {
        searchFollowState = this.m_currentState.computeOutput(point, this.m_outputBuffer);
        if (searchFollowState) {
          this.searchFollowState(point);
        }
        point = iterator.next();
      }
      // remove HEAD / FIRST.
      result = this.m_outputBuffer.remove();
    }
    return result;
  }

  /**
   * Works the internal transition table to find the following by the current
   * state and the given input.
   * 
   * @param point
   *          the current input that caused a follow state to be searched.
   */
  private void searchFollowState(ITracePoint2D point) {
    List<Transition> potentialTransitions = this.m_transitionTable.get(this.m_currentState);
    ICondition condition;
    for (Transition transition : potentialTransitions) {
      condition = transition.getCondition();
      if (condition.isMet(point)) {
        this.m_currentState = transition.getStateFollowing();
        break;
      }
    }
  }
}
