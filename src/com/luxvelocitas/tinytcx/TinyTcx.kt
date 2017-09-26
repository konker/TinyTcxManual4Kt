package com.luxvelocitas.tinytcx

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.stream.XMLStreamWriter


//[TODO: import kotlin.js.Date]

const val xsi = "http://www.w3.org/2001/XMLSchema-instance"

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

/*[XXX: htf to do this?]
fun getValueDate(xml: Element?, name: String): Date? {
    return Date(getValueString(xml, name))
}
*/

fun getAttributeString(xml: Element?, name: String): String? {
    return xml?.getAttribute(name)
}

fun getAttributeNsString(xml: Element?, ns: String, name: String): String? {
    return xml?.getAttributeNS(ns, name)
}

fun writeBasicElement(writer: XMLStreamWriter, name: String, value: Any?) {
    if (value != null) {
        writer.writeStartElement(name)
        writer.writeCharacters(value.toString())
        writer.writeEndElement()
    }
}

private fun NodeList.forEach(action: (Node) -> Unit): Unit {
    for(i in 0 until this.length) {
        action(this.item(i))
    }
}


//-----------------------------------------------------------------------------
enum class SportToken {
    Running, Biking, Other;

    companion object {
        fun fromString(s: String?): SportToken? {
            println("KONK0:" + s)
            if (s == null) return null

            return SportToken.valueOf(s)
        }
    }
}

enum class IntensityToken {
    Active, Resting;

    companion object {
        fun fromString(s: String?): IntensityToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}

enum class TriggerMethodToken {
    Manual, Distance, Location, Time, HeartRate;

    companion object {
        fun fromString(s: String?): TriggerMethodToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}

enum class TrainingTypeToken {
    Workout, Course;

    companion object {
        fun fromString(s: String?): TrainingTypeToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}

enum class BuildTypeToken {
    Internal, Alpha, Beta, Release;

    companion object {
        fun fromString(s: String?): BuildTypeToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}
enum class SensorStateToken {
    Present, Absent;

    companion object {
        fun fromString(s: String?): SensorStateToken? {
            if (s == null) return null
            return valueOf(s)
        }
    }
}


data class HeartRateInBeatsPerMinute(
    var Value: Short
)
{
    fun toXml(writer: XMLStreamWriter, tagName: String) {
        writer.writeStartElement(tagName)
        writeBasicElement(writer, "Value", Value)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): HeartRateInBeatsPerMinute? {
            if (xml == null) return null

            return HeartRateInBeatsPerMinute(
                getValueShort(xml, "Value")!!
            )
        }
    }
}

data class Position(
    var LatitudeDegrees: Double, //[TODO: range(-90, 90)]
    var LongitudeDegrees: Double //[TODO: range(-180, 180)]
)
{
    fun toXml(writer: XMLStreamWriter, tagName: String) {
        writer.writeStartElement(tagName)
        writeBasicElement(writer, "LatitudeDegrees", LatitudeDegrees)
        writeBasicElement(writer, "LongitudeDegrees", LongitudeDegrees)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Position? {
            if (xml == null) return null

            return Position(
                getValueDouble(xml, "LatitudeDegrees")!!,
                getValueDouble(xml, "LongitudeDegrees")!!
            )
        }
    }
}

data class Trackpoint(
        var Time: String, //[TODO: Date],
        var Position: Position? = null,
        var AltitudeMeters: Double? = null,
        var DistanceMeters: Double? = null,
        var HeartRateBpm: HeartRateInBeatsPerMinute? = null,
        var Cadence: Short? = null, //[TODO: range(1,255)]
        var SensorState: SensorStateToken? = null,
        var Extensions: MutableList<Any> = mutableListOf<Any>()
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Trackpoint")
        writeBasicElement(writer, "Time", Time)
        Position?.toXml(writer, "Position")
        writeBasicElement(writer, "AltitudeMeters", AltitudeMeters)
        writeBasicElement(writer, "DistanceMeters", DistanceMeters)
        HeartRateBpm?.toXml(writer, "HeartRateBpm")
        writeBasicElement(writer, "Cadence", Cadence)
        writeBasicElement(writer, "SensorState", SensorState)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Trackpoint? {
            if (xml == null) return null

            return Trackpoint(
                getValueString(xml, "Time")!!,
                Position.fromXml(getElement(xml, "Position")),
                getValueDouble(xml, "AltitudeMeters"),
                getValueDouble(xml, "DistanceMeters"),
                HeartRateInBeatsPerMinute.fromXml(getElement(xml, "HeartRateBpm")),
                getValueShort(xml, "Cadence"),
                SensorStateToken.fromString(getValueString(xml, "SensorState"))
            )
        }
    }
}

data class Track(
    var Trackpoints: MutableList<Trackpoint> = mutableListOf<Trackpoint>()
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Track")
        Trackpoints.forEach {
            trackPoint -> trackPoint.toXml(writer)
        }
        writer.writeEndElement()
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

            return ret
        }
    }
}

