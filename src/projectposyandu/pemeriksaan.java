/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projectposyandu;
import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import koneksi.koneksi;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author setob
 */
public class pemeriksaan extends javax.swing.JPanel {
    
    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
   
    public pemeriksaan() {
        initComponents();
        datatable();
        kosong();
        aktif();
        bt_hapusdata.setEnabled(false); 
        bt_ubahdata.setEnabled(false);
        bt_cetak.setEnabled(false);
        dataFromDBtoCB();
        dataFromDBtoCB1();
        dataFromDBtoCBbidan();
        AutoCompleteDecorator.decorate(cb_jenis);

        jPanel3.setVisible(false);
    }
    ArrayList<String> obat = new ArrayList<String>(); 
    int totalharga = 0;
      void openpdf(String filePath){
          try {
           SwingController control=new SwingController();
            SwingViewBuilder factry=new SwingViewBuilder(control);
            JPanel viewerCompntpnl=factry.buildViewerPanel();
            ComponentKeyBinding.install(control, viewerCompntpnl);
            control.getDocumentViewController().setAnnotationCallback(
            new org.icepdf.ri.common.MyAnnotationCallback(
                    control.getDocumentViewController()));
                   control.openDocument(filePath);
        jScrollPane1.setViewportView(viewerCompntpnl); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Cannot Load Pdf");
        }
}       

    
     protected void aktif(){
        //k_id.requestFocus();
    }
      protected void kosong(){
        tpemeriksaan.setText("");
        cb_bidan.setSelectedItem(null);
        tbalita.setText("");
        tglpemeriksaan.setCalendar(null);
        tpembayaran.setText("");
        tketerangan.setText("");
          setBackground(new Color(0,0,0,0));
         DefaultTableCellRenderer head_render = new DefaultTableCellRenderer(); 
        head_render.setBackground(new Color(32, 136, 203));
        table_pemeriksaan.getTableHeader().setOpaque(false);
        table_pemeriksaan.getTableHeader().setDefaultRenderer(head_render);
        table_pemeriksaan.getTableHeader().setForeground(new Color(255,255,255));
        table_pemeriksaan.setRowHeight(25);
        k_zscore.setText("");
        k_tabel1sd.setText("");
        k_median.setText("");
        k_tinggi.setText("");
        k_berat.setText("");
        k_umur.setText("");
        status_panjang.setText("");
        status_berat.setText("");
        status_gizi.setText("");
        obat.clear();
        totalharga = 0;
    }
      protected void datatable(){
        Object[] Baris = {"ID Pemeriksaan", "Jenis Pemeriksaan", "ID Balita", "Nama Bidan", "Kode Pembayaran", "Tanggal Pemeriksaan","Umur(Bulan)","Tinggi Badan(cm)","Berat Badan(kg)", "Status Panjang/Tinggi", "Status Gizi", "Status Berat","Obat/Vitamin", "Keterangan"};
        tabmode = new DefaultTableModel (null, Baris);
        
        String cari = tcari_bidan.getText();
        try {
            String sql = "SELECT * FROM pemeriksaan where kdpemeriksaan like '%"+cari+"%' or jenpemeriksaan like '%"+cari+"%' order by kdpemeriksaan asc";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String sqlperiksa = "SELECT * from jenis_pemeriksaan where id_pemeriksaan='" + hasil.getString(2) + "'";
                stat = conn.createStatement();
                ResultSet result = stat.executeQuery(sqlperiksa);
                while (result.next()) {
                tabmode.addRow(new Object[]{
                    hasil.getString(1),
                    result.getString("jenis_pemeriksaan"),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                    hasil.getString(7),
                    hasil.getString(9),
                    hasil.getString(10),
                    hasil.getString(11),
                    hasil.getString(12),
                    hasil.getString(13),
                    hasil.getString(14),
                    hasil.getString(15),
                    hasil.getString(6),
                    
         
                });
                System.out.println(result.getString("id_pemeriksaan"));
                }
            }
            table_pemeriksaan.setModel(tabmode); 
        }catch (Exception e){
             JOptionPane.showMessageDialog(null,"datatable gagal "+e);
           }
        
         }
      
      public void dataFromDBtoCB(){
        try {
            String sql  = "SELECT * FROM jenis_pemeriksaan";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            
            while (hasil.next()) {                
                cb_jenis.addItem(hasil.getString("id_pemeriksaan") + " - " + hasil.getString("jenis_pemeriksaan"));
            }
            
            hasil.last();
            int jumlahdata = hasil.getRow();
            hasil.first();
            
        } catch (SQLException e) {
        }
    }
      public void dataFromDBtoCB1(){
        try {
            String sql  = "SELECT * FROM obat";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            
            while (hasil.next()) {                
                cb_obat.addItem(hasil.getString("nama") +  " - " + hasil.getString("harga") );
            }
            
            hasil.last();
            int jumlahdata = hasil.getRow();
            hasil.first();
            
        } catch (SQLException e) {
        }
    }
      
      
        public void dataFromDBtoCBbidan(){
        try {
            String sql  = "SELECT * FROM bidan";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            
            while (hasil.next()) {                
                cb_bidan.addItem(hasil.getString("nama"));
            }
            
            hasil.last();
            int jumlahdata = hasil.getRow();
            hasil.first();
            
        } catch (SQLException e) {
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        mainpanel = new javax.swing.JPanel();
        dataPemeriksaan = new javax.swing.JPanel();
        tcari_bidan = new javax.swing.JTextField();
        label_bidan = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_pemeriksaan = new javax.swing.JTable();
        bcari_bidan = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_ubahdata = new javax.swing.JButton();
        bt_hapusdata = new javax.swing.JButton();
        bt_tambahdata = new javax.swing.JButton();
        bt_cetak = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tambahPemeriksaan = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        k_zscore = new javax.swing.JFormattedTextField();
        k_median = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        bt_cekpanjang = new javax.swing.JButton();
        k_tabel1sd = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        bt_cekgizi = new javax.swing.JButton();
        bt_cekberat = new javax.swing.JButton();
        panduan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        k_berat = new javax.swing.JFormattedTextField();
        k_tinggi = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cek_berat = new javax.swing.JButton();
        cek_panjang = new javax.swing.JButton();
        cek_gizi = new javax.swing.JButton();
        status_gizi = new javax.swing.JTextField();
        status_panjang = new javax.swing.JTextField();
        status_berat = new javax.swing.JTextField();
        k_umur = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cb_obat = new javax.swing.JComboBox<>();
        k_total = new javax.swing.JFormattedTextField();
        k_jumlah = new javax.swing.JFormattedTextField();
        k_harga = new javax.swing.JFormattedTextField();
        k_hitung = new javax.swing.JButton();
        tambah_obat = new javax.swing.JButton();
        lbidan = new javax.swing.JLabel();
        lbidan_id = new javax.swing.JLabel();
        lbidan_nik = new javax.swing.JLabel();
        cb_jenis = new javax.swing.JComboBox<>();
        lbidan_nama = new javax.swing.JLabel();
        lbidan_ttl = new javax.swing.JLabel();
        lbidan_telp = new javax.swing.JLabel();
        lbidan_status = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        bt_batal = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        tketerangan = new javax.swing.JTextField();
        lbidan_status1 = new javax.swing.JLabel();
        tglpemeriksaan = new com.toedter.calendar.JDateChooser();
        tbalita = new javax.swing.JFormattedTextField();
        tpembayaran = new javax.swing.JFormattedTextField();
        tpemeriksaan = new javax.swing.JFormattedTextField();
        btn_cek = new javax.swing.JButton();
        cb_bidan = new javax.swing.JComboBox<>();
        k_obat = new javax.swing.JTextField();
        lbidan_telp1 = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.CardLayout());

        mainpanel.setLayout(new java.awt.CardLayout());

        dataPemeriksaan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tcari_bidan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tcari_bidan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tcari_bidanActionPerformed(evt);
            }
        });
        tcari_bidan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tcari_bidanKeyReleased(evt);
            }
        });
        dataPemeriksaan.add(tcari_bidan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 294, 36));

        label_bidan.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        label_bidan.setText("DATA PEMERIKSAAN");
        dataPemeriksaan.add(label_bidan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 280, 46));

        table_pemeriksaan.setAutoCreateRowSorter(true);
        table_pemeriksaan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        table_pemeriksaan.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        table_pemeriksaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NIK", "NAMA", "TANGGAL LAHIR", "ALAMAT", "NO TELEPON"
            }
        ));
        table_pemeriksaan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table_pemeriksaan.setIntercellSpacing(new java.awt.Dimension(5, 10));
        table_pemeriksaan.setRowHeight(25);
        table_pemeriksaan.getTableHeader().setReorderingAllowed(false);
        table_pemeriksaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_pemeriksaanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_pemeriksaan);

        dataPemeriksaan.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 1640, 530));

        bcari_bidan.setBackground(new java.awt.Color(242, 242, 242));
        bcari_bidan.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        bcari_bidan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/pngtree-search-flat-yellow-color-icon-seo-find-research-photo-image_19943406-removebg-preview (1).png"))); // NOI18N
        bcari_bidan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        bcari_bidan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcari_bidanActionPerformed(evt);
            }
        });
        dataPemeriksaan.add(bcari_bidan, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 40, 36));

        bt_hapus.setText("Hapus Data");
        bt_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_hapusActionPerformed(evt);
            }
        });
        dataPemeriksaan.add(bt_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 1917, 172, 42));

        bt_ubahdata.setBackground(new java.awt.Color(255, 193, 7));
        bt_ubahdata.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        bt_ubahdata.setForeground(new java.awt.Color(255, 255, 255));
        bt_ubahdata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/282091.png"))); // NOI18N
        bt_ubahdata.setText("Ubah Data");
        bt_ubahdata.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        bt_ubahdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_ubahdataActionPerformed(evt);
            }
        });
        dataPemeriksaan.add(bt_ubahdata, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 690, 180, 40));

        bt_hapusdata.setBackground(new java.awt.Color(255, 0, 51));
        bt_hapusdata.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        bt_hapusdata.setForeground(new java.awt.Color(255, 255, 255));
        bt_hapusdata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/images__1_-removebg-preview.png"))); // NOI18N
        bt_hapusdata.setText("Hapus Data");
        bt_hapusdata.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        bt_hapusdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_hapusdataActionPerformed(evt);
            }
        });
        dataPemeriksaan.add(bt_hapusdata, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 690, 170, 40));

        bt_tambahdata.setBackground(new java.awt.Color(7, 155, 7));
        bt_tambahdata.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        bt_tambahdata.setForeground(new java.awt.Color(255, 255, 255));
        bt_tambahdata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/new_database-5122.png"))); // NOI18N
        bt_tambahdata.setText("Tambah Data");
        bt_tambahdata.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        bt_tambahdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tambahdataActionPerformed(evt);
            }
        });
        dataPemeriksaan.add(bt_tambahdata, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 690, 170, 40));

        bt_cetak.setBackground(new java.awt.Color(255, 204, 51));
        bt_cetak.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        bt_cetak.setForeground(new java.awt.Color(255, 255, 255));
        bt_cetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/3616288-removebg-preview (1).png"))); // NOI18N
        bt_cetak.setText("Cetak ");
        bt_cetak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        bt_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cetakActionPerformed(evt);
            }
        });
        dataPemeriksaan.add(bt_cetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 690, 170, 40));
        dataPemeriksaan.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1830, 970));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ombre-7419016_1280 (1).jpg"))); // NOI18N
        jLabel14.setText("l");
        dataPemeriksaan.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 1920, 1380));

        mainpanel.add(dataPemeriksaan, "card2");

        tambahPemeriksaan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(k_zscore, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 120, -1));
        jPanel3.add(k_median, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 120, -1));
        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 830, 580));

        bt_cekpanjang.setText("CEK");
        bt_cekpanjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cekpanjangActionPerformed(evt);
            }
        });
        jPanel3.add(bt_cekpanjang, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 80, -1));
        jPanel3.add(k_tabel1sd, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 120, -1));

        jLabel8.setText("Median");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, -1, -1));

        jLabel9.setText("Tabel 1SD");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));

        jLabel10.setText("Z-Score");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, 20));

        jLabel11.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel11.setText("TABEL STANDAR ANTROPOMETRI ANAK");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, -1, -1));

        bt_cekgizi.setText("CEK");
        bt_cekgizi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cekgiziActionPerformed(evt);
            }
        });
        jPanel3.add(bt_cekgizi, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, -1, -1));

        bt_cekberat.setText("CEK");
        bt_cekberat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cekberatActionPerformed(evt);
            }
        });
        jPanel3.add(bt_cekberat, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, -1, -1));

        panduan.setText("Panduan");
        panduan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panduanActionPerformed(evt);
            }
        });
        jPanel3.add(panduan, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, -1, -1));

        jPanel5.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-70, -10, 1150, 810));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Pemeriksaan");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 18, -1, -1));
        jPanel2.add(k_berat, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 110, -1));
        jPanel2.add(k_tinggi, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 110, -1));

        jLabel2.setText("Berat");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel3.setText("Tinggi");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel5.setText("Status Gizi");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        jLabel6.setText("Status Panjang");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        jLabel7.setText("Status Berat");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        cek_berat.setText("Cek Status Berat");
        cek_berat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cek_beratActionPerformed(evt);
            }
        });
        jPanel2.add(cek_berat, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 130, -1));

        cek_panjang.setText("Cek Status Panjang");
        cek_panjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cek_panjangActionPerformed(evt);
            }
        });
        jPanel2.add(cek_panjang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, -1, -1));

        cek_gizi.setText("Cek Status Gizi");
        cek_gizi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cek_giziActionPerformed(evt);
            }
        });
        jPanel2.add(cek_gizi, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 260, 130, -1));
        jPanel2.add(status_gizi, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 90, -1));
        jPanel2.add(status_panjang, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 90, -1));
        jPanel2.add(status_berat, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 90, -1));
        jPanel2.add(k_umur, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 110, -1));

        jLabel4.setText("Umur");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel13.setText("Keterangan");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel20.setText("bulan");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, -1, -1));

        jLabel21.setText("kg");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, -1));

        jLabel22.setText("cm");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, -1, -1));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 420, 360));

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setText("Pemberian Obat & Vitamin");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel16.setText("Jenis Obat & Vitamin");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel17.setText("Jumlah Obat");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel18.setText("Harga");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel19.setText("Total Harga");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        cb_obat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_obatActionPerformed(evt);
            }
        });
        jPanel4.add(cb_obat, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 100, -1));
        jPanel4.add(k_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 110, -1));
        jPanel4.add(k_jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 110, -1));
        jPanel4.add(k_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 110, -1));

        k_hitung.setText("Hitung");
        k_hitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                k_hitungActionPerformed(evt);
            }
        });
        jPanel4.add(k_hitung, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, -1, -1));

        tambah_obat.setText("Tambah");
        tambah_obat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah_obatActionPerformed(evt);
            }
        });
        jPanel4.add(tambah_obat, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, -1, -1));

        jPanel5.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 320, 220));

        tambahPemeriksaan.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 1070, 800));

        lbidan.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        lbidan.setText("DATA PEMERIKSAAN");
        tambahPemeriksaan.add(lbidan, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 290, 46));

        lbidan_id.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_id.setText("ID Pemeriksaan");
        tambahPemeriksaan.add(lbidan_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 160, 30));

        lbidan_nik.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_nik.setText("Jenis Pemeriksaan");
        tambahPemeriksaan.add(lbidan_nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 160, 30));

        cb_jenis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cb_jenis.setCursor(new java.awt.Cursor(java.awt.Cursor.SE_RESIZE_CURSOR));
        cb_jenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_jenisActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(cb_jenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 340, 50));

        lbidan_nama.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_nama.setText("ID Balita");
        tambahPemeriksaan.add(lbidan_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 136, 30));

        lbidan_ttl.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_ttl.setText("Tanggal Pemeriksaan");
        tambahPemeriksaan.add(lbidan_ttl, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 540, 190, 30));

        lbidan_telp.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_telp.setText("Pemberian Obat");
        tambahPemeriksaan.add(lbidan_telp, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, 160, 30));

        lbidan_status.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_status.setText("Keterangan");
        tambahPemeriksaan.add(lbidan_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 610, 136, 30));

        btn_simpan.setBackground(new java.awt.Color(0, 0, 204));
        btn_simpan.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/images__2_-removebg-preview (1).png"))); // NOI18N
        btn_simpan.setText("SIMPAN");
        btn_simpan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 670, 185, 40));

        bt_batal.setBackground(new java.awt.Color(204, 60, 37));
        bt_batal.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        bt_batal.setForeground(new java.awt.Color(255, 255, 255));
        bt_batal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clear (1).png"))); // NOI18N
        bt_batal.setText("BATAL");
        bt_batal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        bt_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_batalActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(bt_batal, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 670, 185, 40));

        btn_ubah.setBackground(new java.awt.Color(0, 0, 204));
        btn_ubah.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_ubah.setForeground(new java.awt.Color(255, 255, 255));
        btn_ubah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/282091.png"))); // NOI18N
        btn_ubah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        btn_ubah.setLabel("UBAH");
        btn_ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ubahActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(btn_ubah, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 670, 185, 40));

        tketerangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tketerangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tketeranganActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(tketerangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 600, 340, 50));

        lbidan_status1.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_status1.setText("Nama Bidan");
        tambahPemeriksaan.add(lbidan_status1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 136, 30));

        tglpemeriksaan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tambahPemeriksaan.add(tglpemeriksaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 530, 280, 50));

        tbalita.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tbalita.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######"))));
        tbalita.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        tbalita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbalitaActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(tbalita, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 340, 50));

        tpembayaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tpembayaran.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######"))));
        tpembayaran.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        tpembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tpembayaranActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(tpembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, 340, 50));

        tpemeriksaan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tpemeriksaan.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######"))));
        tpemeriksaan.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        tpemeriksaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tpemeriksaanActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(tpemeriksaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 340, 50));

        btn_cek.setBackground(new java.awt.Color(0, 0, 204));
        btn_cek.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_cek.setForeground(new java.awt.Color(255, 255, 255));
        btn_cek.setText("CEK");
        btn_cek.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        btn_cek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cekActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(btn_cek, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, 100, 40));

        cb_bidan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_bidanActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(cb_bidan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 320, 340, 50));

        k_obat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        k_obat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                k_obatActionPerformed(evt);
            }
        });
        tambahPemeriksaan.add(k_obat, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 460, 340, 50));

        lbidan_telp1.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lbidan_telp1.setText("Kode Pembayaran");
        tambahPemeriksaan.add(lbidan_telp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 160, 30));

        mainpanel.add(tambahPemeriksaan, "card3");

        jPanel1.add(mainpanel, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 2052, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tcari_bidanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tcari_bidanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tcari_bidanActionPerformed

    private void tcari_bidanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tcari_bidanKeyReleased
        // TODO add your handling code here:
        String findItem = tcari_bidan.getText();
        try{
            String sql = "SELECT * FROM pemeriksaan where `kdpemeriksaan` like '%" + findItem + "%' or `kode balita` like '%" + findItem + "%' or `kode pemeriksaan` like '%" + findItem + "%' order by kdpemeriksaan asc";
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                tabmode.addRow(new Object[]{
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                    hasil.getString(6),
                    hasil.getString(7)
                });
            }
            table_pemeriksaan.setModel(tabmode);
        } catch (Exception e) {

        }
        datatable();
    }//GEN-LAST:event_tcari_bidanKeyReleased

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "hapus", " yakin ingin menghapus?", JOptionPane.YES_NO_OPTION);
        if (ok == 0){
            String sql = "DELETE FROM pemeriksaan WHERE kdpemeriksaan ="+ tpemeriksaan.getText() ;
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "data berhasil dihapus");
                kosong();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "data gagal dihapus"+ e);
            }
        }
        datatable();
    }//GEN-LAST:event_bt_hapusActionPerformed

    private void tketeranganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tketeranganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tketeranganActionPerformed

    private void bt_tambahdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tambahdataActionPerformed
        kosong();
        mainpanel.removeAll();
        mainpanel.repaint();
        mainpanel.revalidate();
        mainpanel.add(tambahPemeriksaan);
        mainpanel.repaint();
        mainpanel.revalidate();
        btn_ubah.setVisible(false);
        btn_simpan.setVisible(true);
        btn_simpan.setEnabled(false);
        

        tpemeriksaan.setEditable(false);
        Random rand = new Random();
        Integer random = rand.nextInt(100000);
        tpemeriksaan.setText(random.toString());
        tpembayaran.setEditable(false);
        Integer random1 = rand.nextInt(100000);
        tpembayaran.setText(random1.toString());
    }//GEN-LAST:event_bt_tambahdataActionPerformed

    private void bt_ubahdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_ubahdataActionPerformed
        mainpanel.removeAll();
        mainpanel.repaint();
        mainpanel.revalidate();
        mainpanel.add(tambahPemeriksaan);
        mainpanel.repaint();
        mainpanel.revalidate();
        btn_ubah.setVisible(true);
        btn_simpan.setVisible(false);
        btn_cek.setVisible(false);
        tpemeriksaan.setEditable(false);
        tpembayaran.setEditable(false);

    }//GEN-LAST:event_bt_ubahdataActionPerformed

    private void bt_hapusdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusdataActionPerformed
        if(!tpemeriksaan.getText().equals("") && !tpemeriksaan.getText().isEmpty()) {
            bt_hapusdata.setEnabled(true);
            int ok = JOptionPane.showConfirmDialog(null, "hapus", " yakin ingin menghapus?", JOptionPane.YES_NO_OPTION);
            if (ok == 0){
                String sql = "DELETE FROM pemeriksaan WHERE kdpemeriksaan ='"+ tpemeriksaan.getText() + "'" ;
                try {
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "data berhasil dihapus");
                    kosong();
                    //                k_id.requestFocus();
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "data gagal dihapus"+ e);
                }
            }
            datatable();
        } else {
            //bhapus_bidan.setEnabled(false);
            JOptionPane.showMessageDialog(null, "tidak ada data yang dihapus");
        }
    }//GEN-LAST:event_bt_hapusdataActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
    if(!tpemeriksaan.getText().equals("") && !tbalita.getText().equals("") && !tpembayaran.equals("") && !k_obat.equals("") && !cb_bidan.equals(null) 
            &&  tglpemeriksaan.getDate() != null && !status_panjang.getText().equals("") && !status_berat.equals("") && !status_gizi.equals("")
            && !k_umur.getText().equals("") && !k_berat.equals("") && !k_tinggi.equals("")) {
            String tampilan = "yyyy-MM-dd";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String Kttl = String.valueOf(fm.format(tglpemeriksaan.getDate()));
            String idjenis = cb_jenis.getSelectedItem().toString().replaceAll("[^0-9]", "");
          
            try {
                String sqlnik = "SELECT kdpemeriksaan from pemeriksaan where kdpemeriksaan=" + tpemeriksaan.getText();
                java.sql.Statement statnik = conn.createStatement();
                ResultSet hasil = statnik.executeQuery(sqlnik);
                if(!hasil.isBeforeFirst()) {
                    String sqlharga = "SELECT harga from jenis_pemeriksaan where id_pemeriksaan='" + idjenis + "'";
                    Statement statharga = conn.createStatement();
                    ResultSet harga = statharga.executeQuery(sqlharga);
                    while(harga.next()) {
                    Integer total = totalharga + Integer.parseInt(harga.getString("harga"));
                    String sql = "INSERT INTO pemeriksaan values (?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?,?)";
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.setString(1, tpemeriksaan.getText());
                    stat.setString(2, idjenis);
                    stat.setString(3, tbalita.getText());
                    stat.setString(4, cb_bidan.getSelectedItem().toString());
                    stat.setString(5, tpembayaran.getText());
                    stat.setString(6, tketerangan.getText());
                    stat.setString(7, Kttl);
                    stat.setString(8, total.toString());
                    stat.setString(9, k_umur.getText());
                    stat.setString(10, k_berat.getText());
                    stat.setString(11, k_tinggi.getText());
                    stat.setString(12, status_panjang.getText());
                    stat.setString(13, status_gizi.getText());
                    stat.setString(14, status_berat.getText());
                    stat.setString(15, Arrays.toString(obat.toArray()).replace("[", "").replace("]", ""));
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "data berhasil disimpan");
                    }
                    mainpanel.removeAll();
                    mainpanel.add(dataPemeriksaan);
                    mainpanel.repaint();
                    mainpanel.revalidate();
                        String jdbc_driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "";
        String url = "jdbc:mysql://localhost/posyandu3";
        File reportFile = new File(".");
        String dirr = "";

        try {
            Class.forName(jdbc_driver);
            Connection cone = DriverManager.getConnection(url, user, pass);
            Statement stat = cone.createStatement();
            String sql = "SELECT pemeriksaan.kdpemeriksaan, pemeriksaan.kdbalita, pemeriksaan.kdbidan, pemeriksaan.kdpembayaran, pemeriksaan.keterangan, pemeriksaan.tpemeriksaan, pemeriksaan.harga, pemeriksaan.umur, pemeriksaan.berat_badan, pemeriksaan.tinggi_badan, pemeriksaan.status_panjang, pemeriksaan.status_gizi, pemeriksaan.status_berat, pemeriksaan.pemberian_obat_vitamin, jenis_pemeriksaan.jenis_pemeriksaan AS jenis_pemeriksaan FROM pemeriksaan JOIN jenis_pemeriksaan ON pemeriksaan.jenpemeriksaan = jenis_pemeriksaan.id_pemeriksaan where kdpemeriksaan='" + tpemeriksaan.getText() + "'";
            dirr = reportFile.getCanonicalPath() + "/src/projectposyandu/";
            JasperDesign design = JRXmlLoader.load(dirr + "report1.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(design);
            ResultSet rs = stat.executeQuery(sql);
            JRResultSetDataSource rsDataSource = new JRResultSetDataSource(rs);
            JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap(), rsDataSource);
            JFrame frame = new JFrame("Report");
            frame.getContentPane().add(new JRViewer(jp));
            frame.pack();
            frame.setVisible(true);

        } catch (ClassNotFoundException | SQLException | IOException | JRException ex) {
            JOptionPane.showMessageDialog(null, "\nGagal Mencetak\n" + ex,
                "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }

                    kosong();
                }
                    
                 else {
                    JOptionPane.showMessageDialog(null, "kode yang diinput sudah terdaftar!");
                }
              
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "data gagal disimpan "+e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Isi semua kolom yang tersedia!");
        }
        datatable();
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void bt_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_batalActionPerformed
        mainpanel.removeAll();
        mainpanel.add(dataPemeriksaan);
        mainpanel.repaint();
        mainpanel.revalidate();
        kosong();
        datatable();
    }//GEN-LAST:event_bt_batalActionPerformed

    private void btn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahActionPerformed
        // TODO add your handling code here:
          if(!tpemeriksaan.getText().equals("") && !tbalita.getText().equals("") && !tpembayaran.equals("") && !k_obat.equals("") && !cb_bidan.equals(null) &&  !tglpemeriksaan.getDate().equals(null)&& !status_panjang.getText().equals("") && !status_berat.equals("") && !status_gizi.equals("")
            && !k_umur.getText().equals("") && !k_berat.equals("") && !k_tinggi.equals("")) {
           try{
               
            String tampilan = "yyyy-MM-dd";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String Kttl = String.valueOf(fm.format(tglpemeriksaan.getDate()));
             String jenpemeriksaan = cb_jenis.getSelectedItem().toString().replaceAll("[^0-9]", "");
             String sqlharga = "SELECT harga from pemeriksaan where kdpemeriksaan='" + tpemeriksaan.getText() + "'";
             Statement statharga = conn.createStatement();
             ResultSet harga = statharga.executeQuery(sqlharga);
             harga.next();

             Integer total = totalharga + Integer.parseInt(harga.getString("harga"));
            String sql = "UPDATE pemeriksaan set `jenpemeriksaan`=?, `kdbalita`=?, `kdbidan`=?, `kdpembayaran`=?,`keterangan`=? ,`tpemeriksaan`=?, `umur`=?, `berat_badan`=?, `tinggi_badan`=?, `status_panjang`=?,`status_gizi`=? ,`status_berat`=?, harga=?, pemberian_obat_vitamin=?  WHERE `kdpemeriksaan`='" + tpemeriksaan.getText() + "'";
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, jenpemeriksaan);
            stat.setString(2, tbalita.getText());
            stat.setString(3, cb_bidan.getSelectedItem().toString());
            stat.setString(4, tpembayaran.getText());
            stat.setString(5, tketerangan.getText());
            stat.setString(6, Kttl);
            stat.setString(7, k_umur.getText());
            stat.setString(8, k_berat.getText());
            stat.setString(9,  k_tinggi.getText());
            stat.setString(10, status_panjang.getText());
            stat.setString(11, status_gizi.getText());
            stat.setString(12, status_berat.getText());
            stat.setString(13, total.toString());
            stat.setString(14, Arrays.toString(obat.toArray()).replace("[", "").replace("]", "") );
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
            mainpanel.removeAll();
            mainpanel.add(dataPemeriksaan);
            mainpanel.repaint();
            mainpanel.revalidate();
            datatable();
            kosong();
                 

        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "data gagal diubah"+ e);
        }
} else {
                    JOptionPane.showMessageDialog(null, "Isi semua kolom yang tersedia!");
                }
    }//GEN-LAST:event_btn_ubahActionPerformed

    private void tpemeriksaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tpemeriksaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tpemeriksaanActionPerformed

    private void btn_cekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cekActionPerformed
        // TODO add your handling code here:
        if(tbalita.getText() != null && !tbalita.getText().isEmpty() && !tbalita.getText().equals("")) {
            try {

                String sql = "SELECT * from balita where kd_balita = '" + tbalita.getText() + "'";
                java.sql.Statement stat = conn.createStatement();
                ResultSet hasil = stat.executeQuery(sql);
                if(!hasil.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(null, "ID balita tidak ditemukan, silahkan masukkan kembali ID yang benar");
                    tbalita.setText("");
                    btn_simpan.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "ID Balita valid. silahkan lanjutkan");
                    btn_simpan.setEnabled(true);
                    hasil.next();
                    k_umur.setText(hasil.getString("umur"));
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Masukkan ID Balita!");
        }
    }//GEN-LAST:event_btn_cekActionPerformed

    private void tpembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tpembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tpembayaranActionPerformed

    private void tbalitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbalitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbalitaActionPerformed

    private void table_pemeriksaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_pemeriksaanMouseClicked

          int bar = table_pemeriksaan.getSelectedRow();
        String a = tabmode.getValueAt (bar, 0).toString();
        String b = tabmode.getValueAt (bar, 1).toString();
        String c = tabmode.getValueAt (bar, 2).toString();
        String d = tabmode.getValueAt (bar, 3).toString();
        String e = tabmode.getValueAt (bar, 4).toString();
        String f = tabmode.getValueAt (bar, 5).toString();
        String g = tabmode.getValueAt (bar, 6).toString();
        String h = tabmode.getValueAt (bar, 7).toString();
        String i = tabmode.getValueAt (bar, 8).toString();
        String j = tabmode.getValueAt (bar, 9).toString();   
        String k = tabmode.getValueAt (bar, 10).toString();
        String l = tabmode.getValueAt (bar, 11).toString();
        String m = tabmode.getValueAt (bar, 12).toString();   
        String n = tabmode.getValueAt (bar, 13).toString(); 
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(f);
            tglpemeriksaan.setDate(date);

            //        java.util.Date date = formater.parse(b);
        } catch (ParseException ex) {
            Logger.getLogger(pemeriksaan.class.getName()).log(Level.SEVERE, null, ex);
        }

        tpemeriksaan.setText(a);
        tbalita.setText(c);
        cb_bidan.setSelectedItem(d);
        tpembayaran.setText(e);
        tketerangan.setText(n);
        k_berat.setText(i);
        k_tinggi.setText(h);
        k_umur.setText(g);
        status_panjang.setText(j);
        status_gizi.setText(k);
        status_berat.setText(l);
        obat.add(m);
        bt_hapusdata.setEnabled(true);
        bt_ubahdata.setEnabled(true);
        bt_cetak.setEnabled(true);
        
    }//GEN-LAST:event_table_pemeriksaanMouseClicked

    private void bcari_bidanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcari_bidanActionPerformed
        // TODO add your handling code here:
        String findItem = tcari_bidan.getText();
        try{
            String sql = "SELECT * FROM pemeriksaan where `kdpemeriksaan` like '%" + findItem + "%' or `kode balita` like '%" + findItem + "%' or `kode pemeriksaan` like '%" + findItem + "%' order by kdpemeriksaan asc";
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                tabmode.addRow(new Object[]{
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                    hasil.getString(6),
                    hasil.getString(7)
                });
            }
            table_pemeriksaan.setModel(tabmode);
        } catch (SQLException e) {

        }
        datatable();
    }//GEN-LAST:event_bcari_bidanActionPerformed

    private void cb_jenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_jenisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_jenisActionPerformed

    private void cek_beratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cek_beratActionPerformed
        // TODO add your handling code here:
            jPanel2.setVisible(false);
            jPanel4.setVisible(false);
            jPanel3.setVisible(true);
            jPanel5.repaint();
            jPanel5.revalidate();
            openpdf("src//img/bb-u.pdf");
            bt_cekpanjang.setVisible(false);
            bt_cekgizi.setVisible(false);
            bt_cekberat.setVisible(true);
                 bt_cekpanjang.setEnabled(false);
            bt_cekgizi.setEnabled(false);
            bt_cekberat.setEnabled(true);
    }//GEN-LAST:event_cek_beratActionPerformed

    private void cek_giziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cek_giziActionPerformed
        // TODO add your handling code here:
        if(k_berat.getText() != null && k_tinggi.getText() != null && !k_berat.equals("") && !k_tinggi.getText().equals("")) {
            jPanel2.setVisible(false);
            jPanel4.setVisible(false);
            jPanel3.setVisible(true);
            jPanel5.repaint();
            jPanel5.revalidate();
            openpdf("src//img/bb-pb.pdf");
            bt_cekpanjang.setVisible(false);
            bt_cekgizi.setVisible(true);
            bt_cekberat.setVisible(false);
             bt_cekpanjang.setEnabled(false);
            bt_cekgizi.setEnabled(true);
            bt_cekberat.setEnabled(false);
        }
    }//GEN-LAST:event_cek_giziActionPerformed

    private void cek_panjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cek_panjangActionPerformed
        // TODO add your handling code here:
        if(k_berat.getText() != null && k_tinggi.getText() != null && !k_berat.equals("") && !k_tinggi.getText().equals("")){
            jPanel2.setVisible(false);
            jPanel4.setVisible(false);
            jPanel3.setVisible(true);
            jPanel5.repaint();
            jPanel5.revalidate();
            openpdf("src//img/pb-u.pdf");
            bt_cekpanjang.setVisible(true);
            bt_cekgizi.setVisible(false);
            bt_cekberat.setVisible(false);
            bt_cekpanjang.setEnabled(true);
            bt_cekgizi.setEnabled(false);
            bt_cekberat.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null,"Masukkan semua kolom yang ada");
        }
    }//GEN-LAST:event_cek_panjangActionPerformed
    
    private void bt_cekpanjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cekpanjangActionPerformed
        // TODO add your handling code here:
 if(k_berat.getText() != null && k_tinggi.getText() != null && !k_berat.equals("") && !k_tinggi.getText().equals("") && 
                k_median.getText() != null && k_tabel1sd.getText() != null && !k_median.getText().equals("") && !k_tabel1sd.getText().equals("")) {
        Float pbanak = Float.valueOf(k_tinggi.getText());
        Float median = Float.valueOf(k_median.getText());
        Float tabel1sd = Float.valueOf(k_tabel1sd.getText());
        Float zscore;
        if (pbanak > median) {
            zscore = (pbanak - median)/(tabel1sd-median);
            k_zscore.setText(zscore.toString());
            if(zscore < -3.0) {
                status_panjang.setText("Sangat Pendek");
            }
            else if(zscore >= -3.0 && zscore <= -2.0) {
                status_panjang.setText("Pendek");
            } else if(zscore <= 3.0 && zscore >= -2.0) {
                status_panjang.setText("Normal");
            } else if(zscore > 3.0) {
                status_panjang.setText("Tinggi");
            }
            
        }
        else if (pbanak < median) {
            zscore = (pbanak - median)/(median-(tabel1sd));
            k_zscore.setText(zscore.toString());
               if(zscore < -3.0) {
                status_panjang.setText("Sangat Pendek");
            }
            else if(zscore >= -3.0 && zscore <= -2.0) {
                status_panjang.setText("Pendek");
            } else if(zscore <= 3.0 && zscore >= -2.0) {
                status_panjang.setText("Normal");
            } else if(zscore > 3.0) {
                status_panjang.setText("Tinggi");
            }
        }
            jPanel3.setVisible(false);
            jPanel2.setVisible(true);
            jPanel4.setVisible(true);
            k_median.setText("");
            k_zscore.setText("");
            k_tabel1sd.setText("");
} else {
                    JOptionPane.showMessageDialog(null,"Masukkan semua kolom yang ada");
                    }
    }//GEN-LAST:event_bt_cekpanjangActionPerformed

    private void bt_cekgiziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cekgiziActionPerformed
        // TODO add your handling code here:
         if(k_berat.getText() != null && k_tinggi.getText() != null && !k_berat.equals("") && !k_tinggi.getText().equals("") && 
                k_median.getText() != null && k_tabel1sd.getText() != null && !k_median.getText().equals("") && !k_tabel1sd.getText().equals("")) {
        Float bbanak = Float.valueOf(k_berat.getText());
        Float median = Float.valueOf(k_median.getText());
        Float tabel1sd = Float.valueOf(k_tabel1sd.getText());
        Float zscore;
        if (bbanak > median) {
            zscore = (bbanak - median)/(tabel1sd-median);
            k_zscore.setText(zscore.toString());
            if(zscore < -3.0) {
                status_gizi.setText("Gizi Buruk");
            }
            else if(zscore >= -3.0 && zscore <= -2.0) {
                status_gizi.setText("Gizi Kurang");
            } else if(zscore >= -2.0 && zscore <= 1.0) {
                status_gizi.setText("Gizi baik(Normal)");
            }else if(zscore > 1.0 && zscore <= 2.0) {
                status_gizi.setText("Beresiko Gizi Lebih");
            }
            else if(zscore > 2.0 && zscore <= 3.0) {
                status_gizi.setText("Beresiko Gizi Lebih");
            }
            else if(zscore > 3.0) {
                status_gizi.setText("Obesitas");
            }
            
        }
        else if (bbanak < median) {
            zscore = (bbanak - median)/(median-(tabel1sd));
            k_zscore.setText(zscore.toString());
           if(zscore < -3.0) {
                status_gizi.setText("Gizi Buruk");
            }
            else if(zscore >= -3.0 && zscore <= -2.0) {
                status_gizi.setText("Gizi Kurang");
            } else if(zscore >= -2.0 && zscore <= 1.0) {
                status_gizi.setText("Gizi baik(Normal)");
            }else if(zscore > 1.0 && zscore <= 2.0) {
                status_gizi.setText("Beresiko Gizi Lebih");
            }
            else if(zscore > 2.0 && zscore <= 3.0) {
                status_gizi.setText("Beresiko Gizi Lebih");
            }
            else if(zscore > 3.0) {
                status_gizi.setText("Obesitas");
            }
        }
            k_median.setText("");
            k_zscore.setText("");
            k_tabel1sd.setText("");
            jPanel3.setVisible(false);
            jPanel2.setVisible(true);
            jPanel4.setVisible(true);
         } else {
             JOptionPane.showMessageDialog(null,"Masukkan semua kolom yang ada");
         }
    }//GEN-LAST:event_bt_cekgiziActionPerformed

    private void bt_cekberatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cekberatActionPerformed
        // TODO add your handling code here:
        
        if(k_berat.getText() != null && k_tinggi.getText() != null && !k_berat.equals("") && !k_tinggi.getText().equals("") && 
                k_median.getText() != null && k_tabel1sd.getText() != null && !k_median.getText().equals("") && !k_tabel1sd.getText().equals("")) {
        Float bbanak = Float.valueOf(k_berat.getText());
        Float median = Float.valueOf(k_median.getText());
        Float tabel1sd = Float.valueOf(k_tabel1sd.getText());
        Float zscore;
        if (bbanak > median) {
            zscore = (bbanak - median)/(tabel1sd-median);
            k_zscore.setText(zscore.toString());
            if(zscore < -3.0) {
                status_berat.setText("Berat Badan Sangat Kurang");
            }
            else if(zscore >= -3.0 && zscore <= -2.0) {
                status_berat.setText("Berat Badan Kurang");
            } else if(zscore >= -2.0 && zscore <= 1.0) {
                status_berat.setText("Berat Badan Normal"); 
            } else if(zscore > 1.0) {
                status_berat.setText("Resiko Berat Badan Lebih");
            }
            
        }
        else if (bbanak < median) {
            zscore = (bbanak - median)/(median-(tabel1sd));
            k_zscore.setText(zscore.toString());
             if(zscore < -3.0) {
                status_berat.setText("Berat Badan Sangat Kurang");
            }
            else if(zscore >= -3.0 && zscore <= -2.0) {
                status_berat.setText("Berat Badan Kurang");
            } else if(zscore >= -2.0 && zscore <= 1.0) {
                status_berat.setText("Berat Badan Normal"); 
            } else if(zscore > 1.0) {
                status_berat.setText("Resiko Berat Badan Lebih");
            }
        }
            jPanel3.setVisible(false);
            jPanel2.setVisible(true);
            jPanel4.setVisible(true);
            k_median.setText("");
            k_zscore.setText("");
            k_tabel1sd.setText("");
        } else {
            JOptionPane.showMessageDialog(null,"Masukkan semua kolom yang ada");
        }
    }//GEN-LAST:event_bt_cekberatActionPerformed

    private void panduanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_panduanActionPerformed
        // TODO add your handling code here:
         JOptionPane.showMessageDialog(null,"Panduan Mengisi Median dan Tabel 1SD\n" +
"\n" +
"1. Jika ingin mengisi status panjang maka digunakan PB/TB dan Umur, \n" +
"lalu untuk mengisi status gizi maka digunakan BB dan PB/TB, \n" +
"dan untuk mengisi status berat maka digunakan BB dan Umur\n" +
"\n" +
"2. Jika PB/TB dan BB lebih besar dari median,\n" +
"maka isi dari \"Tabel 1SD\" adalah nilai dari \"Table +1SD\", \n" +
"sebaliknya jika  PB/TB dan BB kurang dari median, \n" +
"maka isi dari \"Tabel 1SD\" adalah nilai dari \"Table -1SD\"\n" +
"\n" +
"\n" +
"BB = Berat Badan\n" +
"PB/TB = Panjang Badan/Tinggi Badan");
        
    }//GEN-LAST:event_panduanActionPerformed

    private void cb_obatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_obatActionPerformed
        // TODO add your handling code here:
        String harga = cb_obat.getSelectedItem().toString().replaceAll("[^0-9]", "");
        k_harga.setText(harga);
        
    }//GEN-LAST:event_cb_obatActionPerformed

    private void k_hitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_k_hitungActionPerformed
        // TODO add your handling code here:
        Integer harga = Integer.valueOf(k_harga.getText());
        Integer jumlah = Integer.valueOf(k_jumlah.getText());
        Integer total = harga*jumlah;
        k_total.setText(total.toString());
    }//GEN-LAST:event_k_hitungActionPerformed

    private void tambah_obatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah_obatActionPerformed
        // TODO add your handling code here:
        if(k_jumlah.getText().equals("") && k_total.getText().equals("")) {
           JOptionPane.showMessageDialog(null, "Masukkan semua kolom yang ada");
        } else {
            obat.add(cb_obat.getSelectedItem().toString().replaceAll("[^a-z, A-Z]", "") + " - " + k_jumlah.getText() + " pcs");
            totalharga += Float.valueOf(k_total.getText());
            k_jumlah.setText("");
            k_obat.setText(Arrays.toString(obat.toArray()).replace("[", "").replace("]", ""));
            k_total.setText("");
        }
    }//GEN-LAST:event_tambah_obatActionPerformed

    private void cb_bidanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_bidanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_bidanActionPerformed

    private void k_obatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_k_obatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_k_obatActionPerformed

    private void bt_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cetakActionPerformed
        // TODO add your handling code here:
        bt_cetak.setEnabled(false);
        String jdbc_driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "";
        String url = "jdbc:mysql://localhost/posyandu3";
        File reportFile = new File(".");
        String dirr = "";

        try {
            Class.forName(jdbc_driver);
            Connection cone = DriverManager.getConnection(url, user, pass);
            Statement stat = cone.createStatement();
            String sql = "SELECT pemeriksaan.kdpemeriksaan, pemeriksaan.kdbalita, pemeriksaan.kdbidan, pemeriksaan.kdpembayaran, pemeriksaan.keterangan, pemeriksaan.tpemeriksaan, pemeriksaan.harga, pemeriksaan.umur, pemeriksaan.berat_badan, pemeriksaan.tinggi_badan, pemeriksaan.status_panjang, pemeriksaan.status_gizi, pemeriksaan.status_berat, pemeriksaan.pemberian_obat_vitamin, jenis_pemeriksaan.jenis_pemeriksaan AS jenis_pemeriksaan FROM pemeriksaan JOIN jenis_pemeriksaan ON pemeriksaan.jenpemeriksaan = jenis_pemeriksaan.id_pemeriksaan where kdpemeriksaan='" + tpemeriksaan.getText() + "'";
            dirr = reportFile.getCanonicalPath() + "/src/projectposyandu/";
            JasperDesign design = JRXmlLoader.load(dirr + "report1.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(design);
            ResultSet rs = stat.executeQuery(sql);
            JRResultSetDataSource rsDataSource = new JRResultSetDataSource(rs);
            JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap(), rsDataSource);
            JFrame frame = new JFrame("Report");
            frame.getContentPane().add(new JRViewer(jp));
            frame.pack();
            frame.setVisible(true);

        } catch (ClassNotFoundException | SQLException | IOException | JRException ex) {
            JOptionPane.showMessageDialog(null, "\nGagal Mencetak\n" + ex,
                "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_bt_cetakActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bcari_bidan;
    private javax.swing.JButton bt_batal;
    private javax.swing.JButton bt_cekberat;
    private javax.swing.JButton bt_cekgizi;
    private javax.swing.JButton bt_cekpanjang;
    private javax.swing.JButton bt_cetak;
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_hapusdata;
    private javax.swing.JButton bt_tambahdata;
    private javax.swing.JButton bt_ubahdata;
    private javax.swing.JButton btn_cek;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_bidan;
    private javax.swing.JComboBox<String> cb_jenis;
    private javax.swing.JComboBox<String> cb_obat;
    private javax.swing.JButton cek_berat;
    private javax.swing.JButton cek_gizi;
    private javax.swing.JButton cek_panjang;
    private javax.swing.JPanel dataPemeriksaan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField k_berat;
    private javax.swing.JFormattedTextField k_harga;
    private javax.swing.JButton k_hitung;
    private javax.swing.JFormattedTextField k_jumlah;
    private javax.swing.JFormattedTextField k_median;
    private javax.swing.JTextField k_obat;
    private javax.swing.JFormattedTextField k_tabel1sd;
    private javax.swing.JFormattedTextField k_tinggi;
    private javax.swing.JFormattedTextField k_total;
    private javax.swing.JFormattedTextField k_umur;
    private javax.swing.JFormattedTextField k_zscore;
    private javax.swing.JLabel label_bidan;
    private javax.swing.JLabel lbidan;
    private javax.swing.JLabel lbidan_id;
    private javax.swing.JLabel lbidan_nama;
    private javax.swing.JLabel lbidan_nik;
    private javax.swing.JLabel lbidan_status;
    private javax.swing.JLabel lbidan_status1;
    private javax.swing.JLabel lbidan_telp;
    private javax.swing.JLabel lbidan_telp1;
    private javax.swing.JLabel lbidan_ttl;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JButton panduan;
    private javax.swing.JTextField status_berat;
    private javax.swing.JTextField status_gizi;
    private javax.swing.JTextField status_panjang;
    private javax.swing.JTable table_pemeriksaan;
    private javax.swing.JPanel tambahPemeriksaan;
    private javax.swing.JButton tambah_obat;
    private javax.swing.JFormattedTextField tbalita;
    private javax.swing.JTextField tcari_bidan;
    private com.toedter.calendar.JDateChooser tglpemeriksaan;
    private javax.swing.JTextField tketerangan;
    private javax.swing.JFormattedTextField tpembayaran;
    private javax.swing.JFormattedTextField tpemeriksaan;
    // End of variables declaration//GEN-END:variables

}
