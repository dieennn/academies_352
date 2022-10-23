# Skenario Test

## Repository

1. AuthRepository
    - Saat mendapatkan token
      - Memastikan token tidak null
      - Memastikan token sama dengan dummy

    - Ketika register berhasil
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika register gagal
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Error

    - Ketika login berhasil
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika login gagal
      - Memastikan data exception tidak null
      - Memastikan data Result.Error

    - Ketika logout
      - Memastikan method AppPreferences.clearPerfs telah dipanggil

2. StoryRepository
    - Ketika sukses mengambil data list story
      - Memastikan data tidak bernilai null
      - Memastikan data sama dengan data dummy
      - Memastikan jumlah data sama dengan jumlah data dummy

    - Ketika sukses mengambil data list story peta
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika gagal mengambil data list story peta
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Error

    - Ketika sukses membuat story
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika gagal membuat story
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Error

## ViewModel

1. Register
    - Ketika register berhasil
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika register gagal
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Error

2. Login
    - Ketika login berhasil
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika login gagal
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Error

    - Ketika menyimpan data login
      - Memastikan method AuthRepository.saveLoginInfo telah dipanggil

3. Create Story
    - Ketika sukses membuat story
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Success
      - Memastikan data sama dengan data dummy

    - Ketika gagal membuat story
      - Memastikan data tidak bernilai null
      - Memastikan data adalah Result.Error