data class ActivityLap(
        var StartTime: String, //[TODO: Date],
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
        var Extensions: MutableList<Any> = mutableListOf<Any>()
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Lap")
        writer.writeAttribute("StartTime", StartTime)

        writeBasicElement(writer, "TotalTimeSeconds", TotalTimeSeconds)
        writeBasicElement(writer, "DistanceMeters", DistanceMeters)
        writeBasicElement(writer, "MaximumSpeed", MaximumSpeed)
        writeBasicElement(writer, "Calories", Calories)
        AverageHeartRateBpm?.toXml(writer, "AverageHeartRateBpm")
        MaximumHeartRateBpm?.toXml(writer, "MaximumHeartRateBpm")
        writeBasicElement(writer, "Intensity", Intensity)
        writeBasicElement(writer, "Cadence", Cadence)
        writeBasicElement(writer, "TriggerMethod", TriggerMethod)
        Track.toXml(writer)
        writeBasicElement(writer, "Notes", Notes)

        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): ActivityLap? {
            if (xml == null) return null

            return ActivityLap(
                StartTime = getAttributeString(xml, "StartTime")!!,
                TotalTimeSeconds = getValueDouble(xml, "TotalTimeSeconds")!!,
                DistanceMeters = getValueDouble(xml, "DistanceMeters")!!,
                MaximumSpeed = getValueDouble(xml, "MaximumSpeed"),
                Calories = getValueInt(xml, "Calories")!!,
                AverageHeartRateBpm = HeartRateInBeatsPerMinute.fromXml(getElement(xml, "AverageHeartRateBpm")),
                MaximumHeartRateBpm =  HeartRateInBeatsPerMinute.fromXml(getElement(xml, "MaximumHeartRateBpm")),
                Intensity = IntensityToken.fromString(getValueString(xml, "Intensity"))!!,
                Cadence = getValueShort(xml, "Cadence"),
                TriggerMethod = TriggerMethodToken.fromString(getValueString(xml, "TriggerMethod"))!!,
                Track = Track.fromXml(getElement(xml, "Track"))!!,
                Notes = getValueString(xml, "Notes")
            )
        }
    }
}

data class QuickWorkout(
    var TotalTimeSeconds: Double,
    var DistanceMeters: Double
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("QuickWorkout")
        writeBasicElement(writer, "TotalTimeSeconds", TotalTimeSeconds)
        writeBasicElement(writer, "DistanceMeters", DistanceMeters)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): QuickWorkout? {
            if (xml == null) return null

            return QuickWorkout(
                getValueDouble(xml, "TotalTimeSeconds")!!,
                getValueDouble(xml, "DistanceMeters")!!
            )
        }
    }
}

data class Plan(
        var Type: TrainingTypeToken,
        var IntervalWorkout: Boolean,
        var Name: String?, //[TODO: len(1,15)]
        var Extensions: MutableList<Any> = mutableListOf<Any>()
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Build")
        writeBasicElement(writer, "IntervalWorkout", IntervalWorkout)
        writeBasicElement(writer, "Name", Name)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Plan? {
            if (xml == null) return null

            return Plan(
                TrainingTypeToken.fromString(getValueString(xml, "Type"))!!,
                getValueBoolean(xml, "IntervalWorkout")!!,
                getValueString(xml, "String")
            )
        }
    }
}

data class Training(
    var QuickWorkoutResults: QuickWorkout,
    var Plan: Plan,
    var VirtualPartner: Boolean
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Training")
        QuickWorkoutResults.toXml(writer)
        Plan.toXml(writer)
        writeBasicElement(writer, "VirtualPartner", VirtualPartner)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Training? {
            if (xml == null) return null

            return Training(
                QuickWorkout.fromXml(getElement(xml, "QuickWorkoutResults"))!!,
                Plan.fromXml(getElement(xml, "Plan"))!!,
                getValueBoolean(xml, "VirtualPartner")!!
            )
        }
    }
}

