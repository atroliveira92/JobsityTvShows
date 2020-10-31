package com.jobsity.tvseries.util

import androidx.recyclerview.widget.DiffUtil

/**
 * Class created to be used in generic cases, in which, verifies that the items are the same using
 * [itemsTheSame] and verifies that the data content has not been changed through [equals].
 *
 * Only use the class if the attributes displayed on the screen can be compared with the [equals]
 * method of the [T] provided by [oldList] and [newList].
 */
class GenericDiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val itemsTheSame: (old: T, new: T) -> Boolean
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        itemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}