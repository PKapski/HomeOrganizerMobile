package pl.polsl.homeorganizer.notes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.homeorganizer.R
import pl.polsl.homeorganizer.notes.NotesContent.NotesCallback

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [NoteFragment.OnListFragmentInteractionListener] interface.
 */
class NoteFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false)
        setNotesToolbar()
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                getNotes()
            }
        }
        return view
    }

    private fun RecyclerView.getNotes() {
        NotesContent.getChecklists(object : NotesCallback {
            override fun onSuccessResponse(result: MutableList<Note>) {
                adapter =
                    MyNotesRecyclerViewAdapter(
                        NotesContent.items,
                        listener
                    )
            }
        })
    }

    private fun setNotesToolbar() {
        val addToolbar =
            (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar_with_add)
        val householdToolbar =
            (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar_basic)
        addToolbar.visibility = View.VISIBLE
        householdToolbar.visibility = View.GONE
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Note?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            NoteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onResume() {
        with(view as RecyclerView) {
            getNotes()
        }
        super.onResume()
    }
}
