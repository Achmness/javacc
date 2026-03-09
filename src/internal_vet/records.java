/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal_vet;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import config.config;
import config.session;
import internal.vet;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.RowFilter;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author James
 */
public class records extends javax.swing.JInternalFrame {

    private String recordsText;

    /**
     * Creates new form records
     */
    records(String receiptText) {
            this.recordsText = receiptText;
        }
    public records() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
        bi.setNorthPane(null);
        displayRecords();
        searchTable();
    }
    public String filePath;
    File document;
    public void displayRecords() {
        config db = new config();
        String sql = "SELECT r.r_id, r.ap_id, ap.ap_clientId, p.p_id, r.r_diagnosis, r.r_instructions, r.r_prescription, r.r_notes " +
                    "FROM records r " +
                    "INNER JOIN appointment ap ON r.ap_id = ap.ap_id " +
          "INNER JOIN account ac ON ap.ap_clientId = ac.a_id " + 
                 "LEFT JOIN pet p ON ap.ap_petId = p.p_id";
                    
        
                    
        
        db.displayData(sql, recordsTable);
    }
    
    
    public void searchTable(){
       DefaultTableModel model =  (DefaultTableModel)recordsTable.getModel();
       TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
       recordsTable.setRowSorter(tr);
       tr.setRowFilter(RowFilter.regexFilter(search.getText().trim()));
    }
   private void showAndPrintMedicalRecord(String p_name, String p_species, String p_dateBirth,
                                        String a_fname, String a_lname,
                                        String vet_fname, String vet_lname,
                                        String prescription, String instructions,
                                        String diagnosis, String notes) {

    String recordsText = buildMedicalRecord(
            p_name, p_species, p_dateBirth,
            a_fname, a_lname,
            vet_fname, vet_lname,
            prescription, instructions,
            diagnosis, notes
    );

    JTextArea area = new JTextArea(recordsText, 30, 60);
    area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
    area.setEditable(false);

    JScrollPane scroll = new JScrollPane(area);

    JDialog dialog;
dialog = new JDialog((Frame) null, "Medical Record", false);
dialog.add(scroll);
dialog.pack();
dialog.setLocationRelativeTo(null);
dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
dialog.setVisible(true);

    // Automatically print medical record
    try {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((Printable) new records(recordsText));
        job.print();
    } catch (PrinterException e) {
        JOptionPane.showMessageDialog(this, "Could not print medical record: " + e.getMessage());
    }
}
    

    
private String buildMedicalRecord(String p_name, String p_species, String p_dateBirth,
                                  String a_fname, String a_lname,
                                  String vet_fname, String vet_lname,
                                  String prescription, String instructions,
                                  String diagnosis, String notes) {
    String sql = "SELECT a.a_id, a.a_fname, a_";

    String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String owner = a_fname + " " + a_lname;
    String vet = vet_fname + " " + vet_lname;

    StringBuilder sb = new StringBuilder();

    sb.append("============================================================\n");
    sb.append("                   VETERINARY MEDICAL RECORD\n");
    sb.append("============================================================\n");

    // TABLE HEADER
    sb.append(String.format("| %-18s | %-18s | %-18s |\n", "Patient", "Species", "Date Birth"));
    sb.append("------------------------------------------------------------\n");
    sb.append(String.format("| %-18s | %-18s | %-18s |\n", p_name, p_species, p_dateBirth));
    sb.append("------------------------------------------------------------\n");

    sb.append(String.format("| %-18s | %-18s | %-18s |\n", "Owner", "Date", "Attending Vet"));
    sb.append("------------------------------------------------------------\n");
    sb.append(String.format("| %-18s | %-18s | %-18s |\n", owner, dateStr, vet));
    sb.append("============================================================\n\n");


    // PRESCRIPTION
    sb.append("PRESCRIPTION:\n");
    sb.append(prescription).append("\n\n");

    // INSTRUCTIONS
    sb.append("INSTRUCTIONS:\n");
    sb.append(instructions).append("\n\n");

    // DIAGNOSIS
    sb.append("DIAGNOSIS:\n");
    sb.append(diagnosis).append("\n\n");

    // NOTES
    sb.append("NOTES:\n");
    sb.append(notes).append("\n\n");


    // VET SIGNATURE RIGHT SIDE
    sb.append(String.format("%50s\n", vet));
    sb.append(String.format("%50s\n", "Attending Veterinarian"));

    sb.append("============================================================\n");

    return sb.toString();
}

