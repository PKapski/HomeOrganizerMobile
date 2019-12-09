package pl.polsl.homeorganizer.checklists


import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_checklistitem.view.*
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.checklists.ChecklistItemListFragment.OnListFragmentInteractionListener


class MyChecklistItemRecyclerViewAdapter(
    private val mValues: List<ChecklistItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyChecklistItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ChecklistItem
            mListener?.onListFragmentInteraction(item)
//            item.isChecked=!item.isChecked
//            notifyItemChanged(0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_checklistitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.message
        if (item.isChecked){
            holder.mContentView.paintFlags = holder.mContentView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
