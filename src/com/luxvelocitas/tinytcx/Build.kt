package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

data class Build(
        var Version: Version,
        var Type: BuildTypeToken? = null,
        var Time: String? = null,
        var Builder: String? = null
)
{
    fun toXml(w: XMLStreamWriter) {
        w.writeStartElement("Build")
        Version.toXml(w)
        writeBasicElement(w, "Type", Type)
        writeBasicElement(w, "Time", Time)
        writeBasicElement(w, "Builder", Builder)
        w.writeEndElement()
    }

    fun validate(): Build {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): Build? {
            if (xml == null) return null

            val ret = Build(
                    Version.fromXml(getElement(xml, "Version"))!!,
                    BuildTypeToken.fromString(getValueString(xml, "Type")),
                    getValueString(xml, "Time"),
                    getValueString(xml, "Builder")
            )

            return ret.validate()
        }
    }
}