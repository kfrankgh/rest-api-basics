package com.restassured.restapi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.restassured.restapi.commonMethods;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static com.restassured.restapi.commonMethods.getCommentId;
import static com.restassured.restapi.commonMethods.getIssueId;
import static com.restassured.restapi.commonMethods.sessionId;

public class basicTestsJira {
    Properties properties = new Properties();
    String issueId = "";
    String commentId = "";

    @BeforeTest
    public void getData() throws IOException {

        FileInputStream fis = new
                FileInputStream("C:\\Users\\KFES\\Documents\\Projects\\DemoRestAssured\\src\\resources\\env.properties");
        properties.load(fis);
    }

    @Test
    public void jiraApiCreateSession() throws IOException {
        Response response = commonMethods.createJiraSession();
        JsonPath jsonPath = commonMethods.rawToJson(response);
        String sessionName = jsonPath.get("session.name");
        sessionId = jsonPath.get("session.value");
        Assert.assertEquals(sessionName, "JSESSIONID");
        Assert.assertTrue(sessionId.matches("\\S{32}"));
    }

    @Test
    public void jiraApiCreateIssue() throws IOException {
        sessionId = commonMethods.getJiraSessionID();
        Assert.assertTrue(sessionId.matches("\\S{32}"));
        Response response = createIssue(sessionId);
        issueId = getIssueId(response);
        Assert.assertTrue(issueId.matches("\\d{5}"));
        System.out.println(String.format("IssueId: %s", issueId));
    }

    public Response createIssue(String sessionId){
        RestAssured.baseURI = properties.getProperty("JIRAHOST");

        Response response = given().header("Content-Type", "application/json").and()
                .header("Cookie", String.format("JSESSIONID=%s",sessionId)).and()
                .body("{\"fields\": {" +
                        "\"project\":" +
                        "{" +
                        "\"key\": \"RES\"" +
                        "}," +
                        "\"summary\": \"8th ISSUE added by Rest API\"," +
                        "\"description\": \"Creating of an issue using project keys and issue type names using the REST API\"," +
                        "\"issuetype\": {" +
                        "\"name\": \"Bug\"" +
                        "}" +
                        "}" +
                        "}")
                .when()
                .post("/rest/api/2/issue").then()
                .extract().response();
        return response;
    }

    @Test
    public void jiraApiAddComment() throws IOException {
        sessionId = commonMethods.getJiraSessionID();
        Assert.assertTrue(sessionId.matches("\\S{32}"));
        Response response = createIssue(sessionId);
        issueId = getIssueId(response);
        System.out.println(String.format("IssueId: %s", issueId));
        response = addComment(sessionId, "jiraApiAddComment: Adding a comment");
        commentId = getCommentId(response);
        Assert.assertTrue(commentId.matches("\\d{5}"));
        System.out.println(String.format("CommentId: %s", commentId));
    }

    public Response addComment(String sessionId, String comment){
        RestAssured.baseURI = properties.getProperty("JIRAHOST");

        Response response = given().header("Content-Type", "application/json").and()
                .header("Cookie", String.format("JSESSIONID=%s",sessionId)).and()
                .body("{" +
                        "   \"body\": \"" + comment + "\"," +
                        "   \"visibility\": {" +
                        "   \"type\": \"role\"," +
                        "   \"value\": \"Administrators\"" +
                        "    }" +
                        "}")
                .when()
                .post(String.format("/rest/api/2/issue/%s/comment", issueId)).then()
                .statusCode(201).extract().response();
        return response;
    }

    @Test
    public void jiraApiUpdateComment() throws IOException {
        sessionId = commonMethods.getJiraSessionID();
        Assert.assertTrue(sessionId.matches("\\S{32}"));
        Response response = createIssue(sessionId);
        issueId = getIssueId(response);
        System.out.println(String.format("IssueId: %s", issueId));
        response = addComment(sessionId, "jiraApiUpdateComment: Adding a comment");
        commentId = getCommentId(response);
        Assert.assertTrue(commentId.matches("\\d{5}"));
        response = updateComment(sessionId, "jiraApiUpdateComment: Replacing a comment");
        String updatedCommentId = getCommentId(response);
        Assert.assertEquals(updatedCommentId, commentId);
        System.out.println(String.format("CommentId: %s", commentId));
    }

    public Response updateComment(String sessionId, String comment){
        RestAssured.baseURI = properties.getProperty("JIRAHOST");

        Response response = given().header("Content-Type", "application/json").and()
                .header("Cookie", String.format("JSESSIONID=%s",sessionId)).and()
                .pathParams("commentId",commentId)
                .body("{" +
                        "   \"body\": \"" + comment + "\"," +
                        "   \"visibility\": {" +
                        "   \"type\": \"role\"," +
                        "   \"value\": \"Administrators\"" +
                        "    }" +
                        "}")
                .when()
                .put(String.format(String.format("/rest/api/2/issue/%s/comment/{commentId}"), issueId)).then()
                .statusCode(200).extract().response();
        return response;
    }




}