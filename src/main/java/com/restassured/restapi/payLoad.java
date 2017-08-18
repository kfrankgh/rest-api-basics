package com.restassured.restapi;

public class payLoad {
    public static String getPostBody(){
        String body = "{\n" +
                "\"location\": {\n" +
                "\"lat\": -33.8669710,\n" +
                "\"lng\": 151.1958750\n" +
                " },\n" +
                " \"accuracy\": 50,\n" +
                " \"name\": \"Google Shoes!\",\n" +
                " \"phone_number\": \"(02) 9374 4000\",\n" +
                " \"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\",\n" +
                " \"types\": [\"shoe_store\"],\n" +
                " \"website\": \"http://www.google.com.au/\",\n" +
                " \"language\": \"en-AU\"\n" +
                "}\n";
        return body;
    }

    public static String getDeleteBody(String placeId){
        String body = "{" +
                "\"place_id\": \"" + placeId + "\"}";
        return body;
    }
}
