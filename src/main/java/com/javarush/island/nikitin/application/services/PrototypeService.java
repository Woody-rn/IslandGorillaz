package com.javarush.island.nikitin.application.services;

import com.javarush.island.nikitin.application.constants.AnnotationGoal;
import com.javarush.island.nikitin.application.config.Settings;
import com.javarush.island.nikitin.application.constants.FailMessagesApp;
import com.javarush.island.nikitin.application.exception.AppException;
import com.javarush.island.nikitin.domain.api.InjectLimitData;
import com.javarush.island.nikitin.domain.entity.biota.Biota;
import com.javarush.island.nikitin.domain.entity.biota.LimitData;
import com.javarush.island.nikitin.domain.entity.biota.PreferenceMenu;
import com.javarush.island.nikitin.domain.entity.biota.Property;
import com.javarush.island.nikitin.domain.entity.map.navigation.Navigator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrototypeService {
    private final Settings settings;
    private final Map<String, Biota> cachePrototypes = new HashMap<>();

    public PrototypeService(Settings settings) {
        this.settings = settings;
    }

    public void buildBiotaInstancesForEach(Set<Class<?>> typeClasses) {
        typeClasses.forEach(this::createBiotaInstance);
    }

    public Map<String, Biota> flush() {
        return new HashMap<>(cachePrototypes);
    }

    private <T extends Biota> void createBiotaInstance(Class<?> clazz) {
        String nameCommunity = clazz.getSimpleName();
        if (!cachePrototypes.containsKey(nameCommunity)) {
            LimitData limitData = retrieve(clazz);
            Property property = settings.getPropertyByName(nameCommunity);
            PreferenceMenu preferenceMenu = settings.getPreferenceMenuByName(nameCommunity);
            T unit = newInstance(clazz, limitData, property, preferenceMenu);
            cachePrototypes.put(nameCommunity, unit);
        } else throw new AppException(FailMessagesApp.NOT_CONTAIN_THE_TARGET_CLASS);
    }

    @SuppressWarnings("unchecked")
    private <T extends Biota> T newInstance(Class<?> clazz, LimitData limitData, Property property, PreferenceMenu preferenceMenu) {
        try {
            Constructor<?> constructor = clazz.getConstructor(LimitData.class, Property.class, PreferenceMenu.class);
            return (T) constructor.newInstance(limitData, property, preferenceMenu);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new AppException(e);
        }
    }

    private LimitData retrieve(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(AnnotationGoal.INJECT_LIMIT_DATA.getValue())) {
            String nameCommunity = clazz.getSimpleName();
            return settings.getLimitDataByName(nameCommunity);
        }
        return scanClass(clazz);
    }

    private LimitData scanClass(Class<?> clazz) {
        InjectLimitData annotation = (InjectLimitData) clazz.getAnnotation(AnnotationGoal.INJECT_LIMIT_DATA.getValue());
        return new LimitData(annotation.maxWeight(),
                annotation.maxSpeed(),
                annotation.maxFoodFeed(),
                annotation.maxCountUnit(),
                annotation.dailyWeightLossPct());
    }

    public void setAttributesIntoProto(Navigator navigator, int startData) {
        if (cachePrototypes.isEmpty()) {
            throw new AppException(FailMessagesApp.CASH_PROTO_IS_EMPTY);
        }
        cachePrototypes.values().forEach(el -> {
            el.setNavigator(navigator);
            el.setCurrentDay(startData);
        });
    }
}