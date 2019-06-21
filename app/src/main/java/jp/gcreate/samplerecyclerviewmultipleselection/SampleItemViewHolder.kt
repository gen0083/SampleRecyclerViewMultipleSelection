package jp.gcreate.samplerecyclerviewmultipleselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import jp.gcreate.samplerecyclerviewmultipleselection.databinding.ItemSampleDataBinding

class SampleItemViewHolder(val binding: ItemSampleDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    
    fun bind(data: SampleData) {
        binding.item = data
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
