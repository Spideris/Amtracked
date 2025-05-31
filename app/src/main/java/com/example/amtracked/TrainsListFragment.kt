package com.example.amtraker

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
import com.example.amtracked.R
import com.example.amtracked.TrainStopsFragment
import com.example.amtraker.api.Train
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TrainListFragment : Fragment() {

    private lateinit var trainRecyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: TrainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        println("Fragment Created")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        println("initializing view with inflater")
        return inflater.inflate(R.layout.list_holder_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonRefresh: Button = view.findViewById(R.id.refresh)
        buttonRefresh.setOnClickListener {
            refresh()
        }

        println("view created")

        println("Initializing this recycler with the train recycler?")
        this.trainRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        println("Initializing layout manager")
        this.trainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        println("Updating UI")
        this.updateUI()
        println("Returning recycler view")
    }

    companion object {
        fun newInstance() : TrainListFragment {
            return TrainListFragment()
        }
    }

    private fun updateUI() {
        println("UI attempting update")
        viewModel.trains.observe(viewLifecycleOwner) { trainData ->
            adapter = TrainAdapter(trainData)
            trainRecyclerView.adapter = adapter
//            adapter.initList()
            println("Received train data in UpdateUI")
        }
        println("UI update completing")
    }

    private fun refresh(){
        viewModel.reInit()
        updateUI()
    }

    inner class TrainAdapter(private val trainMap: Map<String, List<Train>>) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

        private var trainAdapterData: List<Train> = listOf()
        init {
            println("TrainAdapter init")
            trainAdapterData = trainMap.values.flatMap { it }
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.train_view_holder, parent, false)

            return TrainViewHolder(view)
        }

        override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
            holder.bind(trainAdapterData[position])
        }

        override fun getItemCount(): Int = trainAdapterData.size

        inner class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val idTextView: TextView = this.itemView.findViewById(R.id.train_id)
            val serviceLineTextView: TextView = this.itemView.findViewById(R.id.sevice_line)
            val stationTextView: TextView = this.itemView.findViewById(R.id.next_station_or_status)
            val destStationTextView: TextView = this.itemView.findViewById(R.id.dest_station)
            val destETATextView: TextView = this.itemView.findViewById(R.id.eta)
            fun bind(train: Train) {
                idTextView.text = buildString {
                    append("Train ")
                    append(train.trainNum)
                }
                serviceLineTextView.text = buildString {
                    append(train.routeName)
                    append(" Line")
                }
                stationTextView.text = buildString {
                    append("Next: ")
                    append(train.eventName)
                    append(" Station")
                }
                destStationTextView.text = buildString {
                    append("To: ")
                    append(train.destName)
                    append(" Station")
                }

                // Finds and formats the arrival time at the next station
                destETATextView.text = buildString {
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

                // Button to see train's details
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