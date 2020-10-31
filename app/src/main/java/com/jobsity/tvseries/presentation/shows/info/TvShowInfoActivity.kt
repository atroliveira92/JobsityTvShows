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
import com.jobsity.tvseries.util.extension.favorite
import com.jobsity.tvseries.util.extension.htmlText
import com.jobsity.tvseries.util.extension.loadPhoto
import kotlinx.android.synthetic.main.show_info_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TvShowInfoActivity: AppCompatActivity() {

    companion object {
        const val TVSHOW_ARG = "tvshow_arg"

        fun startActivity(activity: Activity?, tvShow: TvShow) {
            activity?.let {
                val intent = Intent(activity, TvShowInfoActivity::class.java)
                intent.putExtra(TVSHOW_ARG, tvShow)
                activity.startActivity(intent)
            }
        }
    }

    private val adapter = EpisodesAdapter()
    private val viewModel: TvShowInfoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_info_view)

        rvEpisodes.adapter = adapter

        viewModel.init(intent.getParcelableExtra<TvShow>(TVSHOW_ARG))

        viewModel.viewState.observe(this, Observer {
            adapter.episodes = it.episodes

            val adapter = ArrayAdapter(this, R.layout.spinner_view, it.seasons)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_view)
            spSeasons.adapter = adapter

            txvName.text = it.name
            txvHourAndDays.text = it.hourAndDays
            txvGenres.text = it.genres
            txvRating.text = it.rating

            txvRating.isVisible = it.isShowRating
            imgvRating.isVisible = it.isShowRating

            txvSummary.htmlText(it.summary)
            imgvLike.favorite(it.isFavorite)
            imgvPoster.loadPhoto(it.posterUrl)
        })

        imgvLike.setOnClickListener {

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
}