// You need to add this method to your class
private PdfPCell getCell(String text, int alignment, boolean isBold) {
    Font font;
    if (isBold) {
        font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    } else {
        font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    }
    
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setPadding(5);
    cell.setHorizontalAlignment(alignment);
    cell.setBorder(PdfPCell.NO_BORDER); // Common in these types of forms
    return cell;
}

    
    Color navcolor = new Color(45, 85, 125);
    Color bodycolor = new Color(21,41,62);

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        a_updatepane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        a_approvepane = new javax.swing.JPanel();
        approve = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recordsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(241, 243, 246));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        a_updatepane.setBackground(new java.awt.Color(21, 41, 62));
        a_updatepane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        a_updatepane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                a_updatepaneMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                a_updatepaneMouseExited(evt);
            }
        });
        a_updatepane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(210, 217, 226));
        jLabel1.setText("UPDATE");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });
        a_updatepane.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 8, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wup.png"))); // NOI18N
        a_updatepane.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 4, -1, -1));

        jPanel1.add(a_updatepane, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 56, 88, 30));

        a_approvepane.setBackground(new java.awt.Color(21, 41, 62));
        a_approvepane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        a_approvepane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                a_approvepaneMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                a_approvepaneMouseExited(evt);
            }
        });
        a_approvepane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        approve.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        approve.setForeground(new java.awt.Color(210, 217, 226));
        approve.setText("ADD");
        approve.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                approveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                approveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                approveMouseExited(evt);
            }
        });
        a_approvepane.add(approve, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 4, 40, 26));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wadds.png"))); // NOI18N
        a_approvepane.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 4, -1, -1));

        jPanel1.add(a_approvepane, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 58, 86, 30));

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

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(210, 217, 226));
        jLabel2.setText("Medical Records");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addContainerGap(415, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 50));

        search.setBackground(new java.awt.Color(241, 243, 246));
        search.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchKeyTyped(evt);
            }
        });
        jPanel1.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(382, 58, 170, 30));

        jPanel7.setBackground(new java.awt.Color(214, 206, 160));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setText("Users");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addContainerGap(514, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 50));

        jPanel8.setBackground(new java.awt.Color(214, 206, 160));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel4.setText("Account");
        jPanel8.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jPanel9.setBackground(new java.awt.Color(190, 176, 112));

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Sign out");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, 30));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 50));

        jPanel2.setBackground(new java.awt.Color(21, 41, 62));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(210, 217, 226));
        jLabel6.setText("PRINT");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 4, -1, 22));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/prin.png"))); // NOI18N
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 4, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 56, 86, 30));

        recordsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(recordsTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(62, 120, -1, 310));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        a_updatepane.setBackground(bodycolor);
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        a_updatepane.setBackground(navcolor);
    }//GEN-LAST:event_jLabel1MouseExited

    private void a_updatepaneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_a_updatepaneMouseEntered
        a_updatepane.setBackground(bodycolor);
    }//GEN-LAST:event_a_updatepaneMouseEntered

    private void a_updatepaneMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_a_updatepaneMouseExited
        a_updatepane.setBackground(navcolor);
    }//GEN-LAST:event_a_updatepaneMouseExited

    private void approveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_approveMouseClicked
        this.dispose();
        addRecords addRecords = new addRecords();
            addRecords.setVisible(true);
    }//GEN-LAST:event_approveMouseClicked

    private void approveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_approveMouseEntered
        a_approvepane.setBackground(bodycolor);
    }//GEN-LAST:event_approveMouseEntered

    private void approveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_approveMouseExited
        a_approvepane.setBackground(navcolor);
    }//GEN-LAST:event_approveMouseExited

    private void a_approvepaneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_a_approvepaneMouseEntered
        a_approvepane.setBackground(bodycolor);
    }//GEN-LAST:event_a_approvepaneMouseEntered

    private void a_approvepaneMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_a_approvepaneMouseExited
        a_approvepane.setBackground(navcolor);
    }//GEN-LAST:event_a_approvepaneMouseExited

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        // TODO addPet your handling code here:
    }//GEN-LAST:event_searchActionPerformed

    private void searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyTyped
        searchTable();
    }//GEN-LAST:event_searchKeyTyped

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked

    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
int row = recordsTable.getSelectedRow();

if (row == -1) {
    JOptionPane.showMessageDialog(this, "Select a record first.");
    return;
}

String r_id = recordsTable.getValueAt(row, 0).toString();

