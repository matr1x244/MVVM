package com.geekbrains.mvvm.di.koin

import com.geekbrains.mvvm.domain.PrintKoin
import com.geekbrains.mvvm.presentation.viewmodels.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModuleKoin = module {

    single {
        PrintKoin()
    }

    viewModel { MainFragmentViewModel() }
}