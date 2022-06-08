package com.geekbrains.mvvm.presentation.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.mvvm.databinding.FragmentMainBinding
import com.geekbrains.mvvm.domain.BaseRepo
import com.geekbrains.mvvm.domain.PrintKoin
import com.geekbrains.mvvm.domain.Repo
import com.geekbrains.mvvm.presentation.viewmodels.MainFragmentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModel()
    private val printKoin: PrintKoin by inject()

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
        println("@@@ ${printKoin.showPrint}")
        println("@@@ ${printKoin.showPrintFunc()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}