package com.jobsity.tvseries.presentation.shows.info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.model.TvShowEpisode
import com.jobsity.tvseries.presentation.shows.favorites.FavoritesTvShowsActivity
import com.jobsity.tvseries.presentation.shows.info.EpisodesAdapter.IEpisodeAdapter
import com.jobsity.tvseries.util.extension.favorite
import com.jobsity.tvseries.util.extension.htmlText
import com.jobsity.tvseries.util.extension.loadPhoto
import kotlinx.android.synthetic.main.show_info_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TvShowInfoActivity: AppCompatActivity(), IEpisodeAdapter {

    companion object {
        const val TVSHOW_INFO_REQUEST_CODE = 8374
        const val TVSHOW_ARG = "tvshow_arg"
        private const val FAVORITE_STATUS_ARGS = "favorite_status"
        fun startActivityForResult(activity: Activity?, tvShow: TvShow) {
            activity?.let {
                val intent = Intent(activity, TvShowInfoActivity::class.java)
                intent.putExtra(TVSHOW_ARG, tvShow)
                activity.startActivityForResult(intent, TVSHOW_INFO_REQUEST_CODE)
            }
        }
    }

    private val adapter = EpisodesAdapter(this)
    private val viewModel: TvShowInfoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_info_view)

        rvEpisodes.adapter = adapter

        viewModel.init(intent.getParcelableExtra(TVSHOW_ARG))

        viewModel.viewState.observe(this, Observer {
            adapter.episodes = it.episodes

            val adapter = ArrayAdapter(this, R.layout.spinner_view, it.seasons)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_view)
            spSeasons.adapter = adapter

            txvName.text = it.name
            txvSeasonAndEpisode.text = it.hourAndDays
            txvGenres.text = it.genres
            txvRating.text = it.rating

            txvRating.isVisible = it.isShowRating
            imgvRating.isVisible = it.isShowRating

            txvSummary.htmlText(it.summary)
            imgvLike.favorite(it.isFavorite)
            imgvEpisode.loadPhoto(it.posterUrl)
            imgvLike.isEnabled = it.enableFavoriteClick
            imgvLike.alpha = if (it.enableFavoriteClick) 1.0f else 0.5f
        })

        viewModel.episodeClick.observe(this, Observer {
            EpisodeInfoFragment.newInstance(it).show(supportFragmentManager, "episode_info")
        })

        imgvBack.setOnClickListener {
            onBackPressed()
        }

        imgvLike.setOnClickListener {
            viewModel.didClickOnFavorite()
        }

        spSeasons.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                viewModel.didClickOnSeason(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    override fun onClickEpisode(episode: TvShowEpisode) {
        viewModel.didClickOnEpisode(episode)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(TVSHOW_ARG, viewModel.getCurrentTvShow())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}