package org.starrier.imperator.content.dto;

import lombok.Synchronized;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author starrier
 * @date 2021/12/24
 */
public class Test {

    static class Anamial implements Runnable {
        private volatile int min;
        public ArrayList arr;

        Anamial() {
            System.out.println("init construct");
            min = 0;
            arr = new ArrayList();
        }



        public void run() {
            try {
                synchronized (this){
                    System.out.println(Thread.currentThread().getName() + "  start run and ");
                    System.out.println(Thread.currentThread().getName() + " min before is" + min);
                    Map map = new HashMap();
                    map.put("id", min);
                    map.put("arr", arr);
                    arr.add(map.toString());

                    min = min + 1;
                    System.out.println(Thread.currentThread().getName() + " min after is" + min);
                }
                System.out.println(Thread.currentThread().getName() +"=------ " +arr.toString());
                System.out.println(Thread.currentThread().getName() + " min " + min);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        Anamial anamial = new Anamial();
        Thread thread1 = new Thread(anamial, "anamial1");
        Thread thread2 = new Thread(anamial, "anamial2");
        Thread thread3 = new Thread(anamial, "anamial3");
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
