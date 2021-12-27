package com.android.bishkekroutes

import com.smarteist.autoimageslider.SliderViewAdapter

import android.content.Context
import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable

import android.widget.TextView

import android.widget.Toast

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import com.android.bishkekroutes.model.Route
import com.squareup.picasso.Picasso


class SliderAdapter(var list: List<String>): SliderViewAdapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(var view: View): SliderViewAdapter.ViewHolder(view) {
        var imageView: ImageView
        init {
            imageView = view.findViewById(R.id.imageItem)
        }
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        var view: View = LayoutInflater.from(parent?.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        val width: Int = Resources.getSystem().getDisplayMetrics().widthPixels
        Picasso.get()
            .load(list[position])
            .resize(width, 600)
            .centerCrop()
            .into(viewHolder?.imageView)
    }

}