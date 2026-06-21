package com.oscar.activity.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.oscar.R
import com.oscar.data.model.Votacao
import com.oscar.databinding.FragmentNotificationsBinding
import com.oscar.repository.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var databaseHelper = DatabaseHelper()
    private val binding get() = _binding!!
    private var votacao: Votacao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadData()

        return root
    }

    fun loadData(){
        lifecycleScope.launch (Dispatchers.Main){
            withContext(Dispatchers.IO){
                votacao = databaseHelper.findVotacao()
            }
            if (votacao == null) return@launch

            if (votacao?.filme != null) {
                binding.checkMovie.setImageResource(R.drawable.check_circle_24dp_fill)
            }

            if (votacao?.diretor != null) {
                binding.checkDirector.setImageResource(R.drawable.check_circle_24dp_fill)
            }
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}