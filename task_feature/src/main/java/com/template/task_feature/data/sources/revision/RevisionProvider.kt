package com.template.task_feature.data.sources.revision

import android.content.Context
import com.template.resourses_module.R
import javax.inject.Inject

class RevisionProvider @Inject constructor(
    private val context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.name_revision_shared_preference), Context.MODE_PRIVATE
    )

    private val edit = sharedPreferences.edit()

    var spRevision: Int = sharedPreferences.getInt(context.getString(R.string.key_sp_revision), 0)

    fun updateRevision(newRevision: Int) {
        edit.putInt(context.getString(R.string.key_sp_revision), newRevision).apply()
    }

    fun getRevision(): Int =
        sharedPreferences.getInt(context.getString(R.string.key_sp_revision), 0)

}