package com.example.cardformlib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.cardformlib.OnCardFormFieldFocusedListener;
import com.example.cardformlib.OnCardFormSubmitListener;
import com.example.cardformlib.OnCardFormValidListener;
import com.example.cardformlib.R;
import com.example.cardformlib.utils.CardType;


public class CardForm extends LinearLayout implements
        CardEditText.OnCardTypeChangedListener, OnFocusChangeListener, OnClickListener, OnEditorActionListener,
        TextWatcher {

    private CardEditText mCardNumber;
    private MonthYearEditText mExpirationView;
    private CvvEditText mCvvView;

    private boolean mCardNumberRequired;
    private boolean mExpirationRequired;
    private boolean mCvvRequired;

    private boolean mValid = false;

    private OnCardFormValidListener mOnCardFormValidListener;
    private OnCardFormSubmitListener mOnCardFormSubmitListener;
    private OnCardFormFieldFocusedListener mOnCardFormFieldFocusedListener;

    public CardForm(Context context) {
        super(context);
        init();
    }

    public CardForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public CardForm(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.bt_card_form_fields, this);

        setVisibility(GONE);

        mCardNumber = (CardEditText) findViewById(R.id.bt_card_form_card_number);
        mExpirationView = (MonthYearEditText) findViewById(R.id.bt_card_form_expiration);
        mCvvView = (CvvEditText) findViewById(R.id.bt_card_form_cvv);

        mCardNumber.setOnFocusChangeListener(this);
        mExpirationView.setOnFocusChangeListener(this);
        mCvvView.setOnFocusChangeListener(this);

        mCardNumber.setOnClickListener(this);
        mExpirationView.setOnClickListener(this);
        mCvvView.setOnClickListener(this);

        mCardNumber.setOnCardTypeChangedListener(this);
    }

    public void setRequiredFields(boolean cardNumberRequired, boolean expirationRequired,
                                  boolean cvvRequired, String imeActionLabel) {

        mCardNumberRequired = cardNumberRequired;
        mExpirationRequired = expirationRequired;
        mCvvRequired = cvvRequired;

        if (mCardNumberRequired) {
            mCardNumber.addTextChangedListener(this);

            if (mExpirationRequired) {
                mCardNumber.setNextFocusDownId(mExpirationView.getId());
            } else if (mCvvRequired) {
                mCardNumber.setNextFocusDownId(mCvvView.getId());
            }
        } else {
            mCardNumber.setVisibility(View.GONE);
        }

        if (mExpirationRequired) {
            mExpirationView.addTextChangedListener(this);

            if (mCvvRequired) {
                mExpirationView.setNextFocusDownId(mCvvView.getId());
            }
        } else {
            mExpirationView.setVisibility(View.GONE);
        }

        if (mCvvRequired) {
            mExpirationView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
            setIMEOptionsForLastEditTestField(mExpirationView, imeActionLabel);
        }

        if (mCvvRequired) {
            mCvvView.addTextChangedListener(this);

        } else {
            mCvvView.setVisibility(View.GONE);
        }

        mCardNumber.setOnCardTypeChangedListener(this);

        setVisibility(VISIBLE);
    }

    private void setIMEOptionsForLastEditTestField(EditText editText, String imeActionLabel) {
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setImeActionLabel(imeActionLabel, EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(this);
    }

    public void setOnCardFormValidListener(OnCardFormValidListener listener) {
        mOnCardFormValidListener = listener;
    }

    public void setOnCardFormSubmitListener(OnCardFormSubmitListener listener) {
        mOnCardFormSubmitListener = listener;
    }

    public void setOnFormFieldFocusedListener(OnCardFormFieldFocusedListener listener) {
        mOnCardFormFieldFocusedListener = listener;
    }


    public void setEnabled(boolean enabled) {
        mCardNumber.setEnabled(enabled);
        mExpirationView.setEnabled(enabled);
        mCvvView.setEnabled(enabled);
    }


    public boolean isValid() {
        boolean valid = true;
        if (mCardNumberRequired) {
            valid = valid && mCardNumber.isValid();
        }
        if (mExpirationRequired) {
            valid = valid && mExpirationView.isValid();
        }
        if (mCvvRequired) {
            valid = valid && mCvvView.isValid();
        }
        return valid;
    }


    public void validate() {
        if (mCardNumberRequired) {
            mCardNumber.validate();
        }
        if (mExpirationRequired) {
            mExpirationView.validate();
        }
        if (mCvvRequired) {
            mCvvView.validate();
        }
    }


    public void setCardNumberError() {
        if (mCardNumberRequired) {
            mCardNumber.setError(true);
            requestEditTextFocus(mCardNumber);
        }
    }


    public void setExpirationError() {
        if (mExpirationRequired) {
            mExpirationView.setError(true);
            if (!mCardNumberRequired || !mCardNumber.isFocused()) {
                requestEditTextFocus(mExpirationView);
            }
        }
    }


    public void setCvvError() {
        if (mCvvRequired) {
            mCvvView.setError(true);
            if ((!mCardNumberRequired && !mExpirationRequired) ||
                (!mCardNumber.isFocused() && !mExpirationView.isFocused())) {
                requestEditTextFocus(mCvvView);
            }
        }
    }

    private void requestEditTextFocus(EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    public void closeSoftKeyboard() {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getWindowToken(), 0);
    }


    public String getCardNumber() {
        return mCardNumber.getText().toString();
    }


    public String getExpirationMonth() {
        return mExpirationView.getMonth();
    }


    public String getExpirationYear() {
        return mExpirationView.getYear();
    }


    public String getCvv() {
        return mCvvView.getText().toString();
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        mCvvView.setCardType(cardType);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && mOnCardFormFieldFocusedListener != null) {
            mOnCardFormFieldFocusedListener.onCardFormFieldFocused(v);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnCardFormFieldFocusedListener != null) {
            mOnCardFormFieldFocusedListener.onCardFormFieldFocused(v);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean valid = isValid();
        if (mValid != valid) {
            mValid = valid;
            if (mOnCardFormValidListener != null) {
                mOnCardFormValidListener.onCardFormValid(valid);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO && mOnCardFormSubmitListener != null) {
            mOnCardFormSubmitListener.onCardFormSubmit();
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
