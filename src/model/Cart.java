package model;

import java.util.*;

public class Cart {

    private Map<String, CartItem> items = new HashMap<>();

    public void addItem(Product product) {
        String id = product.getItemID();
        if (items.containsKey(id)) {
            items.get(id).setQuantity(items.get(id).getQuantity() + 1);
        } else {
            items.put(id, new CartItem(product));
        }
    }

    public void updateQuantity(String itemID, int newQty) {
        if (items.containsKey(itemID)) {
            if (newQty <= 0) {
                items.remove(itemID);
            } else {
                items.get(itemID).setQuantity(newQty);
            }
        }
    }

    public void removeItem(String itemID) {
        items.remove(itemID);
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public double getTotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity(){
        int total = 0;
        for (CartItem item : items.values()){
            total += item.getQuantity();
        }
        return total;
    }
}
