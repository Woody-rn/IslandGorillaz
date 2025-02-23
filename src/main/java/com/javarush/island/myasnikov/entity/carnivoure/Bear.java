package com.javarush.island.myasnikov.entity.carnivoure;

import com.javarush.island.myasnikov.utility.Limit;
import com.javarush.island.myasnikov.utility.OrganismTypes;

public class Bear extends Carnivore {

    public Bear(OrganismTypes type, String icon, double weight, Limit limit) {
        super(type, icon, weight, limit);
    }
}
