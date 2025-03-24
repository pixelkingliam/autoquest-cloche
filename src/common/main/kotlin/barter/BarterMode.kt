package pixel.autoquest

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class BarterMode {
    TagOnly,
    PoolOnly,
    TagAndPool,
}
class BarterModeTypeAdapter : TypeAdapter<BarterMode>() {
    override fun write(out: JsonWriter, value: BarterMode?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.ordinal)  // Write the integer value to JSON
        }
    }

    override fun read(reader: JsonReader): BarterMode? {
        val intValue = reader.nextInt()
        return BarterMode.entries.getOrNull(intValue)
    }
}