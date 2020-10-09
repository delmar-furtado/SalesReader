/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.model;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author delmar
 */
public class Sale {    
    private Integer id;
    private Seller seller;
    private List<Item> items;

    public Sale(Integer id, Seller seller, List<Item> items) {
        this.id = id;
        this.seller = seller;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Double getTotal() {
        if (items != null) {
            return items.stream()
                        .map(Item::getValue).reduce(0.0, (accumulator, _item) -> accumulator + _item);
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "Sale{" + "id=" + id + ", seller=" + seller + ", items=" + items + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sale other = (Sale) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
