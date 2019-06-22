package jp.gcreate.samplerecyclerviewmultipleselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import jp.gcreate.samplerecyclerviewmultipleselection.databinding.ItemSampleDataBinding

class SampleItemViewHolder(val binding: ItemSampleDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    
    fun bind(data: SampleData, isSelected: Boolean = false) {
        binding.item = data
        binding.container.isActivated = isSelected
    }
    
    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
        return object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getSelectionKey(): Long? = itemId
            
            override fun getPosition(): Int = adapterPosition
        }
    }
    
    companion object {
        @JvmStatic
        fun createViewHolder(@LayoutRes layoutId: Int, parent: ViewGroup,
            attachToRoot: Boolean): SampleItemViewHolder {
            return SampleItemViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent,
                    attachToRoot))
        }
    }
}
