package com.restassured.restapi;

public class resources {
    public static String getPostData(String JsonOrXml){
        String resource = String.format("/maps/api/place/add/%s",JsonOrXml);
        return resource;
    }

    public static String getDeleteData(){
        String resource = "/maps/api/place/delete/json";
        return resource;
    }

    public static String getGetData(){
        String resource = "/maps/api/place/nearbysearch/json";
        return resource;
    }


}
