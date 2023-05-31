/*
 * Copyright (c) 2022 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 1.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.projectUAS.hitungbmi.ui.hitung

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectUAS.hitungbmi.db.BmiDao
import com.projectUAS.hitungbmi.db.BmiEntity
import com.projectUAS.hitungbmi.model.HasilBmi
import com.projectUAS.hitungbmi.model.KategoriBmi
import com.projectUAS.hitungbmi.model.hitungBmi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HitungViewModel(private val db: BmiDao) : ViewModel() {

    // Hasil BMI bisa null jika pengguna belum menghitung BMI
    private val hasilBmi = MutableLiveData<HasilBmi?>()

    // Navigasi akan bernilai null ketika tidak bernavigasi
    private val navigasi = MutableLiveData<KategoriBmi?>()

    fun hitungBmi(berat: Float, tinggi: Float, isMale: Boolean) {
        val dataBmi = BmiEntity(
            berat = berat,
            tinggi = tinggi,
            isMale = isMale
        )
        hasilBmi.value = dataBmi.hitungBmi()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(dataBmi)
            }
        }
    }

    fun getHasilBmi(): LiveData<HasilBmi?> = hasilBmi

    fun mulaiNavigasi() {
        navigasi.value = hasilBmi.value?.kategori
    }

    fun selesaiNavigasi() {
        navigasi.value = null
    }

    fun getNavigasi() : LiveData<KategoriBmi?> = navigasi
}