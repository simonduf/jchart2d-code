package aw.gui.util;
import java.util.Iterator;

public class ColorIterator implements Iterator {

	/**
	 * To allow clean reset of ColorIterator, also the start HSBColor has to be reset. 
	 * This is done by a deep copy at construction time. 
	 **/
	private HSBColor start;
	private HSBColor iterate;
	private SteppingModel stepModel;
	/**
	 * To allow clean reset of ColorIterator, also the SteppingModel has to be reset. 
	 * This is done by a deep copy at construction time. 
	 **/
	private SteppingModel resetModel;

	public ColorIterator() {
		this.start = new HSBColor(0.0f, 1.0f, 1.0f); // brightest red ever
		this.iterate = (HSBColor) this.start.clone();
		this.stepModel = new HueStep(0.001f);
		this.resetModel = (SteppingModel) this.stepModel.clone();
	}
	private boolean hasnext = true;

	public boolean hasNext() {
		return this.hasnext;
	}

	/**
	 * Returns instances of java.awt.Color or throws a NoSuchElementException, if iterator has finished.
	 **/
	public Object next() {
		if (!hasnext)
			throw new java.util.NoSuchElementException("No more colors to give (call reset for new run!)");
		this.stepModel.doStep(this);
		if (this.iterate.equals(this.start))
			this.hasnext = false;
		return this.iterate.getRGBColor();
	}
	/**
	 * Resets the ColorIterator. It will be able to start a new iteration over the 
	 * colorspace.
	 **/
	public void reset() {
		this.iterate = (HSBColor) this.start.clone();
		// also reset the SteppingModel!!!!
		this.stepModel = (SteppingModel) this.resetModel.clone();
		this.hasnext = true;
	}

	/**
	 * Nothing is done here. Do you really want to remove a color from the colorcircle 
	 * model?
	 * */
	public void remove() {}

	public static interface SteppingModel extends Cloneable {
		void doStep(ColorIterator tostep);
		// widening the clone method of java.lang.Object
		public Object clone();
	}

	/**
	 * Just for protected internal float stepping.
	 **/
	public static abstract class DefaultStepping implements SteppingModel {
		protected float stepping;

		public DefaultStepping() throws IllegalArgumentException {
			this(1.0f / 100.0f);
		}

		public DefaultStepping(float stepping) throws IllegalArgumentException {
			if (stepping > 1.0f || stepping <= 0.0f)
				throw new IllegalArgumentException("Illegal stepping param: choose between 0.1 and 1.0.");
			this.stepping = stepping;
		}
		/**
		 * Too lazy to implement for each subclass. 
		 * An overhead for newInstance() (return dynamic subtype) is paid here.
		 **/
		public Object clone() {
			try {
				DefaultStepping ret = (DefaultStepping) (this.getClass().newInstance());
				ret.stepping = this.stepping;
				return ret;
			} catch (Throwable f) {
				f.printStackTrace();
				return null; // this will never happen (fucking the compiler)
			}
		}
	}

	public static class HueStep extends DefaultStepping {
		public HueStep() throws IllegalArgumentException {
			super();
		}

		public HueStep(float stepping) throws IllegalArgumentException {
			super(stepping);
		}

		/**
		 * Performs a hue step on the given ColorIterator's HSBColor. 
		 * The bounds are watched: if a hue step would cross 1.0f it will be continued 
		 * beginning from 0. if a hue step would cross the hue value of the ColorIterator's 
		 * start hue value, the step will only go as far as this value. Else there would be 
		 * problems with finding the end of the iteration. 
		 **/
		public void doStep(ColorIterator tostep) {
			float increment = tostep.iterate.hue;
			float bound = tostep.start.hue;
			if (increment == bound) {
				increment += this.stepping;
			}
			// before crossing 1.0, i will cross the iteration bound (no care for 1.0 needed)
			else if (increment < bound) {
				increment += this.stepping;
				if (increment > bound)
					increment = bound;
			}
			// more complicated: watch for crossing 1.0, then watch for crossing iteration bound.
			else {
				increment += this.stepping;
				if (increment > 1.0f) {
					increment -= 1.0;
					// only for the case that we jumped look for overtaking the bound
					if (increment > bound)
						increment = bound;
				}
			}
			tostep.iterate.hue = increment;
		}
	}

	public static class LuminanceStep extends DefaultStepping {
		public LuminanceStep() throws IllegalArgumentException {
			super();
		}
		public LuminanceStep(float stepping) throws IllegalArgumentException {
			super(stepping);
		}

