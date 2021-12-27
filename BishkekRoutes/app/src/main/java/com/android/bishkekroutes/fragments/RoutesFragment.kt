package com.android.bishkekroutes.fragments


import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.bishkekroutes.R
import com.android.bishkekroutes.databinding.FragmentRoutesBinding
import com.android.bishkekroutes.model.Info

import com.android.bishkekroutes.model.Route
import com.android.bishkekroutes.viewmodel.RoutesViewModel
import com.android.bishkekroutes.viewmodel.TAG
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class RoutesFragment : Fragment(), SearchView.OnQueryTextListener {
    lateinit var viewModel: RoutesViewModel
    lateinit var binding: FragmentRoutesBinding
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RoutesViewModel::class.java)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutesBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.routesLiveData.observe(
            viewLifecycleOwner,
            Observer { routes ->
                recyclerView.adapter =  RoutesFragment.RoutesAdapter(routes)

            })
    }

    private class RouteHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewStreets: TextView
        val textViewLength: TextView
        val cardView: CardView
        val imageView: ImageView

        init {
            textViewStreets= view.findViewById(R.id.streetsText)
            textViewLength = view.findViewById(R.id.lengthText)
            cardView = view.findViewById(R.id.cardView)
            imageView = view.findViewById(R.id.imageView)

        }
        fun bindName(route: Route){
            var streets: String = ""
            for(street in route.streets)
                streets += " - ${street}"
            streets = streets.replaceFirst(" - ", "")
            textViewStreets.text = "Улицы:\n${streets}"
            textViewLength.text = "Длина пути: ${distance(route.x, route.y)} км."
            val width: Int = Resources.getSystem().getDisplayMetrics().widthPixels
            Picasso.get()
                .load(route.link[0])
                .resize(width, 600)
                .centerCrop()
                .into(imageView)
            cardView.setOnClickListener {
                val action = RoutesFragmentDirections.actionRoutesFragmentToMapsFragment(Info(
                    route.x,
                    route.y,
                    route.link,
                    route.description
                )
                )
                cardView.findNavController().navigate(action)
            }
        }
        //Расстояние между координатами
        fun distance(lat: List<Double>, lon: List<Double>): Double{
            var dist: Double = 0.0
            for(i in 0 until lat.size - 1){
                dist += distanceFor2(lat[i], lon[i], lat[i + 1], lon[i + 1])
            }
            return Math.round(dist * 10.0) / 10.0
        }
        fun distanceFor2(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double{
            var earthRadiusKm = 6371;

            var dLat = degreesToRadians(lat2-lat1);
            var dLon = degreesToRadians(lon2-lon1);

            var lat12 = degreesToRadians(lat1);
            var lat22 = degreesToRadians(lat2);

            var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat12) * Math.cos(lat22);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return earthRadiusKm * c
        }
        fun degreesToRadians(degrees: Double): Double {
            return degrees * Math.PI / 180;
        }
    }

    //инициализация RecyclerView
    private class RoutesAdapter(private val routeItems: List<Route>) : RecyclerView.Adapter<RoutesFragment.RouteHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ):RoutesFragment.RouteHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.route_list_item, parent, false)
            return RoutesFragment.RouteHolder(view)
        }

        override fun getItemCount(): Int = routeItems.size

        override fun onBindViewHolder(holder: RoutesFragment.RouteHolder, position: Int) {
            val episodeItem = routeItems[position]
            holder.bindName(episodeItem)
        }
    }
    //search view
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.top_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        searchView = search.actionView as SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query == null){
            return false
        }
        val searchList = viewModel.routesList.filter {
            it.streets.filter { it.lowercase().contains(query!!.lowercase()) } .size > 0
        }
        recyclerView.adapter = RoutesAdapter(searchList)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText == null){
            return false
        }
        val searchList = viewModel.routesList.filter {
            it.streets.filter { it.toLowerCase().contains(newText!!.toLowerCase()) } .size > 0
        }
        recyclerView.adapter = RoutesAdapter(searchList)
        return true
    }

}