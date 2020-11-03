package ws.petro.sms2email.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ws.petro.sms2email.R
import ws.petro.sms2email.filter.Rule

class RuleListAdapter(context: Context) : RecyclerView.Adapter<RuleListAdapter.WordViewHolder>() {

    private val TAG: String = "RuleListAdapter"
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var rules = emptyList<Rule>() // Cached copy of list

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ruleItemTitle: TextView = itemView.findViewById(R.id.rule_title)
        val ruleItemDescription: TextView = itemView.findViewById(R.id.rule_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.rule_list_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = rules[position]
        holder.ruleItemTitle.text = current.title
        holder.ruleItemDescription.text = current.toEmail
    }

    internal fun setRules(rules: List<Rule>) {
        this.rules = rules
        for (rule in rules) {
            Log.d(TAG, "Have rule item: ${rule.title}")
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = rules.size
}
