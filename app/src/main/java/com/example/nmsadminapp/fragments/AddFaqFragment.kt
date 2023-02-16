package com.example.nmsadminapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nmsadminapp.R
import com.example.nmsadminapp.models.FaqModel
import com.example.nmsadminapp.repo.FaqRepository
import com.example.nmsadminapp.utils.Helper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFaqFragment : Fragment() {
    private lateinit var question: TextInputEditText
    private lateinit var answer: TextInputEditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the views
        initViews(view)

        // Add Faq button click listener to navigate to FaqFragment
        view.findViewById<View>(R.id.add_faq_button).setOnClickListener {
            addNewFaq()
        }
    }

    private fun addNewFaq() {
        // Validate input
        if (!validateInput()) {
            return
        }

        // Get the input
        val question = question.text.toString()
        val answer = answer.text.toString()

        val faq = FaqModel(
            question = question,
            answer = answer
        )

        // send the data to the server
        CoroutineScope(Dispatchers.IO).launch {
            val response = FaqRepository.add(faq, requireContext())
            withContext(Dispatchers.Main) {
                when (response.code) {
                    201 -> {
                        Helper.showToast(requireContext(), "Faq added successfully")
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, FaqFragment())
                            .commit()
                    }
                    else -> {
                        val code = response.code.toString()
                        Helper.showToast(requireContext(), "Error adding faq, try again $code")
                    }
                }
            }
        }
    }

    // Function to initialize the views
    private fun initViews(view: View) {
        question = view.findViewById(R.id.et_faq_question)
        answer = view.findViewById(R.id.et_faq_answer)
    }

    // Function to validate the input
    private fun validateInput(): Boolean {
        if (question.text.toString().isEmpty()) {
            question.error = "Question is required"
            return false
        }
        if (answer.text.toString().isEmpty()) {
            answer.error = "Answer is required"
            return false
        }
        return true
    }
}