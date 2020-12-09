package com.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaleTaxCalculation {
	static DecimalFormat df = new DecimalFormat("#0.00");
	
	static final Double saleTax = 0.1;
	static final Double importTax = 0.05;
	static final Double saleImportTax = 0.15;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter file location");
		String filename = input.next();
		List<String> list = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Paths.get(filename))) {
			list = lines.collect(Collectors.toList());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (list.size() > 0) {
			List<String> reponse = calculateSaleTax(list);
			reponse.forEach(r -> {
				System.out.println(r);
			});
		}
		
		

	}

	private static List<String> calculateSaleTax(List<String> list) {
		
		Double total = 0.0;
		Double actualTotal = 0.0;

		List<String> outputList = new ArrayList<>();
		for (String t : list) {
			int lastIndex = t.lastIndexOf(" ");
			int firstIndex = t.indexOf(" ");
			boolean calSaleTax = false;
			boolean calImportTax = false;
			boolean calSaleImportTax = false;

			if (!t.contains("book") && !t.contains("chocolate") && !t.contains("imported") && !t.contains("pill")) {
				calSaleTax = true;
			} else if (!t.contains("book") && !t.contains("chocolate") && !t.contains("pill")
					&& t.contains("imported")) {
				calSaleImportTax = true;
			} else if (t.contains("imported")
					&& (t.contains("book") || t.contains("chocolate") || t.contains("pill"))) {
				calImportTax = true;
			}

			try {
				int quantity = Integer.valueOf(t.substring(0, firstIndex));
				Double price = quantity * Double.parseDouble(t.substring(lastIndex, t.length()));
				Double priceWithTax = price;
				actualTotal = actualTotal + priceWithTax;
				if (calSaleTax) {
					priceWithTax = price + Math.round(price * saleTax*20)/20.0;
				} else if (calSaleImportTax) {
					priceWithTax = price + Math.round(price * saleImportTax*20)/20.0;
				}
				if (calImportTax) {
					priceWithTax = price + Math.round(price * importTax*20)/20.0;
				}
				total = total + priceWithTax;
				outputList.add(t.substring(0, lastIndex) + " " + df.format(priceWithTax));
			} catch (Exception e) {
				System.out.println("Input Exception" + e);
			}
		}
		Double saleTaxTotal = total - actualTotal;
		outputList.add("Sale Tax " + df.format(saleTaxTotal));
		outputList.add("Total  " + df.format(total));
		return outputList;

	}

}
