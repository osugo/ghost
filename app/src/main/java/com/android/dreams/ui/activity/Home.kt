package com.android.dreams.ui.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.android.dreams.R
import com.android.dreams.ui.fragment.DreamsFragment
import com.android.dreams.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

class Home : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setToolbars()

        if(savedInstanceState == null){
            setDefaultFragment()
        }
    }

    private fun setToolbars() {
        //toolbar init
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.title = getString(R.string.home)

        //bottom bar init
        bottomBar.setOnNavigationItemSelectedListener(this)
        BottomNavigationViewHelper.removeShiftMode(bottomBar)
    }

    //define default fragment that should be loaded when state hasn't been saved
    private fun setDefaultFragment() {
        val item = bottomBar.menu.findItem(R.id.dreams)
        setFragment(DreamsFragment(), item.title)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked

        bottomBar.menu.findItem(item.itemId).isChecked = true

        return when(item.itemId){
            R.id.dreams -> {
                //shows list of saved dreams
                setFragment(DreamsFragment(), item.title)
                true
            }
            R.id.tags -> {
                //shows list of all saved tags
                true
            }
            R.id.search -> {
                //allows for search by keyword
                true
            }
            else -> true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                System.exit(0)
                true
            }
            R.id.add -> {
                startActivity(intentFor<RecordDream>().clearTop())
                true
            }
            else -> false
        }
    }

    private fun setFragment(fragment: Fragment?, title: CharSequence) {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment, fragment.javaClass.simpleName)
                    .commitAllowingStateLoss()

            setTitle(title)
        }
    }
}
