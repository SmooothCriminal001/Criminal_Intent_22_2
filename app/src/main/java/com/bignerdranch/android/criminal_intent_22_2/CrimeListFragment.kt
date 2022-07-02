package com.bignerdranch.android.criminal_intent_22_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminal_intent_22_2.databinding.FragmentCrimeListBinding
import com.bignerdranch.android.criminal_intent_22_2.databinding.ListItemCrimeBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect


class CrimeListFragment : Fragment() {

    private val viewModel: CrimeListViewModel by viewModels()
    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Things to be done in a coroutine tied to this fragment's lifecycle
        viewLifecycleOwner.lifecycleScope.launch {
        	//Things that have to be done every time a fragment hits a specific lifecycle
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.crimes.collect{ crimes ->
                    binding.crimeRecyclerView.adapter = CrimeListAdapter().apply { submitList(crimes) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private inner class CrimeViewHolder(private val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root){

    	fun bind(crime : Crime){
    		binding.crimeTitle.text = crime.title
            binding.crimeDate.text = crime.date.toString()

    		binding.root.setOnClickListener{
                Toast.makeText(
                    binding.root.context,
                    "${crime.title} clicked!",
                    Toast.LENGTH_SHORT
                ).show()
    		}

            binding.crimeSolved.visibility = if(crime.isSolved){
                View.VISIBLE
            }
            else{
                View.GONE
            }
    	}
    }


    object DiffUtilInstance : DiffUtil.ItemCallback<Crime>() {
    	override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
    		return oldItem.id == newItem.id
    	}

    	override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
    		return (oldItem.toString() == newItem.toString())
    	}
    }

    private inner class CrimeListAdapter() : ListAdapter<Crime, CrimeViewHolder>(DiffUtilInstance){
    	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
    		val inflater = LayoutInflater.from(parent.context)
    		val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
    		return CrimeViewHolder(binding)
    	}

    	override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
    		holder.bind(getItem(position))
    	}

    	override fun getItemViewType(position: Int): Int {

    		return 0
    	}
    }
}