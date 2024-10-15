package com.thetestingacademy.Assertions;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

@Test
public class Test_Assertions
{
    RequestSpecification requestSpecification;
    ValidatableResponse validatableResponse;
    Response response;
    String token;
    Integer bookingid;
    String payloadPUT;
    String additionalneeds;

    public void test_post() {
        String payload_POST = "{\n" +
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

        Response response = requestSpecification.when().post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        //    Hamcrest Assertions
//        validatableResponse.body("booking.firstname",Matchers.equalTo("Tony"));
//        validatableResponse.body("booking.lastname",Matchers.equalTo("Stark"));
//        validatableResponse.body("booking.depositpaid",Matchers.equalTo(true));
//        validatableResponse.body("bookingid",Matchers.notNullValue());
//        validatableResponse.body("booking.bookingdates.checkin",Matchers.notNullValue());
//        validatableResponse.body("booking.bookingdates.checkout",Matchers.notNullValue());

        //TestNG Assertions
        bookingid = response.then().extract().path("bookingid");
        String firstname = response.then().extract().path("booking.firstname");
        String lastname = response.then().extract().path("booking.lastname");
        Boolean depositpaid = response.then().extract().path("booking.depositpaid");
        Integer totalprice =response.then().extract().path("booking.totalprice");
        additionalneeds=response.then().extract().path("booking.additionalneeds");
//        Assert.assertNotNull("bookingid");
//        Assert.assertEquals(firstname,"Tony");
//        Assert.assertEquals(lastname,"Stark");
//        Assert.assertTrue(depositpaid,"true");
//        Assert.assertNotNull("depositpaid");

        //AssertJ Assertions
        assertThat("bookingid").isNotBlank().isNotEmpty().isNotNull();
        assertThat(firstname).contains("Tony");
        assertThat(lastname).contains("Stark");
        assertThat(totalprice).isNotNull().isNotNegative().isNotZero();
        assertThat(depositpaid).isTrue();
        assertThat(additionalneeds).isNotEmpty().isNotBlank().isEqualTo("Breakfast");

        //bookingid = response.jsonPath().getString("bookingid");
        //System.out.println(bookingid);
        //return bookingid;
    }
}
