package br.com.fmu.stopwatchapp.ui.timerwatch;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import br.com.fmu.stopwatchapp.databinding.FragmentTimerwatchBinding;

public class TimerWatchFragment extends Fragment {

    private Button start;
    private Button configTime;
    private Button finish;
    private Button pause;
    private TextView textView;

    private CountDownTimer temporizador;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 0; // Tempo inicial

    private FragmentTimerwatchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimerWatchViewModel timerWatchViewModel =
                new ViewModelProvider(this).get(TimerWatchViewModel.class);

        binding = FragmentTimerwatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textTimerWatch;
        start = binding.buttonStart;
        configTime = binding.buttonConfigTime;
        finish = binding.buttonFinish;
        pause = binding.buttonPause;

        configTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        this.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCountDownText();

        return root;
    }

    private void showTimePickerDialog() {
        int selectedMinutes = (int) (timeLeftInMillis / 1000) / 60;

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Converte o tempo selecionado para milissegundos
                        long selectedMinutes = hourOfDay * 60 + minute;
                        timeLeftInMillis = selectedMinutes * 60 * 1000;

                        // Atualiza o texto do cronômetro
                        updateCountDownText();
                    }
                },
                selectedMinutes / 60, // horas
                selectedMinutes % 60, // minutos
                true
        );

        timePickerDialog.show();
    }

    private void startTimer() {
        if (!isTimerRunning) {
            temporizador = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onFinish() {
                    updateCountDownFinish();
                }
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateCountDownText();

                }
            };
            isTimerRunning = true;
        }
    }

    private void pauseTimer() {
        if (isTimerRunning) {
            temporizador.cancel();
            isTimerRunning = false;
        }
    }

    private void resetTimer() {
        timeLeftInMillis = 0; // Reinicia o tempo para 0
        updateCountDownFinish();
    }
    private void updateCountDownFinish(){
        String timeLeftFormatted = "00:00:00";
        textView.setText(timeLeftFormatted);

    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60 ;
        int minutes = (int) ((timeLeftInMillis / (1000*60)) % 60);
        int hours   = (int) ((timeLeftInMillis / (1000*60*60)) % 24);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        textView.setText(timeLeftFormatted);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}