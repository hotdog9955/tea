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
        Socket socket = new Socket();
        socket.setSoTimeout(5000);
        attemptConnection(socket, ip, teaScreenFunctions, tg, screen);

    }

    private static void attemptConnection(Socket socket, InetSocketAddress ip, TeaScreenFunctions teaScreenFunctions, TextGraphics tg, Screen screen) throws IOException {
        while(true){
            try{
                System.out.println("attempting????");
                socket.connect(ip,5000);
                teaScreenFunctions.clearRow(tg, teaScreenFunctions.logo.length+4);
                teaScreenFunctions.putStringCenter(tg, teaScreenFunctions.logo.length+4, "connection successful!");
                screen.refresh();
                break;
            }catch (IOException e) {
                ip = teaScreenFunctions.getInetAddress(screen, tg);
                teaScreenFunctions.clearRow(tg, teaScreenFunctions.logo.length+4);
                teaScreenFunctions.putStringCenter(tg, teaScreenFunctions.logo.length+4, "connection failed, please try again.");
                screen.refresh();
            }
        }
    }
}
