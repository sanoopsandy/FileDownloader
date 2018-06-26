package com.example.jobdownloader.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobdownloader.BR

// Common baseAdapter to load the recyclerview
class BaseRecyclerAdapter : RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>() {

    @LayoutRes
    private var layoutId: Int = 0

    private lateinit var items: List<*>
    private lateinit var context: Context
    private lateinit var clickListener: CustomClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setLayoutId(@LayoutRes layoutId: Int) {
        this.layoutId = layoutId
    }

    fun setItems(items: List<*>) {
        this.items = items
    }

    fun setCustomClickListener(customClickListener: CustomClickListener) {
        clickListener = customClickListener
    }

    fun setContext(context: Context) {
        this.context = context
    }

    inner class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ob: Any?) {
            binding.setVariable(BR.model, ob)
            binding.setVariable(BR.handler, this)
            binding.setVariable(BR.position, adapterPosition)
            binding.executePendingBindings()
        }

        fun onCustomClick(view: View, position: Int) {
            clickListener.onCustomClick(view, position)
        }
    }


    interface CustomClickListener {
        fun onCustomClick(view: View, position: Int)
    }
}