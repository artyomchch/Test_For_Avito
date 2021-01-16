package some.code.testforavito

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_number_list_item.view.*
import some.code.testforavito.models.NumberPost

class NumberRecyclerAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<NumberPost> = ArrayList()







    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NumberViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_number_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NumberViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(numberList: List<NumberPost>){
//        val oldList = items
//        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
//            NumberItemDiffCallback(
//                oldList,
//                numberList
//            )
//        )
        items = numberList
//        diffResult.dispatchUpdatesTo(this)

       //differ.submitList(null)
        differ.submitList(numberList)



    }


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NumberPost>() {


        override fun areContentsTheSame(oldItem: NumberPost, newItem: NumberPost): Boolean {

            return true

        }

        override fun areItemsTheSame(oldItem: NumberPost, newItem: NumberPost): Boolean {
            return true
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)




    class NumberItemDiffCallback(
        var oldNumberList: List<NumberPost>,
        var newNumberList: List<NumberPost>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldNumberList.size
        }

        override fun getNewListSize(): Int {
            return newNumberList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldNumberList == newNumberList)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldNumberList == newNumberList)
        }
    }




   inner class NumberViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView),
    View.OnClickListener
    {
        val numberText = itemView.number_title
        val deleteButton = itemView.delete_button
        val position = itemView.position
        fun bind(numberPost: NumberPost){
            numberText.text = numberPost.number.toString()
            position.text = numberPost.position.toString()
        }

        init {
            deleteButton.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }

        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }


}

