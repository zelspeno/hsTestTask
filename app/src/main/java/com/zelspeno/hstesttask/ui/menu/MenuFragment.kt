package com.zelspeno.hstesttask.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.zelspeno.hstesttask.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by activityViewModels()

    private lateinit var bonusesAdapter: BonusListRecyclerAdapter
    private lateinit var tagsAdapter: TagsListRecyclerAdapter
    private lateinit var dishesAdapter: DishesListRecyclerAdapter

    private lateinit var allDishesList: List<DishUI>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        initAppBar()

        binding.menuSwipeRefreshLayout.setOnRefreshListener {
            onSwipeUpdateList()
        }

        fillUI()

        return binding.root
    }

    /** Fill user's interface */
    private fun fillUI() {
        with(binding) {
            dishesRecyclerView.visibility = View.GONE
            tagsRecyclerView.visibility = View.GONE
            bonusRecyclerView.visibility = View.INVISIBLE
            menuRecyclerViewNotFound.visibility = View.GONE
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getDishesList()
                viewModel.dishesList.collect {
                    val dishes = it
                    if (dishes != null) {
                        allDishesList = viewModel.getDishUI(dishes)
                        val tags = viewModel.getTagsList(allDishesList)
                        dishesAdapter = DishesListRecyclerAdapter(allDishesList)
                        tagsAdapter = TagsListRecyclerAdapter(tags)
                        sendDataToDishesRecyclerView(dishesAdapter)
                        sendDataToTagsRecyclerView(tagsAdapter, dishesAdapter)
                        binding.dishesRecyclerView.visibility = View.VISIBLE
                        binding.tagsRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.menuRecyclerViewNotFound.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getBonusList()
                viewModel.bonusList.collect {
                    val bonuses = it
                    if (bonuses != null) {
                        bonusesAdapter = BonusListRecyclerAdapter(it)
                        sendDataToBonusesRecyclerView(bonusesAdapter)
                        binding.bonusRecyclerView.visibility = View.VISIBLE
                    }
                    else {
                        binding.menuRecyclerViewNotFound.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initAppBar() {
        val appBarLayout = binding.menuAppBar
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {
                appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                binding.bonusRecyclerView.visibility = View.INVISIBLE
            } else {
                binding.bonusRecyclerView.visibility = View.VISIBLE
            }
        })
    }

    /** Init settings for DishesRecyclerView */
    private fun sendDataToDishesRecyclerView(adapterRV: DishesListRecyclerAdapter) {
        val recyclerView = binding.dishesRecyclerView
        with(recyclerView) {
            adapter = adapterRV
            layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    /** Init settings for TagsRecyclerView */
    private fun sendDataToTagsRecyclerView(adapterTags: TagsListRecyclerAdapter,
                                           adapterDishes: DishesListRecyclerAdapter) {
        val recyclerView = binding.tagsRecyclerView
        with(recyclerView) {
            adapter = adapterTags
            layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        }

        adapterTags.setOnItemClickListener(object :
            TagsListRecyclerAdapter.onItemClickListener {
            override fun onItemClick(tag: Tag) {
                viewModel.onTagClick(tag, allDishesList, adapterTags, adapterDishes)
            }
        })
    }

    /** Init settings for BonusesRecyclerView */
    private fun sendDataToBonusesRecyclerView(adapterRV: BonusListRecyclerAdapter) {
        val recyclerView = binding.bonusRecyclerView
        with(recyclerView) {
            adapter = adapterRV
            layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /** Update data on RecyclerView when user pull-to-refresh */
    private fun onSwipeUpdateList() {
        with(binding) {
            dishesRecyclerView.visibility = View.GONE
            tagsRecyclerView.visibility = View.GONE
            menuRecyclerViewNotFound.visibility = View.GONE
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.getDishesList()
                    viewModel.dishesList.collect {
                        val dishes = it
                        if (dishes != null) {
                            allDishesList = viewModel.getDishUI(dishes)
                            val tags = viewModel.getTagsList(allDishesList)
                            dishesAdapter.updateList(allDishesList)
                            tagsAdapter.updateList(tags)
                            dishesRecyclerView.visibility = View.VISIBLE
                            tagsRecyclerView.visibility = View.VISIBLE
                            menuSwipeRefreshLayout.isRefreshing = false
                        } else {
                            menuRecyclerViewNotFound.visibility = View.VISIBLE
                            menuSwipeRefreshLayout.isRefreshing = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}