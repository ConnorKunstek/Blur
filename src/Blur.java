
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class Blur extends JFrame {

    // This frame will hold the primary components:
    // 	An object to hold the buffered image and its associated operations
    //	Components to control the interface

    // Instance variables
    private BufferedImage image;   // the image
    private MyImageObj view;       // a component in which to display an image
    private JLabel infoLabel;
    private JButton ResetButton; // Button to restore original image
    private JButton BlurButton1; // Button to restore original image
    private JButton BlurButton2; // Button to restore original image
    private JButton BlurButton3; // Button to restore original image
    private JButton BlurButton4; // Button to restore original image
    private JButton BlurButton5; // Button to restore original image

    // Constructor for the frame
    public Blur () {

        super();				// call JFrame constructor

        this.buildMenus();		// helper method to build menus
        this.buildComponents();		// helper method to set up components
        this.buildDisplay();		// Lay out the components on the display
    }

    //  Builds the menus to be attached to this JFrame object
    //  Primary side effect:  menus are added via the setJMenuBar call
    //  		Action listeners for the menu items are anonymous inner
    //		classes here
    //  This helper method is called once by the constructor

    private void buildMenus () {

        final JFileChooser fc = new JFileChooser(".");
        JMenuBar bar = new JMenuBar();
        this.setJMenuBar (bar);
        JMenu fileMenu = new JMenu ("File");
        JMenuItem fileopen = new JMenuItem ("Open");
        JMenuItem fileexit = new JMenuItem ("Exit");

        fileopen.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        int returnVal = fc.showOpenDialog(Blur.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            try {
                                image = ImageIO.read(file);
                            } catch (IOException e1){};

                            view.setImage(image);
                            view.showImage();
                        }
                    }
                }
        );
        fileexit.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        fileMenu.add(fileopen);
        fileMenu.add(fileexit);
        bar.add(fileMenu);
    }

    //  Allocate components (these are all instance vars of this frame object)
    //  and set up action listeners for each of them
    //  This is called once by the constructor

    private void buildComponents() {

        // Create components to in which to display image and GUI controls
        // reads a default image
        view = new MyImageObj(readImage("boat.gif"));
        infoLabel = new JLabel("Original Image");
        ResetButton = new JButton("Reset");
        BlurButton1 = new JButton("Blur 1.0");
        BlurButton2 = new JButton("Blur 1.5");
        BlurButton3 = new JButton("Blur 2.0");
        BlurButton4 = new JButton("Blur 2.5");
        BlurButton5 = new JButton("Blur 3.0");

        // Button listeners activate the buffered image object in order
        // to display appropriate function
        ResetButton.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.showImage();
                        infoLabel.setText("Original");
                    }
                }
        );
        BlurButton1.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.BlurImage(1);
                        infoLabel.setText("Blurred by 1.0");
                    }
                }
        );
        BlurButton2.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.BlurImage(1.5);
                        infoLabel.setText("Blurred by 1.5");
                    }
                }
        );
        BlurButton3.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.BlurImage(2);
                        infoLabel.setText("Blurred by 2.0");
                    }
                }
        );
        BlurButton4.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.BlurImage(2.5);
                        infoLabel.setText("Blurred by 2.5");
                    }
                }
        );
        BlurButton5.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.BlurImage(3);
                        infoLabel.setText("Blurred by 3.0");
                    }
                }
        );

    }

    // This helper method adds all components to the content pane of the
    // JFrame object.  Specific layout of components is controlled here

    private void buildDisplay () {

        // Build first JPanel
        JPanel controlPanel = new JPanel();
        GridLayout grid = new GridLayout (1, 5);
        controlPanel.setLayout(grid);
        controlPanel.add(infoLabel);
        controlPanel.add(ResetButton);
        controlPanel.add(BlurButton1);
        controlPanel.add(BlurButton2);
        controlPanel.add(BlurButton3);
        controlPanel.add(BlurButton4);
        controlPanel.add(BlurButton5);

        // Add panels and image data component to the JFrame
        Container c = this.getContentPane();
        c.add(view, BorderLayout.CENTER);
        c.add(controlPanel, BorderLayout.SOUTH);

    }

    // This method reads an Image object from a file indicated by
    // the string provided as the parameter.  The image is converted
    // here to a BufferedImage object, and that new object is the returned
    // value of this method.
    // The mediatracker in this method can throw an exception

    public BufferedImage readImage (String file) {

        Image image = Toolkit.getDefaultToolkit().getImage(file);
        MediaTracker tracker = new MediaTracker (new Component () {});
        tracker.addImage(image, 0);
        try { tracker.waitForID (0); }
        catch (InterruptedException e) {}
        BufferedImage bim = new BufferedImage
                (image.getWidth(this), image.getHeight(this),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage (image, 0, 0, this);
        return bim;
    }

    // The main method allocates the "window" and makes it visible.
    // The windowclosing event is handled by an anonymous inner (adapter)
    // class
    // Command line arguments are ignored.

    public static void main(String[] argv) {

        JFrame frame = new Blur();
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener (
                new WindowAdapter () {
                    public void windowClosing ( WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
    }

    /*****************************************************************

     This is a helper object, which could reside in its own file, that
     extends JLabel so that it can hold a BufferedImage

     I've added the ability to apply image processing operators to the
     image and display the result

     ***************************************************************************/

    public class MyImageObj extends JLabel {

        // instance variable to hold the buffered image
        private BufferedImage bim=null;
        private BufferedImage filteredbim=null;

        //  tell the paintcomponent method what to draw
        private boolean showfiltered=false;

        // here are a few kernels to try
        private final float[] GAUSS5x5SD1 =
                {0.003765f, 0.015019f, 0.023792f, 0.015019f, 0.003765f,
                        0.015019f, 0.059912f, 0.094907f, 0.059912f, 0.015019f,
                        0.023792f, 0.094907f, 0.150342f, 0.094907f, 0.023792f,
                        0.015019f, 0.059912f, 0.094907f, 0.059912f, 0.015019f,
                        0.003765f, 0.015019f, 0.023792f, 0.015019f, 0.003765f};
        private final float[] GAUSS5x5SD15 =
                {0.015026f,	0.028569f,	0.035391f,	0.028569f,	0.015026f,
                        0.028569f,	0.054318f,	0.067288f,	0.054318f,	0.028569f,
                        0.035391f,	0.067288f,	0.083355f,	0.067288f,	0.035391f,
                        0.028569f,	0.054318f,	0.067288f,	0.054318f,	0.028569f,
                        0.015026f,	0.028569f,	0.035391f,	0.028569f,	0.015026f};
        private final float[] GAUSS5x5SD2 =
                {0.023528f,	0.033969f,	0.038393f,	0.033969f,	0.023528f,
                        0.033969f,	0.049045f,	0.055432f,	0.049045f,	0.033969f,
                        0.038393f,	0.055432f,	0.062651f,	0.055432f,	0.038393f,
                        0.033969f,	0.049045f,	0.055432f,	0.049045f,	0.033969f,
                        0.023528f,	0.033969f,	0.038393f,	0.033969f,	0.023528f,};
        private final float[] GAUSS5x5SD25 =
                {0.028672f,	0.036333f,	0.039317f,	0.036333f,	0.028672f,
                        0.036333f,	0.046042f,	0.049824f,	0.046042f,	0.036333f,
                        0.039317f,	0.049824f,	0.053916f,	0.049824f,	0.039317f,
                        0.036333f,	0.046042f,	0.049824f,	0.046042f,	0.036333f,
                        0.028672f,	0.036333f,	0.039317f,	0.036333f,	0.028672f};
        private final float[] GAUSS5x5SD3 =
                {0.031827f,	0.037541f,	0.039665f,	0.037541f,	0.031827f,
                        0.037541f,	0.044281f,	0.046787f,	0.044281f,	0.037541f,
                        0.039665f,	0.046787f,	0.049434f,	0.046787f,	0.039665f,
                        0.037541f,	0.044281f,	0.046787f,	0.044281f,	0.037541f,
                        0.031827f,	0.037541f,	0.039665f,	0.037541f,	0.031827f};

        // This constructor stores a buffered image passed in as a parameter
        public MyImageObj(BufferedImage img) {
            bim = img;
            filteredbim = new BufferedImage
                    (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
            setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

            this.repaint();
        }

        // This mutator changes the image by resetting what is stored
        // The input parameter img is the new image;  it gets stored as an
        //     instance variable
        public void setImage(BufferedImage img) {
            if (img == null) return;
            bim = img;
            filteredbim = new BufferedImage
                    (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
            setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));
            showfiltered=false;
            this.repaint();
        }

        // accessor to get a handle to the bufferedimage object stored here
        public BufferedImage getImage() {
            return bim;
        }


        //  apply the blur operator
        public void BlurImage(double val) {
            if (bim == null) return;
            Kernel kernel = new Kernel(5, 5, GAUSS5x5SD1);
            if(val == (double)1) {
                System.out.println("1");
                kernel = new Kernel(5, 5, GAUSS5x5SD1);
            }
            if(val == (double)1.5){
                System.out.println("1.5");
                kernel = new Kernel(5, 5, GAUSS5x5SD15);
            }
            if(val == (double)2){
                System.out.println("2");
                kernel = new Kernel(5, 5, GAUSS5x5SD2);
            }
            if(val == (double)2.5){
                System.out.println("2.5");
                kernel = new Kernel(5, 5, GAUSS5x5SD25);
            }
            if(val == (double)3){
                System.out.println("3");
                kernel = new Kernel(5, 5, GAUSS5x5SD3);
            }
            ConvolveOp cop = new ConvolveOp (kernel, ConvolveOp.EDGE_NO_OP, null);

            // make a copy of the buffered image
            BufferedImage newbim = new BufferedImage
                    (bim.getWidth(), bim.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
            Graphics2D big = newbim.createGraphics();
            big.drawImage (bim, 0, 0, null);

            // apply the filter the copied image
            // result goes to a filtered copy
            cop.filter(newbim, filteredbim);
            showfiltered=true;
            this.repaint();
        }

        //  show current image by a scheduled call to paint()
        public void showImage() {
            if (bim == null) return;
            showfiltered=false;
            this.repaint();
        }

        //  get a graphics context and show either filtered image or
        //  regular image
        public void paintComponent(Graphics g) {
            Graphics2D big = (Graphics2D) g;
            if (showfiltered)
                big.drawImage(filteredbim, 0, 0, this);
            else
                big.drawImage(bim, 0, 0, this);
        }
    }

}
