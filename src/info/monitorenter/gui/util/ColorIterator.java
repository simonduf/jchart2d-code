/*
 *  ColorIterator.java, something that walks over the Hue-Saturation-Brightness 
 *  color space. 
 *  Copyright (C) Achim Westermann, created on 19.05.2005, 22:01:51
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
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
 */
package info.monitorenter.gui.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator of ther color space.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.2 $
 */
public class ColorIterator implements Iterator {

  /**
   * To allow clean reset of ColorIterator, also the start HSBColor has to be
   * reset. This is done by a deep copy at construction time.
   */
  private HSBColor m_start;

  /** Reference to the currently iterated color. */
  private HSBColor m_iterate;

  /** The stepping model that defines the path throught the color space. */
  private ColorIterator.ISteppingModel m_stepModel;

  /**
   * To allow clean reset of ColorIterator, also the SteppingModel has to be
   * reset. This is done by a deep copy at construction time.
   */
  private ColorIterator.ISteppingModel m_resetModel;

  /**
   * Creates an instance that starts with a red and walks the hue line with a
   * {@link ColorIterator.HueStepper}.
   * <p>
   */
  public ColorIterator() {
    // brightest red ever
    this.m_start = new HSBColor(0.0f, 1.0f, 1.0f);
    this.m_iterate = (HSBColor) this.m_start.clone();
    this.m_stepModel = new HueStepper(0.001f);
    this.m_resetModel = (ColorIterator.ISteppingModel) this.m_stepModel.clone();
  }

  /** Flag to show if more colors are iterateable. */
  private boolean m_hasnext = true;

  /**
   * Returns true if more colors are available.
   * <p>
   * 
   * @return true if more colors are available.
   * 
   * @see java.util.Iterator#hasNext()
   */
  public boolean hasNext() {
    return this.m_hasnext;
  }

  /**
   * Returns instances of java.awt.Color or throws a NoSuchElementException, if
   * iterator has finished.
   * <p>
   * 
   * @return the next available Color.
   * 
   * @throws NoSuchElementException
   *           if {@link #hasNext()} returns false.
   */
  public Object next() throws NoSuchElementException {
    if (!this.m_hasnext) {
      throw new java.util.NoSuchElementException("No more colors to give (call reset for new run!)");
    }
    this.m_stepModel.doStep(this);
    if (this.m_iterate.equals(this.m_start)) {
      this.m_hasnext = false;
    }
    return this.m_iterate.getRGBColor();
  }

  /**
   * Resets the ColorIterator. It will be able to start a new iteration over the
   * colorspace.
   * <p>
   */
  public void reset() {
    this.m_iterate = (HSBColor) this.m_start.clone();
    // also reset the SteppingModel!!!!
    this.m_stepModel = (ColorIterator.ISteppingModel) this.m_resetModel.clone();
    this.m_hasnext = true;
  }

  /**
   * Nothing is done here. Do you really want to remove a color from the
   * colorcircle model?
   * <p>
   */
  public void remove() {
    // nop
  }

  /**
   * Defines the strategy of walking through the HSB color space.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * 
   * @version $Revision: 1.2 $
   */
  public static interface ISteppingModel extends Cloneable {
    /**
     * Performs a step on the given color iterator.
     * <p>
     * 
     * @param tostep
     *          the color iterator to perform a step on.
     */
    public void doStep(final ColorIterator tostep);

    /**
     * Creates a clone of this stepper.
     * <p>
     * 
     * @return a clone of this stepper.
     */
    public Object clone();
  }

  /**
   * Just for protected internal float stepping.
   * <p>
   * 
   * 
   */
  public abstract static class ADefaultStepping implements ColorIterator.ISteppingModel {
    /** The amount of stepping. */
    protected float m_stepping;

    /**
     * Creates a stepper with 100 steps in the color space.
     * <p>
     */
    public ADefaultStepping() {
      this(1.0f / 100.0f);
    }

