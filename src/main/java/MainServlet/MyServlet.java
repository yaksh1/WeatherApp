package MainServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Input
		String city  = request.getParameter("city");
		//API setup
		String apiKey = "35e8005b5b3672382512e5fc04085edd";
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
		
		//API integration
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		// reading data from network
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		// storing in string
		StringBuilder responseContent = new StringBuilder();
		
		//to take input from reader , scanner is needed
		Scanner scanner = new Scanner(reader);
		
		//store
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		
		scanner.close();
		System.out.println(responseContent);
		
		//Type Casting
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
		
		System.out.println(jsonObject);
		
		//Date & time
		long dateTimeStamp = jsonObject.get("dt").getAsLong()*1000;
		String date = new Date(dateTimeStamp).toString();
		
		//Temperature
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelcius = (int)(temperatureKelvin-273.15);
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind Speed
		double speed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather Condition
		String weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//set data as request attributes(to send it to jsp)
		request.setAttribute("date", date);
		request.setAttribute("temperature",temperatureCelcius);
		request.setAttribute("city", city);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", speed);
		request.setAttribute("weatherCondition", weather);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		//forward the request to .jsp file
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
	}

}
