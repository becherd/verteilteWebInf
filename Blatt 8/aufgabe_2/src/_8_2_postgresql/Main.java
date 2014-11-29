package _8_2_postgresql;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		 
		int konto1;
		int konto2;
		double amount;
		 
		System.out.println("Geben Sie die Kontonummer der ersten Bank ein."); 
		konto1 = scan.nextInt();
		System.out.println("Geben Sie die Kontonummer der zweiten Bank ein."); 
		konto2 = scan.nextInt();
		System.out.println("Welcher Betrag soll von Bank 1 zu Bank 2 gebucht werden? (auch negativ möglich)"); 
		amount = scan.nextDouble();
		scan.close();
		
		//von Bank 1 zu Bank 2 buchen (auch negativ möglich)
		Koordinator kor = new Koordinator(konto1, konto2, amount);
		try {
			kor.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
