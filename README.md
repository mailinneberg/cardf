 
 
  Step 1. 
     Add the JitPack repository to your build file
     Add it in your root build.gradle at the end of repositories:

     repositories 
          {
          maven { url 'https://jitpack.io' }
	  }
  
  Step 2. 
     Add the dependency:

	dependencies {
	        compile 'com.github.mailinneberg:Card-From-1.1:1.1'
	}
  
  Step 3. 
    Card Form is a LinearLayout that you can add to your layout:
   
    
    <com.example.cardformlib.view.CardForm 
    android:id="@+id/bt_card_form"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
    
  Step 4. 
    To initialize the view and change which fields are required for the user to enter, use 
    CardForm#setRequiredFields(boolean cardNumberRequired, boolean 
    expirationRequired, boolean cvvRequired, String imeActionLabel).
    
    CardForm cardForm = (CardForm) findViewById(R.id.bt_card_form);
    cardForm.setRequiredFields(true, true, true, "Purchase");
    
 Step 4.1 
    To access the values in the form, there are getters for each field:
    
    cardForm.getCardNumber();
    cardForm.getExpirationMonth();
    cardForm.getExpirationYear();
    cardForm.getCvv();
    cardForm.getPostalCode();
    
 Step 4.2
 
    @Override
    public void onCardFormSubmit() {
        if (cardForm.isValid()) {
            Toast.makeText(this, R.string.is_valid, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.in_valid, Toast.LENGTH_SHORT).show();
        }
    }
