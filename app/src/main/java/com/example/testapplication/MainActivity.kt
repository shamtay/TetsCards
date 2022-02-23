package com.example.testapplication

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.testapplication.cards.CardViewHolder
import com.example.testapplication.cards.CardsAdapter
import com.example.testapplication.cards.DisabledScrollingLayoutManager
import com.example.testapplication.cards.SwipeToDeleteCallback
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.utils.release
import com.example.testapplication.utils.requireAdapter
import com.example.testapplication.utils.swipeTopElement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private var currentValueAnimator: ValueAnimator? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as TestApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
        setCardsAdapter()
        observeSearchResults()

    }

    private fun setButtons() {
        with(binding) {
            goButton.setOnClickListener {
                val text = searchText.text.toString()
                if (text.isNotEmpty()) {
                    viewModel.search(text, true)
                }
            }
            likeButton.setOnClickListener {
                Log.d("MainActivity::", "repo thumb up")
                cancelPendingAnimation()
                currentValueAnimator = recyclerView.swipeTopElement(1) {
                    (recyclerView.adapter as CardsAdapter).removeTop()
                }
            }
            nopeButton.setOnClickListener {
                Log.d("MainActivity::", "repo thumb down")
                cancelPendingAnimation()
                currentValueAnimator = recyclerView.swipeTopElement(-1) {
                    (recyclerView.adapter as CardsAdapter).removeTop()
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cancelPendingAnimation() {
        // in case there is a pending animation
        // we cancel it and update the recycler view to return to initial state
        currentValueAnimator?.release()
        binding.recyclerView.requireAdapter().notifyDataSetChanged()
    }

    private fun setCardsAdapter() {
        val cardsAdapter = CardsAdapter().apply {
            cardsRemoveListener = {
                if (it == 0) { // if number of elements in the cars is 0
                    viewModel.search(binding.searchText.text.toString(), false)
                }
            }
        }
        binding.recyclerView.layoutManager = DisabledScrollingLayoutManager(this)
        binding.recyclerView.adapter = cardsAdapter
        SwipeToDeleteCallback().apply {
            val itemTouchHelper = ItemTouchHelper(this)
            swipeListener = {
                cardsAdapter.removeTop()
                if (it > 0) {
                    Log.d("MainActivity::", "repo thumb up")
                } else {
                    Log.d("MainActivity::", "repo thumb down")
                }
            }
            itemMovingListener = { viewHolder, a, like ->
                (viewHolder as CardViewHolder).updateCaptionsTransparency(like, a)
            }
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun observeSearchResults() {
        (viewModel.searchResultsLiveData.value as? DataFetchState.Success)?.let {
            (binding.recyclerView.adapter as CardsAdapter).setItems(it.result.repos)
        }

        viewModel.searchResultsLiveData.observe(this) {
            when (it) {
                is DataFetchState.Success -> {
                    if (it.result.query == binding.searchText.text.toString() && it.result.query.isNotEmpty()) {
                        (binding.recyclerView.adapter as CardsAdapter).setItems(it.result.repos)
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.goButton.isEnabled = true
                }

                is DataFetchState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.goButton.isEnabled = true
                }

                is DataFetchState.Progress -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.goButton.isEnabled = false
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentValueAnimator?.release()
    }
}