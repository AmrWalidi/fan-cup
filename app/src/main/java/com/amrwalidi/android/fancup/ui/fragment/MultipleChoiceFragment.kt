package com.amrwalidi.android.fancup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentMultipleChoiceBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.MultipleChoiceViewModel
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val QUESTION = "QUESTION"

class MultipleChoiceFragment(private val questionViewModel: QuestionViewModel) : Fragment() {
    private var question: Question? = null
    private var multipleChoiceViewModel: MultipleChoiceViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(QUESTION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "en") ?: "en"

        val languageIdentifier = LanguageIdentification.getClient()
        lifecycleScope.launch {
            val translatedOptions = mutableListOf<String>()
            for (option in question?.options!!) {
                try {
                    val detectedLang = languageIdentifier.identifyLanguage(option).await()
                    if (detectedLang != "und" && detectedLang != lang) {
                        val translatedText = translateText(detectedLang ?: "en", lang, option)
                        translatedOptions.add(translatedText)
                    } else {
                        Log.e("TranslationError", "No Language detected")
                    }
                } catch (e: Exception) {
                    Log.e("TranslationError", "Error: ${e.message}")
                }
            }
            if (translatedOptions.size == question!!.options.size) {
                question!!.options = translatedOptions
            }
        }

        multipleChoiceViewModel = ViewModelProvider(
            this,
            MultipleChoiceViewModel.Factory(lang, question, requireActivity().application)
        )[MultipleChoiceViewModel::class.java]

        val binding: FragmentMultipleChoiceBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_choice, container, false)

        binding.viewModel = multipleChoiceViewModel
        binding.lifecycleOwner = this

        multipleChoiceViewModel?.correctAnswer?.observe(viewLifecycleOwner) {
            if (it) {
                when (multipleChoiceViewModel!!.selectedAnswer.value) {
                    0 -> binding.answer1.setBackgroundResource(R.drawable.correct_answer_container)
                    1 -> binding.answer2.setBackgroundResource(R.drawable.correct_answer_container)
                    2 -> binding.answer3.setBackgroundResource(R.drawable.correct_answer_container)
                    3 -> binding.answer4.setBackgroundResource(R.drawable.correct_answer_container)
                }
                questionViewModel.successfulCompletion()
            }
        }

        multipleChoiceViewModel?.wrongAnswer?.observe(viewLifecycleOwner) {
            if (it) {
                when (multipleChoiceViewModel!!.selectedAnswer.value) {
                    0 -> binding.answer1.setBackgroundResource(R.drawable.wrong_answer_container)
                    1 -> binding.answer2.setBackgroundResource(R.drawable.wrong_answer_container)
                    2 -> binding.answer3.setBackgroundResource(R.drawable.wrong_answer_container)
                    3 -> binding.answer4.setBackgroundResource(R.drawable.wrong_answer_container)
                }
                questionViewModel.wrongAnswer()
                multipleChoiceViewModel!!.removeWrongAnswer()
            }
        }

        return binding.root
    }

    private suspend fun translateText(
        sourceLang: String,
        targetLang: String,
        text: String
    ): String {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder().requireWifi().build()
        translator.downloadModelIfNeeded(conditions).await()

        return translator.translate(text).await()
    }


}