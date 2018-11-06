package behrman.justin.financialmanager.model;

public interface ItemGetter<T> {

    T getItem(int position);

    void removeItem(T object);

}
