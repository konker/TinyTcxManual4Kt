package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.xml.stream.XMLStreamWriter

//-----------------------------------------------------------------------------
// Utils
fun getElement(xml: Element?, name: String): Element? {
    return xml?.getElementsByTagName(name)?.item(0) as Element?
}

fun getValueString(xml: Element?, name: String): String? {
    return xml?.getElementsByTagName(name)?.item(0)?.firstChild?.nodeValue
}

fun getValueShort(xml: Element?, name: String): Short? {
    return getValueString(xml, name)?.toShort()
}

fun getValueInt(xml: Element?, name: String): Int? {
    return getValueString(xml, name)?.toInt()
}

fun getValueLong(xml: Element?, name: String): Long? {
    return getValueString(xml, name)?.toLong()
}

fun getValueDouble(xml: Element?, name: String): Double? {
    return getValueString(xml, name)?.toDouble()
}

fun getValueBoolean(xml: Element?, name: String): Boolean? {
    return getValueString(xml, name)?.equals("true", true)
}

fun getValueOffsetDateTime(xml: Element?, name: String): OffsetDateTime? {
    return OffsetDateTime.parse(getValueString(xml, name))
}

fun getAttributeString(xml: Element?, name: String): String? {
    return xml?.getAttribute(name)
}

fun getAttributeOffsetDateTime(xml: Element?, name: String): OffsetDateTime? {
    if (xml == null) return null
    return OffsetDateTime.parse(xml.getAttribute(name))
}

fun getAttributeNsString(xml: Element?, ns: String, name: String): String? {
    return xml?.getAttributeNS(ns, name)
}

fun writeBasicElement(w: XMLStreamWriter, name: String, value: Any?) {
    if (value != null) {
        w.writeStartElement(name)
        if (value is OffsetDateTime) {
            w.writeCharacters(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value))
        }
        else {
            w.writeCharacters(value.toString())
        }
        w.writeEndElement()
    }
}

fun writeBasicAttribute(w: XMLStreamWriter, name: String, value: Any?) {
    if (value != null) {
        if (value is OffsetDateTime) {
            w.writeAttribute(name, DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value))
        }
        else {
            w.writeAttribute(name, value.toString())
        }
    }
}

fun writeBasicAttributeNs(w: XMLStreamWriter, ns: String, name: String, value: Any?) {
    if (value != null) {
        if (value is OffsetDateTime) {
            w.writeAttribute(ns, name, DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value))
        }
        w.writeAttribute(ns, name, value.toString())
    }
}

internal fun NodeList.forEach(action: (Node) -> Unit) {
    for(i in 0 until this.length) {
        action(this.item(i))
    }
}