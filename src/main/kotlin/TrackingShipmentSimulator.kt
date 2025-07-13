import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class TrackingShipmentSimulator {
    private val shipments = mutableMapOf<String, Shipment>()
    private var numUpdates = mutableStateOf(0)

    fun launch() {
        UpdateSimulator(this, "src/main/kotlin/test.txt")
    }

    fun processUpdate(update: String) {
        numUpdates.value++

        val parts = update.split(",")
        val id = parts[1]
        val shipment = shipments.getOrPut(id) { Shipment(id) }

        shipment.parse(update)
    }

    fun getShipments():MutableMap<String, Shipment> {
        return shipments
    }

    fun getNumUpdates(): MutableState<Int> {
        return numUpdates
    }
}
