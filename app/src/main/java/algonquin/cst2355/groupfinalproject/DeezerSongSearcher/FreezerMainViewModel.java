package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FreezerMainViewModel extends ViewModel {

    // Observe this object:
    public MutableLiveData<String> userString = new MutableLiveData<>("");

    // Getter method to retrieve the MutableLiveData
    public MutableLiveData<String> getUserString() {
        return userString;
    }

    // Setter method to update the MutableLiveData
    public void setUserString(String newValue) {
        userString.setValue(newValue);
    }
}

