package com.example.kotlinrxmvvm.view

import android.graphics.Bitmap
import android.graphics.ColorSpace
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.kotlinrxmvvm.R
import com.example.kotlinrxmvvm.databinding.FragmentDetailBinding
import com.example.kotlinrxmvvm.model.DogPalette
import com.example.kotlinrxmvvm.viewmodel.DetailsViewModel

class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    private var dogUUID = 0
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        arguments?.let {
            dogUUID = DetailFragmentArgs.fromBundle(it).dogUUID
        }
        viewModel.displaySelectedDogDetail(dogUUID)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currDog.observe(this, Observer { dogs ->
            dogs?.let {
                binding.dog = dogs
                it.imageURL?.let { it ->
                    setUpBackgroundColor(it)
                }
            }
        })
    }

    private fun setUpBackgroundColor(url: String) {
        Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Palette.from(resource).generate { palette ->
                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
                    val currPalette = DogPalette(intColor)
                    binding.pallete = currPalette
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })
    }
}