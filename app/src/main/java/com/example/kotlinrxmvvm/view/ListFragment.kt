package com.example.kotlinrxmvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinrxmvvm.R
import com.example.kotlinrxmvvm.databinding.FragmentListBinding
import com.example.kotlinrxmvvm.viewmodel.ListViewModel


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogListAdapter(arrayListOf())
    lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()
        binding.dogsLists.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }
        binding.swiperefresh.setOnRefreshListener {
            binding.dogsLists.visibility = View.GONE
            binding.errorList.visibility = View.GONE
            binding.listLoading.visibility = View.VISIBLE
            viewModel.refreshNow()
            binding.swiperefresh.isRefreshing = false
        }
        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.dogs.observe(this, Observer { dogs ->
            dogs?.let {
                binding.dogsLists.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(dogs)
            }
        })
        viewModel.dogsLoadError.observe(this, Observer { error ->
            error?.let {
                binding.errorList.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.dogsLoading.observe(this, Observer { loading ->
            loading?.let {
                binding.listLoading.visibility = if (it) View.VISIBLE else View.GONE

                if (it) {
                    binding.errorList.visibility = View.GONE
                    binding.dogsLists.visibility = View.GONE
                }
            }
        })
    }
}