package inozemtsev.teststproject2.myapplication.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    private static final String VK_API_BASE_URL = "https://api.vk.com";
    private static final String VK_USERS_GET = "/method/users.get";
    private static final String USER_ID_PARAM_NAME = "user_ids";
    private static final String VERSION_PARAM_NAME = "v";
    private static final String VK_API_VERSION = "5.89";
    private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
    private static final String ACCESS_TOKEN = "8e80e5d18e80e5d18e80e5d1e78ef4b12d88e808e80e5d1d100070080910cec94490971";


    public static URL GenerateURL(String userIds){
        Uri builtUri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(USER_ID_PARAM_NAME, userIds)
                .appendQueryParameter(VERSION_PARAM_NAME, VK_API_VERSION)
                .appendQueryParameter(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .build();

        URL url = null;
        try {
             url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String GetResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream istream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(istream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (UnknownHostException exp){
            return null;
        }finally {
            urlConnection.disconnect();
        }
    }
}
