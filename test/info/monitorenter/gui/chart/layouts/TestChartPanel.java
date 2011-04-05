/*
 *  TestChartPanelMemoryLeak.java of project jchart2d, a 
 *  test for a memory leak detected by Pieter-Jan Busschaert.
 *  Copyright (c) 2007 Achim Westermann, created on 23:52:11.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart.layouts;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.controls.LayoutFactory;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.views.ChartPanel;
import info.monitorenter.util.math.MathUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * TestChartPanelMemoryLeak.java of project jchart2d, a test for a memory leak
 * detected by Pieter-Jan Busschaert.
 * <p>
 * 
 * @author Pieter-Jan Busschaert
 * @author Holger Brandl
 * 
 * 
 * @version $Revision: 1.5 $
 */
public class TestChartPanel extends TestCase {
	/**
	 * Main debug / profiler entry, do not drop - it's used from build xml to
	 * profile.
	 * <p>
	 * 
	 * @param args
	 *            ignored.
	 * 
	 * @throws Exception
	 *             if sth. goes wrong.
	 */
	public static void main(final String[] args) throws Exception {
		TestChartPanel test = new TestChartPanel(TestChartPanel.class.getName());
		test.setUp();
		test.testMemoryLeakAddRemoveTrace();
		test.tearDown();
	}

	/**
	 * Test suite for this test class.
	 * <p>
	 * 
	 * @return the test suite
	 */
	public static Test suite() {

		TestSuite suite = new TestSuite();
		suite.setName(TestChartPanel.class.getName());

		suite.addTest(new TestChartPanel("testNoTraces"));
		suite.addTest(new TestChartPanel("testManyTraces"));
		suite.addTest(new TestChartPanel("testMemoryLeak"));

		return suite;
	}

	/**
	 * Default constructor with test name.
	 * <p>
	 * 
	 * @param testName
	 *            the test name.
	 */
	public TestChartPanel(final String testName) {
		super(testName);
	}

	/**
	 * Creates a <code>{@link ChartPanel}</code> with a
	 * <code>{@link Chart2D}</code> that has many traces and shows it for some
	 * seconds.
	 * <p>
	 * Warning: For now this is only a visual test, especially for seeing if the
	 * space for the labels works correctly. This test will never fail if sth.
	 * is wrong. So watch it!
	 * <p>
	 * 
	 */
	public void testManyTraces() {
		Chart2D chart = new Chart2D();

		// add many traces:
		ITrace2D trace;
		for (int i = 0; i < 10; i++) {
			trace = new Trace2DSimple();
			chart.addTrace(trace);
			for (int j = 0; j < 20; j++) {
				trace.addPoint(j, (Math.random() + 1) * j);
			}
			trace.setName("trace-" + i);
		}

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new ChartPanel(chart));

		JFrame frame = new JFrame();
		frame.getContentPane().add(p);

