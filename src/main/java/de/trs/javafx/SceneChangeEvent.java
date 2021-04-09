package de.trs.javafx;

import javafx.event.Event;
import javafx.event.EventType;

public class SceneChangeEvent extends Event {

    public SceneChangeEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public static final EventType<SceneChangeEvent> GO_TO_CREATE_SCENE = new EventType<>("GO_TO_CREATE_SCENE");

    public static final EventType<SceneChangeEvent> GO_TO_OVERVIEW_SCENE = new EventType<>("GO_TO_OVERVIEW_SCENE");

}
