package Project.Persistency.v1;

import Project.Persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SerializerV1 {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");

    static void writeString(ByteArrayOutputStream bytes, String s) {
        byte[] textByte = s.getBytes(StandardCharsets.UTF_8);
        Serializer.writeInt(bytes, textByte.length);
        bytes.writeBytes(textByte);
    }

    static String readString(ByteBuffer bytes) {
        int length = Serializer.readInt(bytes);
        byte[] text = new byte[length];
        bytes.get(text, 0, length);
        return new String(text);
    }

    static void writeLocalDate(ByteArrayOutputStream bytes, LocalDate d){
        writeString(bytes, d.format(dateFormat));
    }

    static LocalDate readLocalDate(ByteBuffer bytes) {
        String dateString = readString(bytes);
        return LocalDate.parse(dateString, dateFormat);
    }
}
