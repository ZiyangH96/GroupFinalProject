package algonquin.cst2355.groupfinalproject.Dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2355.groupfinalproject.databinding.DictionaryDetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    DictionaryItem selected;
    public MessageDetailsFragment(DictionaryItem m){
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DictionaryDetailsLayoutBinding binding = DictionaryDetailsLayoutBinding.inflate(inflater);
        binding.wordName.setText(selected.getWord());

        binding.databaseId.setText("id= " + selected.getId());
        binding.timeDetail.setText(selected.getTimeAdded());
        return binding.getRoot();
    }
}

