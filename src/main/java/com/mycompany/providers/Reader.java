/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providers;

import com.mycompany.model.Customer;
import com.mycompany.model.Item;
import com.mycompany.model.Sale;
import com.mycompany.model.Seller;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author delmar
 */
public class Reader {

    public static final String SEPARATOR_PERSON_FIELDS = "ç", SEPARATOR_ITEM = ",", SEPARATOR_ITEM_FIELDS = "-";
    public static final String SELLER = "001", CUSTOMER = "002", SALE = "003";

    /**
     * Return the results
     *
     * @param lines
     * @return
     */
    public String getOutputResult(Stream<String> lines) {
        if (lines != null) {
            Set<Seller> sellers = new HashSet<>();
            Set<Customer> customers = new HashSet<>();
            Set<Sale> sales = new HashSet<>();

            System.out.println("Reading!");

            lines.forEach(line -> {
                String[] parts = line.split(SEPARATOR_PERSON_FIELDS);

                try {
                    if (parts[0].equals(SELLER)) {
                        sellers.add(new Seller(parts[1], parts[2], Double.parseDouble(parts[3])));
                    
                    } else if (parts[0].equals(CUSTOMER)) {
                        customers.add(new Customer(parts[1], parts[2], parts[3]));
                    
                    } else if (parts[0].equals(SALE)) {
                        String[] partItems = parts[2].substring(1, parts[2].length() - 1).split(SEPARATOR_ITEM);
                        
                        List<Item> itens = Stream.of(partItems)
                                .map(item -> {
                                    String[] fields = item.split(SEPARATOR_ITEM_FIELDS);
                                    return new Item(Integer.valueOf(fields[0]), Double.parseDouble(fields[1]), Double.parseDouble(fields[2]));
                                })
                                .collect(Collectors.toList());
                        Seller sellerSale = sellers.stream().filter(seller -> seller.getName().equals(parts[3])).findAny().orElse(null);
                        
                        sales.add(new Sale(Integer.valueOf(parts[1]), sellerSale, itens));
                    }
                } catch (NumberFormatException numberFormatException) {
                    Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, "Fail converting number", numberFormatException);
                } catch (Exception ex) {
                    Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, "Fail geting info", ex);
                }
            });
            
            return "Number of sellers:" + sellers.size()
                    + "\nNumber of customers:" + customers.size()
                    + "\nSale most expensive:" + sales.stream().max(Comparator.comparing(Sale::getTotal)).get().getId()
                    + "\nWorst seller:" + sales.stream()
                            .filter(sale -> sale.getSeller() != null)
                            .collect(Collectors.groupingBy(Sale::getSeller))
                            .entrySet().stream()
                            .collect(Collectors.toMap(
                                    e -> e.getKey(),
                                    e -> e.getValue().stream().map(Sale::getTotal).reduce(0.0, (accumulator, _item) -> accumulator + _item)
                            )).entrySet().stream()
                            .min(Map.Entry.comparingByValue()).get().getKey().getName();
        }
        return "Fail reading files";
    }
}
