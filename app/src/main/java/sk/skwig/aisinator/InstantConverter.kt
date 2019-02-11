package sk.skwig.aisinator

import androidx.room.TypeConverter
import java.time.Instant

object InstantConverter {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @JvmStatic
    @TypeConverter
    fun toTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }
}