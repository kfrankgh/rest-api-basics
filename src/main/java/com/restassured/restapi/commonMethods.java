package com.restassured.restapi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class commonMethods {
    public static JsonPath rawToJson(Response response) {
        return new JsonPath(response.asString());

    }

    public static XmlPath rawToXml(Response response) {
        return new XmlPath(response.asString());
    }

    static Properties properties = new Properties();

    public static Response createJiraSession() throws IOException {
        getData();
        //BaseURL or Host
        RestAssured.baseURI = properties.getProperty("JIRAHOST");

        Response response = given().header("Content-Type", "application/json").and()
                .body("{ \"username\": \"kfrankgm\", \"password\": \"pA$uwad)\" }")
                .when()
                .post("/rest/auth/1/session").then()
                .statusCode(200).extract().response();
        return response;
    }

    public static void getData() throws IOException {

        FileInputStream fis = new
                FileInputStream("C:\\Users\\KFES\\Documents\\Projects\\DemoRestAssured\\src\\resources\\env.properties");
        properties.load(fis);
    }

    public static String sessionId = "";

    public static String getJiraSessionID() throws IOException{
        if (sessionId == "") {
            Response response = createJiraSession();
            JsonPath jsonPath = rawToJson(response);
            sessionId = jsonPath.getString("session.value");
        }
        return sessionId;
    }

    public static String getIssueId(Response response){
        JsonPath jsonPath = commonMethods.rawToJson(response);
        return jsonPath.get("id");
    }

    public static String getCommentId(Response response) {
        JsonPath jsonPath = commonMethods.rawToJson(response);
        return jsonPath.get("id");
    }
}