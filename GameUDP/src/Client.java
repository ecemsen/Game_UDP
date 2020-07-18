import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Client
{
    public static void main(String[] args) throws Exception
    {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String inputLine,outputLine,lastTwoServer;
        String mess ="You win!";

        ArrayList usedWords  = new ArrayList();


        System.out.println("ENTER A WORD TO START THE GAME:");
        inputLine = stdIn.readLine();
        usedWords.add(inputLine);
        sendData = inputLine.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5555);
        clientSocket.send(sendPacket);

        while(true) {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            outputLine = new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
            lastTwoServer = outputLine.substring(outputLine.length()-2);

            if(outputLine.equals(mess)){
                System.out.println("YOU WIN! CONGRATULATIONS! ");
                break;
            }

            usedWords.add(outputLine);

            System.out.println("USED WORDS: " + usedWords.toString());
            System.out.println("WORD FROM SERVER: " + outputLine + " ENTER THE WORD THAT START WITH THESE LETTERS: " + lastTwoServer);

            long start = System.currentTimeMillis();
            while((System.currentTimeMillis()-start < 10000) && !stdIn.ready()){
            }

            if(stdIn.ready()) {

                inputLine = stdIn.readLine();

                while (usedWords.contains(inputLine)) {
                    System.out.println("THIS WORD IS USED. TRY ANOTHER ONE: ");

                    long start1 = System.currentTimeMillis();
                    while((System.currentTimeMillis()-start < 10000) && !stdIn.ready()) {
                    }

                    if(stdIn.ready()){
                        inputLine = stdIn.readLine();
                    }

                    else{

                        sendData = mess.getBytes();
                        DatagramPacket sendMess = new DatagramPacket(sendData, sendData.length, IPAddress, 5555);
                        clientSocket.send(sendMess);
                        System.out.println("TIMES UP. YOU LOST...");
                        break;
                    }


                }


                if (!lastTwoServer.equals(inputLine.substring(0, 2))) {

                    sendData = mess.getBytes();
                    DatagramPacket sendMess1 = new DatagramPacket(sendData, sendData.length, IPAddress, 5555);
                    clientSocket.send(sendMess1);
                    System.out.println("GAME OVER. SERVER WINS...");
                    break;

                }

                usedWords.add(inputLine);
                sendData = inputLine.getBytes();
                DatagramPacket sendToServer = new DatagramPacket(sendData, sendData.length, IPAddress, 5555);
                clientSocket.send(sendToServer);



            }
            else {
                sendData = mess.getBytes();
                DatagramPacket sendMess = new DatagramPacket(sendData, sendData.length, IPAddress, 5555);
                clientSocket.send(sendMess); 
                System.out.println("TIMES UP. YOU LOST...");
                break;

            }

        }

    }
}