package com.android.bishkekroutes

import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.bishkekroutes.databinding.FragmentPlacesBinding
import com.android.bishkekroutes.databinding.FragmentRoutesBinding
import com.android.bishkekroutes.fragments.RoutesFragment
import com.android.bishkekroutes.fragments.RoutesFragmentDirections
import com.android.bishkekroutes.model.Info
import com.android.bishkekroutes.model.Place
import com.android.bishkekroutes.model.Route
import com.android.bishkekroutes.viewmodel.PlacesViewModel
import com.android.bishkekroutes.viewmodel.RoutesViewModel
import com.squareup.picasso.Picasso


class PlacesFragment : Fragment(), SearchView.OnQueryTextListener{
    lateinit var viewModel: PlacesViewModel
    lateinit var binding: FragmentPlacesBinding
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewPlaces
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val view = binding.root
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.placesLiveData.observe(
            viewLifecycleOwner,
            Observer { places ->
                recyclerView.adapter =  PlacesFragment.PlacesAdapter(places)

            })
    }
    private class PlaceHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewAddres: TextView
        val cardView: CardView
        val imageView: ImageView

        init {
            textViewName= view.findViewById(R.id.streetsText)
            textViewAddres = view.findViewById(R.id.lengthText)
            cardView = view.findViewById(R.id.cardView)
            imageView = view.findViewById(R.id.imageView)

        }
        fun bindName(place: Place){
            textViewName.text = "${place.name}"
            textViewName.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewName.textSize = 32.0F
            textViewAddres.text = "${place.address}"
            val width: Int = Resources.getSystem().getDisplayMetrics().widthPixels
            Picasso.get()
                .load(place.link[0])
                .resize(width, 600)
                .centerCrop()
                .into(imageView)
            cardView.setOnClickListener {
                val action = PlacesFragmentDirections.actionPlacesFragmentToMapsFragment2(Info(
                    listOf(place.x),
                    listOf(place.y),
                    place.link,
                    place.description
                ))
                cardView.findNavController().navigate(action)
            }
        }
    }
    //инициализация RecyclerView
    private class PlacesAdapter(private val placeItems: List<Place>) : RecyclerView.Adapter<PlacesFragment.PlaceHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ):PlacesFragment.PlaceHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.route_list_item, parent, false)
            return PlacesFragment.PlaceHolder(view)
        }

        override fun getItemCount(): Int = placeItems.size

        override fun onBindViewHolder(holder: PlacesFragment.PlaceHolder, position: Int) {
            val placeItem = placeItems[position]
            holder.bindName(placeItem)
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
        val searchList = viewModel.placesList.filter {
            it.name.contains(query)
        }
        recyclerView.adapter = PlacesAdapter(searchList)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText == null){
            return false
        }
        val searchList = viewModel.placesList.filter {
            it.name.lowercase().contains(newText.lowercase())
        }
        recyclerView.adapter = PlacesAdapter(searchList)
        return true
    }
}