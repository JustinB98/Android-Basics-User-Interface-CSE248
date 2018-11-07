package behrman.justin.financialmanager.model;

public class BooleanProperty {

    private boolean bool;
    private BooleanListener listener;

    public BooleanProperty(boolean initialValue) {
        bool = initialValue;
    }

    public void addListener(BooleanListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public void setValue(boolean value) {
        this.bool = value;
        notifyListener();
    }

    public void notifyListener() {
        if (listener != null) {
            listener.onChange(bool);
        }
    }

    public void removeListener() {
        listener = null;
    }

    public boolean getValue() {
        return bool;
    }

    public interface BooleanListener {
        void onChange(boolean newValue);
    }

}
