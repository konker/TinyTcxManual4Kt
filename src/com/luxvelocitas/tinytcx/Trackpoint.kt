package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import java.time.OffsetDateTime
import javax.xml.stream.XMLStreamWriter

data class Trackpoint(
        var Time: OffsetDateTime,
        var Position: Position? = null,
        var AltitudeMeters: Double? = null,
        var DistanceMeters: Double? = null,
        var HeartRateBpm: HeartRateInBeatsPerMinute? = null,
        var Cadence: Short? = null, //[TODO: range(1,255)]
        var SensorState: SensorStateToken? = null,
        var Extensions: MutableList<Extension> = mutableListOf()
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Trackpoint")
        writeBasicElement(w, "Time", Time)
        Position?.toXml(w, "Position")
        writeBasicElement(w, "AltitudeMeters", AltitudeMeters)
        writeBasicElement(w, "DistanceMeters", DistanceMeters)
        HeartRateBpm?.toXml(w, "HeartRateBpm")
        writeBasicElement(w, "Cadence", Cadence)
        writeBasicElement(w, "SensorState", SensorState)
        w.writeEndElement()
    }

    fun validate(): Trackpoint {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Trackpoint? {
            if (xml == null) return null

            val ret = Trackpoint(
                    getValueOffsetDateTime(xml, "Time")!!,
                    Position.fromXml(getElement(xml, "Position")),
                    getValueDouble(xml, "AltitudeMeters"),
                    getValueDouble(xml, "DistanceMeters"),
                    HeartRateInBeatsPerMinute.fromXml(getElement(xml, "HeartRateBpm")),
                    getValueShort(xml, "Cadence"),
                    SensorStateToken.fromString(getValueString(xml, "SensorState"))
            )

            return ret.validate()
        }
    }
}