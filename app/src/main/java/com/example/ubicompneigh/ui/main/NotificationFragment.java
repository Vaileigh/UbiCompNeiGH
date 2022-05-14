package com.example.ubicompneigh.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ubicompneigh.MainActivity;
import com.example.ubicompneigh.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotificationFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARRAY_LIST = "array_list";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;
    ListView listView;
    ListsPagerAdapter adapter;

    public static PlaceholderFragment newInstance(int index, ArrayList arrayList) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putParcelableArrayList(ARRAY_LIST, arrayList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = binding.simpleListView;
        ArrayList arrayList = new ArrayList<>();
        if (getArguments() != null) {
            arrayList = getArguments().getParcelableArrayList(ARRAY_LIST);
        }
        adapter = new ListsPagerAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);
        //List<Lists> cars = Lists.getAll();
        Log.d("MyApp", String.valueOf(arrayList));


        final TextView textView = binding.sectionLabel;

        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}