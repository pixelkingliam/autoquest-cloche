package pixel.autoquest.item

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class Rarity {
    Mundane,
    Common,
    Curious,
    Uncommon,
    Rare,
    Epic,
    Exalted,
    Mythic,
    Divine,
    Legendary
}
class RarityTypeAdapter : TypeAdapter<Rarity>() {
    override fun write(out: JsonWriter, value: Rarity?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.ordinal)  // Write the integer value to JSON
        }
    }

    override fun read(reader: JsonReader): Rarity? {
        val intValue = reader.nextInt()
        return Rarity.entries.getOrNull(intValue)
    }
}