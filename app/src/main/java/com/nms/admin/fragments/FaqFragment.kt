package com.nms.nmsadminapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.admin.R
import com.example.admin.adapters.FaqListAdapter
import com.example.admin.models.FaqModel
import com.example.admin.repo.FaqRepository
import com.example.admin.utils.Helper
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
                        val listView = view.findViewById<ExpandableListView>(R.id.faq_list_view)
                        val faqListAdapter = FaqListAdapter(requireContext(), list)
                        val listAdapter: ExpandableListAdapter = faqListAdapter // cast to ExpandableListAdapter
                        listView.setAdapter(listAdapter)
                    }
                    else -> {
                        Helper.showToast(requireContext(), "Error fetching faqs")
                    }
                }
            }
        }
    }
}