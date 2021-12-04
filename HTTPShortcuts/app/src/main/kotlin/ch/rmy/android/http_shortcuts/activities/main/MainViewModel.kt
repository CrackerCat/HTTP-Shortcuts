package ch.rmy.android.http_shortcuts.activities.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ch.rmy.android.http_shortcuts.data.RealmViewModel
import ch.rmy.android.http_shortcuts.data.Repository
import ch.rmy.android.http_shortcuts.data.Transactions
import ch.rmy.android.http_shortcuts.data.livedata.ListLiveData
import ch.rmy.android.http_shortcuts.data.models.AppLock
import ch.rmy.android.http_shortcuts.data.models.Category
import ch.rmy.android.http_shortcuts.extensions.toLiveData
import io.reactivex.Single
import io.realm.kotlin.where
import org.mindrot.jbcrypt.BCrypt

open class MainViewModel(application: Application) : RealmViewModel(application) {

    var hasMovedToInitialCategory = false

    fun isAppLocked() = Repository.getAppLock(persistedRealm) != null

    val appLockedSource: LiveData<Boolean>
        get() = Transformations.map(
            persistedRealm
                .where<AppLock>()
                .findAllAsync()
                .toLiveData()
        ) {
            it.isNotEmpty()
        }

    fun removeAppLock(password: String) =
        run {
            var unlocked = false
            Transactions.commit { realm ->
                val appLock = Repository.getAppLock(realm)
                if (appLock != null && BCrypt.checkpw(password, appLock.passwordHash)) {
                    appLock.deleteFromRealm()
                    unlocked = true
                }
            }
                .andThen(Single.fromCallable { unlocked })
        }

    fun getCategories(): ListLiveData<Category> =
        Repository.getBase(persistedRealm)!!
            .categories
            .toLiveData()

    fun getShortcutById(shortcutId: String) = Repository.getShortcutById(persistedRealm, shortcutId)

    fun moveShortcut(shortcutId: String, targetPosition: Int? = null, targetCategoryId: String? = null) =
        Transactions.commit { realm ->
            Repository.moveShortcut(realm, shortcutId, targetPosition, targetCategoryId)
        }

    fun getToolbarTitle() = Repository.getBase(persistedRealm)!!.title

    fun getLiveToolbarTitle(): LiveData<String> =
        Transformations.map(Repository.getBase(persistedRealm)!!.toLiveData()) { base ->
            base?.title
                ?.takeUnless { it.isBlank() }
                ?: ""
        }

    fun setToolbarTitle(title: String) =
        Transactions.commit { realm ->
            Repository.getBase(realm)?.title = title.trim()
        }
}
