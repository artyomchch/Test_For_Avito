package some.code.testforavito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import some.code.testforavito.models.NumberPost
import kotlin.random.Random

class MainActivity : AppCompatActivity(), NumberRecyclerAdapter.OnItemClickListener {
    private lateinit var numberAdapter: NumberRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        addDataSet()

    }

    private fun addDataSet(){
        val data = DataSource.createDataSet()
        numberAdapter.submitList(data)
    }

    fun insertItem(randomNumber: Int){
        val index = Random.nextInt(randomNumber)
        DataSource.addDataSet(index)
        numberAdapter.notifyItemInserted(index)
        //val newItem = NumberPost("new item add, pos: $index", "delete")




    }


    fun removeItem(position: Int){

    }



    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        insertItem(4)
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


}