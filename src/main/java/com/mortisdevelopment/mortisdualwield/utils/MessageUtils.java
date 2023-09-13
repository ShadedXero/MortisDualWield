package com.mortisdevelopment.mortisdualwield.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageUtils {

    private String message;

    public MessageUtils(String message) {
        if (message != null) {
            this.message = message;
        }else {
            this.message = " ";
        }
    }

    public static Component color(String message) {
        if (message == null) {
            return Component.text(" ");
        }
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    public Component color() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.message);
    }

    public String replace(String value, String replacement) {
        String message = this.message.replace(value, replacement);
        setMessage(message);
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
