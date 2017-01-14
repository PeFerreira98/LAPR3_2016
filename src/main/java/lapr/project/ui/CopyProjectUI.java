/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import javax.swing.JOptionPane;
import lapr.project.controller.CopyProjectController;
import lapr.project.controller.OpenProjectController;
import lapr.project.model.Project;

/**
 *
 * @author zero_
 */
public class CopyProjectUI extends javax.swing.JFrame {

    private final CopyProjectController copyProjectController;
    private final OpenProjectController ctrl_open;
    
    /**
     * Creates new form CopyProjectUI
     * @param project
     */
    public CopyProjectUI(Project project) {
        this.ctrl_open = new OpenProjectController(project);
        this.ctrl_open.LoadInformation();
        this.copyProjectController = new CopyProjectController(project);
        initComponents();
        
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nameJText = new javax.swing.JTextField();
        saveJButton = new javax.swing.JButton();
        verifyJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtdesc = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Insert New Name");

        jLabel2.setText("Insert New Description");

        saveJButton.setText("Save");
        saveJButton.setEnabled(false);
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJButtonActionPerformed(evt);
            }
        });

        verifyJButton.setText("Verify");
        verifyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyJButtonActionPerformed(evt);
            }
        });

        txtdesc.setColumns(20);
        txtdesc.setRows(5);
        jScrollPane1.setViewportView(txtdesc);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameJText, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(verifyJButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveJButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(verifyJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveJButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        if (copyProjectController.createProject(nameJText.getText(), txtdesc.getText())) {
            if (copyProjectController.saveInDataBase()) {
                JOptionPane.showMessageDialog(this, "Project Saved!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error Adding Project to database!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error Creating Project!");
        }
    }//GEN-LAST:event_saveJButtonActionPerformed

    private void verifyJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyJButtonActionPerformed
        if(nameJText.getText().isEmpty() || txtdesc.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Invalid information");
        }else if(!this.copyProjectController.validateProjectNameAndDescription(nameJText.getText(), txtdesc.getText())){
            JOptionPane.showMessageDialog(this, "Project name already exists");
        }else{
             JOptionPane.showMessageDialog(this, "Project Validated, You can Save");
             saveJButton.setEnabled(true);
        }
    }//GEN-LAST:event_verifyJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameJText;
    private javax.swing.JButton saveJButton;
    private javax.swing.JTextArea txtdesc;
    private javax.swing.JButton verifyJButton;
    // End of variables declaration//GEN-END:variables
}
