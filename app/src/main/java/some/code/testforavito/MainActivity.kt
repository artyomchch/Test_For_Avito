package some.code.testforavito

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.random.Random

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NumberRecyclerAdapter.OnItemClickListener {
    private lateinit var numberAdapter: NumberRecyclerAdapter
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        addDataSet()
        startRepeating()
    }

    fun startRepeating() {
        //mHandler.postDelayed(mToastRunnable, 0)
        mToastRunnable.run()
    }

    private val mToastRunnable: Runnable = object : Runnable {
        override fun run() {
            asyncRandomNumbers(5)
            Toast.makeText(this@MainActivity, "This is a delayed toast", Toast.LENGTH_SHORT).show()
            mHandler.postDelayed(this, 5000)
        }
    }


    private fun addDataSet(){
        val data = DataSource.createDataSet()
        numberAdapter.submitList(data)

    }

    fun insertItem(randomNumber: Int){
        val index = Random.nextInt(randomNumber)
        DataSource.addDataSet(index)
        numberAdapter.notifyItemInserted(index)

    }


    fun removeItem(position: Int){
        DataSource.deletePosition(position)
        numberAdapter.notifyItemRemoved(position)
    }



    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        Log.d("Thread", "Thread is -> ${Thread.currentThread()} ")
        //asyncRandomNumbers(5)
        removeItem(position)
        numberAdapter.notifyItemChanged(position)
    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            numberAdapter = NumberRecyclerAdapter(this@MainActivity)
            adapter = numberAdapter
        }
    }




    private fun asyncRandomNumbers(randomNumber: Int) = runBlocking{
        GlobalScope.launch {
            val index = Random.nextInt(randomNumber)
            Log.d("Thread", "from thread ${Thread.currentThread().name}")
           // delay(5000)
            DataSource.addDataSet(index)
            returnDataOnMainThread(index)
        }

    }

    suspend fun returnDataOnMainThread(index: Int){
        return withContext(Dispatchers.Main){
            Log.d("Thread", "from thread ${Thread.currentThread().name}")
            numberAdapter.notifyItemInserted(index)
        }
    }










}