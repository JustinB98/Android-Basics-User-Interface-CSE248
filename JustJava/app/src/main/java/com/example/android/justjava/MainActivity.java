package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int price = calculatePrice();
        String priceMessage = createOrderSummary(price);
        displayMessage(priceMessage);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void increment(View view) {
        displayQuantity(++quantity);
    }

    public void decrement(View view) {
        displayQuantity(--quantity);
    }

    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    public int calculatePrice(int quantity) {
        return calculatePrice(quantity, 5);
    }

    public int calculatePrice(int quantity, int pricePerCup) {
        return quantity * pricePerCup;
    }

    public int calculatePrice() {
        return calculatePrice(quantity);
    }

    private String createOrderSummary(int price) {
        String priceMessage = "Name: NAME";
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal $" + price;
        priceMessage += "\nThank you!";
        return priceMessage;
    }

}

/*
        Intent intent = new Intent(Intent.ACTION_SENDTO); // gets mail apps
        intent.setData(Uri.parse("mailto:")); // ??
        intent.putExtra(Intent.EXTRA_SUBJECT, "VALUE"); // sets the subject
        intent.putExtra(Intent.EXTRA_TEXT, "MESSAGE"); // sets the body of email
        if (intent.resolveActivity(getPackageManager()) != null)  { // is everything good to go?
            startActivity(intent); // open up default mailing app
        }
 */