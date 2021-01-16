package some.code.testforavito

import android.util.Log
import some.code.testforavito.logic.NumberLogic
import some.code.testforavito.models.NumberPost
import kotlin.random.Random

class DataSource {

    companion object{
        private val list = ArrayList<NumberPost>()
        private val sortNumber : ArrayList<Int> = arrayListOf()

        fun createDataSet(): ArrayList<NumberPost>{
            var i = 0
            while (i != 3) {
                val index = Random.nextInt(100)
                sortNumber.add(index)
                i++
            }
            sortNumber.sort()
            for (s in sortNumber){
                list.add(
                    NumberPost(s, list.size)
                )
            }
            return list
        }

        fun addDataSet(number: Int): Int {
            val singleNumber: ArrayList<Int> = arrayListOf()
            var k = 0
            Log.d("check", number.toString() + "\n")

            for(i in list.iterator()){
                Log.d("check", "$i")
            }

            Log.d("Thread", "from thread ${Thread.currentThread().name}")
            if (list.size == 0){
                list.add(0 , NumberPost(number, list.size))
                return 0
            }

            if (number > list[list.size-1].number){
                list.add(list.size , NumberPost(number, list.size))
                return list.size
            }

            if (number < list[0].number){
                list.add(0, NumberPost(number, list.size))
                return 0
            }

            for (i in list.iterator()){
                k++
                if (list[k-1].number < number && list[k].number >= number){
                    list.add(k, NumberPost(number, list.size))
                    return k
                }
                else if (list[k-1].number == number){
                    list.add(k-1, NumberPost(number, list.size))
                    break
                }
                else{
                    continue
                }
            }


           // list.add(2, NumberPost(number, list.size))
            return k-1
        }

        fun deletePosition(position: Int){
            list.removeAt(position)
        }
    }
}