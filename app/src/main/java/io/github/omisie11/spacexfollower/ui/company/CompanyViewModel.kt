package io.github.omisie11.spacexfollower.ui.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.omisie11.spacexfollower.data.CompanyRepository
import io.github.omisie11.spacexfollower.data.model.Company

class CompanyViewModel(private val repository: CompanyRepository) : ViewModel() {

    private val mCompanyInfo by lazy { repository.getCompanyInfo() }
    private val _isCompanyInfoLoading by lazy { repository.getCompanyInfoLoadingStatus() }
    private val _snackBar: MutableLiveData<String> = repository.getCompanyInfoSnackbar()

    fun getCompanyInfo(): LiveData<Company> = mCompanyInfo

    fun getCompanyInfoLoadingStatus() = _isCompanyInfoLoading

    fun refreshCompanyInfo() = repository.refreshCompanyInfo()

    fun refreshIfCompanyDataOld() = repository.refreshIfCompanyDataOld()

    fun deleteCompanyInfo() = repository.deleteCompanyInfo()

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String>
        get() = _snackBar

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel running coroutines in repository
        repository.cancelCoroutines()
    }
}