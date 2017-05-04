import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Ipv6Client{
	public static void main(String[] args)throws Exception{

		try(Socket socket = new Socket("codebank.xyz", 38004)){
			InputStream fromServer = socket.getInputStream();
			OutputStream toServer = socket.getOutputStream();
			//Total header length in byte
			int headerLength = 40;
			//IP version 6
			int version = 6 << 28;
			//Traffic Class Dont Implement
			int trafficClass = 0;
			//Flow Label Dont Implement
			int flowLabel = 0;
			//Payload Length, length of packet. Not including packet header
			int payLoadLength;
			//UDP Protocol which is 17 in decimal
			int nextHeader = 17 << 8;
			//Hop Limit set to 20
			int hopLimit = 20;
			//Random IPv4 source address
			int sourceAddr = 1234;
			//IPv4 destination address
			int destinationAddr = socket.getInetAddress().hashCode();
			//counter count up to 12
			int counter = 0;
			//Initial size of data in packet not including header
			int length = 2;
			//build and send a total of 12 packet to server
			while(counter < 12){
				//initialize packet size to data size + header size
				byte[] packet = new byte[length + headerLength];
				//byte array to hold response from server
				byte[] response = new byte[4];
				//byte buffer to manipulate byte array
				ByteBuffer putToPacket = ByteBuffer.wrap(packet);
				//put version, trafic class and flow label to the first line of header
				putToPacket.putInt(version | trafficClass | flowLabel);
				payLoadLength = length<<16;
				//put the payload length, next header and hop limit to second line of header
				putToPacket.putInt(payLoadLength | nextHeader | hopLimit);
				//extend IPv4 source address to IPv6 source address
				putToPacket.putInt(0);
				putToPacket.putInt(0);
				putToPacket.putInt(0 | (int)0xffff);
				putToPacket.putInt(sourceAddr);
				//extend IPv4 destination address to IPv6 destination address
				putToPacket.putInt(0);
				putToPacket.putInt(0);
				putToPacket.putInt(0 | (int)0xffff);
				putToPacket.putInt(destinationAddr);
				//send packet over to server
				toServer.write(packet);
				//get response from server
				fromServer.read(response);
				//print response result
				System.out.println("data length: " + length);
				System.out.print("Response: ");
				printBytesInHex(response);
				//double data size in packet
				length *= 2;
				//increment counter
				counter +=1;
			}
			
			
		}	
	}
	//funtion that prints the byte array into hex format string. 
	public static void printBytesInHex(byte[] bytes){
		for(int i = 0; i<bytes.length; i++){
			System.out.print(String.format("%02X", bytes[i]));
		}
		System.out.println("\n");
	}
}
