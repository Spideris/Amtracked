package com.example.amtracked

import android.icu.util.TimeZone
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amtraker.SharedViewModel
import com.example.amtraker.api.StationMeta
import java.time.ZonedDateTime

/**
 * A fragment representing a list of Items.
 */
class StationMetaListFragment : Fragment() {

    private lateinit var stationMetaRecyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: StationMetaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_holder_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.stationMetaRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView

        this.stationMetaRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        this.updateUI()

//        viewModel.stations.observe(viewLifecycleOwner) { stationMetaData ->
//            adapter = StationMetaAdapter(stationMetaData)
//            adapter?.initList()
//            stationMetaRecyclerView.adapter
//        }
    }

    private fun updateUI() {
        viewModel.stations.observe(viewLifecycleOwner) { stationData ->
            adapter = StationMetaAdapter(stationData)
            stationMetaRecyclerView.adapter = adapter
//            adapter.initList()
        }
    }

    private fun refresh(){
        viewModel.reInit()
        updateUI()
    }

    companion object {

        @JvmStatic
        fun newInstance() : StationMetaListFragment {
            return StationMetaListFragment()
        }

    }

    inner class StationMetaAdapter(private val stationMetaMap: Map<String, StationMeta>) : RecyclerView.Adapter<StationMetaAdapter.StationMetaViewHolder>() {

        private var stationMetaAdapterData: List<StationMeta> = listOf()
        init {
            initList()
        }
        fun initList() {
            stationMetaAdapterData = stationMetaMap.values.toList()
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StationMetaViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.station_meta_view_holder, parent, false)

            return StationMetaViewHolder(view)
        }

        override fun onBindViewHolder(holder: StationMetaViewHolder, position: Int) {
            holder.bind(stationMetaAdapterData[position])
        }

        override fun getItemCount(): Int = stationMetaAdapterData.size

        inner class StationMetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val stationNameTextView: TextView = this.itemView.findViewById(R.id.station_name)
            val stationAddressTextView: TextView = this.itemView.findViewById(R.id.address)
            val stationTimeZoneTextView: TextView = this.itemView.findViewById(R.id.time_zone)

            fun bind(stationMeta: StationMeta) {
                stationNameTextView.text = buildString {
                    append(stationMeta.name + " Station")
                }
                stationAddressTextView.text = buildString {
                    append(stationMeta.address1)
                    if (stationMeta.hasAddress) {
                        append(", " + stationMeta.city + ", " + stationMeta.state)
                    }

                }
                stationTimeZoneTextView.text = buildString {
                    val timeZone = TimeZone.getTimeZone(stationMeta.tz)
                    append("Time Zone: " + timeZone.getDisplayName())
                }
                val buttonStationDetails: Button = itemView.findViewById(R.id.stations_trains)
                buttonStationDetails.setOnClickListener{
                    stationMeta.code?.let { it1 -> StationDetailsFragment.newInstance(it1) }?.let { it2 ->
                         parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                             it2
                         ).addToBackStack(null).commit()
                    }
                }
            }
        }
    }
}