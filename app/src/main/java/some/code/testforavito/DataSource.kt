package some.code.testforavito

import android.util.Log
import some.code.testforavito.models.NumberPost
import kotlin.random.Random

class DataSource {

    companion object{
        private val list = ArrayList<NumberPost>()
        private val sortNumber : ArrayList<Int> = arrayListOf()
        private var deleteNumber: ArrayList<Int> = arrayListOf()

        fun createDataSet(): ArrayList<NumberPost>{  // создания изначального списка
            var i = 0
            while (i != 15) {
                val index = Random.nextInt(150)
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

        fun addDataSet( number: Int): Int { // добавление новых элементов
            var k = 0
            Log.d("check", number.toString() + "\n")

            for(i in list.iterator()){
                Log.d("check", "$i")
            }




            Log.d("Thread", "from thread ${Thread.currentThread().name}")


            if (list.size == 0){  //условие при пустом списке
                list.add(0 , NumberPost(number, list.size))
                return 0
            }

            if (number > list[list.size-1].number){ // условие при наибольшем числе
                list.add(list.size , NumberPost(number, list.size))
                return list.size
            }

            if (number < list[0].number){ //условие при наименьшем числе
                    list.add(0, NumberPost(number, list.size))
                return 0
            }

            for (i in list.iterator()){  // добавления числа в список
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



            return k-1
        }




        fun elements(): ArrayList<Int>{ // список из элементов
            val element: ArrayList<Int> = arrayListOf()
            for (i in list.iterator()){
                element.add(i.number)
            }
            Log.d("elements", element.toString() )
            return element

        }

        fun deleteNumberReturnInt(): Int{  // хранения удаленных чисел
            deleteNumber.sort()
            Log.d("number", "deleteNumberReturnInt: $deleteNumber")

            val del = deleteNumber[0]
            Log.d("number", "deleteNumberReturnInt: $del")
            deleteNumber.remove(del)
            Log.d("number", "deleteNumberReturnInt: $deleteNumber")
            return del
        }

        fun returnNumberDelete(): ArrayList<Int>{ // возращение списка удаленных элементов
            return deleteNumber
        }





        fun deletePosition(position: Int){ //удаления элемента при нажатии кнопки
            deleteNumber.add(list[position].number)
            list.removeAt(position)

        }
    }
}