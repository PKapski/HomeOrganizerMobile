package pl.polsl.homeorganizer.checklists

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.homeorganizer.R
import java.io.Serializable

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ChecklistItemListFragment.OnListFragmentInteractionListener] interface.
 */
class ChecklistItemListFragment : Fragment() {

    // TODO: Customize parameters
    private lateinit var checklist: Checklist

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            checklist = it.getSerializable(CHECKLIST) as Checklist
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_checklistitem_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                buildAdapter()
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: ChecklistItem?)

    }

    companion object {
        private const val CHECKLIST = "checklist"

        @JvmStatic
        fun newInstance(checklist: Checklist) =
            ChecklistItemListFragment().apply {
                arguments = Bundle(1).apply {
                    putSerializable(CHECKLIST, checklist as Serializable)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        with(view as RecyclerView) {
                buildAdapter()
        }
    }

    private fun RecyclerView.buildAdapter() {
        adapter =
            MyChecklistItemRecyclerViewAdapter(
                checklist.itemList,
                listener
            )
    }
}
