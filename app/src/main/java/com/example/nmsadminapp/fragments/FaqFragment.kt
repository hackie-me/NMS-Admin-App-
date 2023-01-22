package com.example.nmsadminapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nmsadminapp.R
import com.example.nmsadminapp.adapters.FaqAdapter
import com.example.nmsadminapp.models.FaqModel
import com.example.nmsadminapp.repo.FaqRepository
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FaqFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add Faq button click listener to navigate to AddFaqFragment
        view.findViewById<View>(R.id.add_faq_button).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddFaqFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        // Fetch all faqs
        fetchFaqs(requireView())
    }

    // Function to fetch faqs
    private fun fetchFaqs(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = FaqRepository.fetchAll()
            withContext(Dispatchers.Main) {
                when (response.code) {
                    200 -> {
                        val list = Gson().fromJson(response.data, Array<FaqModel>::class.java)
                        val recyclerView = view.findViewById<RecyclerView>(R.id.faq_recycler_view)

                        // set layout manager
                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                        // set adapter
                        recyclerView.adapter = FaqAdapter(list)
                    }
                    else -> {
                        Helper.showToast(requireContext(), "Error fetching faqs")
                    }
                }
            }
        }
    }
}