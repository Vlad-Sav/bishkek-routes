package com.android.bishkekroutes.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.android.bishkekroutes.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    lateinit var fab: View
    private val args by navArgs<MapsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_maps, container, false)
        fab = view.findViewById(R.id.floatingActionButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fab.setOnClickListener{
            val action = MapsFragmentDirections.actionMapsFragmentToInfoFragment(args.info)
            fab.findNavController().navigate(action)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {

        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        val info = args.info

        val cameraPoint = LatLng(info.x[0], info.y[0])
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPoint, 15f))

        val latLngList = arrayListOf<LatLng>()
        for((i, j) in info.x.withIndex())
            latLngList.add(LatLng(j, info.y[i]))
        if(latLngList.size == 1){
            map.addMarker(
                MarkerOptions()
                    .position(latLngList[0])
                    .title("A")
            )
        }else{
            map.addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .addAll(latLngList)
                    .color(Color.BLUE)
                    .width(15f))
            map.addMarker(
                MarkerOptions()
                    .position(latLngList[0])
                    .title("A")
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLngList[latLngList.lastIndex])
                    .title("B")
            )
        }

    }

    override fun onMyLocationClick(location: Location) {

    }

    override fun onMyLocationButtonClick(): Boolean {

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }
}