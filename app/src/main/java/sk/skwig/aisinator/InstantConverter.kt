package sk.skwig.aisinator

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime

object InstantConverter {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochSecond(it) }
    }

    @JvmStatic
    @TypeConverter
    fun toTimestamp(instant: Instant?): Long? {
        return instant?.epochSecond
    }
}

object DayOfWeekConverter {
    @JvmStatic
    @TypeConverter
    fun fromColumn(column: Int?): DayOfWeek? {
        return column?.let {
            when (it) {
                0 -> DayOfWeek.SUNDAY
                else -> DayOfWeek.of(it)
            }
        }
    }

    @JvmStatic
    @TypeConverter
    fun toColumn(dayOfWeek: DayOfWeek?): Int? {
        return dayOfWeek?.let {
            when (it) {
                DayOfWeek.SUNDAY -> 0
                else -> it.value
            }
        }
    }
}

object LocalTimeConverter {
    @JvmStatic
    @TypeConverter
    fun fromColumn(column: String?): LocalTime? {
        return column?.let { LocalTime.parse(it) }
    }

    @JvmStatic
    @TypeConverter
    fun toColumn(localTime: LocalTime?): String? {
        return localTime?.let { it.toString() }
    }
}