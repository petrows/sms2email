package ws.petro.sms2email.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ws.petro.sms2email.R

class MainFragment : Fragment() {

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

        recyclerView = layout.findViewById<RecyclerView>(R.id.rules_list_view)

        context?.let { context_itr ->
            val adapter =  RuleListAdapter(context_itr)
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

}
