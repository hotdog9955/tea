import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.*;
import java.net.*;
import java.util.Arrays;


public class TeaScreenFunctions{
    public final String[] logo = {
            "                 =                      ",
            "                 ++                     ",
            "                  ++*+                  ",
            "                    ****                ",
            "                   + ###*               ",
            "                **+  ###                ",
            "               ***   ##                 ",
            "               ####                     ",
            "                ##%#                    ",
            "                                        ",
            "       +++++++++++++++++++++++**####    ",
            "       +++=++++++++++++++++*+=+-::+##   ",
            "        **==+++++++++++++**+=++-:=###   ",
            "         ***==+++++++*****==###%%##     ",
            "           *#*+==+++++++=+###           ",
            "             *#########%%##             "
    };


    // puts a string in the center of the screen
    public void putStringCenter(TextGraphics tg, int row,String message){
        tg.putString((tg.getSize().getColumns()/2) - (message.length()/2), row, message );
    }

    // clears all characters in a row
    public void clearRow(TextGraphics tg, int row){

        for (int col = 0; col < tg.getSize().getColumns(); col++) {
            tg.putString(col, row, " ");
        }
    }

    // displays the splash screen and prompts the user to enter an IP
    public InetSocketAddress splashAndPrompt(Screen screen, TextGraphics tg) throws IOException {
        int i = 0;

        logoSplash(screen, tg, i);

        return getInetAddress(screen, tg);
    }

    // prompts the user to input an IP
    public InetSocketAddress getInetAddress(Screen screen, TextGraphics tg) throws IOException {
        // String to store input
        int width = tg.getSize().getColumns();
        StringBuilder ip = new StringBuilder();
        clearRow(tg, logo.length+5);
        // Reading input
        KeyStroke keyStroke;
        InetSocketAddress finalIp;
        do {
            screen.refresh();
            keyStroke = screen.pollInput();

            if (keyStroke == null) {
                continue;
            }
            if ((keyStroke.getKeyType() == KeyType.Character) && (ip.length() < width)) {
                ip.append(keyStroke.getCharacter());
            } else if (keyStroke.getKeyType() == KeyType.Backspace && !ip.isEmpty()) {
                ip.deleteCharAt(ip.length()-1);
            }
            clearRow(tg,logo.length+5);
            putStringCenter(tg, logo.length+5, ip.toString());

            if (keyStroke.getKeyType() == KeyType.Enter){
                try{
                    finalIp = new InetSocketAddress(ip.toString(),23788);
                    clearRow(tg, logo.length+4);
                    putStringCenter(tg, logo.length+4, "Valid IP found, Connecting...");
                    screen.refresh();
                } catch (UnknownHostException e) {
                    clearRow(tg, logo.length+4);
                    putStringCenter(tg, logo.length+4, "Invalid IP address found, please try again.");
                    screen.refresh();
                    continue;
                }
                break;
            }
        } while (true);
        return finalIp;
    }

    public void rotateMessages(String[] array, String newString){
        if (array == null || array.length == 0) {
            System.out.println("Array is empty or null.");
            return;
        }

        for (int i = array.length - 1; i > 0; i--) {
            array[i] = array[i - 1];
        }

        array[0] = newString;
    }

    // displays the tea logo
    private void logoSplash(Screen screen, TextGraphics tg, int i) throws IOException {
        for (String v : logo){
            i++;
            putStringCenter(tg, i, v);
            screen.refresh();
        }

        String welcomeMessage = "Welcome to Tea!";
        putStringCenter(tg, logo.length+3, welcomeMessage);

        String prompt = "Type a server IP to get started.";
        putStringCenter(tg, logo.length+4, prompt);
    }

    public void chatRoom(Screen screen, TextGraphics tg, Socket socket) throws IOException {

        BufferedWriter bufWrite = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader bufRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String userNum = bufRead.readLine();
        System.out.println(userNum);
        String[] messages = new String[23];
        Arrays.fill(messages, "");
        int width = tg.getSize().getColumns();
        int height = tg.getSize().getRows();

        screen.clear();

        screen.refresh();
        KeyStroke keyStroke;

        StringBuilder message = new StringBuilder();
        do {
            screen.refresh();
            keyStroke = screen.pollInput();

            if (bufRead.ready()){
                System.out.println("MESSAGE READY!");
                rotateMessages(messages,bufRead.readLine());
                for(int i = 0; i < messages.length; i++){
                    clearRow(tg, i);
                    putStringCenter(tg, i, messages[i]);
                    screen.refresh();
                }
            }

            if (keyStroke == null) {
                continue;
            }
            if ((keyStroke.getKeyType() == KeyType.Character) && (message.length() < width)) {
                message.append(keyStroke.getCharacter());
            } else if (keyStroke.getKeyType() == KeyType.Backspace && !message.isEmpty()) {
                message.deleteCharAt(message.length()-1);

            } else if ((keyStroke.getKeyType() == KeyType.Enter) && (!(message.toString().isEmpty()))){
                System.out.println(message);
                bufWrite.write(message + "\n");
                bufWrite.flush();
            }

            clearRow(tg,height-1);
            putStringCenter(tg, height-1, message.toString());
        }while(true);
    }
}
