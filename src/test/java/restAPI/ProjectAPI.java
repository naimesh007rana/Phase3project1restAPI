package restAPI;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class ProjectAPI {
	
	public static class GlobalVariable{
		public static int Emp_ID=1;	
		public static String dltid="";
			
	}
	
	
	@Test
	public void A_GetAllEmployee() {
		
		RestAssured.baseURI="https://dummy.restapiexample.com/api/v1/employees";
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		String responseBody = response.body().asString();
		System.out.println(responseBody);
		System.out.println("Response code is:"+response.statusCode());
		AssertJUnit.assertEquals(response.statusCode(), 200);	
		
	}
	
	@Test
	public void B_GetSingleEmployee() {
		
		RestAssured.baseURI="https://dummy.restapiexample.com/api/v1/employee/1";
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		String responseBody = response.body().asString();
		System.out.println(responseBody);
		System.out.println("Response code is:"+response.statusCode());
		AssertJUnit.assertEquals(response.statusCode(), 200);
				
		JsonPath json = response.jsonPath();
		String actname = json.get("data.employee_name");
		String expname = "Tiger Nixon";
		AssertJUnit.assertEquals(actname, expname);
		
	}
	
	@Test
	public void C_CreateEmployee() throws IOException {
		
		HashMap<String,String> obj = new HashMap<String,String>();
		obj.put("name", "NJR");
		obj.put("salary", "60000");
		obj.put("age", "30");
		
		RestAssured.baseURI="https://dummy.restapiexample.com/api/v1/create";
		RequestSpecification request = RestAssured.given();
		Response response = (Response) request.contentType(ContentType.JSON)
				                               .accept(ContentType.JSON)
				                               .body(obj)
				                               .post();
		
		System.out.println("respose code is:" + response.getStatusCode());
		System.out.println("response body is:"+response.body().asString());
		AssertJUnit.assertEquals(response.statusCode(), 200);
		
		JsonPath json = response.jsonPath();
		String actstatus = json.get("status");
		String expstatus = "success";
		AssertJUnit.assertEquals(actstatus, expstatus);
		Integer empy_id = json.get("data.id");
		System.out.println("empy id is:"+empy_id);
		GlobalVariable.Emp_ID=empy_id;
		System.out.println("emp id is:"+ GlobalVariable.Emp_ID);
		String ID = ""+GlobalVariable.Emp_ID;
		FileWriter fw = new FileWriter("delete.txt");
		fw.flush();
		fw.write(ID);
		fw.close();

	}
	
	@Test
	public void D_DeleteEmployee() throws IOException {
		
		Path p1 = Paths.get("delete.txt");
		GlobalVariable.dltid=Files.readString(p1);
		System.out.println("Delete Id:"+GlobalVariable.dltid);
		
		String URL = "https://dummy.restapiexample.com/api/v1/delete/";
		
		RestAssured.baseURI= URL+GlobalVariable.dltid;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete();
		String responseBody = response.body().asString();
		System.out.println(responseBody);
		System.out.println("Response code is:"+response.statusCode());
		AssertJUnit.assertEquals(response.statusCode(), 200);
		JsonPath json = response.jsonPath();
		String actmsg = json.get("message");
		String expmsg = "Successfully! Record has been deleted";
		AssertJUnit.assertEquals(actmsg, expmsg);
	
		
	}
	
	@Test
	public void E_TestDeletedEmployee() throws IOException {
		
		Path p1 = Paths.get("delete.txt");
		GlobalVariable.dltid=Files.readString(p1);
		System.out.println("Delete Id:"+GlobalVariable.dltid);
		
		String URL = "https://dummy.restapiexample.com/api/v1/employee/";
		
		RestAssured.baseURI= URL+GlobalVariable.dltid;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		String responseBody = response.body().asString();
		System.out.println(responseBody);
		System.out.println("Response code is:"+response.statusCode());
		AssertJUnit.assertEquals(response.statusCode(), 404);
				
				
	}
	
	



}
