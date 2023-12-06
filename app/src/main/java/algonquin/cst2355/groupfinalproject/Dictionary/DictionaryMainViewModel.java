package algonquin.cst2355.groupfinalproject.Dictionary;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DictionaryMainViewModel extends ViewModel {

    //observe this object:
    public MutableLiveData<String> userString = new MutableLiveData("");

    public MutableLiveData<ArrayList<DictionaryItem>> messages = new MutableLiveData< >();
    public MutableLiveData<DictionaryItem> selectedMessage = new MutableLiveData< >();
}