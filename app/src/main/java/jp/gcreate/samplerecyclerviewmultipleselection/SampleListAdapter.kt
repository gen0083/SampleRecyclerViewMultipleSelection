package jp.gcreate.samplerecyclerviewmultipleselection

import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SampleListAdapter : ListAdapter<SampleData, SampleItemViewHolder>(
    object: DiffUtil.ItemCallback<SampleData>() {
        override fun areItemsTheSame(oldItem: SampleData, newItem: SampleData): Boolean {
            return oldItem.id == newItem.id
        }
    
        override fun areContentsTheSame(oldItem: SampleData, newItem: SampleData): Boolean {
            return oldItem == newItem
        }
    }
){
    
    init {
        setHasStableIds(true)
    }
    
    var tracker: SelectionTracker<Long>? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleItemViewHolder {
        return SampleItemViewHolder.createViewHolder(R.layout.item_sample_data, parent, false)
    }
    
    override fun onBindViewHolder(holder: SampleItemViewHolder, position: Int) {
        val item = getItem(position)
        val selected = tracker?.isSelected(item.id) ?: false
        holder.bind(item, selected)
    }
    
    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }
}