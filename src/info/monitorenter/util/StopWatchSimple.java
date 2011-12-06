/*
 * StopWatchSimple, a basic implementation of the interface IStopWatch 
 * meant for measurements of short times (>hours). 
 * Copyright (C) 2002 Achim Westermann, Achim.Westermann@gmx.de
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 * 
 * The contents of this collection are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is the cpDetector code in [sub] packages info.monitorenter and 
 * cpdetector. 
 * 
 * The Initial Developer of the Original Code is
 * Achim Westermann <achim.westermann@gmx.de>.
 * 
 * Portions created by the Initial Developer are Copyright (c) 2007 
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 * 
 * ***** END LICENSE BLOCK ***** * 
 */

package info.monitorenter.util;

/**
 *  A very simple implementation of the IStopWatch that does nothing more
 *  than the interface describes. Only the m_start - value, the running - boolean
 *  and the summation of runtimes is hold: Little RAM - consumption and little
 *  calculations here.
 *
 * @author  <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 * @version 1.1
 */
public class StopWatchSimple implements IStopWatch{
    protected long m_start;
    protected long allms;
    protected boolean running = false;
    
    /**
     * Creates new StopWatchSimple. The measurement of time is not started here.
     */
    public StopWatchSimple() {
    }
    
    /**
     * Creates new StopWatchSimple. The measurement of time is started if param is true.
     */
    public StopWatchSimple(boolean start) {
        if(start)start();
    }
    
    /**
     * This method may serve two purposes:<br>
     * <ul>
     * <li>
     * Running- Reset: <br>
     * The m_start- value of the measurement is set to the current time, even if
     * the StopWatch is running.
     * </li>
     * <li>
     * Next- measurement- Reset:<br>
     * A flag is set which causes the next call to m_start to delete the old
     * m_start - value.
     * </li>
     * </ul>
     * Note that continuous calls to <code>m_start()</code> and <code>stop()</code>
     * without calling reset will always relate the measurement of time to the
     * first time <code>m_start()</code> was called!
     */
    public final void reset() {
        this.m_start = System.currentTimeMillis();
        this.allms = 0;
    }
    
    /**
     * Sets the state to running. This allows a call to <code>stop()</code>
     * to make a new measurement by taking the current time.
     * If <code>reset()</code> was invoked before, the m_start - time is set
     * to the return value of <code>System.currentTimeMillis()</code>.
     * Else the old value is preserved.
     * False is returned if a measurement is already in progress.
     * <b> A call to m_start will only m_start a new measurement with the current
     * Time, if it is the first run or reset was called before. Else the time
     * kept after the next call to stop will be the sum of all previous runtimes.
     */
    public final boolean start(){
        if(!running){
            this.m_start = System.currentTimeMillis();
            this.running = true;
            return true;
        }
        return false;
    }
    
    /**
     * This method does not change the state from running to !running but performs
     * an update of the overall measurement- data inside.
     * The difference to <code>stop()<code>:<br>
     * After <code>stop()</code> has been called the state is set to !running which
     * causes a new m_start-value to be set during the next call to <code>m_start()</code>.
     * The call to <code>snapShot()</code> does not switch the state.
     * If afterwards <code>m_start()</code> is called, no new value gets assigned
     * to the m_start- value of the StopWatch. Despite of this <code>snapShot</code>
     * Adds the period from the m_start-value to now to the internal total measurement-
     * call. To avoid double - summation of the same time- periods a new m_start- value
     * is set directly.
     */
    public long snapShot() {
        long restart = System.currentTimeMillis();
        this.allms += restart-this.m_start;
        this.m_start = restart;
        return allms;
    }
    
    /**
     * Stops the measurement by assinging current time in ms to the stop value.
     * Return true if a valid measurement has been made (StopWatch was running before invoking this method).
     */
    public synchronized boolean stop() {
        if(this.running){
            // filling internal data
            this.allms += System.currentTimeMillis() - this.m_start;
            this.running = false;
            return true;
        }
        return false;
    }
    
    /**
     * Returns the current value of the IStopWatch in ms.
     * This has to be the sum of all previous measurements (circles  of <code>m_start()-stop()</code>)
     * not interrupted by calls to <code>reset()</code>.
     */
    public final long getPureMilliSeconds() {
        if(running)
            this.snapShot();
        return this.allms;
    }
    
    public String toString(){
        if(running)
            this.snapShot();
        return String.valueOf(allms)+" ms";
    }

    public static void main(String[]args){
        try{
            System.out.println("Stopping the sleep for 5000 ms");
            final IStopWatch test = new StopWatchSimple(true);
            Thread.sleep(5000);
            test.stop();
            System.out.println("Stopped the StopWatch: ");
            System.out.println(test.toString());
            System.out.println("Stopping the next sleep of 4000 ms (no reset was called!).");
            System.out.println("    test.start():"+test.start());
            Thread.sleep(4000);
            System.out.println("    test.stop(): "+test.stop());
            System.out.println("Stopped the StopWatch: ");
            System.out.println(test.toString());
            System.out.println("Calling reset(). ");
            test.reset();
            System.out.println("Stopping a sleep of 3000 ms.");
            System.out.println("    test.start(): "+test.start());
            Thread.sleep(3000);
            System.out.println("    test.stop(): "+test.stop());
            System.out.println("Stopped the StopWatch: ");
            System.out.println(test.toString());   
            System.out.println("Calling reset()");
            test.reset();
            new Thread(){
                public void run(){
                    for(int i=0;i<3;i++){
                        try{
                            sleep(1500);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        test.snapShot(); 
                        System.out.println(this.getName()+" performed a snapShot on "+test);
                    }
                }
            }.start();
            System.out.println("Stopping a sleep of 10000 ms. A Thread has been started to take 3 snapShots!");
            System.out.println("    test.start(): "+test.start());
            Thread.sleep(10000);
            System.out.println("    test.stop(): "+test.stop());
            System.out.println("Stopped the StopWatch: ");
            System.out.println(test.toString());   
        }catch(Throwable f){
            f.printStackTrace();
        }
    }

}
