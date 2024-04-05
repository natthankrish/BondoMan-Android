# IF3210 2024 Android SKS

## Anggota kelompok

<table>
    <tr>
        <th>NIM</th>
        <th>Nama</th>
        <th>Panggilan</th>
    </tr>
    <tr>
        <td>13521139</td>
        <td>Nathania Calista Djunaedi</td>
        <td>Nat</td>
    </tr>
    <tr>
        <td>13521162</td>
        <td>Antonio Natthan Krishna</td>
        <td>Nate</td>
    </tr>
    <tr>
        <td>13521170</td>
        <td>Haziq Abiyyu Mahdy</td>
        <td>Haziq</td>
    </tr>
</table>

## Deskripsi aplikasi

Bondoman merupakan aplikasi manajemen transaksi dalam proyek pembangunan seribu candi yang dilakukan oleh Bondowoso. Secara umum, aplikasi ini memiliki fungsionalitas sebagai berikut.

1. Otentikasi pengguna (login dan logout)
2. Create, read, update, delete transaksi
3. Scan nota – memindai nota dan mencatatnya sebagai transaksi
4. Grafik (pie chart) rangkuman transaksi
5. Penyimpanan transaksi dalam file dengan ekstensi .xls dan .xlsx
6. Pengiriman file transaksi melalui gmail
7. Pengecekan expiry JWT secara berkala dengan background service
8. Network sensing – menampilkan pesan jika perangkat tidak terhubung ke internet
9. Generate transaksi random menggunakan broadcast receiver
10. Twibbon

## Library yang digunakan

-   [Retrofit](https://square.github.io/retrofit/) – untuk melakukan HTTP request
-   [Room](https://developer.android.com/training/data-storage/room) – untuk persistence
-   [CameraX](https://developer.android.com/media/camera/camerax) – untuk menggunakan kamera
-   [Lottie](https://lottiefiles.com/) – asset untuk animation loading
-   [Apache POI](https://poi.apache.org/) – untuk menulis file xls/xlsx
-   [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - untuk pie chart
-   [Google play services](https://developers.google.com/android/guides/setup) - untuk lokasi

## Screenshot aplikasi

## Accessibility Testing

1. Halaman Login

    **Sebelum diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.29_1df9db63.jpg>)

    **Setelah diubah**
     ![alt text](<WhatsApp Image 2024-04-05 at 08.58.30_3f8b23a4.jpg>)

     **Perbaikan yang dilakukan**
     1. Membuat input email dan password jadi lebih besar heightnya
     2. Menambahkan hint untuk email dan password
     3. Memperbesar tombol mata
    

2. Halaman Transaksi (Homepage)

    **Sebelum diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.31_7714f3f8.jpg>)

    **Setelah diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.31_4bd2026d.jpg>)

    **Perbaikan yang didlakukan**
    1. Mengubah warna yang digunakan menjadi lebih gelap agar kontras dengan latar belakangnya


3. Halaman Kamera

    **Sebelum diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.32_ec97ef7c.jpg>)

    **Setelah diubah**

    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.32_0af25022.jpg>)
    
    **Perubahan yang dilakukan**
    1. Menambahkan content description untuk kedua tombol (kamera dan gallery)

4. Halaman Graf
    **Tidak ada perubahan**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.33_f8eca732.jpg>)

5. Halaman Twibbon

    **Sebelum diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.33_d1a42f1a.jpg>)

    **Setelah diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.34_30ecbffa.jpg>)

    **Perubahan yang dilakukan**
    1. Menambahkan content description untuk seluruh gambar

6. Halaman Tambah Transaksi

    **Sebelum diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.34_785ddb4c.jpg>)

    **Sesudah diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.34_1b4f6bcb.jpg>)

    **Perubahan yang dilakukan**
    1. Memperbesar size dari input

7. Halaman Edit Transaksi 

    **Tidak ada perubahan**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.35_1d22e9c3.jpg>)

8. Halaman Settings

    **Sebelum diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.35_41d50021.jpg>)

    **Sesudah diubah**
    ![alt text](<WhatsApp Image 2024-04-05 at 08.58.35_fcb1feff.jpg>)

    **Perubahan yang dilakukan**
    1. Menggunakan relative width untuk containernya

<br>

**Keterangan**