    /**
     * Creates a stepper with the given step length.
     * <p>
     * 
     * @param stepping
     *          a step length in-between 0.0 and 1.0.
     * 
     * @throws IllegalArgumentException
     *           if the stepping is <= 0.0 or >=1.0.
     */
    public ADefaultStepping(final float stepping) throws IllegalArgumentException {
      if (stepping > 1.0f || stepping <= 0.0f) {
        throw new IllegalArgumentException("Illegal stepping param: choose within 0.0 and 1.0.");
      }
      this.m_stepping = stepping;
    }

    /**
     * Too lazy to implement for each subclass. An overhead for newInstance()
     * (return dynamic subtype) is paid here.
     * <p>
     * 
     * @return a clone of the stepper.
     */
    public Object clone() {
      ADefaultStepping result;
      try {
        result = (ADefaultStepping) super.clone();
      } catch (Throwable f) {
        f.printStackTrace();
        return null;
      }
      result.m_stepping = this.m_stepping;
      return result;
    }
  }

  /**
   * A stepper that walks along the hue line of the color space.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * @version $Revision: 1.2 $
   */
  public static class HueStepper
      extends ColorIterator.ADefaultStepping {
    /**
     * Creates an instance with 100 steps left.
     * <p>
     */
    public HueStepper() {
      super();
    }

    /**
     * Creates a stepper with the given step length.
     * <p>
     * 
     * @param stepping
     *          a step length in-between 0.0 and 1.0.
     * 
     * @throws IllegalArgumentException
     *           if the stepping is <= 0.0 or >=1.0.
     */
    public HueStepper(final float stepping) throws IllegalArgumentException {
      super(stepping);
    }

    /**
     * Performs a hue step on the given ColorIterator's HSBColor.
     * <p>
     * 
     * The bounds are watched: if a hue step would cross 1.0f it will be
     * continued beginning from 0. if a hue step would cross the hue value of
     * the ColorIterator's start hue value, the step will only go as far as this
     * value. Else there would be problems with finding the end of the
     * iteration.
     * <p>
     * 
     * @param tostep
     *          the iterator to perform the step on.
     */
    public void doStep(final ColorIterator tostep) {
      float increment = tostep.m_iterate.m_hue;
      float bound = tostep.m_start.m_hue;
      if (increment == bound) {
        increment += this.m_stepping;
      } else if (increment < bound) {
        // before crossing 1.0, i will cross the iteration bound (no care for
        // 1.0
        // needed)
        increment += this.m_stepping;
        if (increment > bound) {
          increment = bound;
        }
      } else {
        // more complicated: watch for crossing 1.0, then watch for crossing
        // iteration bound.
        increment += this.m_stepping;
        if (increment > 1.0f) {
          increment -= 1.0;
          // only for the case that we jumped look for overtaking the bound
          if (increment > bound) {
            increment = bound;
          }
        }
      }
      tostep.m_iterate.m_hue = increment;
    }
  }

  /**
   * A stepping model that steps on the luminance line of the HSB color space.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * @version $Revision: 1.2 $
   */
  public static class LuminanceStepper
      extends ColorIterator.ADefaultStepping {
    /**
     * Defcon.
     * <p>
     */
    public LuminanceStepper() {
      // nop
    }

    /**
     * Creates an instance with the given stepping to go on the luminance line
     * of the color space.
     * <p>
     * 
     * @param stepping
     *          the stepping for each step in between 0.0 and 1.0.
     * 
     * @throws IllegalArgumentException
     *           if stepping is <= 0.0 or >= 1.0.
     */
    public LuminanceStepper(final float stepping) throws IllegalArgumentException {
      super(stepping);
    }

    /**
     * Performs a luminance step on the given ColorIterator's HSBColor.
     * <p>
     * 
     * The bounds are watched: if a step would cross 1.0f, it will be continued
     * beginning from 0. if a step would cross the luminance value of the
     * ColorIterator's start luminance, the step will only go as far as this
     * value. Else there would be problems with finding the end of the
     * iteration.
     * <p>
     * 
     * @param tostep
     *          the color iterator to perform the step on.
     */
    public void doStep(final ColorIterator tostep) {
      float increment = tostep.m_iterate.m_lum;
      float bound = tostep.m_start.m_lum;
      if (increment == bound) {
        increment += this.m_stepping;
      } else if (increment < bound) {
        // before crossing 1.0, i will cross the iteration bound (no care for
        // 1.0
        // needed)
        increment += this.m_stepping;
        if (increment > bound) {
          increment = bound;
        }
      } else {
        // more complicated: watch for crossing 1.0, then watch for crossing
        // iteration bound.
        increment += this.m_stepping;
        if (increment > 1.0f) {
          increment -= 1.0;
          if (increment > bound) {
            increment = bound;
          }
        }
      }
      tostep.m_iterate.m_lum = increment;
    }
  }

