package com.javarush.island.myasnikov.entity.herbivore;

import com.javarush.island.myasnikov.utility.Limit;
import com.javarush.island.myasnikov.utility.OrganismTypes;

public class Duck extends Herbivore {

    public Duck(OrganismTypes type, String icon, double weight, Limit limit) {
        super(type, icon, weight, limit);
    }

}
