package com.android.dreams.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kombo on 27/06/2018.
 */
open class Dream: RealmObject() {

    //date set as primary key assuming only one dream will be recorded per night
    @PrimaryKey
    var date: String? = null
    var description: String? = null
    var tag: String? = null

}