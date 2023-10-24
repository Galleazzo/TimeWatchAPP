package br.com.fmu.stopwatchapp.ui.timer;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.fmu.stopwatchapp.R;
import br.com.fmu.stopwatchapp.databinding.FragmentTimerBinding;

public class TimerFragment extends Fragment {

    private FragmentTimerBinding binding;
    private Chronometer textTimer;
    private Button buttonStart;
    private Button buttonPause;
    private Long savedValue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimerViewModel timerViewModel =
                new ViewModelProvider(this).get(TimerViewModel.class);

        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.textTimer = (Chronometer) binding.textTimer;
        this.buttonStart = (Button) binding.buttonStart;
        this.buttonPause = (Button) binding.buttonPause;

        this.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCount(view);
            }
        });

        this.buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseCount(view);
            }
        });
        
        return root;
    }

    public void startCount(View view) {
        if(this.savedValue == null)
            textTimer.setBase(SystemClock.elapsedRealtime());

        if(this.savedValue != null)
            this.textTimer.setBase(savedValue);

        this.textTimer.start();
    }

    public void pauseCount(View view) {
        this.textTimer.stop();
        this.savedValue = SystemClock.elapsedRealtime() - this.textTimer.getBase();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}