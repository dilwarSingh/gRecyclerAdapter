# GenricRecyclerViewAdapter
or you can say
## GRecyclerAdapter
  

    implementation 'in.dilwar:genericRecyclerAdapter:1.1.6'
    

Its an library can be used with Java or kotlin
fully compatiable with Android-DataBinding and AndroidX 


## DataBinding With kotlin-Extentions
 
    val gAdapter = recyclerView.setGenericBindingAdapter<DataModel, ItemRecyclerBinding/*Layout Binding Class*/>(
    				R.layout.item_recycler/*Your Recycler Item Layout*/)
            { viewHolder, data, position ->
             /* viewHolder.binding.data = data
                viewHolder.binding.executePendingBindings() */
            } 
    gAdapter.submitList(emptyList()/*List to Populate data*/)

**Just thats all you need to setup and you are done with the RecyclerView adapter is attached and provided for future modifications**

## Normal With kotlin-Extentions
 
    val gAdapter = recyclerView.setGenericNormalAdapter<DataModel>(R.layout.item_recycler/*Your Recycler Item Layout*/)
            { viewHolder, data, position ->
             /* val textView = viewHolder.view.findViewById<TextView>(R.id.text)
                textView.text = data.name
		
		DO YOUR STUFF HERE
		*/
            } 
    gAdapter.submitList(emptyList()/*List to Populate data*/)


## Full Data Binding
**By this way you can keep all your RecyclerView BoilerPlate Code out of Activity**

#STEP 1 (Create your Recycler Item Layout):
          
    <?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android" xmlns:tools="http://schemas.android.com/tools">
 	<data>
        <variable name="_all" type="com.sample.genericrecycleradapter.dataProviders.DataModel"/>
	 <!-- `variable` name must be `_all` -->
	 <!-- `type` will be your custom Model Class Reference -->
    	</data>
    	<LinearLayout
            android:orientation="vertical"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">

        <TextView
                tools:text="TextView"
                android:text='@{_all.name}'
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:id="@+id/text"
                android:textSize="18sp"
                android:padding="8dp"
                android:textColor="@android:color/black"/>
    </LinearLayout>
    </layout>
    
#STEP 2 (Create Your Recycler View {your Activity Layout}):
          
     <?xml version="1.0" encoding="utf-8"?>
     <layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto">
     <data>
        <import type="com.sample.genericrecycleradapter.dataProviders.DataModel"/>
        <variable name="list" type="java.util.List&lt;DataModel>"/>

	<!-- `variable` `list` will be java.util.List of your custom data model as Defind in the `STEP 1` -->
     <androidx.recyclerview.widget.RecyclerView
                android:layout_width="0dp"
                android:orientation="horizontal"
                app:dataList="@{list}"
                app:layoutRes="@{@layout/item_recycler}"
                app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                android:layout_height="wrap_content"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintHorizontal_bias="0.5"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                tools:listitem="@layout/item_recycler"
                android:layout_marginTop="8dp"
                android:id="@+id/rvFullDataBinding"/>
		
		<!-- the RecyclerView will have 2 Extra Attribute
			`app:dataList` it will receive the input of Custom Data Model List
			`app:layoutRes` it will receive the input of layout File defined in `STEP 1`
		-->
		
     </layout>
		
#STEP 3 (Code in You Activity):
        
	/* Initialization of you Data-Binding Activity Layout */
	val activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_main) 
            activityBinding.list = /* Provide a List of your Custom Model here */
