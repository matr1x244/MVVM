package com.geekbrains.mvvm.domain

class PrintKoin {

    val showPrint = "@@@ + $this" + "Test println Koin (class printKoin)"

    fun showPrintFunc() {
        println("@@@ + $this" + "Test println Koin (showPrintFunc())")
    }
}