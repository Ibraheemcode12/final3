package com.example.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;


// NOTE: All services must be up and running except the gateway and serviceregis


@SpringBootTest
class TestingApplicationTests {

RestTemplate restTemplate = new RestTemplate();

ObjectMapper mapper = new ObjectMapper();


final String UserServiceUrl = "http://localhost:8080/user/";
final String PostServiceUrl = "http://localhost:8082/posts/";
final String Like_com_ServiceUrl = "http://localhost:8085/com_like/";



@BeforeAll
static void deleteuser() throws InterruptedException{
RestTemplate restTemplate2 = new RestTemplate();

	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.set("Username", "Ahmed4");
	HttpEntity<String> request = 
	new HttpEntity<String>(null, headers);
restTemplate2.exchange("http://localhost:8080/user/username", HttpMethod.DELETE,request,String.class);
Thread.sleep(1000);
}




@Test
void add_user () throws Exception{
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
   JSONObject json = new JSONObject();
	  json.put("username","Ahmed4");
	  json.put("password","123");
	  HttpEntity<String> request = 
  new HttpEntity<String>(json.toString(), headers);
String s = restTemplate.postForObject(UserServiceUrl+"sign-up", request, String.class);
String s2 = mapper.readTree(s).path("username").asText();

assertEquals(s2, "Ahmed4");
}  


@Test
void login () throws Exception{
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
   JSONObject json = new JSONObject();
	  json.put("username","Ahmed4");
	  json.put("password","123");
	  HttpEntity<String> request = 
  new HttpEntity<String>(json.toString(), headers);
String s = restTemplate.postForObject(UserServiceUrl+"login", request, String.class);
String s2 = mapper.readTree(s).path("user").path("username").asText();

assertEquals(s2, "Ahmed4");
}  




@Test

void add_post () throws Exception{
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.set("Username", "Ahmed4");
   JSONObject json = new JSONObject();
	  HttpEntity<String> request = 
  new HttpEntity<String>(json.toString(), headers);
String s = restTemplate.postForObject(PostServiceUrl+"post-for-user?content=www23", request, String.class);
String s2 = mapper.readTree(s).asText();

assertEquals(s2, "true");
}  



@Test
    void get_posts() throws JsonMappingException, JsonProcessingException{

		ResponseEntity<String> res =  restTemplate.getForEntity(PostServiceUrl+"/All-posts/Ahmed4",String.class);
        String content = mapper.readTree(res.getBody()).get(0).path("content").asText();
		assertEquals("www23", content);

	}




@Test
    void addcoment() throws JsonMappingException, JsonProcessingException, InterruptedException{

		ResponseEntity<String> res =  restTemplate.getForEntity(PostServiceUrl+"/All-posts/Ahmed4",String.class);
		String id =  mapper.readTree(res.getBody()).get(0).path("id").asText();
	
	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Username", "Ahmed4");
	   JSONObject json = new JSONObject();
	   json.put("postid",id);
	   json.put("content","AMERICA YA :D");
		  HttpEntity<String> request = new HttpEntity<String>(json.toString(), headers);
	String s = restTemplate.postForObject(Like_com_ServiceUrl+"comment", request, String.class);
	String s2 = mapper.readTree(s).asText();
	assertEquals(s2, "true");
	}



	@Test
	void get_user() throws JsonMappingException, JsonProcessingException {
 ResponseEntity<String> res =  restTemplate.getForEntity(UserServiceUrl +"username/Ahmed4", String.class);
 String name =  mapper.readTree(res.getBody()).path("user").path("username").asText();
assertEquals("Ahmed4", name);

	}



	@Test
    void getonecomment() throws JsonMappingException, JsonProcessingException, InterruptedException{

		ResponseEntity<String> res =  restTemplate.getForEntity(PostServiceUrl+"/All-posts/Ahmed4",String.class);
		String id =  mapper.readTree(res.getBody()).get(0).path("id").asText();


		ResponseEntity<String> res2 =  restTemplate.getForEntity(Like_com_ServiceUrl+id,String.class);
        String content = mapper.readTree(res2.getBody()).path("comments").get(0).path("content").asText();

		assertEquals("AMERICA YA :D", content);
        
	}

	
	@Test
    void getone_posts() throws JsonMappingException, JsonProcessingException{
		ResponseEntity<String> res =  restTemplate.getForEntity(PostServiceUrl+"/All-posts/Ahmed4",String.class);
		String id =  mapper.readTree(res.getBody()).get(0).path("id").asText();

				ResponseEntity<String> res2 =  restTemplate.getForEntity(PostServiceUrl+"/one-post/"+id,String.class);
				String content = mapper.readTree(res2.getBody()).path("post").path("content").asText();
		assertEquals("www23", content);

	}



   




}
