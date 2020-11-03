package ws.petro.sms2email.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ws.petro.sms2email.R
import ws.petro.sms2email.filter.Rule

class RuleListAdapter(context: Context) : RecyclerView.Adapter<RuleListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var rules = emptyList<Rule>() // Cached copy of list

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ruleItemView: TextView = itemView.findViewById(R.id.rule_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.rule_list_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = rules[position]
        holder.ruleItemView.text = current.title
    }

    internal fun setRules(rules: List<Rule>) {
        this.rules = rules
        notifyDataSetChanged()
    }

    override fun getItemCount() = rules.size
}
