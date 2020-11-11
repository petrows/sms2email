package ws.petro.sms2email.ui.main

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ws.petro.sms2email.R
import ws.petro.sms2email.SimCard
import ws.petro.sms2email.SimInfo
import ws.petro.sms2email.filter.Rule
import ws.petro.sms2email.filter.RuleDatabase

class RuleEditFragment(rule: Rule?) : Fragment() {

    var rule : Rule? = rule

    companion object {
        fun newInstance() = RuleEditFragment(null)
    }

    private val viewModel: RuleViewModel by activityViewModels()
    private var simCardsInfo : ArrayList<Pair<Int, String>> = ArrayList<Pair<Int, String>>()

    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        simCardsInfo.clear()
        simCardsInfo.add(Pair(-1, getText(R.string.any_sim).toString()))

        val simInfo = SimInfo().getSimInfo(requireActivity())

        if (simInfo != null) {
            for (sim in simInfo) {
                var indexDisplay = sim.slotIndex + 1 // Display nummer from 1
                simCardsInfo.add(Pair(sim.slotIndex, "SIM $indexDisplay: ${sim.name}"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_rule_edit, container, false)

        if (rule != null) {
            layout.findViewById<TextView>(R.id.editor_title).setText(R.string.editor_edit)
            layout.findViewById<EditText>(R.id.edit_title).setText(rule!!.title)
            layout.findViewById<EditText>(R.id.edit_to_email).setText(rule!!.emailTo)
        } else {
            layout.findViewById<TextView>(R.id.editor_title).setText(R.string.editor_new)
            layout.findViewById<EditText>(R.id.edit_title).setText(R.string.editor_new)
        }

        var simSelector = ArrayList<String>()
        for (sim in simCardsInfo) {
            simSelector.add(sim.second)
        }

        val spin = layout.findViewById(R.id.edit_sim) as Spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, 0, simSelector)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = adapter

        if (rule != null) {
            spin.setSelection(simCardsInfo.indexOfFirst { it.first == rule!!.sim })
        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.e_menu_save) {
            if (rule == null) {
                rule = Rule("", 0, -1, "")
            }
            rule!!.title = layout.findViewById<TextView>(R.id.edit_title).text.toString()
            rule!!.emailTo = layout.findViewById<TextView>(R.id.edit_to_email).text.toString()
            rule!!.sim = simCardsInfo[layout.findViewById<Spinner>(R.id.edit_sim).selectedItemPosition].first
            GlobalScope.launch(Dispatchers.IO) {
                val ruleDao = RuleDatabase.getDatabase(requireActivity(), this).ruleDao()
                ruleDao.save(rule!!)
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
        if (id == R.id.e_menu_cancel) {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

}
