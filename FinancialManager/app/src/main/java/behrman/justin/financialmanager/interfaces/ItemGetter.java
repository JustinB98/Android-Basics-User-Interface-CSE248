package behrman.justin.financialmanager.interfaces;

public interface ItemGetter<T> {

    T getItem(int position);

    void removeItem(T object);

}
