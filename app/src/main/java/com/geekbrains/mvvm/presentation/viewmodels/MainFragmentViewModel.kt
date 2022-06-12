package com.geekbrains.mvvm.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekbrains.mvvm.domain.BaseRepo
import com.geekbrains.mvvm.domain.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel() : ViewModel() {

    private val repo: BaseRepo = Repo()

    private val _repos = MutableLiveData<String>()
    val repos: LiveData<String> = _repos

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repo.provideData()
            withContext(Dispatchers.Main){
                _repos.postValue(data) //postValue(..) асинхронный вызов из любого потока!
            }
        }
    }

}