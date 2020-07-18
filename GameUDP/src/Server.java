import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Server
{
    public static void main(String[] args) throws Exception
    {

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket serverSocket = new DatagramSocket(5555);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        String inputLine,outputLine,lastTwoClient;
        String mess ="You win!";

        ArrayList usedWords = new ArrayList();


        while(true)
        {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            inputLine = new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
            lastTwoClient = inputLine.substring(inputLine.length()-2);

            if(inputLine.equals(mess)){
                System.out.println("YOU WIN! CONGRATULATIONS! ");
                break;
            }

            usedWords.add(inputLine);

            System.out.println("USED WORDS: " + usedWords);
            System.out.println("WORD FROM CLIENT: " + inputLine + " ENTER THE WORD THAT START WITH THESE LETTERS: " + lastTwoClient);

            long start = System.currentTimeMillis();
            while((System.currentTimeMillis()-start < 10000) && !stdIn.ready()){
            }

            if(stdIn.ready()) {

                outputLine = stdIn.readLine();

                while (usedWords.contains(outputLine)) {
                    System.out.println("THIS WORD IS USED. TRY ANOTHER ONE: ");

                    long start1 = System.currentTimeMillis();
                    while((System.currentTimeMillis()-start < 10000) && !stdIn.ready()){
                    }
                    if(stdIn.ready()){
                        outputLine = stdIn.readLine();
                    }

                    else{

                        sendData = mess.getBytes();
                        DatagramPacket sendMess = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        serverSocket.send(sendMess);
                        System.out.println("TIMES UP. YOU LOST...");
                        break;
                    }

                }

                if(!lastTwoClient.equals(outputLine.substring(0,2))){

                    sendData = mess.getBytes();
                    DatagramPacket sendMess1 = new DatagramPacket(sendData,sendData.length,IPAddress, port);
                    serverSocket.send(sendMess1);
                    System.out.println("GAME OVER. CLIENT WINS...");
                    break;

                }

                usedWords.add(outputLine);
                sendData = outputLine.getBytes();
                DatagramPacket sendToClient = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendToClient);

            }
            else {
                sendData = mess.getBytes();
                DatagramPacket sendMess = new DatagramPacket(sendData,sendData.length,IPAddress,port);
                serverSocket.send(sendMess);
                System.out.println("TIMES UP. YOU LOST...");
                break;
            }

        }
    }
}