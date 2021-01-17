package some.code.testforavito


import android.app.Activity
import android.content.res.Configuration
import android.media.VolumeShaper
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import some.code.testforavito.models.NumberPost


@Suppress("DEPRECATION", "BlockingMethodInNonBlockingContext")
class MainActivity : AppCompatActivity(), NumberRecyclerAdapter.OnItemClickListener {
    private lateinit var numberAdapter: NumberRecyclerAdapter
    private val mHandler = Handler() // для задержки времени
    private var periodInterval: Int = 0 // начало номера добавления элементов
    private var savedRecyclerLayoutState: Parcelable? = null  // переход к элементу после поврота
    private val BUNDLE_RECYCLER_LAYOUT = "recycler_layout"  // ключ
    private val data = DataSource.createDataSet() // добавления рандомного списка
    private var dataSaveElements = arrayListOf<Int>() // запоминания списка
    private var timerSet: Boolean = false // отсчет таймера





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            initRecyclerView() // инициализация списка
            addDataSet()  // добавления данных в список
            startRepeating() // начало 5 сек добавления рандомных элементов



    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stopRepeating() // остановка добавления элементов

        outState.putInt("period", periodInterval)  // запоминаем период добавления

        outState.putParcelable(
            BUNDLE_RECYCLER_LAYOUT,
            recycler_view.layoutManager?.onSaveInstanceState() // запоминание местоположение
        )



       outState.putIntegerArrayList("data", DataSource.elements()) // запоминание данных

        Log.d("check", "onSaveInstanceState: ")

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("check", "onRestoreInstanceState: ")
        periodInterval = savedInstanceState.getInt("period")  // востановления периода
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT) // вотсановление местоположения
        dataSaveElements = savedInstanceState.getIntegerArrayList("data") as ArrayList<Int> // востановления данных для элементов

        data.clear()  // очищаем данные
        for (i in dataSaveElements){ // добавляем в объект списка
            data.add(
                NumberPost(
                    i, data.size
                )
            )
        }

        for (i in dataSaveElements){
            Log.d("elements", i.toString())
        }



        Log.d("check", "onCreate: $periodInterval")
        super.onRestoreInstanceState(savedInstanceState)

    }


    fun pullEvent(){  // запоминание и добавления в UI (Pull Event) списка удаленных элементов
        DataSource.returnNumberDelete()
        var pull: String = ""
        for (i in DataSource.returnNumberDelete()){
            pull += "$i "
        }
        pull_event.text = pull
    }

    fun startRepeating() {  // добавления раз 5 сек элементов
        mToastRunnable.run()
    }

    fun stopRepeating(){ // отановка добавления
        mHandler.removeCallbacks(mToastRunnable)
    }

    private val mToastRunnable: Runnable = object : Runnable {

        override fun run() {  // триггер на ожидания 5 сек в начале
            if (!timerSet){
                timerSet = true
                mHandler.postDelayed(this, 5000)
            }
            else{
                asyncRandomNumbers(periodInterval)
                mHandler.postDelayed(this, 5000)

            }

        }
    }


    private fun addDataSet(){  // добавление данных
        numberAdapter.submitList(data)
    }



    fun removeItem(position: Int){  // удаление элементов + анимация
        DataSource.deletePosition(position)
        numberAdapter.notifyItemRemoved(position)
    }



    override fun onItemClick(position: Int) { // удаление элемента при нажитие на кнопку
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        Log.d("Thread", "Thread is -> ${Thread.currentThread()} ")
        //asyncRandomNumbers(5)
        removeItem(position)
        numberAdapter.notifyItemChanged(position)
    }

    private fun initRecyclerView(){  // инициализация списка
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            //расположение элементов в зависимоти от поворота экрана
            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT ){
                recycler_view.layoutManager = GridLayoutManager(this@MainActivity, 2)
            }
           else{
                recycler_view.layoutManager = GridLayoutManager(this@MainActivity, 4)
           }
            // оступы от элементов
            val topSpacingItemDecoration = TopSpacingItemDecoration(15)
            addItemDecoration(topSpacingItemDecoration)
            numberAdapter = NumberRecyclerAdapter(this@MainActivity)

            adapter = numberAdapter
        }
    }




    private fun asyncRandomNumbers(period: Int) = runBlocking{  // асинхронная работа
        GlobalScope.launch {
            val index = (period..period+10).random()
            periodInterval += 10


            Log.d("Thread", "from thread ${Thread.currentThread().name}")

            pullEvent() // добавления элементов в пул
            if (DataSource.returnNumberDelete().isNotEmpty()){

                returnDataOnMainThread(DataSource.addDataSet(DataSource.deleteNumberReturnInt())) // возращаем первое число из пула
            }
            else{
                returnDataOnMainThread(DataSource.addDataSet(index))
            }

        }

    }

    suspend fun returnDataOnMainThread(index: Int){ // переход в главный поток
        return withContext(Dispatchers.Main){
            Log.d("Thread", "from thread ${Thread.currentThread().name}")
            Toast.makeText(this@MainActivity, "$index", Toast.LENGTH_SHORT).show()
            numberAdapter.notifyItemInserted(index)
        }
    }










}