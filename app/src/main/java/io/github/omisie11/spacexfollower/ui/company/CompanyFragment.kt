package io.github.omisie11.spacexfollower.ui.company


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

import io.github.omisie11.spacexfollower.R
import io.github.omisie11.spacexfollower.data.CompanyRepository
import io.github.omisie11.spacexfollower.data.model.Company
import kotlinx.android.synthetic.main.fragment_company.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CompanyFragment : Fragment() {

    private val repository: CompanyRepository by inject()
    private val viewModel: CompanyViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCompanyInfo().observe(viewLifecycleOwner, Observer<Company> { companyInfo ->
            text_summary.text = companyInfo.summary
            text_employees.text = companyInfo.employees.toString()
            text_vehicles.text = companyInfo.vehicles.toString()
            text_launch_sites.text = companyInfo.launchSites.toString()
            text_test_sites.text = companyInfo.testSites.toString()
            text_valuation.text = companyInfo.valuation.toString()
            text_address.text = companyInfo.headquarters.address
            text_city.text = companyInfo.headquarters.city
            text_state.text = companyInfo.headquarters.state
        })

        // Observe if data is refreshing and show/hide loading indicator
        viewModel.getCompanyInfoLoadingStatus()
            .observe(viewLifecycleOwner, Observer<Boolean> { isCompanyInfoRefreshing ->
                swipeRefreshLayout.isRefreshing = isCompanyInfoRefreshing
            })

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(this, Observer { text ->
            text?.let {
                Snackbar.make(swipeRefreshLayout, text, Snackbar.LENGTH_LONG).setAction(
                    getString(R.string.snackbar_action_retry), View.OnClickListener {
                        viewModel.refreshCompanyInfo()
                    }).show()
                viewModel.onSnackbarShown()
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            Log.i("CompanyInfoFragment", "onRefresh called from SwipeRefreshLayout")
            viewModel.refreshCompanyInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshIfCompanyDataOld()
    }
}
