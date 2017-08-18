package com.restassured.restapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.restassured.restapi.payLoad;
import com.restassured.restapi.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static com.restassured.restapi.commonMethods.rawToXml;

public class basicTestsGooglePlaces1 {
    Properties properties = new Properties();
    @BeforeTest
    public void getData() throws IOException{
        FileInputStream fis = new
                FileInputStream("C:\\Users\\KFES\\Documents\\Projects\\DemoRestAssured\\src\\resources\\env.properties");
        properties.load(fis);
    }

    @Test
    public void getGooglePlace() {
        //BaseURL or Host
        RestAssured.baseURI = "https://maps.googleapis.com";

        String key = properties.getProperty("KEY");
        String resource = resources.getGetData();

        given().
                param("location","-33.8670522,151.1957362").
                param("radius","50").
                param("key",key).
                when().
                get(resource).
                then().assertThat().statusCode(200).and().contentType("application/json").and().
                //body("results[0].geometry.location.lat", equalTo("-33.8672281"));
                        body("results[0].name",equalTo("51-55 Pirrama Rd")).and().
                body("results[0].place_id",equalTo("ChIJ-aI8TTauEmsRc3TSDkILszo")).and().
                header("Server","pablo");
        //can assert on: Header response, Body, Content type, Status code of response
    }

    @Test
    public void postGooglePlace() {
        //BaseURL or Host
        RestAssured.baseURI = properties.getProperty("HOST");
        String key = properties.getProperty("KEY");
        String body = payLoad.getPostBody();
        String resource = resources.getPostData("json");

        given().
                queryParam("key", key).
                body(body).
                when().
                post(resource).
                then().assertThat().statusCode(200).and().contentType("application/json").and().
                body("scope",equalTo("APP")).and().
                body("status",equalTo("OK")).and().
                header("Server","pablo");
    }

    @Test
    public void postAndDeleteGooglePlace() {
        String requestBody = payLoad.getPostBody();
        RestAssured.baseURI = properties.getProperty("HOST");
        String key = properties.getProperty("KEY");

        //Post a Place and extract response
                Response response = given().

                queryParam("key", key).
                body(requestBody).
                when().
                post(resources.getPostData("json")).
                then().assertThat().statusCode(200).and().contentType("application/json").and().
                body("scope",equalTo("APP")).and().
                body("status",equalTo("OK")).and().
                header("Server","pablo").
                extract().response();

                String stringResponse = response.asString();

        //Convert the raw response to json
        JsonPath js = new JsonPath(stringResponse);
        String placeId = js.get("place_id");
        System.out.println(placeId);

        //Delete the place
        String deleteBody = payLoad.getDeleteBody(placeId);
        given().
                queryParam("key", key).
               body(deleteBody).
                when().
                    post(resources.getDeleteData()).
                then().assertThat().statusCode(200).and().contentType("application/json").and().
                body("status",equalTo("OK")).and().
                header("Server","pablo");
    }

    @Test
        public void postGooglePlaceXML() throws IOException{
        String requestBody = generateStringFromResource
                ("C:\\Users\\KFES\\Documents\\Ken\\Study\\RestApiTestAutomation\\postData.xml");
        RestAssured.baseURI = properties.getProperty("HOST");
        String key = properties.getProperty("KEY");

        //Post a Place and extract response
        Response response = given().

                queryParam("key", key).
                body(requestBody).
                when().
                post(resources.getPostData("xml")).
                then().assertThat().statusCode(200).and().contentType(ContentType.XML).
                extract().response();

        //String stringResponse = response.asString();
            System.out.println(response.asString());
        //Convert the raw response to xml
        XmlPath xml = rawToXml(response);
        String placeId = xml.get("PlaceAddResponse.place_id");
        System.out.println(String.format("place_id: %s",placeId));
        }

    String generateStringFromResource(String resourcePath) throws IOException{
        return new String(Files.readAllBytes(Paths.get(resourcePath)));
    }
}
