package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import java.time.OffsetDateTime
import javax.xml.stream.XMLStreamWriter

data class ActivityLap(
        var StartTime: OffsetDateTime,
        var TotalTimeSeconds: Double,
        var DistanceMeters: Double,
        var MaximumSpeed: Double? = null,
        var Calories: Int,
        var AverageHeartRateBpm: HeartRateInBeatsPerMinute? = null,
        var MaximumHeartRateBpm: HeartRateInBeatsPerMinute? = null,
        var Intensity: IntensityToken,
        var Cadence: Short? = null, //[TODO: range(1,255)]
        var TriggerMethod: TriggerMethodToken,
        var Track: Track = Track(),
        var Notes: String? = null,
        var Extensions: MutableList<Extension> = mutableListOf()
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Lap")
        writeBasicAttribute(w, "StartTime", StartTime.toString())

        writeBasicElement(w, "TotalTimeSeconds", TotalTimeSeconds)
        writeBasicElement(w, "DistanceMeters", DistanceMeters)
        writeBasicElement(w, "MaximumSpeed", MaximumSpeed)
        writeBasicElement(w, "Calories", Calories)
        AverageHeartRateBpm?.toXml(w, "AverageHeartRateBpm")
        MaximumHeartRateBpm?.toXml(w, "MaximumHeartRateBpm")
        writeBasicElement(w, "Intensity", Intensity)
        writeBasicElement(w, "Cadence", Cadence)
        writeBasicElement(w, "TriggerMethod", TriggerMethod)
        Track.toXml(w)
        writeBasicElement(w, "Notes", Notes)

        w.writeEndElement()
    }

    fun validate(): ActivityLap {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): ActivityLap? {
            if (xml == null) return null

            val ret = ActivityLap(
                    StartTime = getAttributeOffsetDateTime(xml, "StartTime")!!,
                    TotalTimeSeconds = getValueDouble(xml, "TotalTimeSeconds")!!,
                    DistanceMeters = getValueDouble(xml, "DistanceMeters")!!,
                    MaximumSpeed = getValueDouble(xml, "MaximumSpeed"),
                    Calories = getValueInt(xml, "Calories")!!,
                    AverageHeartRateBpm = HeartRateInBeatsPerMinute.fromXml(getElement(xml, "AverageHeartRateBpm")),
                    MaximumHeartRateBpm = HeartRateInBeatsPerMinute.fromXml(getElement(xml, "MaximumHeartRateBpm")),
                    Intensity = IntensityToken.fromString(getValueString(xml, "Intensity"))!!,
                    Cadence = getValueShort(xml, "Cadence"),
                    TriggerMethod = TriggerMethodToken.fromString(getValueString(xml, "TriggerMethod"))!!,
                    Track = Track.fromXml(getElement(xml, "Track"))!!,
                    Notes = getValueString(xml, "Notes")
            )

            return ret.validate()
        }
    }
}