data class Version(
    var VersionMajor: Int,
    var VersionMinor: Int,
    var BuildMajor: Int? = null,
    var BuildMinor: Int? = null
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Version")
        writeBasicElement(writer, "VersionMajor", VersionMajor)
        writeBasicElement(writer, "VersionMinor", VersionMinor)
        writeBasicElement(writer, "BuildMajor", BuildMajor)
        writeBasicElement(writer, "BuildMinor", BuildMinor)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Version? {
            if (xml == null) return null

            return Version(
                getValueInt(xml, "VersionMajor")!!,
                getValueInt(xml, "VersionMinor")!!,
                getValueInt(xml, "BuildMajor"),
                getValueInt(xml, "BuildMinor")
            )
        }
    }
}

data class Build(
        var Version: Version,
        var Type: BuildTypeToken? = null,
        var Time: String? = null,
        var Builder: String? = null
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Build")
        Version.toXml(writer)
        writeBasicElement(writer, "Type", Type)
        writeBasicElement(writer, "Time", Time)
        writeBasicElement(writer, "Builder", Builder)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Build? {
            if (xml == null) return null

            return Build(
                Version.fromXml(getElement(xml, "Version"))!!,
                BuildTypeToken.fromString(getValueString(xml, "Type")),
                getValueString(xml, "Time"),
                getValueString(xml, "Builder")
            )
        }
    }
}

sealed class AbstractSource(Name: String) {
    var Name = Name

    abstract fun toXml(writer: XMLStreamWriter, tagName: String)

    companion object {
        fun fromXml(xml: Element?): AbstractSource? {
            if (xml == null) return null

            return when(getAttributeNsString(xml, xsi, "type")) {
                "Application_t" -> Application.fromXml(xml)
                "Device_t" -> Device.fromXml(xml)
                else -> BasicSource.fromXml(xml)
            }

        }
    }
}

class BasicSource(Name: String) : AbstractSource(Name)
{
    override fun toXml(writer: XMLStreamWriter, tagName: String) {
        writer.writeStartElement(tagName)
        writeBasicElement(writer, "Name", Name)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?) : BasicSource? {
            if (xml == null) return null

            return BasicSource(
                getValueString(xml, "Name")!!
            )
        }

    }
}

class Application(
    Name: String,
    var Build: Build,
    var LangID: String, //[TODO: len(2)]
    var PartNumber: String //[TODO: match([\p{Lu}\d]{3}-[\p{Lu}\d]{5}-[\p{Lu}\d]{2}) ]

) : AbstractSource(Name)
{
    override fun toXml(writer: XMLStreamWriter, tagName: String) {
        writer.writeStartElement(tagName)
        writer.writeAttribute(xsi, "type", "Application_t")

        writeBasicElement(writer, "Name", Name)
        Build.toXml(writer)
        writeBasicElement(writer, "LangID", LangID)
        writeBasicElement(writer, "PartNumber", PartNumber)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?) : Application? {
            if (xml == null) return null

            return Application(
                getValueString(xml, "Name")!!,
                Build.fromXml(getElement(xml, "Build"))!!,
                getValueString(xml, "LangID")!!,
                getValueString(xml, "PartNumber")!!
            )
        }

    }
}

class Device(
    Name: String, //[TODO: should delegate to AbstractSource?]
    var UnitId: Long,
    var ProductID: Int,
    var Version: Version

) : AbstractSource(Name)
{
    override fun toXml(writer: XMLStreamWriter, tagName: String) {
        writer.writeStartElement(tagName)
        writer.writeAttribute(xsi, "type", "Device_t")

        writeBasicElement(writer, "Name", Name)
        writeBasicElement(writer, "UnitId", UnitId)
        writeBasicElement(writer, "ProductID", ProductID)
        Version.toXml(writer)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?) : Device? {
            if (xml == null) return null

            return Device(
                getValueString(xml, "Name")!!,
                getValueLong(xml, "UnitId")!!,
                getValueInt(xml, "ProductID")!!,
                Version.fromXml(getElement(xml, "Version"))!!
            )
        }

    }
}

