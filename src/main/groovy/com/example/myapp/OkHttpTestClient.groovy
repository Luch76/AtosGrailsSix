package com.example.myapp

import groovy.json.JsonBuilder
import okhttp3.*
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class OkHttpTestClient {

    String baseUrl;
    OkHttpClient client;

    def initJsonToken(String aBaseUrl, String aUsername, String aPassword) {
        RequestBody body;
        Response response;
        Request request;
        CookieJar cookieJar;
        Integer responseCode;
        String username;
        String password;
        String responseBody;
        def responseJson;

        baseUrl = aBaseUrl;
        username = aUsername;
        password = aPassword;

        println("OkHttpTestClient, init, username: " + username);

        cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        println("OkHttpTestClient, init, baseUrl: " + baseUrl);
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        body = RequestBody.create(mediaType, "{\"username\": \"${username}\", \"password\": \"${password}\"}");
        request = new okhttp3.Request.Builder()
                .url(baseUrl + "/api/login")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        responseCode = response.code();
        responseBody = response.body()?.string();
        try {
            responseJson = new JSONArray(responseBody);
        } catch (Exception e) {
            responseJson = new JSONObject(responseBody);
        }
        response.close();
        println("OkHttpTestClient, init, login-auth: responseCode: " + responseCode);
        return [responseCode: responseCode, responseJson: responseJson];


        /*
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\"username\": \"ADM\", \"password\": \"Airgroup1\"}");
Request request = new Request.Builder()
  .url("http://localhost:8080/api/login")
  .method("POST", body)
  .addHeader("Accept", "application/json")
  .addHeader("Content-Type", "application/json")
  .addHeader("Cookie", "JSESSIONID=4E2B2D2E7F22FB5212641F239A8287A7")
  .build();
Response response = client.newCall(request).execute();
         */

    }

    def initSession(String aBaseUrl, String aUsername, String aPassword) {
        RequestBody body;
        Response response;
        Request request;
        CookieJar cookieJar;
        Integer responseCode;
        String username;
        String password;
        String responseBody;
        def responseJson;

        baseUrl = aBaseUrl;
        username = aUsername;
        password = aPassword;

        println("OkHttpTestClient, init, username: " + username);

        cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        println("OkHttpTestClient, init, baseUrl: " + baseUrl);
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .build();
        body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();
        request = new okhttp3.Request.Builder()
                .url(baseUrl + "/login/authenticate")
                .method("POST", body)
                .build();
        response = client.newCall(request).execute();
        responseCode = response.code();
        responseBody = response.body()?.string();

        response.close();
        println("OkHttpTestClient, init, login-auth: responseCode: " + responseCode);
        return [responseCode: responseCode, responseJson: responseJson];
    }

    def get(String url) {
        Response response;
        okhttp3.Request request;
        def responseJson;
        Integer responseCode;
        String responseBody;

        request = new okhttp3.Request.Builder()
                .url(baseUrl + url)
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        responseCode = response.code();
        responseBody = response.body()?.string();
        try {
            responseJson = new JSONArray(responseBody);
        } catch (Exception e) {
            responseJson = new JSONObject(responseBody);
        }
        response.close();
        return [responseCode: responseCode, responseJson: responseJson];
    }

    def deleteId(String url) {
        RequestBody body;
        okhttp3.Request request;
        Response response;
        Integer responseCode;
        MediaType mediaType;

        mediaType = MediaType.parse("text/plain");
        body = RequestBody.create(mediaType, "");
        request = new okhttp3.Request.Builder()
                .url(baseUrl + url)
                .method("DELETE", body)
                .build();
        response = client.newCall(request).execute();
        responseCode = response.code();
        response.close();
        return [responseCode: responseCode];
    }

    def postJson(String url, def bodyMap) {
        return this.postHelper(url, bodyMap, "POST");
    }

    def putJson(String url, def bodyMap) {
        return this.postHelper(url, bodyMap, "PUT");
    }

    private postHelper(String url, def bodyMap, String method) {
        String jsonRequestString;
        RequestBody body;
        okhttp3.Request request;
        Response response;
        def responseJson;
        Integer responseCode;
        String responseBody;

        jsonRequestString = new JsonBuilder(bodyMap).toString()
        body = RequestBody.create(MediaType.parse("application/json"), jsonRequestString);
        request = new okhttp3.Request.Builder()
                .url(baseUrl + url)
                .method(method, body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
        response = client.newCall(request).execute();
        responseCode = response.code();
        responseBody = response.body()?.string();
        try {
            responseJson = new JSONObject(responseBody);
        } catch (Exception e) {
            responseJson = new JSONArray(responseBody);
        }
        response.close();
        return [responseCode: responseCode, responseJson: responseJson];
    }

    String americanDateAsStringToIsoDateAsString(String dateAmerican) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dateIso = null;

        if (dateAmerican != null) {
            try {
                dateIso = LocalDate.parse(dateAmerican, formatter).toString();
                dateIso = dateIso + "T00:00:00.000Z";
            } catch (Exception e) {
            }
        }
        return dateIso;
    }

}
