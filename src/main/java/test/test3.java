package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class test3 {
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(new File("temp.txt"));
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File("temp.txt")), StandardCharsets.UTF_8));
		String s = "";
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
	}
}
