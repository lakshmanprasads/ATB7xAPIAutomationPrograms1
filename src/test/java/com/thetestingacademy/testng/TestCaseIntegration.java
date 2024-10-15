package com.thetestingacademy.testng;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class TestCaseIntegration {
    //  Create a Token
    // Create a Booking
    //  Perform  a PUT request
    //  Verify that PUT is success by GET Request
    // Delete the ID
    // Verify it doesn't exist GET Request

    RequestSpecification requestSpecification;
    ValidatableResponse validatableResponse;
    Response response;
    String token;
    String bookingid;
    String payloadPUT;

    public String getToken()
    {
        String payload = "{\"username\": \"admin\",\n" +
                "                        \"password\" : \"password123\"}";
        RequestSpecification r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/auth");
        r.contentType(ContentType.JSON).log().all();
        r.body(payload);

        Response response = r.when().post();

        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(200);
        //Extract the token
        token = response.jsonPath().getString("token");
        System.out.println(token);
        return token;
    }

    public String getBookingID()
    {
        String payload_POST="{\n" +
                "    \"firstname\" : \"Tony\",\n" +
                "    \"lastname\" : \"Stark\",\n" +
                "    \"totalprice\" : \"1000\",\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2024-11-29\",\n" +
                "        \"checkout\" : \"2024-11-30\"\n" +
                "    },\n" +
                "    \"additionalneeds\":\"Breakfast\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking");
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.body(payload_POST).log().all();

        Response response=requestSpecification.when().post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        bookingid = response.jsonPath().getString("bookingid");
        System.out.println(bookingid);
        return bookingid;
    }

    @Test //(priority = 1)
    public void test_update_request_put()
    {
        token=getToken();
        bookingid=getBookingID();

                payloadPUT="{\n" +
                "    \"firstname\" : \"James\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingid);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("token",token);
        requestSpecification.body(payloadPUT);

        Response response = requestSpecification.when().put();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);
    }

    @Test  //(priority=0)
    public void test_update_request_get()
    {
        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingid);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("token",token);
        requestSpecification.cookie("bookingid",bookingid);

        Response response = requestSpecification.when().get();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);
        //System.out.println(bookingid);
    }

    @Test   //(priority=2)
    public void test_delete_booking()
    {
        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingid);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("token",token);
        requestSpecification.cookie("bookingid",bookingid);

        Response response = requestSpecification.when().delete();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(201);
    }

    @Test   //(priority=3)
    public void test_after_delete_get()
    {
        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingid);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("token",token);
        requestSpecification.cookie("bookingid",bookingid);

        Response response = requestSpecification.when().get();

        validatableResponse = response.then().log().all();
        System.out.println(bookingid);
        validatableResponse.statusCode(404);
    }

}
