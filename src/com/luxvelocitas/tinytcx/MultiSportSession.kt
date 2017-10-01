package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import java.time.OffsetDateTime
import javax.xml.stream.XMLStreamWriter

data class MultiSportSession(
        var Id: OffsetDateTime, //[TODO: unique]
        var FirstSport: FirstSport,
        var NextSports: MutableList<NextSport> = mutableListOf(),
        var Notes: String? = null
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("MultiSportSession")
        writeBasicAttribute(w, "Id", Id)

        FirstSport.toXml(w)
        NextSports.forEach {
            nextSport -> nextSport.toXml(w)
        }
        writeBasicElement(w, "Notes", Notes)
        w.writeEndElement()
    }

    fun validate(): MultiSportSession {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): MultiSportSession? {
            if (xml == null) return null

            val ret = MultiSportSession(
                    Id = getAttributeOffsetDateTime(xml, "Id")!!,
                    FirstSport = FirstSport.fromXml(getElement(xml, "FirstSport"))!!,
                    Notes = getAttributeString(xml, "Notes")
            )

            xml
            .getElementsByTagName("NextSport")
            ?.forEach {
                node -> ret.NextSports.add(NextSport.fromXml(node as Element)!!)
            }

            return ret.validate()
        }
    }
}