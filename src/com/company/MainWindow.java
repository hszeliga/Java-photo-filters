package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainWindow extends JFrame{
    private DataManager dm;
    private JPanel mainPanel;
    private JCanvasPanel canvasPanel;
    private JPanel buttonPanel;

    private JLabel brightnessLabel;
    private JSpinner brightnessSpinner;
    private JButton increaseBrightnessButton;
    private JButton decreaseBrightnessButton;

    private JLabel tresholdLabel;
    private JSpinner tresholdSpinner;
    private JButton tresholdButton;

    private JLabel gap;
    private JButton lowPassFilterButton;
    private JButton highPassFilterButton;
    private JButton gaussFilterButton;
    private JButton dilationButton;
    private JButton erosionButton;
    private JButton openingButton;
    private JButton closingButton;

    private JLabel otherOptionsLabel;
    private JButton exportButton;
    private JButton reverseChangesButton;




    public MainWindow (String title) {
        super(title);
        dm = new DataManager();
        Utility util = new Utility(dm);

        try {
            BufferedImage bg = ImageIO.read(new File("image.bmp"));
            dm.img = bg;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        brightnessLabel = new JLabel("Type brightness change:");
        SpinnerNumberModel brightnessModel = new SpinnerNumberModel(10, 0, 500, 10);
        brightnessSpinner = new JSpinner(brightnessModel);
        increaseBrightnessButton  = new JButton("Increase Brightness");
        decreaseBrightnessButton  = new JButton("Decrease Brightness");

        tresholdLabel = new JLabel("Type binarization level [0-255]: ");
        SpinnerNumberModel binarizationModel = new SpinnerNumberModel(100, 0, 255, 1);
        tresholdSpinner = new JSpinner(binarizationModel);
        tresholdButton = new JButton("Treshold");

        gap=new JLabel("");
        lowPassFilterButton = new JButton("Low Pass filter");
        highPassFilterButton = new JButton("High Pass filter");
        gaussFilterButton = new JButton("Gauss filter");
        dilationButton = new JButton("Dilation");
        erosionButton = new JButton("Erosion");
        openingButton = new JButton("Opening");
        closingButton = new JButton("Closing");

        otherOptionsLabel = new JLabel("Other options: ");
        exportButton = new JButton("Export image");
        reverseChangesButton = new JButton("Reverse changes");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(18, 1));
        buttonPanel.setBorder(new EmptyBorder(0,10, 0, 0));

        buttonPanel.add(brightnessLabel);
        buttonPanel.add(brightnessSpinner);
        buttonPanel.add(increaseBrightnessButton);
        buttonPanel.add(decreaseBrightnessButton);

        buttonPanel.add(tresholdLabel);
        buttonPanel.add(tresholdSpinner);
        buttonPanel.add(tresholdButton);

        buttonPanel.add(gap);
        buttonPanel.add(lowPassFilterButton);
        buttonPanel.add(highPassFilterButton);
        buttonPanel.add(gaussFilterButton);
        buttonPanel.add(dilationButton);
        buttonPanel.add(erosionButton);
        buttonPanel.add(openingButton);
        buttonPanel.add(closingButton);

        buttonPanel.add(otherOptionsLabel);
        buttonPanel.add(exportButton);
        buttonPanel.add(reverseChangesButton);


        canvasPanel = new JCanvasPanel(dm);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10,10,10));
        mainPanel.add(BorderLayout.CENTER, canvasPanel);
        mainPanel.add(BorderLayout.EAST, buttonPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setSize(new Dimension(750, 384));
        this.setLocationRelativeTo(null);


        increaseBrightnessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.changeBrightness((Integer) brightnessSpinner.getValue(),true);
                canvasPanel.repaint();
            }
        });

        decreaseBrightnessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.changeBrightness(((Integer) brightnessSpinner.getValue())*(-1),false);
                canvasPanel.repaint();
            }
        });

        tresholdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.tresholding((Integer) tresholdSpinner.getValue());
                canvasPanel.repaint();}
        });

        lowPassFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.lowPassing(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        highPassFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.highPassing(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        gaussFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.gauss(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        dilationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.tresholding((Integer) tresholdSpinner.getValue());
                util.dilation(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        erosionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.tresholding((Integer) tresholdSpinner.getValue());
                util.erosion(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        openingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.tresholding((Integer) tresholdSpinner.getValue());
                util.erosion(dm.rootNode.rect);
                util.dilation(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        closingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.tresholding((Integer) tresholdSpinner.getValue());
                util.dilation(dm.rootNode.rect);
                util.erosion(dm.rootNode.rect);
                canvasPanel.repaint();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dm.choice = 2;
                canvasPanel.repaint();
            }
        });

        reverseChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedImage bg = ImageIO.read(new File("image.bmp"));
                    dm.img = bg;
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                canvasPanel.repaint();
            }
        });

    }

    public static void main(String[] args){
        MainWindow mw = new MainWindow("App");
        mw.setVisible(true);
        mw.canvasPanel.repaint();
        Utility util = new Utility(mw.dm);
        util.constructQT();
    }
}
