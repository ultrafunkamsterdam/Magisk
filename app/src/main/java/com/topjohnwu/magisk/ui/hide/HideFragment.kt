package com.topjohnwu.magisk.ui.hide

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.topjohnwu.magisk.R
<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListFragment.kt
import com.topjohnwu.magisk.arch.BaseFragment
import com.topjohnwu.magisk.arch.viewModel
import com.topjohnwu.magisk.databinding.FragmentDenyMd2Binding
=======
import com.topjohnwu.magisk.arch.BaseUIFragment
import com.topjohnwu.magisk.databinding.FragmentHideMd2Binding
import com.topjohnwu.magisk.di.viewModel
import com.topjohnwu.magisk.ktx.addSimpleItemDecoration
import com.topjohnwu.magisk.ktx.addVerticalPadding
import com.topjohnwu.magisk.ktx.fixEdgeEffect
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideFragment.kt
import com.topjohnwu.magisk.ktx.hideKeyboard
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListFragment.kt
class DenyListFragment : BaseFragment<FragmentDenyMd2Binding>() {
=======
class HideFragment : BaseUIFragment<HideViewModel, FragmentHideMd2Binding>() {
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideFragment.kt

    override val layoutRes = R.layout.fragment_hide_md2
    override val viewModel by viewModel<HideViewModel>()

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListFragment.kt
    private lateinit var searchView: SearchView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.setTitle(R.string.denylist)
=======
    private var isFilterVisible
        get() = binding.hideFilter.isVisible
        set(value) {
            if (!value) hideKeyboard()
            MotionRevealHelper.withViews(binding.hideFilter, binding.hideFilterToggle, value)
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity.setTitle(R.string.magiskhide)
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideFragment.kt
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListFragment.kt
        binding.appList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
=======
        binding.hideFilterToggle.setOnClickListener {
            isFilterVisible = true
        }
        binding.hideFilterInclude.hideFilterDone.setOnClickListener {
            isFilterVisible = false
        }
        binding.hideContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideFragment.kt
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) activity?.hideKeyboard()
            }
        })

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListFragment.kt
        binding.appList.apply {
            addEdgeSpacing(top = R.dimen.l_50, bottom = R.dimen.l1)
            addItemSpacing(R.dimen.l1, R.dimen.l_50, R.dimen.l1)
            fixEdgeEffect()
        }
=======
        val resource = requireContext().resources
        val l_50 = resource.getDimensionPixelSize(R.dimen.l_50)
        val l1 = resource.getDimensionPixelSize(R.dimen.l1)
        binding.hideContent.addVerticalPadding(
            l_50,
            l1 + resource.getDimensionPixelSize(R.dimen.internal_action_bar_size)
        )
        binding.hideContent.addSimpleItemDecoration(
            left = l1,
            top = l_50,
            right = l1,
            bottom = l_50,
        )
        binding.hideContent.fixEdgeEffect()

        val lama = binding.hideContent.layoutManager ?: return
        lama.isAutoMeasureEnabled = false
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideFragment.kt
    }

    override fun onPreBind(binding: FragmentHideMd2Binding) = Unit

    override fun onBackPressed(): Boolean {
        if (searchView.isIconfiedByDefault && !searchView.isIconified) {
            searchView.isIconified = true
            return true
        }
        return super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_deny_md2, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = searchView.context.getString(R.string.hide_filter_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.query = query ?: ""
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.query = newText ?: ""
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListFragment.kt
            R.id.action_show_system -> {
                val check = !item.isChecked
                viewModel.isShowSystem = check
                item.isChecked = check
                return true
            }
            R.id.action_show_OS -> {
                val check = !item.isChecked
                viewModel.isShowOS = check
                item.isChecked = check
                return true
            }
=======
            R.id.action_focus_up -> binding.hideContent
                .takeIf { (it.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0 > 10 }
                ?.also { it.scrollToPosition(10) }
                .let { binding.hideContent }
                .also { it.post { it.smoothScrollToPosition(0) } }
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideFragment.kt
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val showSystem = menu.findItem(R.id.action_show_system)
        val showOS = menu.findItem(R.id.action_show_OS)
        showOS.isEnabled = showSystem.isChecked
    }
}
