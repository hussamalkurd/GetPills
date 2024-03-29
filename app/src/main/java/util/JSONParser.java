package util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class JSONParser {
    Context activity;

    // constructor
    public JSONParser(Context act) {
        activity = act;
    }

    // public static final String apiLocLogin ="http://fmv.cc/micron/index.php/marketplace/api/index/";
    //http://fmv.cc/micron/index.php/marketplace/api/index/username/test@gmail.com/password/test@123

    public String execPostScriptJSON(String url, ArrayList<NameValuePair> valuePairs)
            throws IOException {
        String responce = null;
        OkHttpClient client = new OkHttpClient();
        //increased timeout for slow response
        OkHttpClient eagerClient = client.newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Log.i("PARAMETERS", "PARAMETERS ::" + valuePairs);

        FormBody.Builder builder = new FormBody.Builder();

        for (NameValuePair valuePair : valuePairs) {
            String val = "";
            //if(!valuePair.value.contains("@") && !valuePair.value.contains("+"))
            //	val = URLEncoder.encode(valuePair.value, "UTF-8");
            //else
            val = valuePair.value;

            builder.add(valuePair.name, val);

        }
//.header("Authorization","passme")
        Request request = new Request.Builder()

                .url(url)
                .addHeader("Authorization", "passme")
                .post(builder.build()).build();
        Log.i("Registration Request::", request.toString());

        Response response = eagerClient.newCall(request).execute();
        Log.i("REGISTRATION RESPONSE::", response.toString());
        responce = response.body().string();

        return responce;
    }

    public String execMultiPartPostScriptJSON(String url, ArrayList<NameValuePair> valuePairs,
                                              String MEDIA_TYPE_PNG, String filepath, String imagename)
            throws IOException {
        String responce = null;

        OkHttpClient client = new OkHttpClient();
        //increased timeout for slow response
        OkHttpClient eagerClient = client.newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Log.i("PARAMETERS", "PARAMETERS ::" + valuePairs);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (filepath != null && !filepath.equalsIgnoreCase("")) {
            File fileupload = new File(filepath);
            if (fileupload != null) {
                builder.addFormDataPart(imagename, "logo-square.png",
                        RequestBody.create(MediaType.parse(MEDIA_TYPE_PNG), fileupload))
                ;

            }
        }

        for (NameValuePair valuePair : valuePairs) {
            String val = "";
            //if(!valuePair.value.contains("@") && !valuePair.value.contains("+"))
            //	val = URLEncoder.encode(valuePair.value, "UTF-8");
            //else
            val = valuePair.value;

            builder.addFormDataPart(valuePair.name, val);

        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", "passme")
                .post(requestBody).build();
        Log.i("Registration Request::", request.toString());

			/*Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
					.url("https://api.imgur.com/3/image")
					.post(requestBody)
					.build();
*/
        Response response = eagerClient.newCall(request).execute();
        Log.i("REGISTRATION RESPONSE::", response.toString());
        responce = response.body().string();


        return responce;
    }

    public String execMultiPartPostScriptJSON(String url, ArrayList<NameValuePair> valuePairs, List<Map<String, String>> map)
            throws IOException {
        String responce = null;

        OkHttpClient client = new OkHttpClient();
        //increased timeout for slow response
        OkHttpClient eagerClient = client.newBuilder()
                .readTimeout(100, TimeUnit.SECONDS)
                .build();
        Log.i("PARAMETERS", "PARAMETERS ::" + valuePairs);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (int i = 0; i < map.size(); i++) {
            File fileupload = new File(map.get(i).get("filepath"));
            if (fileupload != null) {
                builder.addFormDataPart(map.get(i).get("imagename"), "logo-square.png",
                        RequestBody.create(MediaType.parse(map.get(i).get("file_type")), fileupload))
                ;
            }
            Log.e("images", map.get(i).get("filepath") + "," + map.get(i).get("imagename") + "," + map.get(i).get("file_type"));
        }

        for (NameValuePair valuePair : valuePairs) {
            String val = "";
            //if(!valuePair.value.contains("@") && !valuePair.value.contains("+"))
            //	val = URLEncoder.encode(valuePair.value, "UTF-8");
            //else
            val = valuePair.value;

            builder.addFormDataPart(valuePair.name, val);

        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", "passme")
                .post(requestBody).build();
        Log.i("Registration Request::", request.toString());

			/*Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
					.url("https://api.imgur.com/3/image")
					.post(requestBody)
					.build();
*/
        Response response = eagerClient.newCall(request).execute();
        Log.i("REGISTRATION RESPONSE::", response.toString());
        responce = response.body().string();


        return responce;
    }

    public String exeGetRequest(String url, String params) throws IOException {
        String result = "";

        OkHttpClient client = new OkHttpClient();
        //increased timeout for slow response
        OkHttpClient eagerClient = client.newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String my_url = url + "?" + params;
        Request request = new Request.Builder()
                .url(my_url)
                .build();

        Call call = client.newCall(request);
        Response response = eagerClient.newCall(request).execute();
        ;
        result = response.body().string();
        return result;
    }

}
