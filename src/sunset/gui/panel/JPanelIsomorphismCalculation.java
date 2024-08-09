package sunset.gui.panel;

import ffapl.java.math.isomorphism.calculation.linearfactor.RootFindingStrategyType;
import sunset.gui.util.IsomorphismCalculationUtil;
import sunset.gui.interfaces.IFFaplComponent;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.util.TranslateGUIElements;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;

public class JPanelIsomorphismCalculation extends javax.swing.JPanel implements IFFaplComponent {

    private JLabel jLabel_IsomorphismCalculationTimeLimit;
    private JTextField jTextField_IsomorphismCalculationTimeLimit;
    private JLabel jLabel_RootFindingStrategy;
    private JComboBox<String> jComboBox_RootFindingStrategy;

    public JPanelIsomorphismCalculation() {
        super();
        initGUI();
        translate();
    }


    private void initGUI() {
        try {

            GridBagLayout thisLayout = new GridBagLayout();
            this.setLayout(thisLayout);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            setPreferredSize(new Dimension(400, 300));

            {
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                jLabel_IsomorphismCalculationTimeLimit = new JLabel();
                thisLayout.setConstraints(jLabel_IsomorphismCalculationTimeLimit, gridBagConstraints);
                this.add(jLabel_IsomorphismCalculationTimeLimit);
                jLabel_IsomorphismCalculationTimeLimit.setText("<html>Time limit in seconds:</html>");
                jLabel_IsomorphismCalculationTimeLimit.setName("label_isomorphismcalculation_timelimit");
            }
            {
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 1;
                jTextField_IsomorphismCalculationTimeLimit = new JTextField();
                thisLayout.setConstraints(jTextField_IsomorphismCalculationTimeLimit, gridBagConstraints);
                jTextField_IsomorphismCalculationTimeLimit.setText(String.valueOf(IsomorphismCalculationUtil.getTimeLimitInSeconds()));
                this.add(jTextField_IsomorphismCalculationTimeLimit);
            }
            {
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                jLabel_RootFindingStrategy = new JLabel();
                thisLayout.setConstraints(jLabel_RootFindingStrategy, gridBagConstraints);
                this.add(jLabel_RootFindingStrategy);
                jLabel_RootFindingStrategy.setText("<html>Root-finding strategy:</html>");
                jLabel_RootFindingStrategy.setName("label_rootfindingstrategy");
            }
            {
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 2;
                jComboBox_RootFindingStrategy = new JComboBox<>(RootFindingStrategyType.stringRepresentations());
                this.add(jComboBox_RootFindingStrategy);
                thisLayout.setConstraints(jComboBox_RootFindingStrategy, gridBagConstraints);
                jComboBox_RootFindingStrategy.setSelectedItem(IsomorphismCalculationUtil.getRootFindingStrategyType().stringRepresentation());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void translate() {
        TranslateGUIElements.translateLabel(jLabel_IsomorphismCalculationTimeLimit);
        TranslateGUIElements.translateLabel(jLabel_RootFindingStrategy);
    }

    @Override
    public void storeChanges() {
        handleIsomorphismCalculationTimeLimitTextField();
        handleRootFindingComboBox();
    }

    private void handleIsomorphismCalculationTimeLimitTextField() {
        try {
            int newTimeLimit = Integer.parseInt(jTextField_IsomorphismCalculationTimeLimit.getText());
            if (newTimeLimit >= 1) {
                GUIPropertiesLogic.getInstance().setIntegerProperty(
                        IProperties.ISOMORPHISM_CALCULATION_TIME_LIMIT,
                        Integer.parseInt(jTextField_IsomorphismCalculationTimeLimit.getText()));
            }
        } catch (NumberFormatException ex) {// in this case, the new value is not stored
            // TODO - 28.08.2022: Maybe log this case
        }
    }

    private void handleRootFindingComboBox() {
        String rootFindingComboboxText = (String) jComboBox_RootFindingStrategy.getSelectedItem();

        try {
            RootFindingStrategyType type = RootFindingStrategyType.fromStringRepresentation(rootFindingComboboxText);
            GUIPropertiesLogic.getInstance().setProperty(
                    IProperties.ISOMORPHISM_CALCULATION_ROOT_FINDING_STRATEGY,
                    type.name()
            );
        } catch (NoSuchElementException exception) { // in this case, the new value is not stored
            // TODO - 28.08.2022: Maybe log this case
        }
    }
}
