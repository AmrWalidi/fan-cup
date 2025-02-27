package com.amrwalidi.android.fancup.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.Category
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.repository.AuthenticationRepository
import com.amrwalidi.android.fancup.repository.CategoryRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository()
    private val userRepo = UserRepository(database)
    private val categoryRepo = CategoryRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _formattedCoins = MutableLiveData("0")
    val formattedCoins: LiveData<String> = _formattedCoins

    private val _formattedPoints = MutableLiveData("0")
    val formattedPoints: LiveData<String> = _formattedPoints

    private val _userRank = MutableLiveData("0")
    val userRank: LiveData<String> = _userRank

    private val _userLevel = MutableLiveData("0")
    val userLevel: LiveData<String> = _userLevel

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _pageName = MutableLiveData("Home")
    val pageName: LiveData<String>
        get() = _pageName

    private val _selectedPage = MutableLiveData(arrayOf(false, false, true, false, false))
    val selectedPage: LiveData<Array<Boolean>>
        get() = _selectedPage

    private val _transitionDirection = MutableLiveData<Int>()
    val transitionDirection: LiveData<Int>
        get() = _transitionDirection

    private val _toLoginScreen = MutableLiveData(false)
    val toLoginScreen: LiveData<Boolean>
        get() = _toLoginScreen

    init {
        viewModelScope.launch {
            launch {
                authRepo.loggedUser.collect { u ->
                    if (u == null) returnToLoginScreen()
                }
            }
            _user.value = userRepo.getUser()
            _formattedCoins.value = _user.value?.coins?.let { formatInt(it) }
            _formattedPoints.value = _user.value?.points?.let { formatInt(it) }
            _userRank.value = _user.value?.rank
            _userLevel.value = _user.value?.level
            _categories.value = categoryRepo.getCategories()
        }
    }

    private fun formatInt(number: Int): String {
        return "%,d".format(number)
    }

    fun navigate(newPage: Int) {
        if (newPage - _page.value!! > 0) {
            _transitionDirection.value = 1
        } else {
            _transitionDirection.value = -1
        }
        _page.value = newPage
        _selectedPage.value = Array(5) { false }.apply {
            this[newPage - 1] = true
        }
        _pageName.value = when (newPage) {
            1 -> "Store"
            2 -> "Champions"
            3 -> "Home"
            4 -> "Challenges"
            5 -> "Settings"
            else -> return
        }
    }


    private fun returnToLoginScreen() {
        _toLoginScreen.value = true
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}