

import com.luxvelocitas.tinytcx.TinyTcx
import org.w3c.dom.Document
import java.io.BufferedReader
import java.io.File
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter


fun main(args: Array<String>) {
    val fname = "activity_1966020755.tcx";
//    val fname = "20170921_204103_6.tcx";
    println(fname)

    val bufferedReader: BufferedReader = File(ClassLoader.getSystemResource(fname).file).bufferedReader()

    val xmlString = bufferedReader.use { it.readText() }

    val factory: DocumentBuilderFactory =
        DocumentBuilderFactory
            .newInstance()

    factory.setFeature("http://xml.org/sax/features/namespaces", true);

    println(factory.isNamespaceAware())

    val xml: Document =
        factory
            .newDocumentBuilder()
            .parse(xmlString.byteInputStream())

    val tcx: TinyTcx = TinyTcx.fromXml(xml.documentElement)!!

    println(tcx.Activities[0].Laps[0].Track.Trackpoints[0].Time)
    println(tcx.Activities[0].Creator)

    var stringWriter: StringWriter = StringWriter()

    val writer: XMLStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter)
    tcx.toXml(writer)
    println(stringWriter.toString())
}