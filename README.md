
# Tugas Personal 2

Tugas Personal 2 untuk mata kuliah Business Application Development


# Hi, I'm Muhammad Jamil - System Information! 👋


## Soal

3.Buatlah sebuah program sederhana untuk sebuah klinik melakukan CRUD (Create, Read, Update, Delete) data pasian yang disimpan dalam sebuah table. Ketentuan program sebagai berikut:
a.Di dalam table minimal terdapat field yang menampung nama (char 20), alamat (char 50), NIK (numeric 15) dan tanggal lahir (date). Field lainnya bebas sesuai kreasi anda.
b.Data pasien tidak boleh ada yang sama no. NIK-nya.
c.Tanggal lahir pasien ditampilan dalam format YYYY-MMM-DD, contoh: 2003-Jul-12
d.Buat tombol2 untuk menambah data, update data dan hapus data, 2 tombol navigasi ke record sebelum dan sesudah current record, dan sebuah tombol untuk daftar pasien.
e.Daftar pasien ditampilkan dalam form berbeda, dengan format sederhana saja seperti dibawah ini:
No	Nama Pasien	NIK	Tanggal Lahir	Alamat
				
Untuk keluar dari program bisa ditambahkan tombol ‘keluar’


| No | Nama     | Nik                | Tanggal Lahir | Alamat     |
| :-------- | :------- | :------------------------- | :-------- | :------- |
|  |  |  |  |  |

## Screenshots

- DB Scheme
  ![DB Scheme](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/db_schema.png)

- Create
![Create](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/create.png)

- Read
![Read](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/list%20get.png)

- Update
![Update](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/update.png)

- Delete
![Delete](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/delete.png)


## Tech Stack

**Client:** Java, Swing
**Server:** JDBC MySQL


## Lessons Learned

- Jika mengalami error "No Suitable Driver Found" saat menjalankan program

![Error](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/error_jdbc.png)

- maka hal yang perlu dilakukan adalah menambah library maven JDBC mysql terbaru ke dalam project

![Add Library](https://raw.githubusercontent.com/jamilmuh/TugasPersonal_2_BAD/master/assets/add_library.png)


## Kritik dan saran

If you have any feedback, please reach out to us at muhammadjamil@binus.ac.id

