package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

sealed class AbstractSource constructor(var Name: String) {

    abstract fun toXml(w: XMLStreamWriter, tagName: String)

    companion object {
        fun fromXml(xml: Element?): AbstractSource? {
            if (xml == null) return null

            return when(getAttributeNsString(xml, XSI_URI, "type")) {
                "Application_t" -> Application.fromXml(xml)
                "Device_t" -> Device.fromXml(xml)
                else -> BasicSource.fromXml(xml)
            }

        }
    }
}

class Device(
    Name: String,
    var UnitId: Long?,
    var ProductID: Int?,
    var Version: Version

) : AbstractSource(Name)
{
    override fun toXml(w: XMLStreamWriter, tagName: String) {
        w.writeStartElement(tagName)
        writeBasicAttributeNs(w, XSI_URI, "type", "Device_t")

        writeBasicElement(w, "Name", Name)
        writeBasicElement(w, "UnitId", UnitId)
        writeBasicElement(w, "ProductID", ProductID)
        Version.toXml(w)
        w.writeEndElement()
    }

    fun validate(): Device {
        return this
    }

    companion object {
        fun fromXml(xml: Element?) : Device? {
            if (xml == null) return null

            val ret = Device(
                    getValueString(xml, "Name")!!,
                    getValueLong(xml, "UnitId"),
                    getValueInt(xml, "ProductID"),
                    Version.fromXml(getElement(xml, "Version"))!!
            )

            return ret.validate()
        }

    }
}

class Application(
        Name: String,
        var Build: Build,
        var LangID: String,
        var PartNumber: String

) : AbstractSource(Name)
{
    override fun toXml(w: XMLStreamWriter, tagName: String) {
        w.writeStartElement(tagName)
        writeBasicAttributeNs(w, XSI_URI, "type", "Application_t")

        writeBasicElement(w, "Name", Name)
        Build.toXml(w)
        writeBasicElement(w, "LangID", LangID)
        writeBasicElement(w, "PartNumber", PartNumber)
        w.writeEndElement()
    }

    fun validate(): Application {
        if (LangID.length < 2) {
            throw TcxValidationException("LangID must have length >= 2")
        }
        if ("""[\p{Lu}\d]{3}-[\p{Lu}\d]{5}-[\p{Lu}\d]{2}""".toRegex().matchEntire(PartNumber) == null) {
            throw TcxValidationException("Invalid PartNumber: $PartNumber")
        }
        return this
    }

    companion object {
        fun fromXml(xml: Element?) : Application? {
            if (xml == null) return null

            val ret = Application(
                    getValueString(xml, "Name")!!,
                    Build.fromXml(getElement(xml, "Build"))!!,
                    getValueString(xml, "LangID")!!,
                    getValueString(xml, "PartNumber")!!
            )

            return ret.validate()
        }

    }
}

class BasicSource(Name: String) : AbstractSource(Name)
{
    override fun toXml(w: XMLStreamWriter, tagName: String) {
        w.writeStartElement(tagName)
        writeBasicElement(w, "Name", Name)
        w.writeEndElement()
    }

    fun validate(): BasicSource {
        return this
    }

    companion object {
        fun fromXml(xml: Element?) : BasicSource? {
            if (xml == null) return null

            val ret = BasicSource(
                    getValueString(xml, "Name")!!
            )

            return ret.validate()
        }

    }
}