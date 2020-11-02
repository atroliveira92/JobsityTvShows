package com.jobsity.tvseries.presentation.people.info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.Person
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.presentation.shows.TvShowAdapter
import com.jobsity.tvseries.presentation.shows.info.TvShowInfoActivity
import com.jobsity.tvseries.util.extension.loadRoundPhoto
import kotlinx.android.synthetic.main.error_try_again_view.*
import kotlinx.android.synthetic.main.list_view.*
import kotlinx.android.synthetic.main.person_info_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PeopleInfoActivity: AppCompatActivity(R.layout.person_info_view), TvShowAdapter.ITvShowAdapter {
    companion object {
        private const val PERSON_ARGS = "person_args"
        fun startActivity(activity: Activity?, person: Person) {
            activity?.let {
                val intent = Intent(it, PeopleInfoActivity::class.java)
                intent.putExtra(PERSON_ARGS, person)
                it.startActivity(intent)
            }
        }
    }

    private val viewModel: PeopleInfoViewModel by viewModel()
    private val adapter = TvShowAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent.getParcelableExtra(PERSON_ARGS))

        rvShows.also {
            it.layoutManager = GridLayoutManager(this, 3)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        viewModel.viewState.observe(this, Observer {
            txvPeopleName.text = it.name
            imgvPeople.loadRoundPhoto(it.imageUrl)

            adapter.shows = it.tvShows
            pgShows.isVisible = it.isLoadingVisible
            vError.isVisible = it.showError
            txvErrorMessage.text = it.errorMessage
            btnTryAgain.isVisible = it.showTryAgainButton
        })

        viewModel.tvShowClicked.observe(this, Observer {
            TvShowInfoActivity.startActivityForResult(this, it)
        })

        btnTryAgain.setOnClickListener {
            viewModel.didClickOnTryAgain()
        }

        imgvBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onReachLastItem() {}

    override fun onClick(tvShow: TvShow) {
        viewModel.didClickOnShow(tvShow)
    }
}