package repository.utils;

import com.google.gson.Gson;

import java.io.*;

public class FileUtils {

    public static <T> T writeObjectToJsonFile(final String filename, T object) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            Gson gson = new Gson();
            gson.toJson(object, writer);
            return object;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Filename: \"" + filename + "\" was not found.");
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while trying to open new default json file.");
        }
    }
}
