import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class TugasPersonal2 extends JFrame {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private Connection db;
    private Statement st;
    private ResultSet rs;

    public TugasPersonal2() {
        super("Klinik CRUD Data Pasien");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Buat objek DefaultTableModel dengan kolom yang sesuai
        String[] columnNames = {"No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat"};
        tblModel = new DefaultTableModel(columnNames, 0);

        // Buat tabel dengan data dari DefaultTableModel
        tbl = new JTable(tblModel);

        // Tambahkan JScrollPane untuk tabel
        JScrollPane scrollPane = new JScrollPane(tbl);

        // Tambahkan tabel ke dalam frame dengan menggunakan layout BorderLayout
        add(scrollPane, BorderLayout.CENTER);

        // Buat tombol-tombol
        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnHapus = new JButton("Hapus");
        JButton btnPrev = new JButton("<< Prev");
        JButton btnNext = new JButton("Next >>");
        JButton btnDaftar = new JButton("Daftar Pasien");
        JButton btnKeluar = new JButton("Keluar");

        // Tambahkan tombol-tombol ke dalam panel di bagian bawah frame
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnTambah);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnHapus);
        bottomPanel.add(btnPrev);
        bottomPanel.add(btnNext);
        bottomPanel.add(btnDaftar);
        bottomPanel.add(btnKeluar);
        add(bottomPanel, BorderLayout.SOUTH);

        // Tambahkan action listener untuk tombol-tombol
        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tambahDataDialog();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDataDialog();
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        btnPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prevData();
            }
        });

        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextData();
            }
        });

        btnDaftar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tampilkanDaftarPasien();
            }
        });

        btnKeluar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Membuat koneksi ke database
        try {
            db = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_klinik", "root", "");
            st = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT * FROM pasien");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Menampilkan data pertama
        try {
            while (rs.next()) {
                String no = String.valueOf(tblModel.getRowCount() + 1);
                String nama = rs.getString("nama");
                String nik = rs.getString("nik");
                Date tglLahir = rs.getDate("tgl_lahir");
                String alamat = rs.getString("alamat");

                Object[] rowData = {no, nama, nik, new SimpleDateFormat("yyyy-MMM-dd").format(tglLahir), alamat};
                tblModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Metode untuk menambahkan data pasien baru menggunakan dialog input
    private void tambahDataDialog() {
        String nama = JOptionPane.showInputDialog(this, "Nama Pasien:");
        if (nama != null) { // Jika pengguna tidak membatalkan dialog
            String nik = JOptionPane.showInputDialog(this, "NIK:");
            if (nik != null) {
                String tglLahirStr = JOptionPane.showInputDialog(this, "Tanggal Lahir (YYYY-MM-DD):");
                if (tglLahirStr != null) {
                    try {
                        Date tglLahir = Date.valueOf(tglLahirStr);
                        String alamat = JOptionPane.showInputDialog(this, "Alamat:");

                        // Lakukan proses penambahan data ke database
                        String insertQuery = "INSERT INTO pasien (nama, nik, tgl_lahir, alamat) VALUES (?, ?, ?, ?)";
                        PreparedStatement pst = db.prepareStatement(insertQuery);
                        pst.setString(1, nama);
                        pst.setString(2, nik);
                        pst.setDate(3, tglLahir);
                        pst.setString(4, alamat);
                        pst.executeUpdate();

                        // Setelah berhasil tambahkan data baru ke dalam tabel
                        Object[] rowData = {tblModel.getRowCount() + 1, nama, nik, tglLahirStr, alamat};
                        tblModel.addRow(rowData);
                    } catch (IllegalArgumentException | SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal menambahkan data pasien.");
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    // Metode untuk mengupdate data pasien menggunakan dialog input
    private void updateDataDialog() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                rs.absolute(selectedRow + 1);
                String nama = JOptionPane.showInputDialog(this, "Nama Pasien:", rs.getString("nama"));
                if (nama != null) {
                    String tglLahirStr = JOptionPane.showInputDialog(this, "Tanggal Lahir (YYYY-MM-DD):",
                            new SimpleDateFormat("yyyy-MMM-dd").format(rs.getDate("tgl_lahir")));
                    if (tglLahirStr != null) {
                        try {
                            Date tglLahir = Date.valueOf(tglLahirStr);
                            String alamat = JOptionPane.showInputDialog(this, "Alamat:",
                                    rs.getString("alamat"));

                            // Lakukan proses update data di database
                            String updateQuery = "UPDATE pasien SET nama = ?, tgl_lahir = ?, alamat = ? WHERE nik = ?";
                            PreparedStatement pst = db.prepareStatement(updateQuery);
                            pst.setString(1, nama);
                            pst.setDate(2, tglLahir);
                            pst.setString(3, alamat);
                            pst.setString(4, rs.getString("nik"));
                            pst.execute();

                            pst.close();
                            db.close();

                            // Setelah berhasil update data, perbarui tampilan tabel
                            tblModel.setValueAt(nama, selectedRow, 1);
                            tblModel.setValueAt(tglLahirStr, selectedRow, 3);
                            tblModel.setValueAt(alamat, selectedRow, 4);
                        } catch (IllegalArgumentException | SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Gagal mengupdate data pasien.");
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diupdate.");
        }
    }

    // Metode untuk menghapus data pasien
    private void hapusData() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                rs.absolute(selectedRow + 1);
                String nik = rs.getString("nik");

                // Lakukan proses penghapusan data dari database
                String deleteQuery = "DELETE FROM pasien WHERE nik = ?";
                PreparedStatement pst = db.prepareStatement(deleteQuery);
                pst.setString(1, nik);
                pst.executeUpdate();

                db.close();

                // Setelah berhasil hapus data, hapus juga dari tampilan tabel
                tblModel.removeRow(selectedRow);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.");
        }
    }

    // Method untuk berpindah ke data sebelumnya
    private void prevData() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow > 0) {
            tbl.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            tbl.scrollRectToVisible(tbl.getCellRect(selectedRow - 1, 0, true));
        }
    }

    // Method untuk berpindah ke data selanjutnya
    private void nextData() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < tblModel.getRowCount() - 1) {
            tbl.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            tbl.scrollRectToVisible(tbl.getCellRect(selectedRow + 1, 0, true));
        }
    }

    // Method untuk menampilkan daftar pasien dalam tabel
    private void tampilkanDaftarPasien() {
        JFrame frame = new JFrame("Daftar Pasien");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();

        String[] columnNames = {"No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat"};
        Object[][] data = new Object[tblModel.getRowCount()][5];

        for (int i = 0; i < tblModel.getRowCount(); i++) {
            for (int j = 0; j < 5; j++) {
                data[i][j] = tblModel.getValueAt(i, j);
            }
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TugasPersonal2().setVisible(true);
            }
        });
    }
}
