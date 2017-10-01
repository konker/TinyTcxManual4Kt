@file:Suppress("MemberVisibilityCanPrivate")

package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import javax.xml.stream.XMLStreamWriter

const val XSI_URI = "http://www.w3.org/2001/XMLSchema-instance"


data class TrainingCenterDatabase(
        var Activities: MutableList<Activity> = mutableListOf(),
        var MultiSportSessions: MutableList<MultiSportSession> = mutableListOf(),
        var Author: AbstractSource? = null,
        var Extensions: MutableList<Extension> = mutableListOf()

        //[TODO] var folders: Folders?,
        //[TODO] var workouts: MutableList<Any>,
        //[TODO] var courses: MutableList<Any>,
) {

    fun toXml(w: XMLStreamWriter) {
        w.writeStartDocument("UTF-8", "1.0")

        w.writeStartElement("TrainingCenterDatabase")
        w.writeNamespace("xsi", XSI_URI)

        this.Author?.toXml(w, "Author")
            w.writeStartElement("Activities")
            Activities.forEach {
                activity -> activity.toXml(w)
            }
            MultiSportSessions.forEach {
                activity -> activity.toXml(w)
            }
            w.writeEndElement()

        w.writeEndElement()

        w.writeEndDocument()
    }

    fun validate(): TrainingCenterDatabase {
        return this
    }

    companion object {
        fun fromXml(xml: Element?): TrainingCenterDatabase? {
            if (xml == null) return null

            val activitiesElement: Element? = getElement(xml, "Activities")
            val activities: MutableList<Activity> = mutableListOf()
            val multiSportSessions: MutableList<MultiSportSession> = mutableListOf()

            if (activitiesElement != null) {
                // Handle Activity Nodes
                activitiesElement
                    .getElementsByTagName("Activity")
                    ?.forEach { node ->
                        activities.add(Activity.fromXml(node as Element)!!)
                    }

                // Handle MultiSportSession Nodes
                activitiesElement
                    .getElementsByTagName("MultiSportSession")
                    ?.forEach { node ->
                        multiSportSessions.add(MultiSportSession.fromXml(node as Element)!!)
                    }
            }

            val ret = TrainingCenterDatabase(
                    Author = AbstractSource.fromXml(getElement(xml, "Author")),
                    Activities = activities,
                    MultiSportSessions = multiSportSessions
            )

            return ret.validate()
        }
    }
}