Ada beberapa rekomendasi yang tidak kami lakukan karena kami rasa tidak bagus untuk UI/UX nya, misalnya adalah nama halaman yang terletak di pojok kiri atas. Kami direkomendasikan untuk menggunakan relative size, bukan fixed size. Tapi menurut kamu topbar lebih enak dilihat kalau misalnya menggunakan fixed size. Hal serupa juga terjadi di halaman login, yaitu hint email dirasa kurang kontras dengan latar belakangnya. Namun, hint dari sebuah input memang seharusnya tidak terlalu kontras agar tidak membingungkan 

## Pembagian kerja anggota kelompok.

<table>
    <tr>
        <th>No</th>
        <th>Task</th>
        <th>PIC</th>
    </tr>
    <tr>
        <td>1</td>
        <td  colspan="2"><i>Header dan Navbar</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Header</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td></td>
        <td>Navbar</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td>2</td>
        <td  colspan="2"><i>Login dan Logout</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Interface auth service</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td>Auth repository</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td>Login page</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td>Testing auth backend service</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td>3</td>
        <td  colspan="2"><i>Manipulasi Transaksi</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Entity transaksi</td>
        <td>13521162, 13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Repository transaksi</td>
        <td>13521162, 13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>ViewModel transaksi</td>
        <td>13521162, 13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Dao transaksi</td>
        <td>13521162, 13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Testing repository transaksi</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Halaman add transaksi</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td></td>
        <td>Halaman edit transaksi</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td></td>
        <td>Intent google maps</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td>4</td>
        <td  colspan="2"><i>Melihat Daftar Transaksi yang Sudah Dilakukan</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Halaman transaksi</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td></td>
        <td>RecyclerView transaksi</td>
        <td>13521162</td>
    </tr> 
    <tr>
        <td>5</td>
        <td  colspan="2"><i>Melakukan Scan Nota - Halaman Scan Nota</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Halaman scan nota</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td>Camera</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td>Interface scan service</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td>6</td>
        <td  colspan="2"><i>Melihat Graf Rangkuman Transaksi - Halaman Graf</i></td>
    </tr>
    <tr>
        <td></td>
        <td> Halaman graf portrait</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Halaman graf landscape</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Transaction-graph adapter</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td>7</td>
        <td  colspan="2"><i>Menyimpan Daftar Transaksi dalam Format .xlsx, .xls - Halaman Pengaturan</i></td>
    </tr>
    <tr>
        <td></td>
        <td> Halaman pengaturan</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Transaction-excel adapter (write transaction to OutputStream)</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Transaction Downloader (save to download folder)</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td></td>
        <td>Testing transaction-excel adapter</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td>8</td>
        <td  colspan="2"><i>Intent GMail - Halaman Pengaturan</i></td>
    </tr>
    <tr>
        <td></td>
        <td> Send to gmail</td>
        <td>13521170</td>
    </tr>
     <tr>
        <td>9</td>
        <td  colspan="2"><i>Background Service - Mengecek expiry JWT</i></td>
    </tr>
    <tr>
        <td></td>
        <td> Token check service</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td> Token broadcast receiver</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td>10</td>
        <td  colspan="2"><i>Network Sensing - Deteksi Sinyal</i></td>
    </tr>
    <tr>
        <td></td>
        <td> Network sensing</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td></td>
        <td>Network broadcast receiver</td>
        <td>13521139</td>
    </tr>
    <tr>
        <td>11</td>
        <td  colspan="2"><i>Broadcast Receiver - Randomize Transaksi dari Pengaturan</i></td>
    </tr>
    <tr>
        <td></td>
        <td> Randomize broadcast receiver</td>
        <td>13521170</td>
    </tr>
    <tr>
        <td>12</td>
        <td  colspan="2"><i>Twibbon</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Halaman twibbon</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td></td>
        <td>Kamera twibbon</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td>13</td>
        <td  colspan="2"><i>Responsivitas UI</i></td>
    </tr>
    <tr>
        <td></td>
        <td>Orientasi Layar</td>
        <td>13521162</td>
    </tr>
    <tr>
        <td></td>
        <td>Size Layar</td>
        <td>13521162</td>
    </tr>
</table>



## Alokasi jam kerja

<table>
    <tr>
        <th>NIM</th>
        <th>Jam kerja</th>
    </tr>
    <tr>
        <td>13521139</td>
        <td>???</td>
    </tr>
    <tr>
        <td>13521162</td>
        <td>???</td>
    </tr>
    <tr>
        <td>13521170</td>
        <td>???</td>
    </tr>

</table>
