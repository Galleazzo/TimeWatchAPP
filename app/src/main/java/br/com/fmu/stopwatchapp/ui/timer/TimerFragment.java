package br.com.fmu.stopwatchapp.ui.timer;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import br.com.fmu.stopwatchapp.databinding.FragmentTimerBinding;

public class TimerFragment extends Fragment {

    private FragmentTimerBinding binding;
    private Chronometer textTimer;
    private Button buttonStart;
    private Button buttonPause;
    private Button buttonFinish;
    private Long savedValue;
    private ListView listView;
    private List<CharSequence> listOfTimes = new ArrayList<>();
    private ArrayAdapter<CharSequence> listAdapter;
    private Button buttonclearHistory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimerViewModel timerViewModel =
                new ViewModelProvider(this).get(TimerViewModel.class);

        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.textTimer = (Chronometer) binding.textTimer;
        this.buttonStart = (Button) binding.buttonStart;
        this.buttonPause = (Button) binding.buttonPause;
        this.buttonFinish = (Button) binding.buttonFinish;
        this.listView = (ListView) binding.listView;
        this.buttonclearHistory = (Button) binding.buttonclearHistory;

        this.listAdapter = new ArrayAdapter<>(getParentFragment().getContext(), android.R.layout.simple_list_item_1, this.listOfTimes);


        this.buttonStart.setEnabled(true);
        this.buttonPause.setEnabled(false);
        this.buttonFinish.setEnabled(true);
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

        this.buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishCount(view);
            }
        });

        this.buttonclearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
            }
        });
        
        return root;
    }

    public void startCount(View view) {
        this.buttonPause.setEnabled(true);
        if(this.savedValue == null)
            textTimer.setBase(SystemClock.elapsedRealtime());

        if(this.savedValue != null)
            this.textTimer.setBase(SystemClock.elapsedRealtime() - savedValue);

        this.textTimer.start();
        this.buttonStart.setEnabled(false);
    }

    public void pauseCount(View view) {
        this.buttonStart.setEnabled(true);
        this.textTimer.stop();
        this.savedValue = SystemClock.elapsedRealtime() - this.textTimer.getBase();
        this.buttonPause.setEnabled(false);
    }

    public void finishCount(View view) {
        this.textTimer.stop();
        this.addCountInList(this.textTimer.getText());
        this.textTimer.setText("00:00");

        this.savedValue = null;
        this.buttonPause.setEnabled(false);
        this.buttonStart.setEnabled(true);
    }

    public void addCountInList(CharSequence value) {
        this.listOfTimes.add(value);
        this.listView.setAdapter(this.listAdapter);
    }

    public void clearHistory(){
        this.listAdapter.clear();
        this.listOfTimes.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}