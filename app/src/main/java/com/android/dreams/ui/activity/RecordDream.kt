package com.android.dreams.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.android.dreams.Ghost
import com.android.dreams.R
import com.android.dreams.data.model.Dream
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.realm.Realm
import kotlinx.android.synthetic.main.record_dream.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.intentFor
import timber.log.Timber
import java.util.*

/**
 * Created by kombo on 27/06/2018.
 */
class RecordDream : AppCompatActivity(), View.OnTouchListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    //lazily init Realm to only obtain it when we need it
    private val realm by lazy {
        Realm.getInstance(Ghost.realmConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_dream)

        setupToolbar()

        date.setOnTouchListener(this)

        save.setOnClickListener(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Create Record"
    }

    //onClick doesn't interpret the MotionEvent.EVENT_DOWN event as well as onTouch does and may not show the date picker as and when needed
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val now = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.newInstance(
                this@RecordDream,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show(fragmentManager, "Datepickerdialog")
        return true
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        date.setText((dayOfMonth.toString() + "/" + (monthOfYear + 1).toString() + "/" + year.toString()))
    }

    override fun onClick(v: View?) {
        //validate all data before saving to Realm
        val period = date.text
        val desc = description.text
        val label = tag.text

        if (period.isNullOrEmpty()) {
            showSnackbar(getString(R.string.please_enter_date))
            return
        }

        if (desc.isNullOrEmpty()) {
            showSnackbar(getString(R.string.please_enter_desc))
            return
        }

        if (label.isNullOrEmpty()) {
            showSnackbar(getString(R.string.please_enter_label))
            return
        }

        //save to Realm if validation passes
        if (!period.isNullOrEmpty() and !desc.isNullOrEmpty() and !label.isNullOrEmpty())
            saveToRealm(period.toString(), desc.toString(), label.toString())
        else
            Timber.e("An error occurred during validation")
    }

    private fun saveToRealm(period: String, desc: String, label: String) {
        realm.executeTransaction {
            val dream = Dream()
            dream.apply {
                date = period
                description = desc
                tag = label
            }

            it.copyToRealmOrUpdate(dream)

            Timber.d("Dream saved successfully")

            startActivity(intentFor<Home>().clearTop())
        }
    }

    private fun showSnackbar(message: String) {
        snackbar(parentLayout, message)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }
}