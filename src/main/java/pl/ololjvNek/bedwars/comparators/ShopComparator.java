package pl.ololjvNek.bedwars.comparators;

import pl.ololjvNek.bedwars.data.Shop;

import java.util.Comparator;

public class ShopComparator implements Comparator<Shop> {


    @Override
    public int compare(Shop o1, Shop o2) {
        return o1.getShopName().compareTo(o2.getShopName());
    }
}
