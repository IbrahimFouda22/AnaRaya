//package com.anaraya.anaraya.home.more.surveys.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.anaraya.anaraya.databinding.LayoutItemLastSurveyDetailsBinding
//import com.anaraya.anaraya.databinding.LayoutItemSurveyBinding
//import com.anaraya.anaraya.databinding.LayoutItemTextBoxBinding
//import com.anaraya.anaraya.home.more.surveys.SurveyItemUiState
//import com.anaraya.anaraya.home.more.surveys.details.SurveyQuestionUiState
//import com.anaraya.anaraya.home.more.surveys.interaction.SurveysInteraction
//
//
//class SurveyDetailsAdapter(private val interaction: SurveysInteraction) :
//    ListAdapter<SurveyQuestionUiState, SurveyDetailsAdapter.BaseViewHolder>(
//        SurveysDiffUtil()
//    ) {
//
//    abstract class BaseViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
//
//    }
//
//    class SurveyDetailsViewHolder(val layoutItemSurveyBinding: LayoutItemSurveyBinding) :
//        BaseViewHolder(layoutItemSurveyBinding.root) {
//
//    }
//    class TextBoxViewHolder(val layoutItemTextBoxBinding: LayoutItemTextBoxBinding) :BaseViewHolder(layoutItemTextBoxBinding.root){
//
//    }
//    class LastViewHolder(val layoutItemLastSurveyDetailsBinding: LayoutItemLastSurveyDetailsBinding) :BaseViewHolder(layoutItemLastSurveyDetailsBinding.root){
//
//    }
//
//    class SurveysDiffUtil : DiffUtil.ItemCallback<SurveyQuestionUiState>() {
//        override fun areItemsTheSame(
//            oldItem: SurveyQuestionUiState,
//            newItem: SurveyQuestionUiState,
//        ): Boolean {
//            return oldItem.questionId == newItem.questionId
//        }
//
//        override fun areContentsTheSame(
//            oldItem: SurveyQuestionUiState,
//            newItem: SurveyQuestionUiState,
//        ): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int,
//    ): BaseViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        return SurveyDetailsViewHolder(
//            LayoutItemSurveyBinding.inflate(
//                layoutInflater,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
////        holder.bind(getItem(position)!!)
////        holder.layoutItemSurveyBinding.txtTitleSurvey.setOnClickListener {
////            interaction.onClick(getItem(position).surveyId,getItem(position).title)
////        }
//    }
//
//}