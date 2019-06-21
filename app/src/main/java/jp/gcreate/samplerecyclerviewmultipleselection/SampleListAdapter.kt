package jp.gcreate.samplerecyclerviewmultipleselection

import android.view.ViewGroup
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
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleItemViewHolder {
        return SampleItemViewHolder.createViewHolder(R.layout.item_sample_data, parent, false)
    }
    
    override fun onBindViewHolder(holder: SampleItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    
    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }
}