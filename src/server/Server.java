package server;

import messages.Message;
import messages.MessageType;
import messages.Status;
import messages.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
    private static final int PORT = 8697;
    private static final HashMap<String, User> names = new HashMap<>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static ArrayList<User> users = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        System.out.println("Chat server is running");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Kết nối lỗi " + e.getMessage());
        }
        try {
            while (true){
                new ListenServer(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    public static class ListenServer extends Thread{
        private String name;
        Socket socket;
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public ListenServer(Socket socket){
            this.socket = socket;
        }
        public void run(){
            System.out.println("Attempting to connect a user ...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);
                writers.add(output);
                sendNotification(firstMessage);
                addToList();

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        System.out.println(inputmsg.getType() + " - " + inputmsg.getName() + ": " + inputmsg.getMsg());
                        switch (inputmsg.getType()) {
                            case USER:
                                write(inputmsg);
                                break;
                            case CONNECTED:
                                addToList();
                                break;
                            case STATUS:
                                changeStatus(inputmsg);
                                break;
                        }
                    }
                }
            } catch (SocketException socketException) {
                System.out.println("Socket Exception for user " + name);
            } catch (Exception e){
                System.out.println("Exception in run() method for user: " + name);
            } finally {
                closeConnections();
            }
        }

        private void closeConnections() {
            System.out.println("closeConnections() method");
            System.out.println("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (name != null) {
                names.remove(name);
                System.out.println("User: " + name + " has been removed!");
            }
            if (user != null){
                users.remove(user);
                System.out.println("User object: " + user + " has been removed!");
            }
            if (output != null){
                writers.remove(output);
                System.out.println("Writer object: " + user + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            System.out.println("closeConnections() method Exit");
        }

        private Message removeFromList() throws IOException {
            System.out.println("removeFromList() method Enter");
            Message msg = new Message();
            msg.setMsg("has left the chat.");
            msg.setType(MessageType.DISCONNECTED);
            msg.setName("SERVER");
            msg.setList(names);
            write(msg);
            System.out.println("removeFromList() method Exit");
            return msg;
        }

        private Message changeStatus(Message inputmsg) throws IOException {
            System.out.println(inputmsg.getName() + " has changed status to  " + inputmsg.getStatus());
            Message msg = new Message();
            msg.setName(user.getName());
            msg.setType(MessageType.STATUS);
            msg.setMsg("");
            User userObj = names.get(name);
            userObj.setStatus(inputmsg.getStatus());
            write(msg);
            return msg;
        }

        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.setMsg("Welcome, You have now joined the server C500 chat! Enjoy chatting!");
            msg.setType(MessageType.CONNECTED);
            msg.setName("SERVER");
            write(msg);
            return msg;
        }

        private Message sendNotification(Message firstMessage) throws IOException {
            Message msg = new Message();
            msg.setMsg("has joined the chat.");
            msg.setType(MessageType.NOTIFICATION);
            msg.setName(firstMessage.getName());
            msg.setPicture(firstMessage.getPicture());
            write(msg);
            return msg;
        }

        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers){
                msg.setList(names);
                msg.setUsers(users);
                msg.setCount(names.size());
                writer.writeObject(msg);
                writer.reset();
            }
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) {
            System.out.println(firstMessage.getName() + "is trying to connect");
            if (!names.containsKey(firstMessage.getName())) {
                this.name = firstMessage.getName();
                user = new User();
                user.setName(firstMessage.getName());
                user.setStatus(Status.ONLINE);
                user.setPicture(firstMessage.getPicture());

                users.add(user);
                names.put(name, user);

                System.out.println(name + " has been added to the list");
            } else {
                System.out.println(firstMessage.getName() + " is already connected");
                //throw new DuplicateUsernameException(firstMessage.getName() + " is already connected");
            }
        }
    }
}
