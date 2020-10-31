package com.jobsity.tvseries.presentation.shows.info

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShowEpisode
import com.jobsity.tvseries.util.extension.htmlText
import com.jobsity.tvseries.util.extension.loadPhoto
import kotlinx.android.synthetic.main.episode_info_view.*


class EpisodeInfoFragment: BottomSheetDialogFragment() {
    companion object {
        const val EPISODE_ARGS = "tvShowEpisode_args"
        fun newInstance(tvShowEpisode: TvShowEpisode): EpisodeInfoFragment {
            val fragment = EpisodeInfoFragment()

            val args = Bundle()
            args.putParcelable(EPISODE_ARGS, tvShowEpisode)
            fragment.arguments = args

            return fragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.episode_info_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val episode = arguments?.getParcelable(EPISODE_ARGS) as TvShowEpisode

        imgvEpisode.loadPhoto(episode.originalImage)
        txvEpisode.text = episode.name
        val seasonAndEp = "${String.format(getString(R.string.season_), episode.season)} ● ${String.format(getString(R.string.episode_), episode.number)}"
        txvSeasonAndEpisode.text = seasonAndEp
        txvSummary.htmlText(episode.summary)

        imgvClose.setOnClickListener {
            dismiss()
        }
    }
}