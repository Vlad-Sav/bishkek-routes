package com.android.bishkekroutes

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseRepository {
    companion object {
        private var INSTANCE: FirebaseDatabase? = null
        private var REFERENCE_ROUTES: DatabaseReference? = null
        private var REFERENCE_PLACES: DatabaseReference? = null
        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = Firebase.database
            }
            if(REFERENCE_ROUTES == null || REFERENCE_PLACES == null){
                REFERENCE_ROUTES = INSTANCE!!.getReference("routes")
                REFERENCE_PLACES = INSTANCE!!.getReference("places")
            }
        }
        fun getRefRoutes(): DatabaseReference {
            return REFERENCE_ROUTES ?:
            throw IllegalStateException("Repository must be initialized")
        }
        fun getRefPlaces(): DatabaseReference {
            return REFERENCE_PLACES ?:
            throw IllegalStateException("Repository must be initialized")
        }
    }
}