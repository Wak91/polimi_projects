package app.androbenchmark.util;

import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by Matteo Ferroni on 21/05/14.
 */
public final class HTTPUtil {

    private static final String TAG = "HTTPUtil";

    // Constant values to be used in file sending
    private static final String END_LINE = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY =  "*****";
    private static final String FILE_PARAMETER_NAME = "uploaded_file";
    private static final int MAX_BUFFER_SIZE = 1 * 1024 * 1024;

    public static enum RequestMethod {
        GET, POST
    }


    /**
     * Execute an HTTP request (GET or POST)
     *
     * @param urlString
     * @param parameters
     * @param requestMethod
     * @return
     * @throws java.io.IOException
     */
    public static String sendRequestOverHTTP(String urlString, List<NameValuePair> parameters,
                                                RequestMethod requestMethod)
                                            throws IOException {
        // Init streams
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            // Encode parameters (if any) and add them to GET URLs
            String encodedParameters = null;
            if(parameters != null){
                encodedParameters = encodeParameters(parameters);
                if(requestMethod == RequestMethod.GET)
                    urlString += "?" + encodedParameters;
            }

            // Setup connection
            URL url = new URL(urlString);
            if (url == null)
                throw new IOException("Invalid URL string");

            connection = (HttpURLConnection) url.openConnection();
            if (connection == null)
                throw new IOException("Cannot open a valid connection");

            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setDoInput(true);

            // Set request method
            connection.setRequestMethod(requestMethod.toString());

            // If any encoded parameter found and it's a POST request, embed them in the request
            if(requestMethod == RequestMethod.POST && encodedParameters != null){
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(encodedParameters.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(encodedParameters);
                out.close();
            }

            // Execute request
            connection.connect();

            if(connection.getResponseCode() == 200){
                // Convert the InputStream into a string
                inputStream = connection.getInputStream();
                return streamToString(inputStream);
            }else
                throw new IOException(String.format("Server returned an error code: %d",
                                                                    connection.getResponseCode()));
        } finally {
            // Close streams
            if (inputStream != null)
                inputStream.close();

            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * Execute an HTTPS request (GET or POST)
     *
     * @param urlString
     * @param parameters
     * @param requestMethod
     * @return
     * @throws java.io.IOException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     */
    public static String sendRequestOverHTTPS(String urlString, List<NameValuePair> parameters,
                                                RequestMethod requestMethod)
                                            throws IOException, NoSuchAlgorithmException,
                                                    KeyManagementException {
        // Init streams
        InputStream inputStream = null;
        HttpsURLConnection connection = null;

        try {
            // Encode parameters (if any) and add them to GET URLs
            String encodedParameters = null;
            if (parameters != null) {
                encodedParameters = encodeParameters(parameters);
                if (requestMethod == RequestMethod.GET)
                    urlString += "?" + encodedParameters;
            }

            // Setup connection
            URL url = new URL(urlString);
            if (url == null)
                throw new IOException("Invalid URL string");

            connection = (HttpsURLConnection) url.openConnection();
            if (connection == null)
                throw new IOException("Cannot open a valid connection");

            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setDoInput(true);

            // Set request method
            connection.setRequestMethod(requestMethod.toString());

            // If any encoded parameter found and it's a POST request, embed them in the request
            if (requestMethod == RequestMethod.POST && encodedParameters != null) {
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(encodedParameters.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(encodedParameters);
                out.close();
            }

            // Setup SSL
            TrustManager tm[] = {new PubKeyManager()};
            if (tm == null)
                throw new IOException("Cannot create a PubKeyManager");

            SSLContext context = SSLContext.getInstance("TLS");
            if (context == null)
                throw new IOException("Cannot create an SSLContext");
            context.init(null, tm, null);

            // Execute request
            connection.setSSLSocketFactory(context.getSocketFactory());
            connection.connect();

            if (connection.getResponseCode() == 200) {
                // Convert the InputStream into a string
                inputStream = connection.getInputStream();
                return streamToString(inputStream);
            } else
                throw new IOException(String.format("Server returned an error code: %d",
                                                                    connection.getResponseCode()));
        } finally {
            // Close streams
            if (inputStream != null)
                inputStream.close();

            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * Send a file over HTTP
     *
     * @param urlString
     * @param parameters
     * @return
     * @throws java.io.IOException
     */
    public static String sendFileOverHTTP(String urlString, List<NameValuePair> parameters,
                                          String fileName, InputStream fileStream)
                                        throws IOException {
        if(fileStream == null || fileName == null)
            throw new IOException("Please provide valid file information");

        // Init streams
        InputStream inputStream = null;
        DataOutputStream outputStream = null;
        HttpURLConnection connection = null;

        try {
            // Setup connection
            URL url = new URL(urlString);
            if (url == null)
                throw new IOException("Invalid URL string");

            connection = (HttpURLConnection) url.openConnection();
            if (connection == null)
                throw new IOException("Cannot open a valid connection");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestMethod(RequestMethod.POST.toString());
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            connection.setRequestProperty(FILE_PARAMETER_NAME, fileName);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + END_LINE);
            outputStream.writeBytes("Content-Disposition: form-data; " +
                                    "name=\"" + FILE_PARAMETER_NAME + "\";" +
                                    "filename=\"" + fileName + "\"" + END_LINE);
            outputStream.writeBytes(END_LINE);

            int bytesAvailable = fileStream.available();
            Log.e(TAG, String.format("bytesAvailable: %s", bytesAvailable));
            int bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileStream.read(buffer, 0, bufferSize);

            // Read file's bytes and write them on the request body
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileStream.available();
                bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
                bytesRead = fileStream.read(buffer, 0, bufferSize);
            }

            // Send multipart form data necessary after file data
            // If any encoded parameter found, embed them in the request body
            if(parameters != null){
                for (NameValuePair parameter : parameters) {
                    outputStream.writeBytes(END_LINE);
                    outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + END_LINE);

                    outputStream.writeBytes("Content-Disposition: form-data; " +
                                            "name=\"" + parameter.getName() + "\"" + END_LINE);
                    outputStream.writeBytes(END_LINE);
                    outputStream.writeBytes(parameter.getValue());
                }
            }

            // Closing statements
            outputStream.writeBytes(END_LINE);
            outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + END_LINE);

            outputStream.flush();
            outputStream.close();
            fileStream.close();

            // Execute request
            connection.connect();

            if(connection.getResponseCode() == 200){
                // Convert the InputStream into a string
                inputStream = connection.getInputStream();
                return streamToString(inputStream);
            }else
                throw new IOException(String.format("Server returned an error code: %d",
                                                                    connection.getResponseCode()));
        } finally {
            // Close streams
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null)
                fileStream.close();
            if (fileStream != null)
                fileStream.close();
            if (connection != null)
                connection.disconnect();
        }
    }


    /**
     * Convert a generic InputStream in a String (UTF-8)
     */
    private static String streamToString(InputStream inputStream) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder responseContent = new StringBuilder();
        String line;
        while((line = buffer.readLine()) != null)
            responseContent.append(line);
        return responseContent.toString();
    }

    /**
     * Encode parameters
     * @param parameters
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    private static String encodeParameters(List<NameValuePair> parameters)
                                        throws UnsupportedEncodingException {
        String encodedParameters = "";

        // Add parameters if any
        if (parameters != null) {
            int i = 0;
            for (NameValuePair parameter : parameters) {
                if(i == 0)
                    encodedParameters = String.format("%s=%s",
                                                    URLEncoder.encode(parameter.getName(), "UTF-8"),
                                                    URLEncoder.encode(parameter.getValue(), "UTF-8"));
                else
                    encodedParameters += String.format("&%s=%s",
                                                    URLEncoder.encode(parameter.getName(), "UTF-8"),
                                                    URLEncoder.encode(parameter.getValue(), "UTF-8"));
                i++;
            }
        }
        return encodedParameters;
    }
}
