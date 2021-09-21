package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //1
        int[] a = {18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5};
        //2
        double[] x = new double[18];
        final double UP_LIMIT = 15.0;
        final double DOWN_LIMIT = -4.0;
        for(int i=0; i<16; ++i){
            x[i] = Math.random()*(UP_LIMIT - DOWN_LIMIT) + DOWN_LIMIT;
        }
        //3 -- массив a уже определен в области видимости,
        //поэтому новый массив назовем a2 так же как и x -> x2
        double[][] a2 = new double[14][18];
        int[] nums = {5, 8, 11, 12, 13, 15, 16};
        for(int i=0; i<14; ++i){
            for(int j=0; j<18; ++j){
                double x2 = x[j];
                if(a[i] == 7){
                    a2[i][j] = Math.cos(
                                        Math.exp(
                                                Math.log(
                                                        Math.abs(x2)
                                                )
                                        )
                    );
                }
                else if(Arrays.asList(nums).contains(a[i])){
                    a2[i][j] = Math.sin(
                                        Math.exp(
                                                Math.pow(((double)1/4)/x2, 3)
                                        )
                    );

                }
                else{
                    a2[i][j] = Math.log(
                                        Math.exp(
                                                Math.asin(
                                                        Math.pow((x2+1)/18, 2)
                                                )
                                        )
                    );
                }
                System.out.printf("%.2f ", a2[i][j]);

                
            }
            System.out.println("\n");
        }


    }
}
