package com.sample.genericrecycleradapter.dataProviders

class DataProvider {
    companion object {
        @JvmStatic
        fun getDummyList(name: String = "name"): List<DataModel> {
            val list = mutableListOf<DataModel>()

            for (i in 1 until 100) {
                list.add(DataModel("${name}_$i"))
            }
            return list
        }
    }
}