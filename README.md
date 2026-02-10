Refleksi 1

Prinsip Clean Code yang Diterapkan

1. Penamaan yang jelas: Nama *Class*, *Function*, dan *Variable* sudah deskriptif sehingga kode mudah dipahami tanpa komentar tambahan (contoh: `ProductRepository`, `findAll`, `createProductPage`).

2. Single Responsibility Principle: Setiap *Class* hanya memiliki satu tanggung jawab: `Product` sebagai model data, `ProductRepository` untuk penyimpanan, `ProductServiceImpl` untuk logika , dan `ProductController` untuk menangani HTTP request.

3. Interface Segregation: `ProductService` dipakai sebagai interface supaya kalau mau ganti implementasi, controller-nya tidak perlu ikut diubah.

Refleksi 2

1. Jumlah unit test dalam satu class tidak ada patokan yang pasti yang penting setiap function diuji untuk skenario positif (berhasil) dan negatif (gagal/tidak ditemukan). Code coverage 100% tidak menjamin kode bebas bug, coverage hanya mengukur baris mana yang dieksekusi saat test, bukan apakah logikanya benar. Bisa saja semua baris tereksekusi tapi assertion-nya kurang tepat sehingga bug tetap lolos.

2. Membuat functional test baru dengan setup dan instance variable yang sama persis akan menimbulkan duplikasi kode. Ini melanggar prinsip DRY (Don't Repeat You). Solusinya, buat satu *base class* yang berisi `@LocalServerPort`, `baseUrl`, dan `@BeforeEach` setup, lalu setiap functional test class meng-extend base class tersebut supaya setup tidak ditulis ulang.
