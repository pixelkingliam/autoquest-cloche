package pixel.autoquest

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object BSerializer {
    fun serialize(obj: Any): ByteArray {
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(obj)
        return bos.toByteArray()
    }
    inline fun <reified T> deserialize(data: ByteArray): T? {
        val ois = ObjectInputStream(ByteArrayInputStream(data))
        val obj = ois.readObject()
        return obj as? T

    }
}