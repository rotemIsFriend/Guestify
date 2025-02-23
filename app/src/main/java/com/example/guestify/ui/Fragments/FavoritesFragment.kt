package com.example.guestify.ui.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestify.R
import com.example.guestify.data.model.Event
import com.example.guestify.databinding.FragmentFavoritesBinding
import com.example.guestify.ui.viewModels.EventsViewModel
import com.example.guestify.ui.Adapters.EventAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val eventsViewModel: EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())

        eventsViewModel.observeFavoriteEvents()
        observeFavorites()




        return binding.root
    }

    private fun showConfirmationDialog(event: Event) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_event_confirmation))
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_event_this_action_cannot_be_undone))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            eventsViewModel.deleteEvent(event)
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun observeFavorites() {
        eventsViewModel.favoriteEventsLiveData.observe(viewLifecycleOwner) { eventsList ->
            binding.recyclerViewFavorites.adapter =
                EventAdapter(eventsList, eventsViewModel, object :
                    EventAdapter.EventListener {
                    override fun onEventClicked(index: Int) {
                        val bundle = bundleOf("eventId" to eventsList[index].id)
                        findNavController().navigate(
                            R.id.action_favoritesFragment_to_eventDetailsFragment,
                            bundle
                        )
                    }

                    override fun onEventDeleted(index: Int) {
                        showConfirmationDialog(eventsList[index])
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
