package edu.louisiana.cacs;

import java.io.IOException;

import edu.louisiana.cacs.csce450GProject.Parser;

public class Main{
    public static void main(String[] args){
        System.out.println("Hello World from Main");
        try {
			Parser.parse("data/sample.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}