  /**
   * A stepping model that steps on the saturation line of the HSB color space.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * @version $Revision: 1.2 $
   */
  public static class SaturationStepper
      extends ColorIterator.ADefaultStepping {
    /**
     * Defcon.
     * <p>
     */
    public SaturationStepper() {
      // nop
    }

    /**
     * Creates an instance with the given stepping to go on the saturation line
     * of the color space.
     * <p>
     * 
     * @param stepping
     *          the stepping for each step in between 0.0 and 1.0.
     * 
     * @throws IllegalArgumentException
     *           if stepping is <= 0.0 or >= 1.0.
     */
    public SaturationStepper(final float stepping) throws IllegalArgumentException {
      super(stepping);
    }

    /**
     * Performs a saturation step on the given ColorIterator's HSBColor.
     * <p>
     * he bounds are watched: if a step would cross 1.0f, it will be continued
     * beginning from 0. if a step would cross the luminance value of the
     * ColorIterator's start luminance, the step will only go as far as this
     * value. Else there would be problems with finding the end of the
     * iteration.
     * <p>
     * 
     * @param tostep
     *          the color iterator to perform the step on.
     */
    public void doStep(final ColorIterator tostep) {
      float increment = tostep.m_iterate.m_sat;
      float bound = tostep.m_start.m_sat;
      if (increment == bound) {
        increment += this.m_stepping;
      } else if (increment < bound) {
        // before crossing 1.0, i will cross the iteration bound (no care for
        // 1.0
        // needed)
        increment += this.m_stepping;
        if (increment > bound) {
          increment = bound;
        }
      } else {
        // more complicated: watch for crossing 1.0, then watch for crossing
        // iteration bound.
        increment += this.m_stepping;
        if (increment > 1.0f) {
          increment -= 1.0;
          if (increment > bound) {
            increment = bound;
          }
        }
      }
      tostep.m_iterate.m_sat = increment;
    }
  }

  /**
   * Base class for stepping models that may step in each direction of the Hue
   * Saturation Luminance color space.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * 
   * @version $Revision: 1.2 $
   */
  public abstract static class APiggyBackStepper
      extends ColorIterator.ADefaultStepping {
    /** The hue stepper to use. */
    protected HueStepper m_huestep;

    /** The saturation stepper to use. */
    protected SaturationStepper m_satstep;

    /** The luminance stepper to use. */
    protected LuminanceStepper m_lumstep;

    /**
     * Defcon.
     * <p>
     */
    public APiggyBackStepper() {
      this(0.002f, 0.2f, 0.2f);
    }

    /**
     * Creates an instance that uses the given step lengths for hue, luminance
     * and saturation.
     * <p>
     * 
     * @param hueStepping
     *          the step length on the hue line of the HSB color space.
     * 
     * @param satStepping
     *          the step length on the saturation line of the HSB color space.
     * 
     * @param lumStepping
     *          the step length on the luminance line of the HSB color space.
     * 
     * @throws IllegalArgumentException
     *           if any of the arguments is <= 0.0 or >= 1.0.
     */
    public APiggyBackStepper(final float hueStepping, final float satStepping,
        final float lumStepping) throws IllegalArgumentException {
      this.m_huestep = new HueStepper(hueStepping);
      this.m_satstep = new SaturationStepper(satStepping);
      this.m_lumstep = new LuminanceStepper(lumStepping);
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
      APiggyBackStepper ret = (APiggyBackStepper) super.clone();
      ret.m_huestep = (HueStepper) this.m_huestep.clone();
      ret.m_satstep = (SaturationStepper) this.m_satstep.clone();
      ret.m_lumstep = (LuminanceStepper) this.m_lumstep.clone();
      return ret;
    }
  }

