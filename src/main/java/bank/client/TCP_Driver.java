package bank.client;

import bank.Bank;
import bank.commands.Command;
import bank.commands.CommandBank;

import java.io.*;

import java.net.Socket;
import java.util.Arrays;

public class TCP_Driver implements bank.BankDriver {
	private Socket serverSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private CommandBank bank;
	@Override
	public void connect(String[] args) throws IOException, IllegalArgumentException {
		System.out.println("connect called with arguments " + Arrays.deepToString(args));
		try{
			serverSocket = new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println("ServerSocket established");
			out = new ObjectOutputStream(serverSocket.getOutputStream());
			System.out.println("outpustream established");
			in = new ObjectInputStream(serverSocket.getInputStream());
			System.out.println("inputstream established");

			bank = new CommandBank(req -> {
				try{
					out.writeObject(req);
					out.flush();
					return (Command) in.readObject();
				}catch (ClassNotFoundException | IOException e){
					throw new RuntimeException(e);
				}
			});

			System.out.println("Connection established");
		}catch(IOException | IllegalArgumentException e){
			System.out.println("Connection could not be established");
			throw e;
		}
	}

	@Override
	public void disconnect() throws IOException {
		try{
			serverSocket.close();
			bank = null;
			System.out.println("Connection closed");
		}catch (IOException e){
			System.out.println("Connection could not be closed");
			throw e;
		}
	}

	@Override
	public Bank getBank() {
		return bank;
	}
}

