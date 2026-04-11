/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal_admin;

import config.config;
import config.session;
import internal.admin;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.UIManager.getString;

/**
 *
 * @author James
 */
public class viewUsers extends javax.swing.JFrame {

    /**
     * Creates new form viewUsers
     */
    public int userId;  
    public viewUsers(int id) {
        initComponents();
        this.userId = id;
            loadUserData(this.userId);
    }
    
    private void loadUserData(int id) {
            try {
                Connection conn = config.connectDB();
                PreparedStatement pst = conn.prepareStatement(
                        "SELECT * FROM account WHERE a_id = ?"
                );
                pst.setInt(1, id);
                System.out.println(""+ id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {


                  
String fullName = rs.getString("a_fname") + " " + rs.getString("a_lname");
name.setText(fullName.trim());  
                    email.setText(rs.getString("a_email"));
                    user.setText(rs.getString("a_user"));
                    contact.setText("0"+rs.getString("a_contact"));
                    userType.setText(rs.getString("a_type"));
                    address.setText(rs.getString("a_address"));
                    status.setText(rs.getString("a_status"));

                    byte[] imgBytes = rs.getBytes("a_image");
                    if (imgBytes != null && image != null) {
                        ImageIcon limage = new ImageIcon(imgBytes);
                        Image scaledImage = limage.getImage().getScaledInstance(
                                image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH
                        );
                        image.setIcon(new ImageIcon(scaledImage));
                    } else if (image != null) {
                        image.setIcon(null);
                    }
                } else {
                    System.out.println("No user found with this ID: " + id);
                }

                rs.close();
                pst.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    public String destination = "";
    File selectedFile;
    public String oldpath;
    public String path;
    public String path2 = null;
    
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
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        user = new javax.swing.JLabel();
        userType = new javax.swing.JLabel();
        address = new javax.swing.JLabel();
        contact = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        email = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(45, 85, 125));
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

        jPanel4.setBackground(new java.awt.Color(47, 62, 80));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(210, 217, 226));
        jLabel2.setText("Account");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 13, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/left.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 12, 20, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 764, 40));

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(210, 217, 226));
        jLabel1.setText("User");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 60, -1, -1));

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(210, 217, 226));
        jLabel4.setText("Name");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 142, -1, -1));

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(210, 217, 226));
        jLabel6.setText("Contact Number");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 172, -1, -1));

        jLabel12.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(210, 217, 226));
        jLabel12.setText("User Type");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 238, -1, -1));

        jLabel11.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(210, 217, 226));
        jLabel11.setText("Address");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 204, -1, -1));

        jLabel7.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(210, 217, 226));
        jLabel7.setText("Status");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 270, -1, -1));

        jLabel8.setFont(new java.awt.Font("Georgia", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(210, 217, 226));
        jLabel8.setText("Email");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 100, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageMouseClicked(evt);
            }
        });
        jPanel2.add(image, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 14, 190, 154));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 60, 220, 182));

        status.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        status.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 264, 138, 28));

        user.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        user.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 62, 160, 34));

        userType.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        userType.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(userType, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 232, 138, 28));

        address.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        address.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(address, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 200, 136, 28));

        contact.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        contact.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 166, 136, 28));

        name.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        name.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 134, 134, 28));

        email.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        email.setForeground(new java.awt.Color(210, 217, 226));
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 100, 134, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        this.dispose();
        users u = new users();
        admin admin = new admin(u);
        admin.setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

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
            java.util.logging.Logger.getLogger(viewUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(viewUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(viewUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(viewUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (session.isInstanceEmpty()) {
                JOptionPane.showMessageDialog(null, "Unauthorized. Please log in.");
                new gui.signin().setVisible(true);
            } else {
             
                                int id = 0;
                new updateUser(id).setVisible(true);
                internal_admin.users u = new internal_admin.users();
                new admin(u).setVisible(true);
            }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel address;
    public javax.swing.JLabel contact;
    public javax.swing.JLabel email;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    public javax.swing.JLabel name;
    public javax.swing.JLabel status;
    public javax.swing.JLabel user;
    public javax.swing.JLabel userType;
    // End of variables declaration//GEN-END:variables
}
