package com.example.nmsadminapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nmsadminapp.R
import com.example.nmsadminapp.models.FaqModel

class FaqAdapter(private val faq: Array<FaqModel>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question = itemView.findViewById<TextView>(R.id.tv_question)!!
        val answer = itemView.findViewById<TextView>(R.id.tv_answer)!!
        private val ansContainer = itemView.findViewById<View>(R.id.answer_container)

        // Add click listener to expand and collapse answer
        init {
            itemView.setOnClickListener {
                if (ansContainer.visibility == View.GONE) {
                    // expand answer container and add animation
                    ansContainer.visibility = View.VISIBLE
                    ansContainer.alpha = 0f
                    ansContainer.translationY = 100f
                    ansContainer.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .start()

                    // set height to wrap content to show answer text
                    ansContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    // collapse answer container and add animation
                    ansContainer.visibility = View.GONE
                    ansContainer.alpha = 1f
                    ansContainer.translationY = 0f
                    ansContainer.animate()
                        .alpha(0f)
                        .translationY(100f)
                        .setDuration(300)
                        .start()

                    // set height to 0 to hide answer text
                    ansContainer.layoutParams.height = 0
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rc_list_item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.question.text = faq[position].question
        holder.answer.text = faq[position].answer
    }

    override fun getItemCount(): Int = faq.size
}