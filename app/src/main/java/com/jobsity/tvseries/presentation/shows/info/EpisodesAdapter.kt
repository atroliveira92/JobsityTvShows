package com.jobsity.tvseries.presentation.shows.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShowEpisode
import com.jobsity.tvseries.presentation.shows.info.EpisodesAdapter.EpisodeViewHolder
import com.jobsity.tvseries.util.GenericDiffCallback
import com.jobsity.tvseries.util.extension.loadPhoto
import kotlinx.android.synthetic.main.episode_row.view.*

class EpisodesAdapter(var listener: IEpisodeAdapter): RecyclerView.Adapter<EpisodeViewHolder>() {
    var episodes = emptyList<TvShowEpisode>()
        @Synchronized set(value) {
            val result = DiffUtil.calculateDiff(
                GenericDiffCallback(
                    field,
                    value
                ) { oldEpisode: TvShowEpisode, newEpisode: TvShowEpisode ->
                    oldEpisode == newEpisode
                }
            )
            result.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.episode_row, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) = holder.bind(episodes[position])

    inner class EpisodeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgvEpisode = itemView.imgvEpisode
        val txvEpisode = itemView.txvEpisode

        fun bind(tvShowEpisode: TvShowEpisode) {
            if (tvShowEpisode.mediumImage != null) {
                imgvEpisode.loadPhoto(tvShowEpisode.mediumImage)
            }
            txvEpisode.text = tvShowEpisode.name

            itemView.setOnClickListener {
                listener.onClickEpisode(tvShowEpisode)
            }
        }
    }

    interface IEpisodeAdapter {
        fun onClickEpisode(episode: TvShowEpisode)
    }
}