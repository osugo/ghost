package com.android.dreams

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by kombo on 27/06/2018.
 */
class Ghost : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        Realm.init(this)
        Realm.setDefaultConfiguration(realmConfig)
    }

    companion object {
        lateinit var instance: Ghost
            private set

        val realmConfig: RealmConfiguration
            get() = RealmConfiguration.Builder()
                        .name(instance.getString(R.string.db_name))
                        .schemaVersion(1)
                        .deleteRealmIfMigrationNeeded()
                        .build()

    }
}