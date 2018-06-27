package com.android.dreams.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.dreams.R
import com.android.dreams.data.adapter.DreamsAdapter
import com.android.dreams.data.model.Dream
import com.android.dreams.data.viewmodel.DreamsViewModel
import io.realm.RealmList
import kotlinx.android.synthetic.main.dreams_fragment.*

/**
 * Created by kombo on 27/06/2018.
 */

/**
 * ViewLifecycleFragment is extended to account for instances where fragment may be detached from Activity
 */
class DreamsFragment : ViewLifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dreams_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        loadData()
    }

    //attach ViewModel to fragment to begin data load
    private fun loadData() {
        val dreamsViewModel = ViewModelProviders.of(this).get(DreamsViewModel::class.java)
        dreamsViewModel.getDreamsList().observe(lifecyclerOwner!!, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    showResults(it)
                } else {
                    toggleViewsVisibility()
                }
            }
        })
    }

    private fun showResults(dreamsList: RealmList<Dream>) {
        dreamsRecycler.layoutManager = LinearLayoutManager(activity as AppCompatActivity)

        val adapter = DreamsAdapter(activity as AppCompatActivity, dreamsList)
        dreamsRecycler.adapter = adapter

        empty.visibility = View.GONE
        dreamsRecycler.visibility = View.VISIBLE
    }

    private fun toggleViewsVisibility() {
        empty.visibility = View.VISIBLE
        dreamsRecycler.visibility = View.GONE
    }
}