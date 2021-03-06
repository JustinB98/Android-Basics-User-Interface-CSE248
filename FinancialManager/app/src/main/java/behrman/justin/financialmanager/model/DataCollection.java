package behrman.justin.financialmanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class DataCollection {

    /**
     * Just all the transactions unsorted
     */
    private ArrayList<Transaction> dataList;

    /**
     * Transactions organized by date
     */
    private HashMap<Date, ArrayList<Transaction>> dataMap;

    private double total;

    public DataCollection(ArrayList<Transaction> dataList, HashMap<Date, ArrayList<Transaction>> dataMap, double total) {
        // sort the data first
        Collections.sort(dataList);
        this.dataList = dataList;
        this.dataMap = dataMap;
        this.total = total;
    }

    public ArrayList<Transaction> getDataList() {
        return dataList;
    }

    public HashMap<Date, ArrayList<Transaction>> getDataMap() {
        return dataMap;
    }

    public double getTotal() {
        return total;
    }

}
