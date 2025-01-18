package com.example.amtracked

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amtraker.SharedViewModel
import com.example.amtraker.api.Train
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val STATION_CODE = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [StationDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StationDetailsFragment : Fragment() {

    private lateinit var trainRecyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var trainsAdapter: TrainsAdapter
    
    private var stationCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        arguments?.let {
            stationCode = it.getString(STATION_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.trainRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        this.trainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        this.updateUI()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param stationCode Parameter 1.
         * @return A new instance of fragment StationDetailsFragment.
         */
        @JvmStatic
        fun newInstance(stationCode: String) =
            StationDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(STATION_CODE, stationCode)
                }
            }
    }

    private fun updateUI() {
        var selectStationMeta = stationCode?.let { viewModel.getStationByCode(it) }
        if (selectStationMeta != null) {

            val stationNameTextView: TextView = requireView().findViewById(R.id.station_name)
            val stationAddressTextView: TextView = requireView().findViewById(R.id.address)
            val stationTimeZoneTextView: TextView = requireView().findViewById(R.id.time_zone)

            stationNameTextView.text = selectStationMeta.name
            stationAddressTextView.text = buildString {
                append(selectStationMeta.address1)
                if (selectStationMeta.hasAddress) {
                    append(", " + selectStationMeta.city + ", " + selectStationMeta.state)
                }
            }
            stationTimeZoneTextView.text = selectStationMeta.tz

            trainsAdapter = viewModel.getTrainsByKeys(selectStationMeta.trains)
                ?.let { TrainsAdapter(it) }!!
            trainRecyclerView.adapter = trainsAdapter
        }
    }

    private fun refresh(){
        viewModel.reInit()
        updateUI()
    }

//    inner class SelectTrainAdapter(private var train: Train)
    inner class TrainsAdapter(private val trainsList: List<Train>) : RecyclerView.Adapter<TrainsAdapter.TrainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.train_view_holder, parent, false)

            return TrainViewHolder(view)
        }

        override fun getItemCount(): Int = trainsList.size

        override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
            holder.bind(trainsList[position])
        }

        inner class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val idTextView: TextView = this.itemView.findViewById(R.id.train_id)
            val serviceLineTextView: TextView = this.itemView.findViewById(R.id.sevice_line)
            val statusTextView: TextView = this.itemView.findViewById(R.id.next_station_or_status)
            val destStationTextView: TextView = this.itemView.findViewById(R.id.dest_station)
            val destETATextView: TextView = this.itemView.findViewById(R.id.eta)
            fun bind(train: Train) {
                val selectStation = train.stations.find { it.code == stationCode }
                idTextView.text = buildString {
                    append("Train ")
                    append(train.trainNum)
                }
                serviceLineTextView.text = buildString {
                    append(train.routeName)
                    append(" Line")
                }
                destStationTextView.text = buildString {
                    append("To: ")
                    append(train.destName)
                    append(" Station")
                }
                statusTextView.text = buildString {
                    if (selectStation != null) {
                        if (selectStation.status == "Station") append("At ")

                        append(selectStation.status)
                    }
                }
                destETATextView.text = buildString {
                    // Finds and formats the arrival time at the next station
                    val nextStation = train.stations.find { it.code == train.eventCode }
                    val nextStationArrival = nextStation?.arr
                    var formattedArrString = "Unavailable"
                    if (nextStationArrival != null) {

                        val zonedDateTime = ZonedDateTime.parse(nextStationArrival + '[' + nextStation.tz + ']')
                        val formatter = DateTimeFormatter.ofPattern("hh:mm a z MM/dd", Locale.ENGLISH)
                        formattedArrString = zonedDateTime.format(formatter)
                    }
                    append("ETA: $formattedArrString")
                }
                val buttonStationStops: Button = itemView.findViewById(R.id.train_stops)
                buttonStationStops.setOnClickListener{
                    train.trainID?.let { it1 -> TrainStopsFragment.newInstance(it1) }?.let { it2 ->
                        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                            it2
                        ).addToBackStack(null).commit()
                    }
                }
            }
        }
    }
}