package project2demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.json.Json;
import javax.json.JsonObject;
import net.sf.json.JSONObject;

public class PeerThread extends Thread {
	private BufferedReader bufferedReader;
	//get a socket  and use it to get the inputstream 
	public PeerThread(Socket socket) throws IOException{
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	// pick up other messages from peers and print our usename and mess
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				JsonObject jsonObject =Json.createReader(bufferedReader).readObject();
			if (jsonObject.containsKey("username"))
				System.out.println("["+ jsonObject.getString("username")+"]:" + jsonObject.getString("message"));
			} catch (Exception e) {
				flag = false;
				interrupt();
			}
		}
	}

} 


//BufferedReader bR = new BufferedReader(bufferedReader);
//String line = "";
//StringBuilder responseStrBuilder = new StringBuilder();
//while((line =  bR.readLine()) != null){
//    responseStrBuilder.append(line);
//}
//bufferedReader.close();
//String str =	responseStrBuilder.toString();
//JSONObject jsonObject = JSONObject.fromObject(str);