package com.example.budgetbee

import android.widget.SearchView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

fun SearchView.queryTextChanges(): Flow<String> = callbackFlow {
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            trySend(newText ?: "")
            return true
        }
    }
    setOnQueryTextListener(listener)
    awaitClose { setOnQueryTextListener(null) }
}
