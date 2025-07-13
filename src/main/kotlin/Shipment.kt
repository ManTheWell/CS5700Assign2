import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Shipment(private val ID: String) {
    private var numUpdates: Int = 0

    private var status: String = ""
    private var location: String = ""
    private var expDeliveryTime: String = ""

    private val updates = mutableListOf<String>()
    private val notes = mutableListOf<String>()


    private fun convertTime(epocString : String): String {
        val epochMillis: Long = epocString.toLongOrNull() ?: return ""

        val instant = Instant.ofEpochMilli(epochMillis)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' HH:mm")
            .withZone(ZoneId.systemDefault()) // Or ZoneId.of("UTC")
        return formatter.format(instant)
    }

    fun parse(update: String) {
        val parts = update.split(",")
        status = parts[0]
        val timestamp = convertTime(parts[2])

        when (status) {
            "created" -> {
                updates.add(0, "Shipment created on $timestamp")
            }

            "shipped" -> {
                expDeliveryTime = parts.getOrNull(3) ?: ""
                updates.add(0, "Shipment shipped on $timestamp")
            }

            "location" -> {
                status = "new location"
                location = parts.getOrNull(3) ?: ""

                updates.add(0, "New shipment location: $location on $timestamp")
            }

            "delivered" -> {
                updates.add(0, "Shipment delivered at ${convertTime(timestamp)}")
            }

            "delayed" -> {
                expDeliveryTime = convertTime(parts.getOrNull(3) ?: "")

                updates.add(0, "Shipment delayed on $timestamp, new expected delivery date $expDeliveryTime")
            }

            "lost" -> {
                expDeliveryTime = "none"
                updates.add(0, "Shipment lost on ${convertTime(timestamp)}, last known location: $location")

                location = "unknown"
            }

            "canceled" -> {
                location = "none"
                expDeliveryTime = "none"

                updates.add(0, "Shipment canceled on $timestamp")
            }

            "noteadded" -> {
                notes.add(0, "(($timestamp): " + (parts.getOrNull(3) ?: ""))
            }
        }
    }
}