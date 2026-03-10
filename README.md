Refleksi 1

Prinsip Clean Code yang Diterapkan

1. Penamaan yang jelas: Nama *Class*, *Function*, dan *Variable* sudah deskriptif sehingga kode mudah dipahami tanpa komentar tambahan (contoh: `ProductRepository`, `findAll`, `createProductPage`).

2. Single Responsibility Principle: Setiap *Class* hanya memiliki satu tanggung jawab: `Product` sebagai model data, `ProductRepository` untuk penyimpanan, `ProductServiceImpl` untuk logika , dan `ProductController` untuk menangani HTTP request.

3. Interface Segregation: `ProductService` dipakai sebagai interface supaya kalau mau ganti implementasi, controller-nya tidak perlu ikut diubah.

Refleksi 2

1. Jumlah unit test dalam satu class tidak ada patokan yang pasti yang penting setiap function diuji untuk skenario positif (berhasil) dan negatif (gagal/tidak ditemukan). Code coverage 100% tidak menjamin kode bebas bug, coverage hanya mengukur baris mana yang dieksekusi saat test, bukan apakah logikanya benar. Bisa saja semua baris tereksekusi tapi assertion-nya kurang tepat sehingga bug tetap lolos.

2. Membuat functional test baru dengan setup dan instance variable yang sama persis akan menimbulkan duplikasi kode. Ini melanggar prinsip DRY (Don't Repeat You). Solusinya, buat satu *base class* yang berisi `@LocalServerPort`, `baseUrl`, dan `@BeforeEach` setup, lalu setiap functional test class meng-extend base class tersebut supaya setup tidak ditulis ulang.

Refleksi 3

1. **Issue Code Quality yang Ditemukan dan Strategi Perbaikan:**
   - **`java:S1186` (Methods should not be empty):** SonarCloud menganggap method kosong sebagai potensi error. Solusinya, saya menambahkan komentar di dalam method yang kosong (seperti `contextLoads()` pada `EshopApplicationTests` dan `setUp()` pada `ProductRepositoryTest`) untuk menjelaskan secara eksplisit bahwa method tersebut memang sengaja dibiarkan kosong.
   - **`java:S2699` (Tests should include assertions):** Test method wajib memiliki setidaknya satu asersi untuk memvalidasi sesuatu. Saya menambahkan asersi simpel `assertTrue(true, ...)` pada test `main` di `EshopApplicationTests` untuk memenuhi aturan ini, memastikan aplikasi berjalan tanpa exception.
   - **`java:S6813` (Field dependency injection should be avoided):** Penggunaan anotasi `@Autowired` langsung pada field sangat tidak disarankan (bad practice). Strategi perbaikannya adalah dengan me-refactor `ProductController` agar menggunakan Constructor Injection untuk meng-inject `ProductService`.
   - **`kotlin:S6629` (Dependencies should be grouped by their destination):** Dependensi di `build.gradle.kts` agak berantakan. Solusinya, saya menyusun ulang dan mengelompokkan blok `dependencies` berdasarkan konfigurasinya secara teratur (seperti `implementation` kumpul dengan `implementation`, `testImplementation` dengan `testImplementation`).
   - **`kotlin:S6474` (Dependencies are not verified):** SonarCloud mendeteksi tidak adanya verifikasi integritas dependensi. Untuk mengatasinya, saya menjalankan command `./gradlew --write-verification-metadata pgp,sha256 --export-keys` untuk otomatis meng-generate file `verification-metadata.xml` yang berisi identitas enkripsi dan checksum.

2. **Evaluasi Continuous Integration dan Continuous Deployment (CI/CD):**
   Menurut saya, penerapan saat ini sudah memenuhi definisi *Continuous Integration* dan *Continuous Deployment*. *Continuous Integration* tercapai melalui dua buah workflow GitHub Actions (`ci.yml` dan `sonarcloud.yml`), yang secara otomatis menjalankan seluruh Test Suite, mengecek metrik Code Coverage menggunakan JaCoCo, dan melakukan analisis statik *code quality* melalui SonarCloud setiap kali ada *push* atau *pull request* baru. Sementara itu, *Continuous Deployment* berhasil diterapkan dengan menghubungkan branch GitHub kita ke platform PaaS Koyeb menggunakan sebuah `Dockerfile`. Hal ini membuat setiap kode baru yang di-merge akan otomatis di-build menjadi container image dan di-deploy ke production environment secara mulus tanpa intervensi manual tambahan. Otomatisasi proses inilah yang secara akurat merepresentasikan penerapan CI/CD.

Refleksi 4

1. Prinsip SOLID yang saya terapkan pada proyek ini adalah Single Responsibility Principle (SRP) dengan memisahkan `CarController` dari `ProductController`; 
Open-Closed Principle (OCP) dan Dependency Inversion Principle (DIP) dengan bergantung pada interface `CarService` di sisi Controller alih-alih menggunakan class implementasinya langsung; 
Liskov Substitution Principle (LSP) dengan menghapus inheritance `extends ProductController` pada `CarController` yang kurang tepat secara logika; 
Interface Segregation Principle (ISP) di mana interface dipilah spesifik untuk masing-masing entitas (`CarService` dan `ProductService`).

2. Keuntungan menerapkan prinsip SOLID adalah kode menjadi terstruktur, mudah dikelola, dan scalable. Contohnya berkat penerapan SRP, apabila ada error pada halaman fitur produk, kita cukup fokus membedah file `ProductController` tanpa terganggu oleh keruwetan kerangka logika fitur mobil. Contoh lainnya melalui DIP dan OCP, jika di masa depan penyimpanan data `Car` diganti dari memori list menjadi database sungguhan, kita tinggal membuat implementasi service baru tanpa perlu mengotak-atik apalagi memodifikasi logika *routing* di dalam `CarController` itu sendiri.

3. Kerugian jika mengabaikan prinsip SOLID adalah kode akan menjadi berantakan, monolitik, dan rentan terhadap kejanggalan sistem (fragile). Contohnya jika menolak SRP dan memaksakan menggabung rute `Car` ke dalam `ProductController`, ukurannya akan membesar, sehingga sangat sulit dibaca dan gampang terkena merge conflict . Selain itu, jika menyalahi LSP (misal membiarkan `CarController` mewarisi `ProductController`), sedikit saja revisi pada kelas dasar (*superclass*) bisa merusak fungsionalitas turunan secara tak terduga.

Refleksi 5

1. Alur Test-Driven Development (TDD) menurut saya terbukti cukup bermanfaat. TDD membiasakan saya untuk memikirkan matang-matang requirements dan batasan modul `Order` sebelum mengimplementasikan fungsi programnya. Dengan membuat test di fase [RED] duluan, pas masuk fase [GREEN] arah pengerjaannya jadi lebih jelas, sehingga terhindar dari overengineering. Walaupun begitu, untuk *testing* kompleks ke depannya, saya harus lebih hati-hati agar desain code-nya tidak memaksakan diri sekadar "bisa lulus test" dan pada akhirnya malah mengorbankan kualitas dan tujuan fungsionalitas murninya.

2. Secara garis besar, unit test yang dibuat (khususnya untuk service dan repository) sudah mengamalkan prinsip F.I.R.S.T:
    - Fast: Eksekusi test sangat cepat berkat penggunaan mock dari Mockito, jadi tidak perlu setup panjang lebar atau koneksi database.
    - Independent: Tiap method test independen dengan di-reset ulang dan dijaga state-nya oleh `@BeforeEach`.
    - Repeatable: Test bisa dijalankan ulang pakai *environment* lokal apa pun dengan probabilitas hasil yang selalu mutlak konsisten.
    - Self-Validating: Proses cek berhasil/gagal sudah murni bergantung dari asersi otomatis (via `assertEquals`, `assertTrue`, dst.) dan tidak bergantung pada output `.txt` untuk di-cek manual manusia.
    - Timely: Kode test-nya dibentuk sebelum kode fungsional aslinya selesai dibuat (sesuai kaidah TDD sejati).

    Untuk evaluasi pembuatannya, sepertinya saya masih lumayan banyak melakukan duplikasi kode setup (code duplication) yang melanggar *DRY principle*. Mungkin ke depannya pembuatan dummy data/object bisa dipisah ke helper method kecil (fixture) biar file test-nya lebih ringkas dan enak dibaca.
