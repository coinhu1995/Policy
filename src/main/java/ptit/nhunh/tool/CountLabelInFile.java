package ptit.nhunh.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountLabelInFile {
	public static void main(String[] args) throws FileNotFoundException{
		int label_1 = 0, label_2 = 0, label_3 = 0, label_4 = 0;
		Scanner scan = new Scanner(new File("input.train"));
		while(scan.hasNext()){
			String s = scan.nextLine();
			if(s.charAt(0) == '1'){
				label_1++;
			}
			if(s.charAt(0) == '2'){
				label_2++;
			}
			if(s.charAt(0) == '3'){
				label_3++;
			}
			if(s.charAt(0) == '4'){
				label_4++;
			}
		}
		System.out.println("Label 1: "+label_1);
		System.out.println("Label 2: "+label_2);
		System.out.println("Label 3: "+label_3);
		System.out.println("Label 4: "+label_4);
		
		scan = new Scanner(new File("input.test"));
		while(scan.hasNext()){
			String s = scan.nextLine();
			if(s.charAt(0) == '1'){
				label_1++;
			}
			if(s.charAt(0) == '2'){
				label_2++;
			}
			if(s.charAt(0) == '3'){
				label_3++;
			}
			if(s.charAt(0) == '4'){
				label_4++;
			}
		}
		System.out.println("Label 1: "+label_1);
		System.out.println("Label 2: "+label_2);
		System.out.println("Label 3: "+label_3);
		System.out.println("Label 4: "+label_4);
		scan.close();
	}
}
