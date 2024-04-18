package com.example.a2340project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObservableTest {
    private SimpleObservable observable;
    private Observer observerA;
    private Observer observerB;

    @Before
    public void setUp() {
        observable = new SimpleObservable();
        observerA = new Observer() {
            @Override
            public void update() {
                // Mock update implementation for Observer A
            }
        };
        observerB = new Observer() {
            @Override
            public void update() {
                // Mock update implementation for Observer B
            }
        };
    }

    @Test
    public void registerObserver() {
        observable.registerObserver(observerA);
        assertEquals(1, observable.getObserverCount());
        observable.registerObserver(observerB);
        assertEquals(2, observable.getObserverCount());
    }

    @Test
    public void removeObserver() {
        observable.registerObserver(observerA);
        observable.registerObserver(observerB);
        observable.removeObserver(observerA);
        assertEquals(1, observable.getObserverCount());
        observable.removeObserver(observerB);
        assertEquals(0, observable.getObserverCount());
    }

    @Test
    public void notifyObservers() {
        final int[] count = {0};

        Observer observer = new Observer() {
            @Override
            public void update() {
                count[0]++;
            }
        };

        observable.registerObserver(observer);
        observable.notifyObservers();
        assertEquals(1, count[0]);

        observable.registerObserver(observerA);
        observable.notifyObservers();
        assertEquals(2, count[0]);
    }

    @Test
    public void testNoDuplicateRegistration() {
        observable.registerObserver(observerA);
        observable.registerObserver(observerA);
        assertEquals(1, observable.getObserverCount());
    }

    @Test
    public void testNotificationOrder() {
        List<Integer> notificationOrder = new ArrayList<>();
        Observer observer1 = () -> notificationOrder.add(1);
        Observer observer2 = () -> notificationOrder.add(2);

        observable.registerObserver(observer1);
        observable.registerObserver(observer2);
        observable.notifyObservers();

        assertEquals(Arrays.asList(1, 2), notificationOrder);
    }

    @Test
    public void testNotifyWithNoObservers() {
        try {
            observable.notifyObservers();
            assertTrue(true);
        } catch (Exception e) {
            fail("Should not throw any exceptions when no observers are registered.");
        }
    }

    @Test
    public void testRemoveNonExistentObserver() {
        observable.registerObserver(observerA);
        observable.removeObserver(observerB);
        assertEquals(1, observable.getObserverCount());
    }

    @Test
    public void testClearAllObservers() {
        observable.registerObserver(observerA);
        observable.registerObserver(observerB);
        observable.removeObserver(observerA);
        observable.removeObserver(observerB);
        assertEquals(0, observable.getObserverCount());
        observable.notifyObservers();
    }


}