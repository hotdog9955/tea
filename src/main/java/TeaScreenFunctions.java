import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


public class TeaScreenFunctions {
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
    public InetSocketAddress splashAndPrompt(Screen screen, TextGraphics tg) throws IOException, InterruptedException {
        int i = 0;

        logoSplash(screen, tg, i);

        return getInetAddress(screen, tg);
    }

    // prompts the user to input an IP
    public InetSocketAddress getInetAddress(Screen screen, TextGraphics tg) throws IOException {
        // String to store input
        int width = tg.getSize().getColumns();
        StringBuilder ip = new StringBuilder();

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
                    finalIp = new InetSocketAddress(ip.toString(),80);
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

    // displays the tea logo
    private void logoSplash(Screen screen, TextGraphics tg, int i) throws IOException, InterruptedException {
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
    }
}
