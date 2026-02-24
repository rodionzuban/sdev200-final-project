package ui.elements;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class AppLabel extends Label {
    public AppLabel(String text) {
        setFont(new Font(16));
        setText(text);
    }

    public AppLabel(AppLabelType type, String text) {
        switch (type) {
            case TITLE:
                setFont(new Font(24));
                break;
            case SUBTITLE:
                setFont(new Font(20));
                break;
            default:
                setFont(new Font(18));
                break;
        }

        setText(text);
    }

    public AppLabel(AppLabelType type) {
        switch (type) {
            case TITLE:
                setFont(new Font(24));
                break;
            case SUBTITLE:
                setFont(new Font(20));
                break;
            default:
                setFont(new Font(16));
                break;
        }
    }
}
