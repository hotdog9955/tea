import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();



        TeaScreenFunctions teaScreenFunctions = new TeaScreenFunctions();
        InetSocketAddress ip = teaScreenFunctions.splashAndPrompt(screen, tg );
        Socket _socket = attemptConnection(ip, teaScreenFunctions, tg, screen);

    }

    private static Socket attemptConnection(InetSocketAddress ip, TeaScreenFunctions teaScreenFunctions, TextGraphics tg, Screen screen) throws IOException {

        while(true){
            try{
                Socket socket = new Socket();
                socket.setSoTimeout(5000);
                socket.connect(ip,80);
                teaScreenFunctions.clearRow(tg, teaScreenFunctions.logo.length+4);
                teaScreenFunctions.putStringCenter(tg, teaScreenFunctions.logo.length+4, "connection successful!");
                screen.refresh();
                return socket;
            }catch (IOException e) {
                teaScreenFunctions.clearRow(tg, teaScreenFunctions.logo.length+4);
                teaScreenFunctions.putStringCenter(tg, teaScreenFunctions.logo.length+4, "connection failed, please try again.");
                ip = teaScreenFunctions.getInetAddress(screen, tg);
                screen.refresh();
            }
        }
    }
}
