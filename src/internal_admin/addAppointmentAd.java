/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal_admin;

import config.config;
import config.session;
import internal.admin;
import internal.client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author James
 */
public class addAppointmentAd extends javax.swing.JFrame {

    /**
     * Creates new form addAppointment
     */
    public addAppointmentAd() {
        initComponents();
        date();
        displayPets();
    }
    
    private void date(){
        Date date = new Date();
        ap_date.setDateFormatString("MMMM dd, yyyy");
        ap_date.setDate(date);
    }
    
    public void displayPets() {
        config db = new config();
        String sql = "SELECT p_id, owner_id, p_name, p_species, p_breed, p_dateBirth FROM pet";
        db.displayData(sql, petTable);
    }
    public boolean petExists(String petId) {
    try {
        Integer.parseInt(petId);
    } catch (NumberFormatException e) {
        return false; 
    }

    String query = "SELECT COUNT(*) FROM pet WHERE p_id = ?";
    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(query)) {
        
        pst.setString(1, petId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0; 
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}
    
     public boolean ownerExists(String ownerId) {
    try {
        Integer.parseInt(ownerId);
    } catch (NumberFormatException e) {
        return false; 
    }

    String query = "SELECT COUNT(*) FROM pet WHERE owner_id = ?";
    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(query)) {
        
        pst.setString(1, ownerId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0; 
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}
     
     public boolean validateTime(String apTime) {
    if (apTime == null || apTime.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Time cannot be empty");
        return false;
    }
    
    // Regex pattern for HH:MM AM/PM format (supports 1 or 2 digit hours)
    String timePattern = "^(0?[1-9]|1[0-2]):[0-5][0-9] (?i)(AM|PM)$";
    
    if (!apTime.matches(timePattern)) {
        JOptionPane.showMessageDialog(this, "Invalid format. Use HH:MM AM/PM (e.g., 9:30 AM)");
        return false;
    }
    
    try {
        // "h:mm a" handles "9:00 AM" and "11:00 AM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime time = LocalTime.parse(apTime.toUpperCase(), formatter);
        
        // Define Veterinary Clinic boundaries (8:00 AM to 5:00 PM)
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        
        LocalTime lunchStart = LocalTime.of(12, 0);
        LocalTime lunchEnd = LocalTime.of(13, 0);
        
        if (time.isBefore(startTime) || time.isAfter(endTime)) {
            JOptionPane.showMessageDialog(this, "Appointments must be between 8:00 AM and 5:00 PM.");
            return false;
        }
        
        if ((time.equals(lunchStart) || time.isAfter(lunchStart)) && time.isBefore(lunchEnd)) {
            JOptionPane.showMessageDialog(this, "The clinic is on lunch break from 12:00 PM to 1:00 PM.");
            return false;
        }
        
        return true; 
        
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this, "Could not parse time. Please check your entry.");
        return false;
    }
}
    
    private boolean isTimeSlotConflict(String apDate, String newTimeStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
    LocalTime newTime = LocalTime.parse(newTimeStr.toUpperCase(), formatter);
    
    // Define the buffer (1 hour before and 1 hour after)
    LocalTime bufferStart = newTime.minusMinutes(59);
    LocalTime bufferEnd = newTime.plusMinutes(59);

    String query = "SELECT ap_time FROM appointment WHERE ap_date = ? AND ap_status != 'Cancelled'";

    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(query)) {

        pst.setString(1, apDate);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String existingTimeStr = rs.getString("ap_time");
            try {
                LocalTime existingTime = LocalTime.parse(existingTimeStr.toUpperCase(), formatter);

                // If existing time falls within the 1-hour window of the new time
                // Example: Existing is 10:00 AM. New is 10:30 AM. 
                // 10:30 is between 9:01 and 10:59 -> CONFLICT.
                if (existingTime.isAfter(bufferStart) && existingTime.isBefore(bufferEnd)) {
                    JOptionPane.showMessageDialog(this, 
                        "Conflict: There is already an appointment at " + existingTimeStr + 
                        ".\nPlease allow at least 1 hour between appointments.");
                    return true; // Conflict found
                }
            } catch (DateTimeParseException e) {
                // Skip entries that don't match the format
                continue;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // No conflict
}
    

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        breed = new javax.swing.JLabel();
        dateBirth = new javax.swing.JLabel();
        ap_reasons = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        ap_date = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        ap_pet = new javax.swing.JTextField();
        ap_time = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        ap_notes = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        petTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(241, 243, 246));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(45, 85, 125));
        jPanel1.setToolTipText("");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 219, -1, -1));

        jPanel3.setBackground(new java.awt.Color(47, 62, 80));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(210, 217, 226));
        jLabel5.setText("Add");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 6, 54, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 160, 118, 28));

        jPanel4.setBackground(new java.awt.Color(47, 62, 80));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(210, 217, 226));
        jLabel2.setText("Appointment");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 13, 163, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/left.png"))); // NOI18N
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
        });
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 12, 22, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 40));

        name.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        name.setForeground(new java.awt.Color(210, 217, 226));
        name.setText("Reasons");
        jPanel1.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 84, 78, 28));

        breed.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        breed.setForeground(new java.awt.Color(210, 217, 226));
        breed.setText("Date ");
        jPanel1.add(breed, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 116, 50, 32));

        dateBirth.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        dateBirth.setForeground(new java.awt.Color(210, 217, 226));
        dateBirth.setText("Time");
        jPanel1.add(dateBirth, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 152, 80, 42));

        ap_reasons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ap_reasonsActionPerformed(evt);
            }
        });
        jPanel1.add(ap_reasons, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 86, 198, 28));

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(210, 217, 226));
        jLabel1.setText("Notes");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 56, 54, -1));
        jPanel1.add(ap_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 122, 198, 28));

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(210, 217, 226));
        jLabel4.setText("Pet");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 54, 32, 24));

        ap_pet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ap_petActionPerformed(evt);
            }
        });
        jPanel1.add(ap_pet, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 50, 198, 28));
        jPanel1.add(ap_time, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 160, 198, 26));

        ap_notes.setColumns(20);
        ap_notes.setRows(5);
        jScrollPane3.setViewportView(ap_notes);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 52, 198, 98));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 602, 196));

        jPanel2.setBackground(new java.awt.Color(241, 243, 246));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        petTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(petTable);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 32, 582, 232));

        jLabel6.setBackground(new java.awt.Color(47, 62, 80));
        jLabel6.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel6.setText("PETS & OWNER");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 6, 192, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 194, 602, 252));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ap_reasonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ap_reasonsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_reasonsActionPerformed

    private void ap_petActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ap_petActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_petActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        session sess = session.getInstance();

