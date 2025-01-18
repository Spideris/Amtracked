package com.example.amtracked

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amtraker.SharedViewModel
import com.example.amtraker.TrainListFragment.TrainAdapter
import com.example.amtraker.api.Station
import com.example.amtraker.api.StationMeta
import com.example.amtraker.api.Train
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TRAIN_ID = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [TrainStopsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrainStopsFragment : Fragment() {

    private lateinit var trainStopsRecyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var stationAdapter: TrainStopsAdapter

    private var trainId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        arguments?.let {
            trainId = it.getString(TRAIN_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_train_stops, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.trainStopsRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        this.trainStopsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        this.updateUI()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param trainId Parameter 1.
         * @return A new instance of fragment TrainStopsFragment.
         */
        @JvmStatic
        fun newInstance(trainId: String) =
            TrainStopsFragment().apply {
                arguments = Bundle().apply {
                    putString(TRAIN_ID, trainId)
                }
            }
    }

    private fun updateUI() {
        var selectTrain = trainId?.let { viewModel.getTrainById(it) }
        if (selectTrain != null) {
            stationAdapter = TrainStopsAdapter(selectTrain.stations)
            trainStopsRecyclerView.adapter = stationAdapter
        }
    }

    private fun refresh(){
        viewModel.reInit()
        updateUI()
    }

//    inner class SelectTrainAdapter(private var train: Train)
    inner class TrainStopsAdapter(private val stationList: List<Station>) : RecyclerView.Adapter<TrainStopsAdapter.StopViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.stop_view_holder, parent, false)

            return StopViewHolder(view)
        }

        override fun getItemCount(): Int = stationList.size

        override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
            holder.bind(stationList[position])
        }

        inner class StopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val stationNameTextView: TextView = this.itemView.findViewById(R.id.station_name)
            val stationETATextView: TextView = this.itemView.findViewById(R.id.eta)
            val stationStatusTextView: TextView = this.itemView.findViewById(R.id.status)

            fun bind(station: Station) {
                stationNameTextView.text = buildString {
                    append(station.name + " Station")
                }
                stationETATextView.text = buildString {
                    var nextStationArrival = station.arr
                    var formattedArrString = "Unavailable"
                    if (nextStationArrival != null) {

                        val zonedDateTime = ZonedDateTime.parse(nextStationArrival + '[' + station.tz + ']')
                        val formatter = DateTimeFormatter.ofPattern("hh:mm a z MM/dd", Locale.ENGLISH)
                        formattedArrString = zonedDateTime.format(formatter)
                    }

                    append("ETA: $formattedArrString")
                }
                stationStatusTextView.text = buildString {
                    if (station.status == "Station") append("At ")
                    append(station.status)
                }

                val buttonStationDetails: Button = itemView.findViewById(R.id.stations_trains)
                buttonStationDetails.setOnClickListener{
                    station.code?.let { it1 -> StationDetailsFragment.newInstance(it1) }?.let { it2 ->
                        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                            it2
                        ).addToBackStack(null).commit()
                    }
                }
            }
        }
    }
}