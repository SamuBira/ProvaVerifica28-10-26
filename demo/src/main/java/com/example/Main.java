package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        List<String> users = new ArrayList<String>();

        int gold = 10;
        int pit = 30;
        int parterre = 60;

        System.out.println("Hello world!");
        ServerSocket ss = new ServerSocket(3000);
        Socket mySocket = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);

        out.println("WELCOME");

        String loginUsername;

        while (true) {
            loginUsername = in.readLine();
            String[] parti = loginUsername.split(" ");
            if (parti.length == 2 && parti[0].equals("LOGIN")) {
                if (!users.contains(parti[1])) {
                    users.add(parti[1]);
                    break;
                }
                out.println("ERR: UserInUse");
            }
            out.println("ERR: UNKNOWEDCMD");
        }
        out.println("OK");

        boolean quit = false;
        do {
            String comandoClient = in.readLine();
            if (comandoClient == null)
                break; // Gestisce la disconnessione del client

            switch (comandoClient) {
                case "BUY":
                    // Il server si mette in attesa della stringa tipo "Gold 2"
                    String tipoBiglietto = in.readLine();
                    if (tipoBiglietto == null) {
                        quit = true;
                        break;
                    }

                    // 2. CORREZIONE SPLIT: Dividi per spazio " ", non per ""
                    String[] parti = tipoBiglietto.split(" ");
                    int numeroPrimitivo = 0;

                    // 3. CORREZIONE LOOP: Rimosso il do-while interno inutile e dannoso
                    if (parti.length == 2) {
                        try {
                            numeroPrimitivo = Integer.parseInt(parti[1]);

                            // Trasformiamo in minuscolo per evitare problemi di maiuscole/minuscole
                            switch (parti[0].toLowerCase()) {
                                case "gold":
                                    if (numeroPrimitivo > gold || numeroPrimitivo <= 0) {
                                        out.println("ERR: NOT_ENOUGH_TICKETS");
                                    } else {
                                        gold -= numeroPrimitivo;
                                        out.println("OK_BUY");
                                    }
                                    break;

                                case "pit":
                                    if (numeroPrimitivo > pit || numeroPrimitivo <= 0) {
                                        out.println("ERR: NOT_ENOUGH_TICKETS");
                                    } else {
                                        pit -= numeroPrimitivo;
                                        out.println("OK_BUY");
                                    }
                                    break;

                                case "parterre":
                                    if (numeroPrimitivo > parterre || numeroPrimitivo <= 0) {
                                        out.println("ERR: NOT_ENOUGH_TICKETS");
                                    } else {
                                        parterre -= numeroPrimitivo;
                                        out.println("OK_BUY");
                                    }
                                    break;

                                default:
                                    out.println("ERR: UNKNOWEDCMD");
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            out.println("ERR: INVALID_NUMBER");
                        }
                    } else {
                        out.println("ERR: UNKNOWEDCMD");
                    }
                    break;
                case "N":
                    out.println("AVAIL Gold:" + gold + " Pit:" + pit + " Parterre:" + parterre);
                    break;
                case "QUIT":
                    out.println("BYE");
                    quit = true;
                    break;
                default:
                    out.println("ERR: UNKNOWEDCMD");
                    break;
            }
        } while (!quit);

        // Chiudi le risorse alla fine
        in.close();
        out.close();
        mySocket.close();
        ss.close();
    }
}