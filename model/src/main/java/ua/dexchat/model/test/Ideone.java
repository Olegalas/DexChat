package ua.dexchat.model.test;

/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Ideone
{
    interface A{

        A getNext();
        void setNext(A next);
        void show(int n);

        public class AI implements A{
            AI next;
            String id;

            @Override
            public A getNext(){
                return next;
            }

            @Override
            public void setNext(A next){
                this.next=(AI)next;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public AI(String id, A next){
                this.next=(AI)next;
                this.id=id;
            }

            public void show(int n){
                System.out.print("" + n + ":" + id);
                if (null != next){
                    next.show(n+1);
                }
                System.out.println();
            }

        }
    }

    public static void main (String[] args) throws java.lang.Exception
    {
        A.AI start = new A.AI("forth", null);
        start = new A.AI("third", start);
        start = new A.AI("second", start);
        start = new A.AI("first", start);
        start.show(1);
        // your code goes here

        invert(start);

        System.out.println();
        start.show(1);

    }

    public static void swapFirstOnLust(A a){

        if(a.getNext() != null){
            swapFirstOnLust(a.getNext());
            swap(a);
        }
    }

    public static void invert(A a){


        swapFirstOnLust(a);

        if(a.getNext() != null){
            invert(a.getNext());
        }


    }

    public static void swap(A a){

        if(a.getNext().getNext() == null){

            A lust = a;
            a = a.getNext();
            a.setNext(lust);
            lust.setNext(null);

        }

        A lust = a;
        A lustNext = a.getNext();

        a = a.getNext();
        a.setNext(lustNext.getNext());
        a.setNext(lust);

    }
}