class TrackingShipmentSimulator {
    private val shipments = mutableMapOf<String, Shipment>()

    fun launch() {
        val updateSim = UpdateSimulator(this, "src/main/kotlin/test.txt")
    }

    fun processUpdate(update: String) {
        val parts = update.split(",")
        val id = parts[1]
        val shipment = shipments.getOrPut(id) { Shipment(id) }

        shipment.parse(update)
    }
}
