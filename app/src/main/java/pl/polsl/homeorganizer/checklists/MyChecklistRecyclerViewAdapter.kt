package pl.polsl.homeorganizer.checklists


import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_checklist.view.*
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.checklists.ChecklistFragment.OnListFragmentInteractionListener
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Checklist] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyChecklistRecyclerViewAdapter(
    private val mValues: List<Checklist>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyChecklistRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Checklist
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_checklist, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitleView.text = item.title
        holder.mCreatorView.text = item.creator
        holder.mDateView.text = getDateFromId(item.id)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }


    private fun getDateFromId(id: String): CharSequence? {
        val sdf = SimpleDateFormat("yyyy-dd-MM")
        val date = Date(id.substring(0, 8).toInt(16) * 1000L)

        return sdf.format(date)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.checklistTitle
        val mCreatorView: TextView = mView.checklistCreator
        val mDateView: TextView = mView.checklistDate

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
