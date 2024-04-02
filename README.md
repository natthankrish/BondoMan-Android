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

## Pembagian kerja anggota kelompok.

-   Header dan Navbar
    -   Header: Nate
    -   Navbar: Nate
-   Login dan Logout
    -   Interface auth service: Nat
    -   Auth repository: Nat
    -   Login page: Nat
    -   Testing auth backend service: Nat
-   Melakukan Penambahan, Pengubahan, dan Penghapusan Transaksi
    -   Entity transaksi: Haziq, Nate
    -   Repository transaksi: Haziq, Nate
    -   ViewModel transaksi: Haziq, Nate
    -   Dao transaksi: Haziq, Nate
    -   Testing repository transaksi: Haziq
    -   Halaman add transaksi: Nate
    -   Halaman edit transaksi: Nate
    -   Intent google maps: Nat
-   Melihat Daftar Transaksi yang Sudah Dilakukan
    -   Halaman transaksi: Nate
    -   RecyclerView transaksi: Nate
-   Melakukan Scan Nota - Halaman Scan Nota
    -   Halaman scan nota: Nat
    -   Camera: Nat
    -   Interface scan service: Nat
-   Melihat Graf Rangkuman Transaksi - Halaman Graf
    -   Halaman graf portrait: Haziq
    -   Halaman graf landscape: Haziq
    -   Transaction-graph adapter: Haziq
-   Menyimpan Daftar Transaksi dalam Format .xlsx, .xls - Halaman Pengaturan
    -   Halaman pengaturan: Haziq
    -   Transaction-excel adapter (write transaction to OutputStream): Haziq
    -   Transaction Downloader (save to download folder): Haziq
    -   Testing transaction-excel adapter: Haziq
-   Intent GMail - Halaman Pengaturan
    -   Send to gmail: Haziq
-   Background Service - Mengecek expiry JWT
    -   Token check service: Nat
    -   Token broadcast receiver: Nat
-   Network Sensing - Deteksi Sinyal
    -   Network sensing: Nat
    -   Network broadcast receiver: Nat
-   Broadcast Receiver - Randomize Transaksi dari Pengaturan
    -   Randomize broadcast receiver: Haziq
-   Twibbon
    -   Halaman twibbon: Nate
    -   Kamera twibbon: Nate

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
