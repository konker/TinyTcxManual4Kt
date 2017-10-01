package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class Track(
    var Trackpoints: MutableList<Trackpoint> = mutableListOf()
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Track")
        Trackpoints.forEach {
            trackPoint -> trackPoint.toXml(w)
        }
        w.writeEndElement()
    }

    fun validate(): Track {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Track? {
            if (xml == null) return null

            val ret = Track()

            xml
            .getElementsByTagName("Trackpoint")
            ?.forEach {
                node -> ret.Trackpoints.add(Trackpoint.fromXml(node as Element)!!)
            }

            return ret.validate()
        }
    }
}