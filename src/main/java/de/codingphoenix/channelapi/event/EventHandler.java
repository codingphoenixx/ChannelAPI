package de.codingphoenix.channelapi.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class EventHandler {

    private HashMap<EventListener, Class<? extends Event>> listeners = new HashMap<>();

    public void registerEventListener(EventListener listener) {
        var interfaceTypes = listener.getClass().getGenericInterfaces();

        for (Type interfaceType : interfaceTypes) {
            if (interfaceType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
                Type type = parameterizedType.getActualTypeArguments()[0];
                listeners.put(listener, (Class<? extends Event>) type);
                return;
            }
        }
        throw new UnsupportedOperationException("No generic type provided. Maybe use no lambda or use other registerEventListener method?");
    }


    public <T extends Event> void registerEventListener(Class<T> eventClass, EventListener<T> listener) {
        listeners.put(listener, eventClass);
    }

    public void unregisterEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    public <T extends Event> T triggerEvent(T event) {
        listeners.forEach((eventListener, eventClass) -> {
            if (!eventClass.isAssignableFrom(event.getClass())) {
                return;
            }
            eventListener.handleEvent(event);
        });
        return event;
    }


}