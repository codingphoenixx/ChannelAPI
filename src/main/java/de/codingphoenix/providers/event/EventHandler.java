package de.codingphoenix.providers.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventHandler {

    private List<EventListener> listeners = new ArrayList<>();

    public void registerEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void unregisterEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    public <T extends Event> T triggerEvent(T event) {
        System.out.printf("Triggering event: %s\n", event);
        for (EventListener<? extends Event> listener : listeners) {
            System.out.println("+ Found listener: " + listener);
            System.out.println("+ Handling event in Listener: " + listener.getClass());
            if (listenerAcceptsEvent(listener, event.getClass())) {
                System.out.println("+ Listener accepts event.");
                @SuppressWarnings("unchecked")
                EventListener<T> typedListener = (EventListener<T>) listener;
                typedListener.handleEvent(event);
            } else {
                System.out.println("+ Listener does not accept event.");
            }
        }
        return event;
    }

    private boolean listenerAcceptsEvent(EventListener<?> listener, Class<? extends Event> eventClass) {
        System.out.println(listener.getClass().getGenericInterfaces().length + " interfaces found. " + Arrays.toString(listener.getClass().getGenericInterfaces()));
        Type[] interfaceTypes = listener.getClass().getGenericInterfaces();
        for (Type interfaceType : interfaceTypes) {
            System.out.println(interfaceType);
            if (interfaceType instanceof ParameterizedType parameterizedType) {
                Type[] typeArgs = parameterizedType.getActualTypeArguments();
                System.out.println("Found ParameterizedType: " + Arrays.toString(typeArgs));
                for (Type typeArg : typeArgs) {
                    if (typeArg instanceof Class) {
                        Class<?> typeClass = (Class<?>) typeArg;
                        if (typeClass.isAssignableFrom(eventClass) || eventClass.isAssignableFrom(typeClass)) {
                            return true;
                        }
                    }
                }
            }else System.out.println("No ParameterizedType found.");
        }
        return false;
    }

    private List<Class<?>> getAllInterfacesFromClass(Class<?> clazz) {
        List<Class<?>> list = new ArrayList<>(List.of(clazz.getInterfaces()));
        if (clazz.getSuperclass() != null)
            list.add(clazz.getSuperclass());
        return list;
    }
}


