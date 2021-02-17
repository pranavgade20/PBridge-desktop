import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    MainFrame() {
        setTitle("PBridge");
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;

        JButton start = new JButton("Start");
        start.addActionListener(actionEvent -> {
            new Thread(TextServer::new).start();
            new Thread(TrackpadServer::new).start();
            new Thread(DrawingpadServer::new).start();
        });
        pane.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(actionEvent -> {
            TextServer.stop();
            TrackpadServer.stop();
            DrawingpadServer.stop();
        });
        c.gridy++;
        pane.add(stop);

        JScrollPane scrollPane = new JScrollPane(pane);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        setPreferredSize(new Dimension(100, 100));
        setLocation(100,100);
        setVisible(true);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.out.println("Closing");
                TextServer.stop();
                super.windowClosing(windowEvent);
            }
        });
    }
}
