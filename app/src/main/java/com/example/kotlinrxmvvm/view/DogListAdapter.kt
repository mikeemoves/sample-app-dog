package com.example.kotlinrxmvvm.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinrxmvvm.R
import com.example.kotlinrxmvvm.databinding.ItemDogBinding
import com.example.kotlinrxmvvm.model.DogBreed

class DogListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    fun updateDogList(newDogLists: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogLists)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val databinding =
            DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)

        return DogViewHolder(databinding)
    }

    override fun getItemCount() = dogsList.size


    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog = dogsList[position]

        holder.view.dogItemLL.setOnClickListener {
            val uuid = holder.view.itemDogID.text.toString().toInt()
            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
            action.dogUUID = uuid
            Navigation.findNavController(holder.view.root).navigate(action)
        }
    }

    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}
