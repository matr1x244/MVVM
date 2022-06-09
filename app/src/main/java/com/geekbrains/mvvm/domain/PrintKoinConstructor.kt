package com.geekbrains.mvvm.domain

class PrintKoinConstructor(private val data: String) {

    fun showPrintConstructor() {
        println("@@@ Koin showPrintConstructor() $data")
    }
}