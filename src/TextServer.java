import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TextServer {
    static int PORT = 11111;
    static ServerSocket socket;
    static Socket client;
    static Robot robot;

    TextServer() {
        stop();
        try {
            robot = new Robot();
            socket = new ServerSocket(PORT);
            System.out.println("Started TestServer on port " + PORT);

            while (true) {
                Socket c = socket.accept();
                System.out.println("Client connected to TestServer");
                if (client != null) {
                    try {
                        client.close();
                    } catch (Exception ignored) {}

                }

                client = c;

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());
                String line, res;
                while ((line = reader.readLine()) != null && !line.equals("bye")) {
                    if ((res = parse(line.replaceAll("\\\\n", "\n"))) != null) {
                        writer.write(res.replaceAll("\n", "\\n") + '\n');
                        writer.flush();
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    static void stop() {
        try {
            if (socket != null) socket.close();
        } catch (Exception ignored) {}
        socket = null;
        try {
            if (client != null) client.close();
        } catch (Exception ignored) {}
        client = null;
        hist = "";
        System.out.println("Stopped TestServer");
    }

    static String hist = "";
    static String parse(String line) {
        if (line.startsWith("cls")) {
            hist = "";
        } else if (line.startsWith("s")) {
            System.out.println(line);
            line = line.substring(2);
            int prev_len = hist.length();
            int new_len = line.length();
            int i;
            for (i = 0; i < prev_len && i < new_len; i++) {
                if (hist.charAt(i) != line.charAt(i)) break;
            }
            while (i < prev_len--) keyPress(KeyEvent.VK_BACK_SPACE);
            while (i < new_len) keyPress(line.charAt(i++));
            hist = line;
        } else if (line.equals("hi")) return "hi";
        else System.out.println("Unknown: " + line);

        return null;
    }

    static void keyPress(int ch) {
        if (Character.isUpperCase(ch)) {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(ch);
            robot.keyRelease(ch);
            robot.keyRelease(KeyEvent.VK_SHIFT);
        } else if (Character.isLowerCase(ch)) {
            robot.keyPress(ch - ('a' - 'A'));
            robot.keyRelease(ch - ('a' - 'A'));
        } else {
            robot.keyPress(ch);
            robot.keyRelease(ch);
        }
    }
}