		// frame.validate();
		frame.setSize(new Dimension(400, 400));
		frame.setVisible(true);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setVisible(false);
		frame.dispose();
	}

	/**
	 * Tests a memory leak that was found in jchart2d-1.1.0 and was related to
	 * adding and removing traces with charts wrapped in a {@link ChartPanel}.
	 * <p>
	 * 
	 */
	public void testMemoryLeakAddRemoveTrace() {
		Chart2D chart = new Chart2D();
		ChartPanel chartPanel = new ChartPanel(chart);
		PropertyChangeListener[] propertyChangeListeners = chart
				.getPropertyChangeListeners();
		Map<String, Integer> listeners2CountBeforeAddTrace = TestChartPanel
				.getPropertyChangeListenerStatisticsForChartPerProperty(chart);
		
		Map<Class<?>, Integer>listenerTypes2CountBeforeAddTrace = getPropertyChangeListenerStatisticsForChartPerListenerInstance(chart);
		// this call is for fooling checkstyle (unused local variable):
		chartPanel.setEnabled(false);
		ITrace2D trace;
		for (int i = 0; i < 100; i++) {
			System.out.print("Adding big trace " + i + " (16383 points)...");
			trace = new Trace2DLtd(16383);
			chart.addTrace(trace);
			System.out.println("   done!");
			try {
				if (i % 20 == 0) {
					System.out
							.print("Running garbage collection and finalization (I know: never do this)...");
					System.gc();
					System.runFinalization();
					System.out.println("    done!");
					System.out
							.println("chart.getPropertyChangeListeners().length: "
									+ chart.getPropertyChangeListeners().length);
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
			chart.removeTrace(trace);
		}

		System.runFinalization();
		System.gc();
		chart.setBackground(Color.LIGHT_GRAY);
		propertyChangeListeners = chart.getPropertyChangeListeners();

		// reporting / analysis
		Map<String, Integer> listeners2CountAfterAddTrace = TestChartPanel
				.getPropertyChangeListenerStatisticsForChartPerProperty(chart);
		Map<Class<?>, Integer>listenerTypes2CountAfterAddTrace = getPropertyChangeListenerStatisticsForChartPerListenerInstance(chart);

		System.out.println();
		System.out.println("Before add trace:");
		for (Map.Entry<String, Integer> entry : listeners2CountBeforeAddTrace
				.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}

		System.out.println();
		System.out.println("After add trace:");
		for (Map.Entry<String, Integer> entry : listeners2CountAfterAddTrace
				.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}

		/*
		 * Now compare the set of listeners before and  after adding and removing the traces.
		 */
		Set<String> distinctListenedPropertiesBeforeAddRemoveTrace = listeners2CountBeforeAddTrace.keySet();
		Set<String> distinctListenedPropertiesAfterAddRemoveTrace = listeners2CountBeforeAddTrace.keySet();
		for(String propertyBefore: distinctListenedPropertiesBeforeAddRemoveTrace){
			assertTrue("Property "+propertyBefore+" was listened on (on the chart) before adding and removing a trace, but not afterwards." , distinctListenedPropertiesAfterAddRemoveTrace.contains(propertyBefore));
		}
		for(String propertyAfter : distinctListenedPropertiesAfterAddRemoveTrace){
			assertTrue("Property "+propertyAfter+" is listened on (on the chart) after adding and removing a trace, but not before." , distinctListenedPropertiesBeforeAddRemoveTrace.contains(propertyAfter));
		}
		/*
		 * Now compare the amount of listeners for every property before and
		 * after adding and removing the traces.
		 */
		Integer amountBefore,amountAfter;
		for(String property:distinctListenedPropertiesBeforeAddRemoveTrace) {
			amountBefore = listeners2CountBeforeAddTrace.get(property);
			amountAfter = listeners2CountAfterAddTrace.get(property);
			assertEquals(
					"The amount of listeners on property "
							+ property
							+ " on the chart before adding and removing traces is not equal.",
					amountBefore, amountAfter);
		}
		chart.destroy();
	}

	/**
	 * Creates a <code>{@link ChartPanel}</code> with a
	 * <code>{@link Chart2D}</code> that has no traces and shows it for some
	 * seconds.
	 * <p>
	 * 
	 */
	public void testNoTraces() {
		Chart2D chart = new Chart2D();

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new ChartPanel(chart));

		JFrame frame = new JFrame();
		frame.getContentPane().add(p);

		frame.validate();
		frame.setSize(new Dimension(400, 400));
		frame.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setVisible(false);
		frame.dispose();
	}

	/**
	 * Helper method that analyzes the
	 * <code>{@link PropertyChangeListener}</code> instances of a given chart:
	 * it returns a map that contains the property to listen on (key) along with
	 * the count of listeners of that property (value).
	 * <p>
	 * This may be used to find out, if the amount of listeners on a certain
	 * property increases in-between invocation of a method that affects the
	 * chart.
	 * <p>
	 * 
	 * @param chart
	 *            the chart to analyze the listeners of.
	 * 
	 * @return a map that contains the property to listen on (key) along with
	 *         the count of listeners of that property (value).
	 */
	private static Map<String, Integer> getPropertyChangeListenerStatisticsForChartPerProperty(
			final Chart2D chart) {
		Class<?> clazz;
		Integer count;
		PropertyChangeListener[] propertyChangeListeners = chart
				.getPropertyChangeListeners();
		String property;
		Map<String, Integer> props2count = new HashMap<String, Integer>();

		for (int i = propertyChangeListeners.length - 1; i >= 0; i--) {
			clazz = propertyChangeListeners[i].getClass();
			// count the properties:
			if (propertyChangeListeners[i] instanceof PropertyChangeListenerProxy) {
				property = ((PropertyChangeListenerProxy) propertyChangeListeners[i])
						.getPropertyName();
				count = props2count.get(property);
				count = MathUtil.increment(count);
				props2count.put(property, count);
			} else if (clazz == LayoutFactory.BasicPropertyAdaptSupport.class) {
				/*
				 * TODO: very unstable code as implicit knowledge is used: We
				 * know which events BasicPropertyAdaptSupport listens for.
				 */
				String[] props = new String[] {
						Chart2D.PROPERTY_BACKGROUND_COLOR,
						Chart2D.PROPERTY_FONT,
						Chart2D.PROPERTY_FOREGROUND_COLOR };
				for (String prop : props) {
					count = props2count.get(prop);
					count = MathUtil.increment(count);
					props2count.put(prop, count);

				}
			} else {
				fail("Unhandeled propert change listener instance in chart: "
						+ propertyChangeListeners[i].getClass().getName());
			}

		}

		return props2count;

	}

	/**
	 * Helper method that analyzes the
	 * <code>{@link PropertyChangeListener}</code> instances of a given chart:
	 * it returns a map that contains listener <code>{@link Class}</code>(key)
	 * along with the count of instances registered on the given chart (value).
	 * <p>
	 * This may be used to find out the implementation class that messes up, if
	 * the amount of listeners of that type increases in-between invocation of a
	 * method that affects the chart.
	 * <p>
	 * 
	 * @param chart
	 *            the chart to analyze the listeners of.
	 * 
	 * @return a map that contains listener <code>{@link Class}</code>(key)
	 *         along with the count of instances registered on the given chart
	 *         (value).
	 */
	private static Map<Class<?>, Integer> getPropertyChangeListenerStatisticsForChartPerListenerInstance(
			final Chart2D chart) {
		Class<?> clazz;
		Integer count;
		PropertyChangeListener[] propertyChangeListeners = chart
				.getPropertyChangeListeners();
		Map<Class<?>, Integer> classes2count = new HashMap<Class<?>, Integer>();

		for (int i = propertyChangeListeners.length - 1; i >= 0; i--) {
			clazz = propertyChangeListeners[i].getClass();
			count = null;
			// count the listener classes:
			count = classes2count.get(clazz);
			count = MathUtil.increment(count);
			classes2count.put(clazz, count);
		}

		for (Map.Entry<Class<?>, Integer> entry : classes2count.entrySet()) {
			System.out.println((entry.getKey()).getName() + " : "
					+ entry.getValue());
		}

		return classes2count;

	}

}
