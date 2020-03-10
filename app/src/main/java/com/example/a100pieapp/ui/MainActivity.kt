package com.example.a100pieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a100pieapp.R
import com.example.a100pieapp.ui.adapter.ListAdapter
import com.example.a100pieapp.viewModel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, ListViewModel.ListViewModelFactory(this))
            .get(ListViewModel::class.java)
    }
    private val listAdapter by lazy {
        ListAdapter(this, ArrayList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        populateList()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //private method
    private fun initView() {
        var linearLayoutManager = LinearLayoutManager(this)
        list_view.layoutManager = linearLayoutManager
        list_view.adapter = listAdapter
        var itemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        list_view.addItemDecoration(itemDecoration)

        viewModel.currencyList.observe(this, Observer {
            listAdapter.addItem(it)
        })
        viewModel.showProgressBar.observe(this, Observer {
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun populateList() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.populateUI()
        }
    }
}
