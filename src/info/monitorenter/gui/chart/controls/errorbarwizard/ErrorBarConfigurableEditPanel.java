/*
 *  ErrorBarConfigurableEditPanel.java of project jchart2d, a panel 
 *  for configuration of a single ErrorBarConfigurable. 
 *  Copyright 2006 (C) Achim Westermann, created on 09:50:20.
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
package info.monitorenter.gui.chart.controls.errorbarwizard;

import info.monitorenter.gui.chart.IErrorBarPolicy;
import info.monitorenter.gui.chart.errorbars.ErrorBarPainterConfigureable;
import info.monitorenter.gui.chart.errorbars.ErrorBarPolicyRelative;
import info.monitorenter.util.FileUtil;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel for selection of
 * {@link info.monitorenter.gui.chart.errorbars.ErrorBarPainterConfigureable#setEndPointPainter(IPointPainter)},
 * {@link info.monitorenter.gui.chart.errorbars.ErrorBarPainterConfigureable#setStartPointPainter(IPointPainter)}
 * and
 * {@link info.monitorenter.gui.chart.errorbars.ErrorBarPainterConfigureable#setSegmentPainter(IPointPainter)}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.1 $
 */
public class ErrorBarConfigurableEditPanel
    extends JPanel {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -6564631494967160532L;

  /**
   * Creates a panel that offers configuration of the given error bar painter.
   * <p>
   * 
   * @param errorBarPainter
   *          the configurable error bar painter to configure.
   */
  public ErrorBarConfigurableEditPanel(final ErrorBarPainterConfigureable errorBarPainter) {

    super();

    // complex layout needed for ensuring that
    // both labes are displayed vertically stacked but
    // with the same distance to their text fields regardless
    // of their label width:
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(2, 2, 2, 2);

  }

}
