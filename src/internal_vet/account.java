/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal_vet;

import config.config;
import config.session;
import config.singleton;
import gui.signin;
import internal.vet;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author James
 */
public class account extends javax.swing.JInternalFrame {

    /**
     * Creates new form account
     */
    public account() {
        initComponents();
        SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            displayCurrentUser();
        }
    });

    this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
    BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
    bi.setNorthPane(null);
    }
    
    Color navcolor = new Color(21,41,62);
    Color bodycolor = new Color(45, 85, 125);
    
    public String destination = "";
    File selectedFile;
    public String oldpath;
    public String path;
    
    public void imageUpdater(String existingFilePath, String newFilePath){
        File existingFile = new File(existingFilePath);
        if (existingFile.exists()) {
            String parentDirectory = existingFile.getParent();
            File newFile = new File(newFilePath);
            String newFileName = newFile.getName();
            File updatedFile = new File(parentDirectory, newFileName);
            existingFile.delete();
            try {
                Files.copy(newFile.toPath(), updatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image updated successfully.");
            } catch (IOException e) {
                System.out.println("Error occurred while updating the image: "+e);
            }
        } else {
            try{
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException e){
                System.out.println("Error on update!");
            }
        }
   }
    
        public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
           
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            
        
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            
          
            int newHeight = (int) ((double) desiredWidth / originalWidth * originalHeight);
            
            return newHeight;
        } catch (IOException ex) {
            System.out.println("No image found!");
        }
        
        return -1;
    }
    
    
    public  ImageIcon ResizeImage(String ImagePath, byte[] pic, JLabel label) {
        ImageIcon MyImage = null;
            if(ImagePath !=null){
                MyImage = new ImageIcon(ImagePath);
            }else{
                MyImage = new ImageIcon(pic);
            }

        int newHeight = getHeightFromWidth(ImagePath, label.getWidth());

        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), newHeight, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    public int FileExistenceChecker(String path){
            File file = new File(path);
            String fileName = file.getName();

            Path filePath = Paths.get("src/images", fileName);
            boolean fileExists = Files.exists(filePath);

            if (fileExists) {
                return 1;
            } else {
                return 0;
            }

        }

    
    public void displayCurrentUser() {

    session sess = session.getInstance();

    if (sess == null) {
        System.out.println("No user logged in.");
        return;
    }
    config db = new config();
        
        try(Connection con = db.connectDB()){
            String sql = "SELECT * FROM account WHERE a_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setInt(1, sess.getId());
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                sess.setUsername(rs.getString("a_user"));
                sess.setEmail(rs.getString("a_email"));
                sess.setFname(rs.getString("a_fname"));
                sess.setLname(rs.getString("a_lname"));
                sess.setContact(rs.getString("a_contact"));
                sess.setAddress(rs.getString("a_address"));

                byte[] imgBytes = rs.getBytes("a_image");

            if (imgBytes != null && imgBytes.length > 0) {
                ImageIcon imageIcon = new ImageIcon(imgBytes);
                Image img = imageIcon.getImage();
                Image scaledImg = img.getScaledInstance(
                        image.getWidth(),
                        image.getHeight(),
                        Image.SCALE_SMOOTH
                );
                image.setIcon(new ImageIcon(scaledImg));
            } else {
                image.setIcon(null);
            }
                
            }

    user.setText("<html><b>User</b><br><span style='font-weight:normal;'>"
        + (sess.getUsername() != null ? sess.getUsername() : "")
        + "</span></html>");

    email.setText("<html><b>Email</b><br><span style='font-weight:normal;'>" 
        + (sess.getEmail() != null ? sess.getEmail() : "")
        + "</span></html>");

    String fullName = 
    (sess.getFname() != null ? sess.getFname() : "") + " " +
    (sess.getLname() != null ? sess.getLname() : "");

    fullname.setText("<html><b>Full Name</b><br><span style='font-weight:normal;'>"
        + fullName.trim()
        + "</span></html>");


    contact.setText("<html><b>Contact</b><br><span style='font-weight:normal; font-size: 12px; font-family:Arial;'>"
            + (sess.getContact() != null ? sess.getContact().trim() : "")
            + "</span></html>");
    address.setText("<html><b>Address</b><br><span style='font-weight:normal;'>"
            + (sess.getAddress() != null ? sess.getAddress() : "")
            + "</span></html>");


}       catch (SQLException ex) {
            Logger.getLogger(account.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        managepane = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fullname = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        address = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        user = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        contact = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(241, 243, 246));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 219, -1, -1));

        jPanel7.setBackground(new java.awt.Color(47, 62, 80));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(210, 217, 226));
        jLabel4.setText("Account");
        jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 50));

        jPanel4.setBackground(new java.awt.Color(45, 85, 125));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(21, 41, 62));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(210, 217, 226));
        jLabel1.setText("Personal Information");
        jPanel9.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 4, -1, 20));

        managepane.setBackground(new java.awt.Color(21, 41, 62));
        managepane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                managepaneMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                managepaneMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                managepaneMouseExited(evt);
            }
        });
        managepane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(210, 217, 226));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Manage");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel14MouseExited(evt);
            }
        });
        managepane.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, -6, -1, 42));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/right.png"))); // NOI18N
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel15MouseExited(evt);
            }
        });
        managepane.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, -6, -1, 42));

        jPanel9.add(managepane, new org.netbeans.lib.awtextra.AbsoluteConstraints(278, 0, 102, 28));

        jPanel4.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 378, 28));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wname.png"))); // NOI18N
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 34, 40, -1));

        fullname.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        fullname.setForeground(new java.awt.Color(210, 217, 226));
        fullname.setText("Full Name");
        jPanel4.add(fullname, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 328, 54));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 380, 80));

        jPanel11.setBackground(new java.awt.Color(45, 85, 125));
        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wadd.png"))); // NOI18N
        jPanel11.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 3, 28, 32));

        address.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        address.setForeground(new java.awt.Color(210, 217, 226));
        address.setText("Address");
        jPanel11.add(address, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 2, 328, 58));

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 380, 52));

        jPanel13.setBackground(new java.awt.Color(45, 85, 125));
        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wemail.png"))); // NOI18N
        jPanel13.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 3, 30, 34));

        email.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        email.setForeground(new java.awt.Color(210, 217, 226));
        email.setText("Email");
        jPanel13.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, -8, 300, 76));

        jPanel1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 380, 52));

        jPanel14.setBackground(new java.awt.Color(45, 85, 125));
        jPanel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wh.png"))); // NOI18N
        jPanel14.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 4, 30, -1));

        user.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        user.setForeground(new java.awt.Color(210, 217, 226));
        user.setText("User");
        jPanel14.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, -8, 322, 74));

        jPanel1.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 256, 380, 52));

        jPanel12.setBackground(new java.awt.Color(45, 85, 125));
        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wcon.png"))); // NOI18N
        jPanel12.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 6, -1, -1));

        contact.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        contact.setForeground(new java.awt.Color(210, 217, 226));
        contact.setText("Contact Number");
        jPanel12.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 0, 150, 58));

        jPanel1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 312, 380, 50));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageMouseClicked(evt);
            }
        });
        jPanel5.add(image, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 34, 120, 120));

        jPanel10.setBackground(new java.awt.Color(47, 62, 80));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(210, 217, 226));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("       Profile Picture ");
        jPanel10.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(-18, 4, 158, -1));

        jPanel5.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 156, 28));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 62, 156, 166));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 499));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        manage add = new manage();
        add.setVisible(true);
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.dispose();
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseEntered
        managepane.setBackground(bodycolor);
    }//GEN-LAST:event_jLabel14MouseEntered

    private void jLabel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseExited
        managepane.setBackground(navcolor);
    }//GEN-LAST:event_jLabel14MouseExited

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        manage add = new manage();
        add.setVisible(true);
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseEntered
        managepane.setBackground(bodycolor);
    }//GEN-LAST:event_jLabel15MouseEntered

    private void jLabel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseExited
        managepane.setBackground(navcolor);
    }//GEN-LAST:event_jLabel15MouseExited

    private void managepaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_managepaneMouseClicked
        manage add = new manage();
        add.setVisible(true);
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.dispose();
    }//GEN-LAST:event_managepaneMouseClicked

    private void managepaneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_managepaneMouseEntered
        managepane.setBackground(bodycolor);
    }//GEN-LAST:event_managepaneMouseEntered

    private void managepaneMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_managepaneMouseExited
        managepane.setBackground(navcolor);
    }//GEN-LAST:event_managepaneMouseExited

    private void imageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageMouseClicked

    }//GEN-LAST:event_imageMouseClicked

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
            java.util.logging.Logger.getLogger(account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (session.isInstanceEmpty()) {
                    JOptionPane.showMessageDialog(null, "Unauthorized. Please log in.");
                    new gui.signin().setVisible(true);
                } else {

                    internal_vet.account vet = new internal_vet.account();
                    new vet().setVisible(true);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel address;
    private javax.swing.JLabel contact;
    private javax.swing.JLabel email;
    private javax.swing.JLabel fullname;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel managepane;
    private javax.swing.JLabel user;
    // End of variables declaration//GEN-END:variables
}
