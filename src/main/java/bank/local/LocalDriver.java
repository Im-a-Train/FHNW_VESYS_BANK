/*
 * Copyright (c) 2020 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import bank.Bank;

import java.net.Socket;

public class LocalDriver implements bank.BankDriver {
	private Bank localBank = null;
	private Socket socket;

	@Override
	public void connect(String[] args) {
		localBank = new LocalBank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() {
		localBank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return localBank;
	}

}