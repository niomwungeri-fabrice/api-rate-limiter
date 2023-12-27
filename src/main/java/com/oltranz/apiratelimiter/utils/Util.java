package com.oltranz.apiratelimiter.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static <T> String pojoToJson(T t) {
        String jsonString;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(t);
        } catch (IOException ex) {
            jsonString = ex.getMessage();
        }
        return jsonString;
    }

    public static <T> T jsonToPojo(String jsonString, Class<T> tClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType response = mapper.constructType(tClass);
        Reader reader = new StringReader(jsonString);
        T tResponse = mapper.readValue(reader, response);
        return tResponse;
    }
    public static String generateUsername(String name) throws NoSuchAlgorithmException {
        // Replace spaces with hyphens and convert to lowercase
        String transformedName = name.replace(" ", "-").toLowerCase();

        // Generate a consistent hash of the name
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(transformedName.getBytes());

        // Convert the byte array to a hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        // Use a part of the hash to create a unique number
        String nameHash = sb.toString();
        int uniqueNumber = Math.abs(nameHash.hashCode()) % 10000;  // Get a 4-digit number

        // Combine the transformed name with the unique number
        return String.format("%s-%s", transformedName, uniqueNumber);
    }
}
