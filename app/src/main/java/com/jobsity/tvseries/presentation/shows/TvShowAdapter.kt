package com.jobsity.tvseries.presentation.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.util.GenericDiffCallback
import com.jobsity.tvseries.util.extension.loadPhoto
import com.jobsity.tvseries.util.extension.setPortraitHeight
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.show_row.view.*


class TvShowAdapter(val listener: ITvShowAdapter): RecyclerView.Adapter<TvShowAdapter.TVShowViewHolder>() {
    var shows = emptyList<TvShow>()
        set(value) {
            val result = DiffUtil.calculateDiff(
                GenericDiffCallback(
                    field,
                    value
                ) { oldShow: TvShow, newShow: TvShow ->
                    oldShow == newShow
                }
            )
            result.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_row, parent, false)
        val width = (parent.width / 3) - view.context.resources.getDimension(R.dimen.margin_tv_show_row)
        return TVShowViewHolder(view, width)
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bind(shows[position])
        if (shows.size - 1 == position) {
            listener.onReachLastItem()
        }
    }

    inner class TVShowViewHolder(itemView: View, width: Float): RecyclerView.ViewHolder(itemView) {
        //I'm placing the views in variables because of a bug on last version of kotlin synthetic not
        //holding the viewHolder views references
        private val txvName = itemView.txvName
        private val txvRating = itemView.txvRating
        private val txvYear = itemView.txvYear
        private val txvSeparator = itemView.txvSeparator
        private val imgvRatingIcon = itemView.imgvRatingIcon
        private val imgvShow = itemView.imgvShow

        init {
            itemView.setPortraitHeight(width)
        }

        fun bind(tvShow: TvShow) {
            txvName.text = tvShow.name
            txvRating.text = tvShow.rating
            txvYear.text = tvShow.premier
            txvRating.text = tvShow.rating

            txvRating.isVisible = !tvShow.rating.isNullOrEmpty()
            txvSeparator.isVisible = !tvShow.rating.isNullOrEmpty()
            imgvRatingIcon.isVisible = !tvShow.rating.isNullOrEmpty()

            imgvShow.loadPhoto(tvShow.posterMedium)

            itemView.setOnClickListener {
                listener.onClick(tvShow)
            }
        }
    }

    interface ITvShowAdapter {
        fun onReachLastItem()
        fun onClick(tvShow: TvShow)
    }
}