		/**
		 * Performs a luminance step on the given ColorIterator's HSBColor. 
		 * The bounds are watched: if a step would cross 1.0f, it will be continued 
		 * beginning from 0. if a step would cross the luminance value of the ColorIterator's 
		 * start luminance, the step will only go as far as this value. Else there would be 
		 * problems with finding the end of the iteration. 
		 **/
		public void doStep(ColorIterator tostep) {
			float increment = tostep.iterate.lum;
			float bound = tostep.start.lum;
			if (increment == bound) {
				increment += this.stepping;
			}
			// before crossing 1.0, i will cross the iteration bound (no care for 1.0 needed)
			else if (increment < bound) {
				increment += this.stepping;
				if (increment > bound)
					increment = bound;
			}
			// more complicated: watch for crossing 1.0, then watch for crossing iteration bound.
			else {
				increment += this.stepping;
				if (increment > 1.0f) {
					increment -= 1.0;
					if (increment > bound)
						increment = bound;
				}
			}
			tostep.iterate.lum = increment;
		}
	}

	public static class SaturationStep extends DefaultStepping {
		public SaturationStep() throws IllegalArgumentException {
			super();
		}
		public SaturationStep(float stepping) throws IllegalArgumentException {
			super(stepping);
		}

		/**
		 * Performs a luminance step on the given ColorIterator's HSBColor. 
		 * The bounds are watched: if a step would cross 1.0f, it will be continued 
		 * beginning from 0. if a step would cross the luminance value of the ColorIterator's 
		 * start luminance, the step will only go as far as this value. Else there would be 
		 * problems with finding the end of the iteration. 
		 **/
		public void doStep(ColorIterator tostep) {
			float increment = tostep.iterate.sat;
			float bound = tostep.start.sat;
			if (increment == bound) {
				increment += this.stepping;
			}
			// before crossing 1.0, i will cross the iteration bound (no care for 1.0 needed)
			else if (increment < bound) {
				increment += this.stepping;
				if (increment > bound)
					increment = bound;
			}
			// more complicated: watch for crossing 1.0, then watch for crossing iteration bound.
			else {
				increment += this.stepping;
				if (increment > 1.0f) {
					increment -= 1.0;
					if (increment > bound)
						increment = bound;
				}
			}
			tostep.iterate.sat = increment;
		}
	}

	public static abstract class PiggyBackStepper extends DefaultStepping {
		HueStep huestep;
		SaturationStep satstep;
		LuminanceStep lumstep;
		public PiggyBackStepper() throws IllegalArgumentException {
			this(0.002f,0.2f, 0.2f);
		}
		public PiggyBackStepper(float hueStepping, float satStepping, float lumStepping)
			throws IllegalArgumentException {
			this.huestep = new HueStep(hueStepping);
			this.satstep = new SaturationStep(satStepping);
			this.lumstep = new LuminanceStep(lumStepping);
		}

		public Object clone() {
			PiggyBackStepper ret = (PiggyBackStepper) super.clone();
			ret.huestep = (HueStep) this.huestep.clone();
			ret.satstep = (SaturationStep) this.satstep.clone();
			ret.lumstep = (LuminanceStep) this.lumstep.clone();
			return ret;
		}
	}

	public static class HSBStepper extends PiggyBackStepper {
		public HSBStepper() throws IllegalArgumentException {
			super();
		}

		public void doStep(ColorIterator tostep) {
			// technique: without testing the step is done
			// this allows to restart with huestep even if start.hue==iterate.hue after having performed a step of different kind
			this.huestep.doStep(tostep);
			if (tostep.iterate.hue == tostep.start.hue) {
				this.satstep.doStep(tostep);
				if (tostep.iterate.sat == tostep.start.sat) {
					this.lumstep.doStep(tostep);
				}
			}
		}
	}
	public static class HSStepper extends PiggyBackStepper {
		public HSStepper() throws IllegalArgumentException {
			super();
		}
		public void doStep(ColorIterator tostep) {
			// technique: without testing the step is done
			// this allows to restart with huestep even if start.hue==iterate.hue after having performed a step of different kind
			this.huestep.doStep(tostep);
			if (tostep.iterate.hue == tostep.start.hue) {
				this.satstep.doStep(tostep);
			}
		}
	}

	public static void main(String[] args) {
		final javax.swing.JFrame frame = new javax.swing.JFrame("ColorCircleIterator-test");

		javax.swing.JPanel panel = new javax.swing.JPanel() {
			private ColorIterator color = new ColorIterator();
			{
				//System.out.println("start: " + color.start.toString());
				//System.out.println("iterate: " + color.iterate.toString());
				int wdt = 0;
				while (color.hasNext()) {
					wdt++;
					color.next();
				}
				System.out.println("found " + wdt + " different colors.");
				System.out.println("size: " + wdt);
				this.setSize(wdt, 100);
				this.setPreferredSize(new java.awt.Dimension(wdt, 100));
				this.setMinimumSize(new java.awt.Dimension(wdt, 100));
			}
			public void paint(java.awt.Graphics g) {
				super.paint(g);
				color.reset(); //refresh iterator
				int width = this.getWidth();
				int height = this.getHeight();
				int pxdrawn = 0;
				while (color.hasNext()) {
					if (pxdrawn == width)
						break;
					g.setColor((java.awt.Color) color.next());
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
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setResizable(true);
		frame.setVisible(true);
	}
}