import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;
/**
 * @author MMMarmelade
 * 2020/4/29
 */
public class KMEANSdemo {

    private static kmeansPanel kp;

    private static final int k = 3;// 3 classes
    private static final int pointsNum = 17;
    // sample points
    private static int[] knownPx = { 70, 160, 65, 150, 170, 100, 150, 350, 450, 320, 380, 400, 280, 220, 300, 290,
            390 };
    private static int[] knownPy = { 40, 55, 105, 180, 190, 250, 255, 60, 70, 190, 185, 230, 310, 390, 350, 450, 410 };

    // 3 initial center points
    private final Point c1 = new Point(30, 50, 1);
    private final Point c2 = new Point(300, 60, 2);
    private final Point c3 = new Point(260, 400, 3);

    public static void main(final String args[]) {
        final KMEANSdemo kmeansdemo = new KMEANSdemo();
        kmeansdemo.draw();
    }

    class Point {
        int x, y;
        int label;
        Point next = null;

        public Point(final int x, final int y, final int label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }

    }

    private void addPoint(final Point headP, final Point newP) {
        Point temp = headP;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newP;
    }

    private Point kmeans() {
        final Point pointHead = new Point(0, 0, 0);
        int diffx1 = 1000, diffy1 = 1000, diffx2 = 1000, diffy2 = 1000, diffx3 = 1000, diffy3 = 1000;
        final int threshold = 4;
        while (diffx1 > threshold || diffy1 > threshold || diffx2 > threshold || diffy2 > threshold
                || diffx3 > threshold || diffy3 > threshold) {
            // mark every point a label, and every point is stored in the pointHead
            int newPointLabel;
            for (int i = 0; i < pointsNum; i++) {
                newPointLabel = nearestLabel(knownPx[i], knownPy[i]);
                final Point newPoint = new Point(knownPx[i], knownPy[i], newPointLabel);
                addPoint(pointHead, newPoint);

                final Graphics g = kp.getGraphics();
                switch (newPointLabel) {
                    case 1:
                        g.setColor(Color.BLUE);
                        break;
                    case 2:
                        g.setColor(Color.ORANGE);
                        break;
                    case 3:
                        g.setColor(Color.RED);
                        break;
                }
                g.fillOval(knownPx[i], knownPy[i], 5, 5);
                try {
                    Thread.currentThread().sleep(200);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            final int oldc1x = c1.x, oldc1y = c1.y, oldc2x = c2.x, oldc2y = c2.y, oldc3x = c3.x, oldc3y = c3.y;
            // computing the new center points
            for (int i = 0; i < k; i++) {
                recenter(i + 1, pointHead);
            }
            diffx1 = Math.abs(c1.x - oldc1x);
            diffy1 = Math.abs(c1.y - oldc1y);
            diffx2 = Math.abs(c2.x - oldc2x);
            diffy2 = Math.abs(c2.y - oldc2y);
            diffx3 = Math.abs(c3.x - oldc3x);
            diffy3 = Math.abs(c3.y - oldc3y);

        }

        return pointHead;
    }

    private void recenter(final int label, Point p) {
        int ncx = 0, ncy = 0;
        int c = 0;
        while (p.next != null) {
            p = p.next;
            if (p.label == label) {
                ncx = ncx + p.x;
                ncy = ncy + p.y;
                c++;
            }
        }
        switch (label) {
            case 1:
                c1.x = ncx / c;
                c1.y = ncy / c;
                break;
            case 2:
                c2.x = ncx / c;
                c2.y = ncy / c;
                break;
            case 3:
                c3.x = ncx / c;
                c3.y = ncy / c;
                break;
        }

    }

    private int nearestLabel(final int x, final int y) {
        final float d1 = (float) Math.sqrt(Math.pow(x - c1.x, 2) + Math.pow(y - c1.y, 2));
        final float d2 = (float) Math.sqrt(Math.pow(x - c2.x, 2) + Math.pow(y - c2.y, 2));
        final float d3 = (float) Math.sqrt(Math.pow(x - c3.x, 2) + Math.pow(y - c3.y, 2));
        if (d1 < d2) {
            if (d1 < d3)
                return 1;
            else
                return 3;
        } else {
            if (d2 < d3)
                return 2;
            else
                return 3;
        }
    }

    private void draw() {
        // JPanel jp = new JPanel();
        final JFrame window = new JFrame("kmeans聚类实验");
        window.setBounds(400, 100, 600, 500);
        kp = new kmeansPanel();
        final JButton kb = new JButton("点击开始聚类");

        kb.setBounds(240, 420, 120, 50);
        kb.addMouseListener(new MouseListener() {

            public void mouseClicked(final MouseEvent e) {

                final Point pHead = kmeans();

            }

            public void mousePressed(final MouseEvent e) {
            }

            public void mouseReleased(final MouseEvent e) {
            }

            public void mouseEntered(final MouseEvent e) {
            }

            public void mouseExited(final MouseEvent e) {
            }
        });
        kp.add(kb);
        window.add(kp);

        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    static class kmeansPanel extends JPanel {
        @Override
        public void paint(final Graphics g) {
            super.paint(g);
            // 已知点
            for (int i = 0; i < pointsNum; i++){
                g.fillOval(knownPx[i], knownPy[i], 5, 5);
            }
            
        }
    }
}