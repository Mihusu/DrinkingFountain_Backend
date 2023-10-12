package aau.project.drinkingfountainbackend.util;

import java.util.Base64;

public class Base64Utility {
    /**
     * Convert a byte array to a Base64-encoded string.
     *
     * @param bytes The byte array to be encoded.
     * @return The Base64-encoded string.
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Convert a Base64-encoded string back to a byte array.
     *
     * @param base64String The Base64-encoded string.
     * @return The decoded byte array.
     */
    public static byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
}

