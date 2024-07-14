package com.anaraya.anaraya.screens.more.surveys.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemSurveyBinding
import com.anaraya.anaraya.screens.more.surveys.SurveyItemUiState
import com.anaraya.anaraya.screens.more.surveys.interaction.SurveysInteraction


class SurveysAdapter(private val interaction: SurveysInteraction) :
    ListAdapter<SurveyItemUiState, SurveysAdapter.SurveysViewHolder>(
        SurveysDiffUtil()
    ) {

    class SurveysViewHolder(val layoutItemSurveyBinding: LayoutItemSurveyBinding) :
        RecyclerView.ViewHolder(layoutItemSurveyBinding.root) {

        fun bind(surveyItemUiState: SurveyItemUiState) {
            layoutItemSurveyBinding.item = surveyItemUiState
            layoutItemSurveyBinding.executePendingBindings()
        }
    }

    class SurveysDiffUtil : DiffUtil.ItemCallback<SurveyItemUiState>() {
        override fun areItemsTheSame(oldItem: SurveyItemUiState, newItem: SurveyItemUiState): Boolean {
            return oldItem.surveyId == newItem.surveyId
        }

        override fun areContentsTheSame(oldItem: SurveyItemUiState, newItem: SurveyItemUiState): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SurveysViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SurveysViewHolder(
            LayoutItemSurveyBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SurveysViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.layoutItemSurveyBinding.txtTitleSurvey.setOnClickListener {
            interaction.onClick(getItem(position).surveyId,getItem(position).title)
        }
    }

}