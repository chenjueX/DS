package project2demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import javax.json.Json;
import javax.json.JsonObject;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import net.sf.json.JSONObject;

public class Peer {

	public static void main(String[] args) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("enter username & port for this peer ");
		String[] setupValues = bufferedReader.readLine().split(" ");
		ServerThread serverThread = new ServerThread(setupValues[1]);
		serverThread.start();
		new Peer().updateListenToPeers(bufferedReader, setupValues[1], serverThread);
	}
	//更新需要pick的peers each peer will be represented as a hostname and port number/
	public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread) throws Exception{
		System.out.println(">enter (space separted) hostname: port#");
		System.out.println(" Peers to receive messages from (s to skip):");
		String input = bufferedReader.readLine();
		String [] inputValues = input.split(" ");
		if(!input.equals("s")) for (int i = 0; i < inputValues.length; i++) {
			String[] address = inputValues[i].split(":");
			Socket socket = null;
		try {
			socket = new Socket(address[0], Integer.valueOf(address[1]));
			new PeerThread(socket).start();
		} catch (Exception e) {
			if(socket != null) socket.close();
			else System.out.println("invalid input. skipping to nest step");
		}
		}
		communicate(bufferedReader, username, serverThread);
	}
	//sending messages 
	public void communicate(BufferedReader bufferedReader, String username, ServerThread serverThread) {
		try {
			System.out.println("> you can now communicate (e to exit and c to change)");
			boolean flag = true;
			while(flag) {
				String message = bufferedReader.readLine();
				if (message.equals("e")) {
					flag =false;
					break;
				}else if (message.equals("c")) {
					updateListenToPeers(bufferedReader, username, serverThread);
				}else {
					StringWriter stringWriter = new StringWriter();
					Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
							                       .add("username", username)
							                       .add("message", message)
							                       .build());
					serverThread.sendMessage(stringWriter.toString());
				}
			}
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
