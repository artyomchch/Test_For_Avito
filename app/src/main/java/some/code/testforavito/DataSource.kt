package some.code.testforavito

import android.util.Log
import some.code.testforavito.logic.NumberLogic
import some.code.testforavito.models.NumberPost
import kotlin.random.Random

class DataSource {

    companion object{
        private val list = ArrayList<NumberPost>()
        private val sortNumber : ArrayList<Int> = arrayListOf()
        private var deleteNumber: ArrayList<Int> = arrayListOf()

        fun createDataSet(): ArrayList<NumberPost>{
            var i = 0
            while (i != 3) {
                val index = Random.nextInt(200)
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

        fun addDataSet( number: Int): Int {
            var k = 0
            Log.d("check", number.toString() + "\n")

            for(i in list.iterator()){
                Log.d("check", "$i")
            }

//            if (deleteNumber.isNotEmpty()){
//                deleteNumber.sort()
//                val del = deleteNumber[0]
//                deleteNumber.remove(0)
//              //  number = del
//            }



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

        fun addDeleteData(): Int{
            var k = 0

            deleteNumber.sort()
            val del = deleteNumber[0]
            deleteNumber.remove(0)


            if (list.size == 0){
                list.add(0 , NumberPost(del, list.size))
                return 0
            }

            if (del > list[list.size-1].number){
                list.add(list.size , NumberPost(del, list.size))
                return list.size
            }

            if (del < list[0].number){
                list.add(0, NumberPost(del, list.size))
                return 0
            }

            for (i in list.iterator()){
                k++
                if (list[k-1].number < del && list[k].number >= del){
                    list.add(k, NumberPost(del, list.size))
                    return k
                }
                else if (list[k-1].number == del){
                    list.add(k-1, NumberPost(del, list.size))
                    break
                }
                else{
                    continue
                }
            }


            return k-1

        }


        fun elements(): ArrayList<Int>{
            val element: ArrayList<Int> = arrayListOf()
            for (i in list.iterator()){
                element.add(i.number)
            }
            Log.d("elements", element.toString() )
            return element

        }

        fun deleteNumberReturnInt(): Int{
            deleteNumber.sort()
            Log.d("number", "deleteNumberReturnInt: $deleteNumber")

            val del = deleteNumber[0]
            Log.d("number", "deleteNumberReturnInt: $del")
            deleteNumber.remove(del)
            Log.d("number", "deleteNumberReturnInt: $deleteNumber")
            return del
        }

        fun returnNumberDelete(): ArrayList<Int>{
            return deleteNumber
        }





        fun deletePosition(position: Int){
            deleteNumber.add(list[position].number)
            list.removeAt(position)

        }
    }
}