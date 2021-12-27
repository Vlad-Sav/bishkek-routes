package com.android.bishkekroutes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.bishkekroutes.DatabaseRepository
import com.android.bishkekroutes.model.Place
import com.android.bishkekroutes.model.Route
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PlacesViewModel: ViewModel() {
    //val database: FirebaseDatabase
    val myRef: DatabaseReference
    lateinit var placesList: ArrayList<Place>
    val placesLiveData: MutableLiveData<List<Place>> by lazy {
        MutableLiveData<List<Place>>()
    }

    init {
        //database = Firebase.database
        myRef = DatabaseRepository.getRefPlaces()
        placesList  = ArrayList()
        //myRef = database.getReference("routes")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<Place>()
                for(s in snapshot.children){
                    var place = s.getValue(Place::class.java)
                    list.add(place!!)

                }
                placesList = list
                placesLiveData.value = placesList
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}