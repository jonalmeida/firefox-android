/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.browser.preview

import mozilla.components.browser.state.state.SessionState
import mozilla.components.concept.engine.HitResult
import mozilla.components.feature.contextmenu.ContextMenuCandidate
import mozilla.components.feature.contextmenu.getLink
import mozilla.components.feature.contextmenu.isImage
import mozilla.components.feature.contextmenu.isUri
import mozilla.components.feature.contextmenu.isUrlSchemeAllowed
import mozilla.components.feature.contextmenu.isVideoAudio

fun createPreviewCandidate(
    additionalValidation: (SessionState, HitResult) -> Boolean = { _, _ -> true },
    launchFragment: (SessionState, HitResult) -> Unit,
) = ContextMenuCandidate(
    id = "mozac.browser.preview.open_in_preview",
    label = "Preview page",
    showFor = { tab, hitResult ->
        tab.isUrlSchemeAllowed(hitResult.getLink()) &&
            (hitResult.isUri() || hitResult.isImage() || hitResult.isVideoAudio()) &&
            additionalValidation(tab, hitResult)
    },
    action = { tab, hitResult ->
        launchFragment.invoke(tab, hitResult)
    },
)
