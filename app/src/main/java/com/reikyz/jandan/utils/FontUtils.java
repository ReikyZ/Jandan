package com.reikyz.jandan.utils;

import android.content.Context;


import com.reikyz.jandan.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by reikyZ on 16/8/31.
 */
public class FontUtils {


    boolean[][] arr;
    int all_2_4 = 2;
    int all_8_12 = 8;
    int all_12_16 = 12;
    int all_16_32 = 16;
    int all_32_128 = 32;

    public boolean[][] drawString(Context context, String str) {
        byte[] data = null;
        int[] code = null;
        int byteCount;
        int lCount;
        arr = new boolean[all_16_32][all_16_32];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < 0x80) {
                continue;
            }
            code = getByteCode(str.substring(i, i + 1));
            data = read(context, code[0], code[1]);
            byteCount = 0;
            for (int line = 0; line < all_16_32; line++) {
                lCount = 0;
                for (int k = 0; k < all_2_4; k++) {
                    for (int j = 0; j < 8; j++) {
                        if (((data[byteCount] >> (7 - j)) & 0x1) == 1) {
                            arr[line][lCount] = true;
                            System.out.print("*");
                        } else {
                            System.out.print(" ");
                            arr[line][lCount] = false;
                        }
                        lCount++;
                    }
                    byteCount++;
                }
                System.out.println();
            }
        }
        return arr;
    }

    protected byte[] read(Context context, int areaCode, int posCode) {
        byte[] data = null;
        try {
            int area = areaCode - 0xa0;
            int pos = posCode - 0xa0;
            InputStream in = context.getResources().openRawResource(R.raw.asc12);
            long offset = all_32_128 * ((area - 1) * 94 + pos - 1);
            in.skip(offset);
            data = new byte[all_32_128];
            in.read(data, 0, all_32_128);
            in.close();
        } catch (Exception ex) {
            System.err.println("SORRY,THE FILE CAN'T BE READ");
        }
        return data;

    }

    protected int[] getByteCode(String str) {
        int[] byteCode = new int[2];
        try {
            byte[] data = str.getBytes("GB2312");
            byteCode[0] = data[0] < 0 ? 256 + data[0] : data[0];
            byteCode[1] = data[1] < 0 ? 256 + data[1] : data[1];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return byteCode;
    }

    public boolean[][] show(Context context, String str) {
//        String str = "00:00";
        boolean[][] arr = new boolean[14][str.length() * 8];
        int n = str.length();

        char ch[] = new char[n];

        for (int i = 0; i < n; i++) {
            ch[i] = str.charAt(i);
        }

        byte[][] asc = new byte[n][16];

        try {
            for (int i = 0; i < n; i++) {
                InputStream file = context.getResources().openRawResource(R.raw.asc16);

                int off = ch[i] * 16;

                file.skip(off);
                file.read(asc[i]);

                file.close();

            }

            for (int i = 0; i < 14; i++) {

                for (int id = 0; id < n; id++) {

                    for (int j = 0; j < 8; j++) {

                        int bit = (asc[id][i] & (0x80 >> j)) >> (7 - j);
                        if (bit == 1) {
                            System.out.print("*");
                            arr[i][id * 8 + j] = true;

                        } else {
                            System.out.print("-");
                            arr[i][id * 8 + j] = false;
                        }
                    }
                }

                System.out.println();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arr;
    }


}
