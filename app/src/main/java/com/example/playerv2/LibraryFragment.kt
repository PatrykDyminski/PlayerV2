package com.example.playerv2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_library.*

class LibraryFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val myDataset = ArrayList<String>()
        myDataset.add("fsf")

        //viewManager = LinearLayoutManager(getContext())
        //viewAdapter = MyAdapter(myDataset)

        recyclerView = my_recycler_view.apply {
            //layoutManager = viewManager
            //adapter = viewAdapter
        }
    }


}
