package doomchit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class Roulette_copy extends Base {
    JButton stopButton = new JButton("멈추기");
    int[] x2 = {220, 240, 220, 130, 120};
    int[] y2 = {360, 420, 480, 520, 380};
    int[] angles = {-70, 30, 75, 120, 195};
    int[] lengths = {50, 50, 85, 140, 80};
    int angle = -60;
    int sArc = 90;
    int asd = 0;
    String[] str = "1시간 추가\\매점 쿠폰(5,000)\\2시간 추가\\3시간 추가\\꽝".split("\\\\");
    JLabel label;

    BufferedImage img;

    public Roulette_copy() {
        super("룰렛", 400, 600);
        setLayout(null);

        File f = new File("src/doomchit/datafiles/이미지/.test.png");
        if(f.exists()) f.delete();

        label = new JLabel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                File f = new File("src/doomchit/datafiles/이미지/.test.png");
                if (!f.exists()) {

                    Color[] col = {Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN, Color.GREEN};

                    double[] percent = {0.25, 0.1, 0.15, 0.1, 0.4};
                    for (int i = 0; i < col.length; i++) {
                        g2.setColor(col[i]);
                        g2.fillArc(0, 0, 300, 300, sArc, -(int) (360 * percent[i]));
                        sArc -= (int) (360 * percent[i]);
                    }

                    g2.setColor(Color.BLACK);
                    for (int i = 0; i < str.length; i++) {
                        g2.drawString(str[i], x2[i] - 50, y2[i] - 250);
                    }
                } else {
                    g2.rotate(Math.toRadians(asd), 150, 150);
                    g2.drawImage(img(".test.png", 300, 300).getImage(), 0, 0, 300, 300, null);
                }

            }
        };

        addComp(label, 50, 220, 300, 300);
        addComp(stopButton, 160, 20, 80, 30);

//        label.setBorder(new LineBorder(Color.BLACK));

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
//                if (angle > 360) angle = 0;
                if (asd > 360) asd = 0;
                asd++;
//                sArc--;
//                for (int i = 0; i < x2.length; i++) {
//                    x2[i] = 200 + (int) (Math.cos(Math.toRadians(angles[i])) * lengths[i]);
//                    y2[i] = 420 + (int) (Math.sin(Math.toRadians(angles[i])) * lengths[i]);
//                    angles[i] ++;
//                }
//                angle++;
                label.repaint();
                label.revalidate();
            }
        };

        timer.schedule(task, 0, 1);

        stopButton.addActionListener(a -> {
            timer.cancel();
            int prize = asd;
            System.out.println(prize);
            if (prize >= 0 && prize <= 144) {
                msg("꽝입니다.");
            } else if (prize >= 145 && prize <= 180) {
                msg("3시간이 추가되었습니다.");
                CHARGE_TIME += (3600 * 3);
            } else if (prize >= 181 && prize <= 234) {
                msg("2시간이 추가되었습니다.");
                CHARGE_TIME += (3600 * 2);
            } else if (prize >= 235 && prize <= 270) {
                msg("매점 쿠폰이 당첨되었습니다.");
                execute("update coupon set roulette = roulette + 1 where m_no = " + NO);
                dispose();
            } else {
                msg("1시간이 추가되었습니다.");
                CHARGE_TIME += 3600;
                dispose();
            }

            int h = CHARGE_TIME / 3600;
            int m = CHARGE_TIME % 3600 / 60;
            int s = CHARGE_TIME % 60;
            execute("update time set t_charge = " + (h + ":" + m + ":" + s) + " where m_no = " + NO);
            dispose();
        });

        img = new BufferedImage(label.getWidth(), label.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        label.printAll(g2d);
        g2d.dispose();

        try {
            ImageIO.write(img, "png", new File("/home/leedongyun/Desktop/프로그래밍2/src/doomchit/datafiles/이미지/.test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getContentPane().setBackground(Color.WHITE);

        setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] col = {Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN, Color.GREEN};

        for (int i = 0; i < str.length; i++) {
            g2.setColor(col[i]);
            g2.fillRect(20, 100 + (i * 30), 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawRect(20, 100 + (i * 30), 10, 10);
            g2.drawString(str[i], 40, 110 + (i * 30));
        }

        g2.setColor(Color.RED);
        g2.fillRect(190, 100, 20, 100);
        int[] x = {175, 200, 225};
        int[] y = {200, 240, 200};
        g2.fillPolygon(x, y, 3);
    }

    public static void main(String[] args) {
        NO = 1;
        new Roulette_copy();
    }
}
