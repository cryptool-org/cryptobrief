package sunset.gui.panel;

import sunset.gui.FFaplJFrame;
import sunset.gui.interfaces.IFFaplComponent;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.util.TranslateGUIElements;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class JPanelDebugger extends JPanel implements IFFaplComponent {
    private JLabel jLabel_BreakpointColor;
    private JLabel jLabel_DebugColor;
    private JLabel jLabel_IntegerThreshold;

    private JButton colorPickerButton;
    private JButton debugColorPickerButton;
    private JSpinner jSpinner_IntegerThreshold;

    private final FFaplJFrame _frame;
    private Color breakpointColor;
    private Color debuglineColor;


    public JPanelDebugger(FFaplJFrame frame) {
        super();
        _frame = frame;
        initGUI();
        translate();
    }


    private void initGUI() {
        try {

            // Setup GridBagLayout and the constraints object
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // 5px padding around components
            gbc.fill = GridBagConstraints.HORIZONTAL;

            setPreferredSize(new Dimension(400, 300));

            // --- Load Colors ---
            try {
                breakpointColor = new Color(Integer.parseInt(GUIPropertiesLogic.getInstance().getProperty(IProperties.GUI_BREAKPOINT_COLOR)));
            } catch (NumberFormatException e) {
                breakpointColor = new Color(250, 234, 230);
            }

            try {
                debuglineColor = new Color(Integer.parseInt(GUIPropertiesLogic.getInstance().getProperty(IProperties.GUI_ACTIVE_DEBUG_COLOR)));
            } catch (NumberFormatException e) {
                debuglineColor = new Color(33, 84, 255);
            }

            // --- Row 1: Breakpoint Color ---
            {
                jLabel_BreakpointColor = new JLabel();
                //jLabel_BreakpointColor.setText("Color of Breakpoint Background");
                jLabel_BreakpointColor.setName("label_breakpoint_color");

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 0.0;
                this.add(jLabel_BreakpointColor, gbc);
            }

            {
                colorPickerButton = new JButton();
                colorPickerButton.setPreferredSize(new Dimension(25, 25));
                colorPickerButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                colorPickerButton.setBackground(breakpointColor);
                colorPickerButton.setContentAreaFilled(false);
                colorPickerButton.setOpaque(true);

                colorPickerButton.addActionListener(e -> {
                    Color selectedColor = JColorChooser.showDialog(_frame, "Select Breakpoint Color", breakpointColor);
                    if (selectedColor != null) {
                        breakpointColor = selectedColor;
                        colorPickerButton.setBackground(breakpointColor);
                    }
                });

                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                this.add(colorPickerButton, gbc);
            }

            // --- Row 2: Active Debug Line Color ---
            {
                jLabel_DebugColor = new JLabel();
                //jLabel_DebugColor.setText("Color of Active Debug Line");
                jLabel_DebugColor.setName("label_debug_color");

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.weightx = 0.0;
                this.add(jLabel_DebugColor, gbc);
            }

            {
                debugColorPickerButton = new JButton();
                debugColorPickerButton.setBackground(debuglineColor);
                debugColorPickerButton.setPreferredSize(new Dimension(25, 25));
                debugColorPickerButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                debugColorPickerButton.setContentAreaFilled(false);
                debugColorPickerButton.setOpaque(true);

                debugColorPickerButton.addActionListener(e -> {
                    Color selectedColor = JColorChooser.showDialog(_frame, "Select Debug Line Color", debuglineColor);
                    if (selectedColor != null) {
                        debuglineColor = selectedColor;
                        debugColorPickerButton.setBackground(debuglineColor);
                    }
                });

                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.weightx = 1.0;
                this.add(debugColorPickerButton, gbc);
            }

            // --- Row 3: Integer Scientific Notation Cutoff ---
            {
                jLabel_IntegerThreshold = new JLabel();
                //jLabel_IntegerThreshold.setText("Scientific Notation Threshold");
                jLabel_IntegerThreshold.setName("label_debugger_integerthreshold");

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.weightx = 0.0;
                this.add(jLabel_IntegerThreshold, gbc);
            }

            {

                int int_threshold = 10;
                try {
                    int_threshold = Integer.parseInt(GUIPropertiesLogic.getInstance().getProperty(IProperties.DEBUGGER_INTEGER_THRESHOLD));
                } catch (NumberFormatException ignored){

                }
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(int_threshold, 0, null, 1);
                jSpinner_IntegerThreshold = new JSpinner(spinnerModel);
                jSpinner_IntegerThreshold.setPreferredSize(new Dimension(100, 25));

                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.weightx = 1.0;
                this.add(jSpinner_IntegerThreshold, gbc);
            }

            // --- The "Pack Up" Filler Panel ---
            {
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 2; // Span across both columns
                gbc.weighty = 1.0; // Consume all remaining vertical space
                this.add(new JPanel(), gbc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void translate() {
        TranslateGUIElements.translateLabel(jLabel_BreakpointColor);
        TranslateGUIElements.translateLabel(jLabel_DebugColor);
        TranslateGUIElements.translateLabel(jLabel_IntegerThreshold);
    }


    @Override
    public void storeChanges() {
        if (breakpointColor != null) {
            GUIPropertiesLogic.getInstance().setProperty(IProperties.GUI_BREAKPOINT_COLOR, String.valueOf(breakpointColor.getRGB()));
            FFaplJFrame.getCurrentCodePanel().repaint();
        }

        if (debuglineColor != null) {
            GUIPropertiesLogic.getInstance().setProperty(IProperties.GUI_ACTIVE_DEBUG_COLOR, String.valueOf(debuglineColor.getRGB()));
            FFaplJFrame.getCurrentCodePanel().repaint();
        }

        String int_threshold = jSpinner_IntegerThreshold.getValue().toString();
        if (int_threshold != null) {
            try {
                // only set property if int is parseable
                String new_value = String.valueOf(Integer.parseInt(int_threshold));
                Object prev_value = GUIPropertiesLogic.getInstance().setProperty(IProperties.DEBUGGER_INTEGER_THRESHOLD, new_value);
                if (!Objects.equals(prev_value, new_value)){
                    FFaplJFrame.getDebuggerPanel().repaintTree();
                }
            } catch (NumberFormatException ignored){

            }
        }
    }
}
