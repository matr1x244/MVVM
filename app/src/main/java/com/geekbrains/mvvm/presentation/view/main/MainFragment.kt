package com.geekbrains.mvvm.presentation.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.geekbrains.mvvm.databinding.FragmentMainBinding
import com.geekbrains.mvvm.domain.PrintKoin
import com.geekbrains.mvvm.domain.PrintKoinConstructor
import com.geekbrains.mvvm.presentation.viewmodels.MainFragmentViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.CopyOnWriteArrayList

class MainFragment : Fragment(), CoroutineScope by MainScope() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModel()
    private val printKoin: PrintKoin by inject()

    private var coroutines2: Job? = null

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
        coroutines()
        coroutines2()
        coroutinesExpetion()
        flowStarting()
        callBackPrint()
//        flowTesting()
        flowDistinctUntilChanged()
    }

    private fun coroutines() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(5_000)
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "coroutines start in MainFragment", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun coroutines2() {
        coroutines2 = CoroutineScope(Dispatchers.IO).launch {
            while (coroutines2?.isActive == true) {
                delay(1_000)
                println("@@@ isActive + ${coroutines2?.isActive}")
                println("@@@ isCancelled + ${coroutines2?.isCancelled}")
                println("@@@ isCompleted + ${coroutines2?.isCompleted}")
            }
        }
        btnStop()
    }

    private fun btnStop() {
        binding.btnStop.setOnClickListener {
            coroutines2?.cancel()
            println("@@@ STOP + $coroutines2")
        }
    }

    private fun coroutinesExpetion() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            println("VVV $throwable")
        }
        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler + SupervisorJob()) {
            val mResult = 10 / 0
            println("VVV + $mResult")
        }
    }

    /**
     * .distinctUntilChanged() если приходит два одинаковых значения - запрос не будет тогда выполняться
     */
    private fun flowDistinctUntilChanged(){
        val someCallBack = SomeCallBack() // создаем экземпляр класса
        val flow = createFlowCallBack(someCallBack) // создаем flow

        flow
            .distinctUntilChanged() // если приходит два одинаковых значения - запрос не будет тогда выполняться
            .onEach {
                println("@@DD index = $it")
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
        binding.btnCallBackFlow.setOnClickListener {
            someCallBack.invoke()
        }
    }

    /**
     * Пример использования корутин flow с использованием фильтра при callback
     * через .filet выводим только +4 шаг. в printlm
     */
    private fun flowTesting(){
        val someCallBack = SomeCallBack() // создаем экземпляр класса
        val flow = createFlowCallBack(someCallBack) // создаем flow

        flow
//            .map { it * 2 }
//            .filter { it % 4 == 0 }
//            .sample(2000) // .sample используется для получения результата каждый "2000" секунды. последний данные отдает при запросе
            .withIndex() // получаем значение по индексу
//            .debounce(1000) // если проходит 1000 сек, приходит ответ если не выдержано это время а запрос идёт тогда данные не будут отдаваться
            .distinctUntilChanged() // если приходит два одинаковых значения - запрос не будет тогда выполняться
            .onEach {
                println("@@EE index = ${it.index} and value = ${it.value}")
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
        binding.btnCallBackFlow.setOnClickListener {
            someCallBack.invoke()
        }
    }

    private fun flowStarting(){
        var flow = flow {
            repeat(5){
                delay(1_000)
                emit("EEE $it") //!!!
            }
        }

        /**
         * первый способ принцип работы
         */

        CoroutineScope(Dispatchers.IO).launch {
                flow
                    .map { it + 2 } // через map можем преобразовать тип и т.д менять какие либо данные
                    .collect {
                 println("EEE ____ $it")
                }
            }

        /**
         * второй способ принцип работы такой же как первый
         */
            flow
                .map { value -> Int } // через map можем преобразовать тип и т.д менять какие либо данные
                .onEach {
                    println("EEE onEACH $it")
                }
                .launchIn(CoroutineScope(Dispatchers.IO))

    }

    private fun callBackPrint(){
        val someCallBack = SomeCallBack() // создаем экземпляр класса
        val flow = createFlowCallBack(someCallBack) // создаем flow

        CoroutineScope(Dispatchers.IO).launch {
            flow.collect{
                println("FFF $it")
            }
        }

        binding.btnCallBackFlow.setOnClickListener {
            someCallBack.invoke() // запускаем слушателя по кнопке
        }
    }

    private fun createFlowCallBack(someCallBack: SomeCallBack) = callbackFlow {
        val listener = object : SomeCallBack.Listener{
            override fun onChange(value: Int) {
                trySend(value) // отправляем данные
            }
        }
        someCallBack.addListener(listener) //добавляем слушателя
        awaitClose{someCallBack.remove(listener)} // отписываем слушателя
    }

    class SomeCallBack {
        var increment = 0
        val listeners = CopyOnWriteArrayList<Listener>() // создаем массив

        fun addListener(listener : Listener) = listeners.add(listener)

        fun remove(listener: Listener) = listeners.remove(listener)

        fun invoke() = listeners.forEach {
            it.onChange(increment++)
            increment = 1 // для  .distinctUntilChanged() private fun flowDistinctUntilChanged()
        } // отслеживаем изменения

        interface Listener {
            fun onChange(value: Int)
        }
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
        cancel()
        super.onDestroyView()
        _binding = null
    }
}