String apReasons = ap_reasons.getText().trim();
String apPet = ap_pet.getText().trim();
String apTime = ap_time.getText().trim();
String apNotes = ap_notes.getText().trim();

if(apPet.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Pet ID is required.");
    return;
}

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

String apDate = "";
if (ap_date.getDate() != null) {
    apDate = sdf.format(ap_date.getDate());
} else {
    JOptionPane.showMessageDialog(this, "Please select a date first!");
    return;
}

if(apReasons.isEmpty() || apTime.isEmpty() || apNotes.isEmpty()){
    JOptionPane.showMessageDialog(this, "Please fill in all fields");
    return;
}
if (!validateTime(apTime)) {
        ap_time.requestFocus(); // Focus back on time field for user correction
        return; 
    }
if (isTimeSlotConflict(apDate, apTime)) {
        ap_time.requestFocus();
        return; // Stop if there is a conflict
    }

config db = new config();

try (Connection conn = db.connectDB()) {

    // 🔹 Get owner automatically from pet table
    String owner = "";
    String getOwner = "SELECT owner_id FROM pet WHERE p_id = ?";
    PreparedStatement pst = conn.prepareStatement(getOwner);
    pst.setString(1, apPet);

    ResultSet rs = pst.executeQuery();

    if(rs.next()){
        owner = rs.getString("owner_id");

        // optional: display owner in textbox


    }else{
        JOptionPane.showMessageDialog(this,
            "Pet ID [" + apPet + "] does not exist.\nRegister the pet first.");
        ap_pet.requestFocus();
        return;
    }

    // 🔹 Insert appointment
    String sql = "INSERT INTO appointment (ap_petId, ap_clientId, ap_reasons, ap_date, ap_time, ap_notes, ap_status) VALUES (?, ?, ?, ?, ?, ?, ?)";

    PreparedStatement insert = conn.prepareStatement(sql);
    insert.setString(1, apPet);
    insert.setString(2, owner);
    insert.setString(3, apReasons);
    insert.setString(4, apDate);
    insert.setString(5, apTime);
    insert.setString(6, apNotes);
    insert.setString(7, "Pending");

    insert.executeUpdate();

    JOptionPane.showMessageDialog(this, "Appointment Added Successfully!");

    this.dispose();
    users u = new users();
    admin admin = new admin(u);
    admin.setVisible(true);

} catch (Exception e) {
    e.printStackTrace();
}      
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        this.dispose();
        users u = new users();
        admin admin = new admin(u);
        admin.setVisible(true);        
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(addAppointmentAd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addAppointmentAd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addAppointmentAd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addAppointmentAd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addAppointmentAd().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ap_date;
    private javax.swing.JTextArea ap_notes;
    public javax.swing.JTextField ap_pet;
    private javax.swing.JTextField ap_reasons;
    public javax.swing.JTextField ap_time;
    private javax.swing.JLabel breed;
    private javax.swing.JLabel dateBirth;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel name;
    private javax.swing.JTable petTable;
    // End of variables declaration//GEN-END:variables
}
