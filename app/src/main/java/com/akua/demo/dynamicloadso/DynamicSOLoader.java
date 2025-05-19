package com.akua.demo.dynamicloadso;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DynamicSOLoader {

    private static final String TAG = "DynamicSOLoader";
    private static final String LIB_NAME = "libdynamicloadso.so";

    public static void loadLibraryFromInternalStorage(Activity activity) throws Exception {
        Context context = activity.getApplicationContext();

        try {
            StringBuilder lib_internal_path = new StringBuilder();
            StringBuilder lib_external_path = new StringBuilder();
            lib_internal_path.append(context.getFilesDir().getAbsolutePath())
                    .append(File.separator)
                    .append("custom")
                    .append(File.separator)
                    .append("arm64-v8a")
                    .append(File.separator)
                    .append(LIB_NAME);
            lib_external_path.append(context.getExternalFilesDir(null).getAbsolutePath())
                    .append(File.separator)
                    .append("custom")
                    .append(File.separator)
                    .append("arm64-v8a")
                    .append(File.separator)
                    .append(LIB_NAME);  // Add arm64-v7a if needed

            File lib_file = new File(lib_external_path.toString());
            if (lib_file.exists()) {
                copyFileToInternalStorage(lib_external_path.toString(), lib_internal_path.toString());
            }

            File internal_lib_file = new File(lib_internal_path.toString());
            if (!internal_lib_file.exists()) {
                System.loadLibrary("dynamicloadso");
                Log.d(TAG, "loadLibraryFromInternalStorage: Loaded from APK");
                return;
            }

            System.load(internal_lib_file.getAbsolutePath());
            Log.d(TAG, "loadLibraryFromInternalStorage: Loaded from external storage");

        } catch (Exception e) {
            throw new Exception("Failed to load library", e);
        }
    }

    public static File copyFileToInternalStorage(String externalFilePath, String internalFilePath) throws IOException {
        File externalFile = new File(externalFilePath); // Source file
        if (!externalFile.exists()) {
            throw new IOException("Source file does not exist: " + externalFilePath);
        }

        // Create a File object for the destination file
        File internalFile = new File(internalFilePath);

        // Ensure the parent directory exists
        File parentDir = internalFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        // Copy the file
        try (FileInputStream inputStream = new FileInputStream(externalFile);
             FileOutputStream outputStream = new FileOutputStream(internalFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error on copying file: " + e.getMessage(), e);
            throw e;
        }

        return internalFile;
    }

    public static native String stringFromJNI();
}
