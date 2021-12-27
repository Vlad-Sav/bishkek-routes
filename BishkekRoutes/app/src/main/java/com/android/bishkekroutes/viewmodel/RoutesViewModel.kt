package com.android.bishkekroutes.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.bishkekroutes.DatabaseRepository
import com.android.bishkekroutes.model.Route
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
const val TAG = "route"
class RoutesViewModel: ViewModel() {
    //val database: FirebaseDatabase
    val myRef: DatabaseReference
    lateinit var routesList: ArrayList<Route>
    val routesLiveData: MutableLiveData<List<Route>> by lazy {
        MutableLiveData<List<Route>>()
    }

    init {
        //database = Firebase.database
        myRef = DatabaseRepository.getRefRoutes()
        routesList  = ArrayList()
        //myRef = database.getReference("routes")
        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<Route>()
                for(s in snapshot.children){
                    var route = s.getValue(Route::class.java)
                    list.add(route!!)

                }
                routesList = list
                routesLiveData.value = routesList
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })



    }
}