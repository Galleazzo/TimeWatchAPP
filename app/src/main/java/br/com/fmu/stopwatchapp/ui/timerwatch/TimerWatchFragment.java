package br.com.fmu.stopwatchapp.ui.timerwatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.fmu.stopwatchapp.databinding.FragmentTimerwatchBinding;

public class TimerWatchFragment extends Fragment {

    private FragmentTimerwatchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimerWatchViewModel timerWatchViewModel =
                new ViewModelProvider(this).get(TimerWatchViewModel.class);

        binding = FragmentTimerwatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTimerwatch;
        timerWatchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}