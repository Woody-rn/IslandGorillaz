package com.javarush.island.arsinov.model.animals.carnivores;

import com.javarush.island.arsinov.model.Cell;
import com.javarush.island.arsinov.model.animals.Animal;
import com.javarush.island.arsinov.model.animals.AnimalAttributes;
import com.javarush.island.arsinov.model.animals.AnimalParameters;
import com.javarush.island.arsinov.model.animals.AnimalType;

import java.util.List;

public class Fox extends Carnivore {

    private static final AnimalAttributes ATTRIBUTES = AnimalParameters.getAttributes(AnimalType.BOA);

    public Fox(int x, int y, AnimalAttributes attributes) {
        super("Fox", x, y, attributes);
    }


    @Override
    protected Carnivore createOffspring() {
        return new Fox(getX(), getY(), ATTRIBUTES);
    }

    @Override
    public void performActions(Cell currentCell, List<Animal> animalsInCell) {
        hunt(currentCell);
        move(currentCell);
        reproduce(currentCell);
    }
}
