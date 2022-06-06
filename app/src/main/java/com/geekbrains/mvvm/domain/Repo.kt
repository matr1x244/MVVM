package com.geekbrains.mvvm.domain

import kotlin.random.Random

class Repo : BaseRepo {

    val quotes = "testing println"

    override fun provideData(): String {
        val quotes = arrayOf("One", "Two", "Three", "github", "gitlab", "geekbrains")
        val randomValue = Random.nextInt(quotes.size)
        return quotes[randomValue]
        println(quotes)
    }

}