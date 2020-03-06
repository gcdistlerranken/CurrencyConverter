package com.google.distlergrace.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity
{
    //String Constants
    final String NO  = "NO ";
    final String NEG = "NEGATIVE ";
    final String IP  = "INPUT PROVIDED FOR AMOUNT TO CONVERT";
    final String NC  = "NO CONVERTING RATE TO SELF RATE";

    //Currency Symbols
    final String DOLLAR = "\u0024";
    final String EURO   = "\u20ac";
    final String POUND  = "\u00a3";
    final String FRANC  = "\u20A3";
    final String YEN    = "\u00A5";

    //Conversion Constants
    //  US DOLLAR CONVERSIONS
    final double USD_TO_EEURO   = 0.92;
    final double USD_TO_BPOUND  = 0.77;
    final double USD_TO_SFRANC  = 0.98;
    final double USD_TO_AUSTD   = 1.49;
    final double USD_TO_CANAD   = 1.33;
    final double USD_TO_NEWZD   = 1.55;
    final double USD_TO_JYEN    = 109.79;

    //  EUROPEAN EURO TO CONVERSIONS
    final double EEURO_TO_USD     = 1.08;
    final double EEURO_TO_BPOUND  = 0.83;
    final double EEURO_TO_SFRANC  = 1.06;
    final double EEURO_TO_AUSTD   = 1.61;
    final double EEURO_TO_CANAD   = 1.43;
    final double EEURO_TO_NEWZD   = 1.68;
    final double EEURO_TO_JYEN    = 119.06;

    //  BRITISH POUND TO CONVERSIONS
    final double BPOUND_TO_USD     = 1.30;
    final double BPOUND_TO_EEURO   = 1.20;
    final double BPOUND_TO_SFRANC  = 1.28;
    final double BPOUND_TO_AUSTD   = 1.94;
    final double BPOUND_TO_CANAD   = 1.72;
    final double BPOUND_TO_NEWZD   = 2.02;
    final double BPOUND_TO_JYEN    = 143.08;

    //  SWISS FRANC TO CONVERSIONS
    final double SFRANC_TO_USD     = 1.02;
    final double SFRANC_TO_EEURO   = 0.94;
    final double SFRANC_TO_BPOUND  = 0.78;
    final double SFRANC_TO_AUSTD   = 1.52;
    final double SFRANC_TO_CANAD   = 1.35;
    final double SFRANC_TO_NEWZD   = 1.58;
    final double SFRANC_TO_JYEN    = 111.91;

    //  AUSTRALIAN DOLLAR TO CONVERSIONS
    final double AUSTD_TO_USD     = 0.67;
    final double AUSTD_TO_EEURO   = 0.62;
    final double AUSTD_TO_BPOUND  = 0.52;
    final double AUSTD_TO_SFRANC  = 0.66;
    final double AUSTD_TO_CANAD   = 0.89;
    final double AUSTD_TO_NEWZD   = 1.04;
    final double AUSTD_TO_JYEN    = 73.83;

    //  CANADIAN DOLLAR TO CONVERSIONS
    final double CANAD_TO_USD     = 0.76;
    final double CANAD_TO_EEURO   = 0.70;
    final double CANAD_TO_BPOUND  = 0.58;
    final double CANAD_TO_SFRANC  = 0.74;
    final double CANAD_TO_AUSTD   = 1.12;
    final double CANAD_TO_NEWZD   = 1.17;
    final double CANAD_TO_JYEN    = 83.00;

    //  NEW ZEALAND DOLLAR TO CONVERSIONS
    final double NEWZD_TO_USD     = 0.64;
    final double NEWZD_TO_EEURO   = 0.59;
    final double NEWZD_TO_BPOUND  = 0.49;
    final double NEWZD_TO_SFRANC  = 0.63;
    final double NEWZD_TO_AUSTD   = 0.96;
    final double NEWZD_TO_CANAD   = 0.85;
    final double NEWZD_TO_JYEN    = 70.67;

    //  JAPANESE YEN TO CONVERSIONS
    final double JYEN_TO_USD     = 0.009;
    final double JYEN_TO_EEURO   = 0.084;
    final double JYEN_TO_BPOUND  = 0.007;
    final double JYEN_TO_SFRANC  = 0.009;
    final double JYEN_TO_AUSTD   = 0.014;
    final double JYEN_TO_CANAD   = 0.012;
    final double JYEN_TO_NEWZD   = 0.014;

    //Widgets
    EditText    editTextInput;
    Spinner     spinnerConvertFrom;
    Spinner     spinnerConvertTo;
    Button      buttonConvert;
    Toast       t;

    //Non-Widget Variables
    boolean     keepGoing;
    double      inputtedAmount;
    double      conversionRate;
    double      convertedAmount;
    String      convertedFrom;
    String      convertedTo;
    int         from;
    int         to;
    DecimalFormat df = new DecimalFormat("###,###,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerConvertFrom  = findViewById(R.id.spinnerConvertFrom);
        spinnerConvertTo    = findViewById(R.id.spinnerConvertTo);
        editTextInput       = findViewById(R.id.editTextInput);
        buttonConvert       = findViewById(R.id.buttonConvert);

        keepGoing       = true;
        inputtedAmount  = 0.0;
        conversionRate  = 0.0;
        convertedAmount = 0.0;
        from            = -1;
        to              = -1;
        convertedFrom   = "";
        convertedTo     = "";

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        this, R.array.currencies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set 'adapter' To Spinners
        spinnerConvertFrom.setAdapter(adapter);
        spinnerConvertTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                keepGoing = checkForNoInput();

                if (keepGoing)
                {
                    doTheConversion();
                }
            }
        });
    }

    private boolean checkForNoInput()
    {
        //Check For No Input
        if (editTextInput.getText().toString().isEmpty())
        {
            t = Toast.makeText(getApplicationContext(), NO + IP, Toast.LENGTH_LONG);
            t.show();

            return false;
        }
        //Amount Provided
        else
        {
            inputtedAmount = Double.valueOf(editTextInput.getText().toString());

            //First Check For Negative Input
            if (inputtedAmount < 0)
            {
                t = Toast.makeText(getApplicationContext(), NEG + IP, Toast.LENGTH_LONG);
                t.show();

                return false;
            }

            return true;
        }
    }

    private void doTheConversion()
    {
        from = spinnerConvertFrom.getSelectedItemPosition();
        to   = spinnerConvertTo.getSelectedItemPosition();

        switch (from)
        {
            case 0:
                convertedFrom = " US Dollars";
                switch (to)
                {
                    case 0:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 1:
                        conversionRate = USD_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = USD_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = USD_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = USD_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = USD_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = USD_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = USD_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 1:
                convertedFrom = " European Euros";
                switch (to)
                {
                    case 0:
                        conversionRate = EEURO_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 2:
                        conversionRate = EEURO_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = EEURO_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = EEURO_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = EEURO_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = EEURO_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = EEURO_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 2:
                convertedFrom = " British Pounds";
                switch (to)
                {
                    case 0:
                        conversionRate = BPOUND_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = BPOUND_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 3:
                        conversionRate = BPOUND_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = BPOUND_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = BPOUND_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = BPOUND_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = BPOUND_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 3:
                convertedFrom = " Swiss Francs";
                switch (to)
                {
                    case 0:
                        conversionRate = SFRANC_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = SFRANC_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = SFRANC_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 4:
                        conversionRate = SFRANC_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = SFRANC_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = SFRANC_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = SFRANC_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 4:
                convertedFrom = " Australian Dollars";
                switch (to)
                {
                    case 0:
                        conversionRate = AUSTD_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = AUSTD_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = AUSTD_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = AUSTD_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 5:
                        conversionRate = AUSTD_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = AUSTD_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = AUSTD_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 5:
                convertedFrom = " Canadian Dollars";
                switch (to)
                {
                    case 0:
                        conversionRate = CANAD_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = CANAD_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = CANAD_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = CANAD_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = CANAD_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 6:
                        conversionRate = CANAD_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = CANAD_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 6:
                convertedFrom = " New Zealand Dollars";
                switch (to)
                {
                    case 0:
                        conversionRate = NEWZD_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = NEWZD_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = NEWZD_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = NEWZD_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = NEWZD_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = NEWZD_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;

                    case 7:
                        conversionRate = NEWZD_TO_JYEN;
                        convertedTo    = toType(7);
                        break;
                }

                break;

            case 7:
                convertedFrom = " Japanese Yen";
                switch (to)
                {
                    case 0:
                        conversionRate = JYEN_TO_USD;
                        convertedTo    = toType(0);
                        break;

                    case 1:
                        conversionRate = JYEN_TO_EEURO;
                        convertedTo    = toType(1);
                        break;

                    case 2:
                        conversionRate = JYEN_TO_BPOUND;
                        convertedTo    = toType(2);
                        break;

                    case 3:
                        conversionRate = JYEN_TO_SFRANC;
                        convertedTo    = toType(3);
                        break;

                    case 4:
                        conversionRate = JYEN_TO_AUSTD;
                        convertedTo    = toType(4);
                        break;

                    case 5:
                        conversionRate = JYEN_TO_CANAD;
                        convertedTo    = toType(5);
                        break;

                    case 6:
                        conversionRate = JYEN_TO_NEWZD;
                        convertedTo    = toType(6);
                        break;

                    case 7:
                        conversionRate = 0;

                        t = Toast.makeText(getApplicationContext(), NC, Toast.LENGTH_LONG);
                        t.show();

                        break;
                }

                break;
        }

        convertedAmount = inputtedAmount * conversionRate;
        t = Toast.makeText(getApplicationContext(),
                       df.format(inputtedAmount).toString() + convertedFrom + " = " +
                           df.format(convertedAmount).toString() + convertedTo,
                           Toast.LENGTH_LONG);
        t.show();
    }

    private String toType(int fromType)
    {
        String retVal = "";

        switch (fromType)
        {
            case 0:
                retVal = " US Dollars";
                break;

            case 1:
                retVal = " European Euros";
                break;

            case 2:
                retVal = " British Pounds";
                break;

            case 3:
                retVal = " Swiss Francs";
                break;

            case 4:
                retVal = " Australian Dollars";
                break;

            case 5:
                retVal = " Canadian Dollars";
                break;

            case 6:
                retVal = " New Zealand Dollars";
                break;

            case 7:
                retVal = " Japanese Yen";
                break;
        }

        return retVal;
    }
}
