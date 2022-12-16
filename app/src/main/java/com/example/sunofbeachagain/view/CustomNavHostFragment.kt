package com.example.navigation.taobao.Fragment

import android.view.View
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.example.navigation.HideSwitchFragmentNavigator
import androidx.navigation.fragment.NavHostFragment

import com.example.sunofbeachagain.R

class CustomNavHostFragment : NavHostFragment() {

    /**
     * Create the FragmentNavigator that this NavHostFragment will use. By default, this uses
     * [FragmentNavigator], which replaces the entire contents of the NavHostFragment.
     *
     *
     * This is only called once in [.onCreate] and should not be called directly by
     * subclasses.
     * @return a new instance of a FragmentNavigator
     */
    @Deprecated("Use {@link #onCreateNavController(NavController)}")
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return HideSwitchFragmentNavigator(
            requireContext(), childFragmentManager,
            getContainerId()
        )
    }

    private fun getContainerId(): Int {
        val id = id
        return if (id != 0 && id != View.NO_ID) {
            id
        } else R.id.home_fragment_container
        // Fallback to using our own ID if this Fragment wasn't added via
        // add(containerViewId, Fragment)
    }
}