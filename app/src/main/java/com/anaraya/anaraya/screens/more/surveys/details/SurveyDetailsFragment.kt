package com.anaraya.anaraya.screens.more.surveys.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.anaraya.anaraya.R
import com.anaraya.anaraya.composables.CustomButton
import com.anaraya.anaraya.composables.ItemCheckBoxes
import com.anaraya.anaraya.composables.ItemMultipleChoice
import com.anaraya.anaraya.composables.TextFieldTitle
import com.anaraya.anaraya.databinding.FragmentSurveyDetailsBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SurveyDetailsFragment : Fragment() {
    private val navArgs by navArgs<SurveyDetailsFragmentArgs>()
    private lateinit var binding: FragmentSurveyDetailsBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private lateinit var toolBar: MaterialToolbar
    private val mapMultipleChoice: MutableMap<Int, String> = mutableMapOf()
    private val mapMultipleChoicePositions: MutableMap<Int, Int> = mutableMapOf()
    private var mapCheckBoxes: MutableMap<Int, List<String>> = mutableMapOf()
    private val mapTextBox: MutableMap<Int, String> = mutableMapOf()

    @Inject
    lateinit var factory: SurveyDetailsViewModel.AssistedFactory
    private val viewModel by viewModels<SurveyDetailsViewModel> {
        SurveyDetailsViewModel.createFactory(navArgs.surveyId, factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSurveyDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)
        toolBar = requireActivity().findViewById(R.id.toolBarActivity)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.surveysUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.msgSubmitSurvey != null) {
                    if(it.isSucceedSubmitSurvey)
                        Toast.makeText(context, it.msgSubmitSurvey, Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context,
                            getString(R.string.please_fill_out_all_fields), Toast.LENGTH_SHORT).show()
                }
                if (it.survey != null) {
                    toolBar.title = it.survey.title
                    if (it.survey.surveyQuestions.isNotEmpty()) {
                        binding.composeSurveyDetails.apply {
                            // Dispose of the Composition when the view's LifecycleOwner
                            // is destroyed
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                // In Compose world
                                MaterialTheme {
                                    LazyColumn(
                                        contentPadding = PaddingValues(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(it.survey.surveyQuestions) { item ->
                                            if (item.answerType == 3) TextFieldTitle(
                                                title = item.question,
                                                hintText = stringResource(id = R.string.text_here),
                                                value = mapTextBox[item.questionId] ?: ""
                                            ) { value ->
                                                mapTextBox[item.questionId] = value
                                            }
                                            if (item.answerType == 1) {
                                                ItemMultipleChoice(
                                                    title = item.question,
                                                    list = item.questionChoices,
                                                    selected = mapMultipleChoicePositions[item.questionId]
                                                        ?: -1
                                                ) { id, position ->
                                                    mapMultipleChoice[item.questionId] = id
                                                    mapMultipleChoicePositions[item.questionId] =
                                                        position
                                                }
                                            }
                                            if (item.answerType == 2) {
                                                ItemCheckBoxes(
                                                    item.question,
                                                    item.questionChoices,
                                                    mapCheckBoxes[item.questionId] ?: emptyList()
                                                ) { list ->
                                                    mapCheckBoxes[item.questionId] = list
                                                }
                                            }
                                        }
                                        item {
                                            CustomButton {
                                                viewModel.submitSurvey(
                                                    prepareBody()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
//        toolBar.title = navArgs.surveyTitle
        showToolBar(requireActivity(), true)
        showBottomNavBar(requireActivity(), false)
        showCardHome(requireActivity(), false)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(), true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    private fun prepareBody(): SurveyBodyUiState {
        return SurveyBodyUiState(
            surveyId = navArgs.surveyId,
            surveyAnswers = getAllQuestion()
        )
    }

    private fun getAllQuestion(): MutableList<SurveyAnswerBodyUiState> {
        val list: MutableList<SurveyAnswerBodyUiState> = mutableListOf()
        mapTextBox.forEach { (questionId, questionAnswer) ->
            list.add(
                SurveyAnswerBodyUiState(
                    questionId = questionId,
                    textBoxAnswer = questionAnswer,
                    answerChoiceIds = emptyList()
                )
            )
        }
        mapMultipleChoice.forEach { (questionId, questionAnswer) ->
            list.add(
                SurveyAnswerBodyUiState(
                    questionId = questionId,
                    textBoxAnswer = "",
                    answerChoiceIds = listOf(questionAnswer)
                )
            )
        }
        mapCheckBoxes.forEach { (questionId, questionAnswer) ->
            list.add(
                SurveyAnswerBodyUiState(
                    questionId = questionId,
                    textBoxAnswer = "",
                    answerChoiceIds = questionAnswer
                )
            )
        }
        return list
    }
}