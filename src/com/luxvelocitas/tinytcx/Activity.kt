package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import java.time.OffsetDateTime
import javax.xml.stream.XMLStreamWriter

data class Activity(
        var Sport: SportToken,
        var Id: OffsetDateTime, //[TODO: unique]
        var Laps: MutableList<ActivityLap> = mutableListOf(), //[TODO: len(>0)]
        var Notes: String? = null,
        var Training: Training? = null,
        var Creator: AbstractSource? = null,
        var Extensions: MutableList<Extension>? = mutableListOf()
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Activity")
        writeBasicAttribute(w, "Sport", Sport.toString())

        writeBasicElement(w, "Id", Id)
        Laps.forEach {
            lap -> lap.toXml(w)
        }
        writeBasicElement(w, "Notes", Notes)
        Training?.toXml(w)
        Creator?.toXml(w, "Creator")
        w.writeEndElement()
    }

    fun validate(): Activity {
        if (Laps.size == 0) {
            throw TcxValidationException("Activity.Laps must have length > 0")
        }
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Activity? {
            if (xml == null) return null

            val ret = Activity(
                    Sport = SportToken.fromString(getAttributeString(xml, "Sport"))!!,
                    Id = getValueOffsetDateTime(xml, "Id")!!,
                    Notes = getAttributeString(xml, "Notes"),
                    Training = Training.fromXml(getElement(xml, "Training")),
                    Creator = AbstractSource.fromXml(getElement(xml, "Creator"))
            )

            xml
            .getElementsByTagName("Lap")
            ?.forEach {
                node -> ret.Laps.add(ActivityLap.fromXml(node as Element)!!)
            }

            return ret.validate()
        }
    }
}