  /**
   * Performs hue steps until it has walked the whole hue line, then performs a
   * saturation step to start with hue steps again. If the saturation steps have
   * walked the whole saturation line, a luminance step is done before starting
   * with hue steps again.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * @version $Revision: 1.2 $
   */
  public static class HSBStepper
      extends ColorIterator.APiggyBackStepper {
    /**
     * Defcon.
     * <p>
     */
    public HSBStepper() {
      super();
    }

    /**
     * @see info.monitorenter.gui.util.ColorIterator.ISteppingModel#doStep(info.monitorenter.gui.util.ColorIterator)
     */
    public void doStep(final ColorIterator tostep) {
      // technique: without testing the step is done
      // this allows to restart with huestep even if start.hue==iterate.hue
      // after having performed a step of different kind
      this.m_huestep.doStep(tostep);
      if (tostep.m_iterate.m_hue == tostep.m_start.m_hue) {
        this.m_satstep.doStep(tostep);
        if (tostep.m_iterate.m_sat == tostep.m_start.m_sat) {
          this.m_lumstep.doStep(tostep);
        }
      }
    }
  }

  /**
   * Performs hue steps until it has walked the whole hue line, then performs a
   * saturation step to start with hue steps again.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * @version $Revision: 1.2 $
   */
  public static class HSStepper
      extends ColorIterator.APiggyBackStepper {
    /**
     * Defcon.
     * <p>
     */
    public HSStepper() {
      // nop
    }

    /**
     * @see info.monitorenter.gui.util.ColorIterator.ISteppingModel#doStep(info.monitorenter.gui.util.ColorIterator)
     */
    public void doStep(final ColorIterator tostep) {
      // technique: without testing the step is done
      // this allows to restart with huestep even if start.hue==iterate.hue
      // after having performed a step of different kind
      this.m_huestep.doStep(tostep);
      if (tostep.m_iterate.m_hue == tostep.m_start.m_hue) {
        this.m_satstep.doStep(tostep);
      }
    }
  }

  /**
   * Main entry for a test application.
   * <p>
   * 
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    final javax.swing.JFrame frame = new javax.swing.JFrame("ColorCircleIterator-test");

    javax.swing.JPanel panel = new javax.swing.JPanel() {
      /**
       * Generated <code>serialVersionUID</code>.
       */
      private static final long serialVersionUID = 3258408422146715703L;

      private ColorIterator m_color = new ColorIterator();
      {
        // System.out.println("start: " + color.start.toString());
        // System.out.println("iterate: " + color.iterate.toString());
        int wdt = 0;
        while (this.m_color.hasNext()) {
          wdt++;
          this.m_color.next();
        }
        System.out.println("found " + wdt + " different colors.");
        System.out.println("size: " + wdt);
        this.setSize(wdt, 100);
        this.setPreferredSize(new java.awt.Dimension(wdt, 100));
        this.setMinimumSize(new java.awt.Dimension(wdt, 100));
      }

      /**
       * @see java.awt.Component#paint(java.awt.Graphics)
       */
      public void paint(final java.awt.Graphics g) {
        super.paint(g);
        // refresh iterator
        this.m_color.reset();
        int width = this.getWidth();
        int height = this.getHeight();
        int pxdrawn = 0;
        while (this.m_color.hasNext()) {
          if (pxdrawn == width) {
            break;
          }
          g.setColor((java.awt.Color) this.m_color.next());
          g.drawLine(pxdrawn, 0, pxdrawn, height);
          pxdrawn++;
        }
      }
    };

    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(panel);
    java.awt.Container contentPane = frame.getContentPane();
    contentPane.setLayout(new java.awt.BorderLayout());
    contentPane.add(scroll, java.awt.BorderLayout.CENTER);

    frame.setLocation(200, 200);
    frame.setSize(new java.awt.Dimension(400, 100));
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(final java.awt.event.WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setResizable(true);
    frame.setVisible(true);
  }
}
