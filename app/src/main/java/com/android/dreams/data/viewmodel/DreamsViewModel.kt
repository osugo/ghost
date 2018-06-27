package com.android.dreams.data.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dreams.Ghost
import com.android.dreams.data.model.Dream
import io.realm.Realm
import io.realm.RealmList

/**
 * Created by kombo on 27/06/2018.
 */

/**
 * Loading data from a ViewModel is best practice as it's independent of configuration changes and activity/fragment lifecycles
 * Data integrity is preserved and loaded exactly how it was before
 */
class DreamsViewModel: ViewModel() {

    private var dreams: MutableLiveData<RealmList<Dream>>? = null

    private val realm by lazy {
        Realm.getInstance(Ghost.realmConfig)
    }

    public fun getDreamsList(): MutableLiveData<RealmList<Dream>>{
        if(dreams == null){
            dreams = MutableLiveData()

            fetchFromDb()
        }

        return dreams!!
    }

    private fun fetchFromDb(){
        val results = realm.where(Dream::class.java).findAll()

        if(results.isNotEmpty()){
            val list = realm.copyFromRealm(results) //detach results from Realm since DB may close before or during data read
            val items = RealmList<Dream>()
            items.addAll(list)

            dreams?.postValue(items)
        } else {
            dreams?.postValue(RealmList()) // send back empty list
        }
    }
}