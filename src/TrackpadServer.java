import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TrackpadServer {
    static int PORT = 11111 + 1;
    static ServerSocket socket;
    static Socket client;
    static Robot robot;

    TrackpadServer() {
        stop();
        try {
            robot = new Robot();
            width = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
            height = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
            socket = new ServerSocket(PORT);
            System.out.println("Started TrackpadServer on port " + PORT);

            while (true) {
                Socket c = socket.accept();
                System.out.println("Client connected to TrackpadServer");
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
        System.out.println("Stopped TrackpadServer");
    }

    static int width, height;
    static double dampening = 0.3;
    static String parse(String line) {
        if (line.startsWith("m")) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            double x = Double.parseDouble(line.split(" ")[1]) * dampening;
            double y = Double.parseDouble(line.split(" ")[2]) * dampening;
            System.out.println(line);
            robot.mouseMove((int) (point.x + x*width), (int) (point.y + y*height));
        } else if (line.startsWith("p")) {
            switch (line.charAt(2)) {
                case '1':
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
            }
        } else if (line.equals("hi")) return "hi";
        else System.out.println("Unknown: " + line);

        return null;
    }
}