data class Activity(
        var Sport: SportToken,
        var Id: String, //[TODO: Date?, unique]
        var Laps: MutableList<ActivityLap> = mutableListOf<ActivityLap>(), //[TODO: len(>0)]
        var Notes: String? = null,
        var Training: Training? = null,
        var Creator: AbstractSource? = null,
        var Extensions: MutableList<Any>? = mutableListOf<Any>()
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("Activity")
        writer.writeAttribute("Sport", Sport.toString())

        writeBasicElement(writer, "Id", Id)
        Laps.forEach {
            lap -> lap.toXml(writer)
        }
        writeBasicElement(writer, "Notes", Notes)
        Training?.toXml(writer)
        Creator?.toXml(writer, "Creator")
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): Activity? {
            if (xml == null) return null

            val ret = Activity(
                Sport = SportToken.fromString(getAttributeString(xml, "Sport"))!!,
                Id = getValueString(xml, "Id")!!,
                Notes = getAttributeString(xml, "Notes"),
                Training = Training.fromXml(getElement(xml, "Training")),
                Creator = AbstractSource.fromXml(getElement(xml, "Creator"))
            )

            xml
            .getElementsByTagName("Lap")
            ?.forEach {
                node -> ret.Laps.add(ActivityLap.fromXml(node as Element)!!)
            }

            return ret
        }
    }
}

data class FirstSport(
    var Activity: Activity
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("FirstSport")
        Activity.toXml(writer)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): FirstSport? {
            if (xml == null) return null

            return FirstSport(
                Activity.fromXml(getElement(xml, "Activity"))!!
            )
        }
    }
}

data class NextSport(
    var Transition: ActivityLap? = null,
    var Activity: Activity
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("NextSport")
        Transition?.toXml(writer)
        Activity.toXml(writer)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): NextSport? {
            if (xml == null) return null

            return NextSport(
                ActivityLap.fromXml(getElement(xml, "Transition")),
                Activity.fromXml(getElement(xml, "Activity"))!!
            )
        }
    }
}

data class MultiSportSession(
    var Id: String, //[TODO: Date?, unique]
    var FirstSport: FirstSport,
    var NextSports: MutableList<NextSport> = mutableListOf<NextSport>(),
    var Notes: String? = null
)
{
    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartElement("MultiSportSession")
        writer.writeAttribute("Id", Id)

        FirstSport.toXml(writer)
        NextSports.forEach {
            nextSport -> nextSport.toXml(writer)
        }
        writeBasicElement(writer, "Notes", Notes)
        writer.writeEndElement()
    }

    companion object {
        fun fromXml(xml: Element?): MultiSportSession? {
            if (xml == null) return null

            val ret = MultiSportSession(
                Id = getAttributeString(xml, "Id")!!,
                FirstSport = FirstSport.fromXml(getElement(xml, "FirstSport"))!!,
                Notes = getAttributeString(xml, "Notes")
            )

            xml
            .getElementsByTagName("NextSport")
            ?.forEach {
                node -> ret.NextSports.add(NextSport.fromXml(node as Element)!!)
            }

            return ret
        }
    }
}

data class TinyTcx(
    var Activities: MutableList<Activity> = mutableListOf<Activity>(),
    var MultiSportSessions: MutableList<MultiSportSession> = mutableListOf<MultiSportSession>(),
    var Author: AbstractSource? = null,
    var Extensions: MutableList<Any> = mutableListOf<Any>()

    //[TODO] var folders: Folders?,
    //[TODO] var workouts: MutableList<Any>,
    //[TODO] var courses: MutableList<Any>,
) {

    fun toXml(writer: XMLStreamWriter) {
        writer.writeStartDocument("UTF-8", "1.0")

        writer.writeStartElement("TrainingCenterDatabase")
        writer.writeNamespace("xsi", xsi)
        this.Author?.toXml(writer, "Author")
            writer.writeStartElement("Activities")
            Activities.forEach {
                activity -> activity.toXml(writer)
            }
            MultiSportSessions.forEach {
                activity -> activity.toXml(writer)
            }
            writer.writeEndElement()
        writer.writeEndElement()

        writer.writeEndDocument()
    }

    companion object {
        fun fromXml(xml: Element?): TinyTcx? {
            if (xml == null) return null

            val activitiesElement: Element? = getElement(xml, "Activities")
            val activities: MutableList<Activity> = mutableListOf<Activity>()
            val multiSportSessions: MutableList<MultiSportSession> = mutableListOf<MultiSportSession>()

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

            val ret: TinyTcx = TinyTcx(
                Author = AbstractSource.fromXml(getElement(xml, "Author")),
                Activities = activities,
                MultiSportSessions = multiSportSessions
            )

            return ret
        }
    }
}

