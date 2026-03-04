package config;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class profileImage extends JLabel {

    @Override
    protected void paintComponent(Graphics g) {

        if (getIcon() == null) {
            super.paintComponent(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int diameter = Math.min(getWidth(), getHeight());

        Shape circle = new Ellipse2D.Float(0, 0, diameter, diameter);
        g2.setClip(circle);

        g2.drawImage(((ImageIcon) getIcon()).getImage(),
                     0, 0, diameter, diameter, this);

        g2.dispose();
    }
}