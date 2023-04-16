package org.mozilla.fenix.browser.preview

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import mozilla.components.feature.customtabs.CustomTabsToolbarFeature
import mozilla.components.feature.session.SessionFeature
import mozilla.components.support.base.feature.ViewBoundFeatureWrapper
import mozilla.components.support.ktx.android.util.dpToPx
import org.mozilla.fenix.R
import org.mozilla.fenix.databinding.FragmentBrowserPreviewBinding
import org.mozilla.fenix.ext.requireComponents
import org.mozilla.fenix.tabstray.EXPANDED_OFFSET_IN_PORTRAIT_DP

class BrowserPreviewFragment : AppCompatDialogFragment() {

    private val sessionFeature = ViewBoundFeatureWrapper<SessionFeature>()
    private val customTabsToolbarFeature = ViewBoundFeatureWrapper<CustomTabsToolbarFeature>()
    private val navArgs: BrowserPreviewFragmentArgs by navArgs()

    private lateinit var rootView: FragmentBrowserPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.TabTrayDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        Dialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = FragmentBrowserPreviewBinding.inflate(inflater, container, false)

        return rootView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomSheetBehavior.from(rootView.tabWrapper).apply {
            expandedOffset = EXPANDED_OFFSET_IN_PORTRAIT_DP.dpToPx(resources.displayMetrics)
        }

        rootView.swipeRefresh.isEnabled = false

        sessionFeature.set(
            feature = SessionFeature(
                requireComponents.core.store,
                requireComponents.useCases.sessionUseCases.goBack,
                rootView.engineView,
                navArgs.activeSessionId,
            ),
            owner = this,
            view = view,
        )

        customTabsToolbarFeature.set(
            feature = CustomTabsToolbarFeature(
                requireComponents.core.store,
                rootView.toolbar,
                navArgs.activeSessionId,
                requireComponents.useCases.customTabsUseCases,
                window = activity?.window,
                closeListener = { dismissAllowingStateLoss() },
            ),
            owner = this,
            view = view,
        )

        super.onViewCreated(view, savedInstanceState)
    }
}
