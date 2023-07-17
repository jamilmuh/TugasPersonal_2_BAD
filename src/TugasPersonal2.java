import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class TugasPersonal2 extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public TugasPersonal2() {
        super("Klinik CRUD Data Pasien");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Buat objek DefaultTableModel dengan kolom yang sesuai
        String[] columnNames = {"No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Buat tabel dengan data dari DefaultTableModel
        table = new JTable(tableModel);

        // Tambahkan JScrollPane untuk tabel
        JScrollPane scrollPane = new JScrollPane(table);

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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_klinik", "root", "");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM pasien");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Menampilkan data pertama
        try {
            while (resultSet.next()) {
                String no = String.valueOf(tableModel.getRowCount() + 1);
                String nama = resultSet.getString("nama");
                String nik = resultSet.getString("nik");
                Date tglLahir = resultSet.getDate("tgl_lahir");
                String alamat = resultSet.getString("alamat");

                Object[] rowData = {no, nama, nik, new SimpleDateFormat("yyyy-MMM-dd").format(tglLahir), alamat};
                tableModel.addRow(rowData);
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
                        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1, nama);
                        preparedStatement.setString(2, nik);
                        preparedStatement.setDate(3, tglLahir);
                        preparedStatement.setString(4, alamat);
                        preparedStatement.executeUpdate();

                        // Setelah berhasil tambahkan data baru ke dalam tabel
                        Object[] rowData = {tableModel.getRowCount() + 1, nama, nik, tglLahirStr, alamat};
                        tableModel.addRow(rowData);
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
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                resultSet.absolute(selectedRow + 1);
                String nama = JOptionPane.showInputDialog(this, "Nama Pasien:", resultSet.getString("nama"));
                if (nama != null) {
                    String tglLahirStr = JOptionPane.showInputDialog(this, "Tanggal Lahir (YYYY-MM-DD):",
                            new SimpleDateFormat("yyyy-MMM-dd").format(resultSet.getDate("tgl_lahir")));
                    if (tglLahirStr != null) {
                        try {
                            Date tglLahir = Date.valueOf(tglLahirStr);
                            String alamat = JOptionPane.showInputDialog(this, "Alamat:",
                                    resultSet.getString("alamat"));

                            // Lakukan proses update data di database
                            String updateQuery = "UPDATE pasien SET nama = ?, tgl_lahir = ?, alamat = ? WHERE nik = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                            preparedStatement.setString(1, nama);
                            preparedStatement.setDate(2, tglLahir);
                            preparedStatement.setString(3, alamat);
                            preparedStatement.setString(4, resultSet.getString("nik"));
                            preparedStatement.executeUpdate();

                            // Setelah berhasil update data, perbarui tampilan tabel
                            tableModel.setValueAt(nama, selectedRow, 1);
                            tableModel.setValueAt(tglLahirStr, selectedRow, 3);
                            tableModel.setValueAt(alamat, selectedRow, 4);
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
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                resultSet.absolute(selectedRow + 1);
                String nik = resultSet.getString("nik");

                // Lakukan proses penghapusan data dari database
                String deleteQuery = "DELETE FROM pasien WHERE nik = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, nik);
                preparedStatement.executeUpdate();

                // Setelah berhasil hapus data, hapus juga dari tampilan tabel
                tableModel.removeRow(selectedRow);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.");
        }
    }

    // Method untuk berpindah ke data sebelumnya
    private void prevData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow > 0) {
            table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            table.scrollRectToVisible(table.getCellRect(selectedRow - 1, 0, true));
        }
    }

    // Method untuk berpindah ke data selanjutnya
    private void nextData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < tableModel.getRowCount() - 1) {
            table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            table.scrollRectToVisible(table.getCellRect(selectedRow + 1, 0, true));
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
        Object[][] data = new Object[tableModel.getRowCount()][5];

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < 5; j++) {
                data[i][j] = tableModel.getValueAt(i, j);
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
