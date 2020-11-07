package ws.petro.sms2email.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
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
        startupCheckPermissions()
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

        val info: SimInfo = SimInfo()

        info.getSimInfo(requireActivity())

        recyclerView = layout.findViewById<RecyclerView>(R.id.rules_list_view)

        context?.let { context_itr ->
            val adapter =  RuleListAdapter(context_itr)
            adapter.setOnClick { rule -> onItemClicked(rule) }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context_itr)
            viewModel.allRules.observe(
                viewLifecycleOwner,
                Observer { rules ->
                    // Update the cached copy of the words in the adapter.
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

    private fun startupCheckPermissions() {
        startupCheckPersmission(Manifest.permission.READ_PHONE_STATE)
    }

    private fun startupCheckPersmission(permission: String) {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "No read SIM access granted, requesting")

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission), 0
            )
            return null
        }
    }

}
