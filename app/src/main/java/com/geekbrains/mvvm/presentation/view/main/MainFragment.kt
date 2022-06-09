package com.geekbrains.mvvm.presentation.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.mvvm.databinding.FragmentMainBinding
import com.geekbrains.mvvm.domain.BaseRepo
import com.geekbrains.mvvm.domain.PrintKoin
import com.geekbrains.mvvm.domain.PrintKoinConstructor
import com.geekbrains.mvvm.domain.Repo
import com.geekbrains.mvvm.presentation.viewmodels.MainFragmentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModel()
    private val printKoin: PrintKoin by inject()

    /**
     * если у класса уже есть входные параметры
     * (пример: class PrintKoinConstructor(private val data: String)
     * тогда при inject { parametersOf() } - обязательно!!!
     */
    private val printKoinConstructor: PrintKoinConstructor by inject { parametersOf("printKoinConstructor testing") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStart()
        mvvmLiveData()
        koinPrint()
    }

    private fun btnStart() {
        binding.btn.setOnClickListener {
            viewModel.getData()
        }
    }

    private fun mvvmLiveData() {
        viewModel.repos.observe(viewLifecycleOwner) {
            binding.textView.text = it
        }
    }

    private fun koinPrint() {
        binding.textKoin0.text = printKoin.showPrint
        binding.textKoin1.text = printKoin.showPrintFunc().toString()
        println("@@@ ${printKoin.showPrint}")
        println("@@@ ${printKoin.showPrintFunc()}")

        binding.textKoin2.text = printKoinConstructor.showPrintConstructor().toString()
        println("@@@ ${printKoinConstructor.showPrintConstructor()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}