package com.company;

import java.awt.*;

public class Utility {
    DataManager dm;

    public Utility(DataManager dm){
        this.dm = dm;
    }

    void changeBrightness(float x, boolean increasing){
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
               if((dm.img.getRaster().getSample(i,j,0)+x)>0 && (dm.img.getRaster().getSample(i,j,0)+x)<255)
                    dm.img.getRaster().setSample(i,j,0,(dm.img.getRaster().getSample(i,j,0)+x));
               else {
                   if (increasing==true)
                    dm.img.getRaster().setSample(i, j, 0, 255);
                   else
                       dm.img.getRaster().setSample(i, j, 0, 0);
               }
            }
        }
    }


//x - prog binaryzacji, b - color channel (=0 dla grayscale 8bit img)
// jesli wartosc jest <= to s=0 (czarny), a jak nie to s=255 (bialy)
    void tresholding(int x){
       for(int i=0; i<dm.img.getWidth(); i++){
           for(int j=0; j<dm.img.getHeight(); j++){
               if(dm.img.getRaster().getSample(i,j,0)<=x)
                   dm.img.getRaster().setSample(i,j,0,0);
               else
                   dm.img.getRaster().setSample(i,j,0,255);
           }
       }
    }

    int[] neighbours(int width, int height) {
        int[] neighbours = new int[8];
        int k=0;
        for (int i = width-1; i <= width+1; i++) {
            for (int j = height-1; j <= height+1; j++) {

                if(i==width && j==height)
                    continue;
                else{
                    neighbours[k]=dm.pixels[i][j];
                    k++;
                }
            }
        }
        return neighbours;
    }

    void lowPassing(Rectangle rect) {
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
                for(int i=1; i<dm.img.getWidth()-1; i++){
                    for(int j=1; j<dm.img.getHeight()-1; j++){
                int middle=dm.img.getRaster().getSample(i,j,0)*(1/9); // 1/9 -->normalizacja

                int[] localNeighbor= neighbours(i,j);

                for(int k=0;k< localNeighbor.length;k++) {
                    middle = middle+(localNeighbor[k] * 1/9); // tez
                }
                if(middle>255)
                    dm.img.getRaster().setSample(i,j,0,255);
                else
                    dm.img.getRaster().setSample(i,j,0,middle);
            }
        }
    }


    void highPassing(Rectangle rect) {
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
        for(int i=1; i<dm.img.getWidth()-1; i++){
            for(int j=1; j<dm.img.getHeight()-1; j++){
                int middle=dm.img.getRaster().getSample(i,j,0)*9;

                int[] localNeighbor= neighbours(i,j);

                for(int k=0;k< localNeighbor.length;k++) {
                    middle = middle+(localNeighbor[k] * (-1));
                }

                if(middle<=0)
                    dm.img.getRaster().setSample(i,j,0,0);
                else if(middle>255)
                    dm.img.getRaster().setSample(i,j,0,255);
                else
                    dm.img.getRaster().setSample(i,j,0,middle);
            }
        }
    }

    void gauss(Rectangle rect) {
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
        for(int i=1; i<dm.img.getWidth()-1; i++){
            for(int j=1; j<dm.img.getHeight()-1; j++){
                int middle=dm.img.getRaster().getSample(i,j,0)*16;

                int[] localNeighbor= neighbours(i,j);

                for(int k=0;k< localNeighbor.length;k++) {
                    if(k==0||k==2||k==5||k==7)
                        middle = middle+((localNeighbor[k] * 1));
                    else
                        middle = middle+((localNeighbor[k] * 2));
                }
                middle=middle/32;

                if(middle<0)
                    dm.img.getRaster().setSample(i,j,0,0);
                else if(middle>255)
                    dm.img.getRaster().setSample(i,j,0,255);
                else
                    dm.img.getRaster().setSample(i,j,0,middle);
            }
        }
    }

    void dilation(Rectangle rect){
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
        for(int i=1; i<dm.img.getWidth()-1; i++){
            for(int j=1; j<dm.img.getHeight()-1; j++){
                int[] localNeighbor = neighbours(i, j);
                for (int k = 0; k < localNeighbor.length; k++) {
                    if (localNeighbor[k] == 0) {
                        dm.img.getRaster().setSample(i, j, 0, 0);
                        break;
                    }
                }
            }
        }
    }

    void erosion(Rectangle rect) {
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
        for(int i=1; i<dm.img.getWidth()-1; i++){
            for(int j=1; j<dm.img.getHeight()-1; j++){
                int[] localNeighbor = neighbours(i, j);
                for (int k = 0; k < localNeighbor.length; k++) {
                    if (localNeighbor[k] == 255) {
                        dm.img.getRaster().setSample(i, j, 0, 255);
                        break;
                    }
                }
            }
        }
    }

    void constructQT(){
        int width = 600;
        int height = 330;

        Rectangle initialRectangle = new Rectangle(new Point(0,0), width, height);

        dm.rootNode = new Node(initialRectangle, null, null, null, null);
    }

}
