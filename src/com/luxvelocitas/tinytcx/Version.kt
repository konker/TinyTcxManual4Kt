package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class Version(
    var VersionMajor: Int,
    var VersionMinor: Int,
    var BuildMajor: Int? = null,
    var BuildMinor: Int? = null
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Version")
        writeBasicElement(w, "VersionMajor", VersionMajor)
        writeBasicElement(w, "VersionMinor", VersionMinor)
        writeBasicElement(w, "BuildMajor", BuildMajor)
        writeBasicElement(w, "BuildMinor", BuildMinor)
        w.writeEndElement()
    }

    fun validate(): Version {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Version? {
            if (xml == null) return null

            val ret = Version(
                    getValueInt(xml, "VersionMajor")!!,
                    getValueInt(xml, "VersionMinor")!!,
                    getValueInt(xml, "BuildMajor"),
                    getValueInt(xml, "BuildMinor")
            )

            return ret.validate()
        }
    }
}