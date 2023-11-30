import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class TeaScreenFunctions {
    private String[] logo = {
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


    public void putStringCenter(TextGraphics tg, int row,String message){
        tg.putString((tg.getSize().getColumns()/2) - (message.length()/2), row, message );
    }

    public void clearRow(TextGraphics tg, int row){

        for (int col = 0; col < tg.getSize().getColumns(); col++) {
            tg.putString(col, row, " ");
        }
    }

    // takes in text graphics and integer (where it is width wise
    public InetAddress displaySplash(Screen screen, TextGraphics tg) throws IOException, InterruptedException {
        int i = 0;
        TerminalSize size = screen.getTerminalSize();
        int width = size.getColumns();
        int height = size.getRows();

        for (String v : logo){
            i++;
            putStringCenter(tg, i, v);
            screen.refresh();
            Thread.sleep(75);
        }

        String welcomeMessage = "Welcome to Tea!";
        putStringCenter(tg, logo.length+3, welcomeMessage);

        String prompt = "Type a server IP to get started.";
        putStringCenter(tg, logo.length+4, prompt);

        // String to store input
        StringBuilder ip = new StringBuilder();

        // Reading input
        KeyStroke keyStroke = null;
        InetAddress finalIp;
        do {
            screen.refresh();
            keyStroke = screen.pollInput();

            if (keyStroke == null) {
                continue;
            }
            if ((keyStroke.getKeyType() == KeyType.Character) && (ip.length() < 15)) {
                ip.append(keyStroke.getCharacter());
            } else if (keyStroke.getKeyType() == KeyType.Backspace && !ip.isEmpty()) {
                ip.deleteCharAt(ip.length()-1);
            }
            clearRow(tg,logo.length+5);
            putStringCenter(tg, logo.length+5, ip.toString());

            if (keyStroke.getKeyType() == KeyType.Enter){
                try{
                    finalIp = InetAddress.getByName(ip.toString());
                    clearRow(tg, logo.length+4);
                    putStringCenter(tg, logo.length+4, "Valid IP found, Connecting...");
                    screen.refresh();
                } catch (UnknownHostException e) {
                    clearRow(tg, logo.length+4);
                    putStringCenter(tg, logo.length+4, "Invalid IP address found, please try again.");
                    continue;
                }
                break;
            }
        } while (true);

        return finalIp;
    }
}
