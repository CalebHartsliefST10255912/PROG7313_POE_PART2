package com.example.budgetbee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SimpleItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        searchView.setIconified(false)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SimpleItemAdapter()
        recyclerView.adapter = adapter

        // Sample data source - replace this with your Room DB query
        val sampleData = listOf("Apple", "Banana", "Carrot", "Date", "Eggplant")

        // Set up live search
        lifecycleScope.launch {
            searchView.queryTextChanges()
                .debounce(300)
                .map { query ->
                    sampleData.filter {
                        it.contains(query, ignoreCase = true)
                    }
                }
                .collect { filteredList ->
                    adapter.submitList(filteredList)
                }
        }

        return view
    }
}
