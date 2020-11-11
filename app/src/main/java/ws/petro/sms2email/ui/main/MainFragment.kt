package ws.petro.sms2email.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ws.petro.sms2email.R
import ws.petro.sms2email.SimInfo
import ws.petro.sms2email.filter.Rule

class MainFragment : Fragment() {
    private val TAG: String = "MainFragment"
    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: RuleViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.main_fragment, container, false)

        // Check and request needed permissions
        startupCheckPermissions()

        recyclerView = layout.findViewById<RecyclerView>(R.id.rules_list_view)

        context?.let { context_itr ->
            val adapter =  RuleListAdapter(context_itr)
            adapter.setOnClick { rule -> onItemClicked(rule) }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context_itr)
            viewModel.allRules.observe(
                viewLifecycleOwner,
                Observer { rules ->
                    // Update the cached copy of the data in the adapter.
                    rules?.let { adapter.setRules(it) }
                }
            )
        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun onItemClicked(rule: Rule) {
        val nextFrag = RuleEditFragment(rule)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, nextFrag, "RuleEditFragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.m_menu_add_rule) {
            val nextFrag = RuleEditFragment(null)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, nextFrag, "RuleEditFragment")
                .addToBackStack(null)
                .commit()
        }
        if (id == R.id.m_menu_settings) {
            val nextFrag = SettingsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, nextFrag, "SettingsFragment")
                .addToBackStack(null)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This function checks required perms and request them from user
     */
    private fun startupCheckPermissions() {
        val permsToCheck = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS
        )
        val permsToRequest = ArrayList<String>()

        for (perm in permsToCheck) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    perm
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Permission $perm -> OK")
            } else {
                Log.d(TAG, "Permission $perm -> Denied, need to request")
                permsToRequest.add(perm)
            }
        }

        if (permsToRequest.size > 0) {
            Log.d(TAG, "Requesting: $permsToRequest")

            requestPermissions(
                permsToRequest.toTypedArray(), 0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, perm ->
            val granted = (grantResults[index] == PackageManager.PERMISSION_GRANTED)
            Log.d(TAG, "Perms result: $perm -> $granted")
        }
    }

}
