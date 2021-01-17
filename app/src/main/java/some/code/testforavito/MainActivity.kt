package some.code.testforavito

import android.graphics.Movie
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import some.code.testforavito.models.NumberPost


@Suppress("DEPRECATION", "BlockingMethodInNonBlockingContext")
class MainActivity : AppCompatActivity(), NumberRecyclerAdapter.OnItemClickListener {
    private lateinit var numberAdapter: NumberRecyclerAdapter
    private val mHandler = Handler()
    private var periodInterval: Int = 0
    private var savedRecyclerLayoutState: Parcelable? = null
    private val BUNDLE_RECYCLER_LAYOUT = "recycler_layout"
    private val data = DataSource.createDataSet()
    private var dataSaveElements = arrayListOf<Int>()
    private var timerSet: Boolean = false





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            initRecyclerView()
            addDataSet()
            startRepeating()



    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stopRepeating()

        outState.putInt("period", periodInterval)

        outState.putParcelable(
            BUNDLE_RECYCLER_LAYOUT,
            recycler_view.layoutManager?.onSaveInstanceState()
        )



       outState.putIntegerArrayList("data", DataSource.elements())

        Log.d("check", "onSaveInstanceState: ")

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("check", "onRestoreInstanceState: ")
        periodInterval = savedInstanceState.getInt("period")
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        dataSaveElements = savedInstanceState.getIntegerArrayList("data") as ArrayList<Int>

        data.clear()
        for (i in dataSaveElements){
            data.add(
                NumberPost(
                i, data.size
            ))
        }
        //dataSave.clear()
        for (i in dataSaveElements){
            Log.d("elements", i.toString())
        }

       // stopRepeating()
      //  startRepeating()

        Log.d("check", "onCreate: $periodInterval")
        super.onRestoreInstanceState(savedInstanceState)

    }



    fun startRepeating() {
        //mHandler.postDelayed(mToastRunnable, 5000)
        mToastRunnable.run()
    }

    fun stopRepeating(){
        mHandler.removeCallbacks(mToastRunnable)
    }

    private val mToastRunnable: Runnable = object : Runnable {

        override fun run() {
            if (!timerSet){
                timerSet = true
                mHandler.postDelayed(this, 5000)
            }
            else{
                asyncRandomNumbers(periodInterval)
                mHandler.postDelayed(this, 5000)


            }
          //  Toast.makeText(this@MainActivity, "5 sec", Toast.LENGTH_SHORT).show()


        }
    }


    private fun addDataSet(){
//        val data = DataSource.createDataSet()
        numberAdapter.submitList(data)

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




    private fun asyncRandomNumbers(period: Int) = runBlocking{
        GlobalScope.launch {
            val index = (period..period+10).random()
            periodInterval += 10
          //  Thread.sleep(5000)
          //  val index = Random.nextInt(randomNumber)
            Log.d("Thread", "from thread ${Thread.currentThread().name}")
           // delay(5000)

            returnDataOnMainThread(DataSource.addDataSet(index))
        }

    }

    suspend fun returnDataOnMainThread(index: Int){
        return withContext(Dispatchers.Main){
            Log.d("Thread", "from thread ${Thread.currentThread().name}")
            Toast.makeText(this@MainActivity, "$index", Toast.LENGTH_SHORT).show()
            numberAdapter.notifyItemInserted(index)
        }
    }










}