package sk.skwig.aisinator.feature

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(open val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)