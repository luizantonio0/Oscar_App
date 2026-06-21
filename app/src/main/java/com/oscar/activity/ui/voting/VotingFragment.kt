package com.oscar.activity.ui.voting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oscar.activity.ChooseDirector
import com.oscar.activity.ChooseMovie
import com.oscar.databinding.FragmentVotingBinding

class VotingFragment : Fragment() {

    private var _binding: FragmentVotingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVotingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.cardViewMovie.setOnClickListener {
            startActivity(Intent(activity, ChooseMovie::class.java))
        }

        binding.cardViewDirector.setOnClickListener {
            startActivity(Intent(activity, ChooseDirector::class.java))
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}