try {

    config db = new config();

String sql = "SELECT r.*, p.p_name, p.p_species, p.p_dateBirth, " +
             "ac.a_fname, ac.a_lname, " + 
             "v.a_fname AS vet_fname, v.a_lname AS vet_lname " + 
             "FROM records r " +
             "LEFT JOIN appointment ap ON r.ap_id = ap.ap_id " +
             "LEFT JOIN pet p ON ap.ap_petId = p.p_id " +
             "LEFT JOIN account ac ON ap.ap_clientId = ac.a_id " +
             "LEFT JOIN account v ON ap.ap_vetId = v.a_id " + 
             "WHERE r.r_id = ?";

    Connection conn = db.connectDB();
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, r_id);

    ResultSet rs = pst.executeQuery();

    if (rs.next()) {

        // Ask where to save PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("Prescription.pdf"));
        int option = fileChooser.showSaveDialog(this);
        if(option != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        String filePath = file.getAbsolutePath();

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
        Font smallFont = new Font(Font.FontFamily.HELVETICA, 9);

        // Clinic Header
        Paragraph clinic = new Paragraph("PET CARE & VETERINARY", headerFont);
        clinic.setAlignment(Element.ALIGN_LEFT);
        document.add(clinic);

        Paragraph address = new Paragraph("Brgy. Tuyan, Sitio Botong, Naga, Cebu 6037", smallFont);
        document.add(address);

        document.add(new Paragraph(" "));

        Paragraph title = new Paragraph("PRESCRIPTION SLIP", headerFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        // Patient Info Table
        PdfPTable infoTable = new PdfPTable(4);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new int[]{2,4,2,4});

        infoTable.addCell(getCell("Patient:", PdfPCell.ALIGN_LEFT, true));
        infoTable.addCell(getCell(rs.getString("p_name"), PdfPCell.ALIGN_LEFT, false));

        infoTable.addCell(getCell("Owner:", PdfPCell.ALIGN_LEFT, true));
        infoTable.addCell(getCell(rs.getString("a_fname")+" "+rs.getString("a_lname"), PdfPCell.ALIGN_LEFT, false));

        infoTable.addCell(getCell("Species:", PdfPCell.ALIGN_LEFT, true));
        infoTable.addCell(getCell(rs.getString("p_species"), PdfPCell.ALIGN_LEFT, false));
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = sdf.format(new java.util.Date());
        infoTable.addCell(getCell("Date:", PdfPCell.ALIGN_LEFT, true));
        infoTable.addCell(getCell(formattedDate, PdfPCell.ALIGN_LEFT, false));
        
        infoTable.addCell(getCell("D.O.B/Age:", PdfPCell.ALIGN_LEFT, true));
        infoTable.addCell(getCell(rs.getString("p_dateBirth"), PdfPCell.ALIGN_LEFT, false));

    
        infoTable.addCell(getCell("Attending Veterinarian", PdfPCell.ALIGN_LEFT, true));
        String vName = rs.getString("vet_fname");
        String vLname = rs.getString("vet_lname");
        String fullVetName =  "Dr. " + (vName != null ? vName : "") + " " + (vLname != null ? vLname : "");
        infoTable.addCell(getCell(fullVetName.trim().isEmpty() ? "None Assigned" : fullVetName, PdfPCell.ALIGN_LEFT, false));

        document.add(infoTable);
        document.add(new Paragraph(" "));

        Font rxFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        document.add(new Paragraph("Rx", rxFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Prescription
        document.add(new Paragraph("        " + rs.getString("r_prescription"), normalFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        

        // Diagnosis and Instructions
        document.add(new Paragraph("                "+ rs.getString("r_instructions"), normalFont));
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        
        
        document.add(new Paragraph("Diagnosis: " + rs.getString("r_diagnosis"), normalFont));
        
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("Notes       : " + rs.getString("r_notes"), normalFont));

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Vet Signature
        Paragraph signature = new Paragraph(
                "Dr. " + rs.getString("vet_fname")+" "+rs.getString("vet_lname") + "\nVeterinarian",
                normalFont
        );
        signature.setAlignment(Element.ALIGN_RIGHT);
        document.add(signature);

        document.close();

        JOptionPane.showMessageDialog(this,"Prescription PDF Generated Successfully!");

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        }
    } else {
    JOptionPane.showMessageDialog(this, "No record details found in the database for ID: " + r_id);
}

} catch(Exception e){
    e.printStackTrace();
}
    }//GEN-LAST:event_jLabel6MouseClicked

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
            java.util.logging.Logger.getLogger(records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JPanel a_approvepane;
    private javax.swing.JPanel a_updatepane;
    private javax.swing.JLabel approve;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable recordsTable;
    private javax.swing.JTextField search;
    // End of variables declaration//GEN-END:variables
}
