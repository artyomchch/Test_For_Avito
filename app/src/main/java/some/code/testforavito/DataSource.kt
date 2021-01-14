package some.code.testforavito

import some.code.testforavito.models.NumberPost

class DataSource {

    companion object{
        private val list = ArrayList<NumberPost>()
        fun createDataSet(): ArrayList<NumberPost>{

            var counter = 0

            while (counter != 15){
                list.add(
                    NumberPost(  counter.toString(),
                                "delete")
                )
                counter++
            }

            return list
        }

        fun addDataSet(position: Int) {
            list.add(
                NumberPost(position.toString(), "delete")
            )

        }
    }
}