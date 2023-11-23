package br.com.fmu.stopwatchapp.ui.timerwatch;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
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
    private Button finishCount;
    private Button pause;
    private TextView textView;

    private CountDownTimer temporizador;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 0;
    private FragmentTimerwatchBinding binding;
    private Vibrator vibrator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimerWatchViewModel timerWatchViewModel =
                new ViewModelProvider(this).get(TimerWatchViewModel.class);

        binding = FragmentTimerwatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        this.textView = binding.textTimerWatch;
        this.start = (Button) binding.buttonStart;
        this.configTime = (Button) binding.buttonConfigTime;
        this.finishCount = (Button) binding.buttonFinish;
        this.pause = (Button) binding.buttonPause;

        this.vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        this.configTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        this.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        this.finishCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCountDownText();
        final TextView textView = binding.textTimerWatch;

        this.start.setEnabled(false);
        this.pause.setEnabled(false);
        this.finishCount.setEnabled(false);

        return root;
    }

    private void showTimePickerDialog() {
        if (this.vibrator != null) {
            this.vibrator.vibrate(50);
        }
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
        if (this.vibrator != null) {
            this.vibrator.vibrate(50);
        }
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
            }.start();
            this.configTime.setEnabled(false);
            this.start.setEnabled(false);
            this.pause.setEnabled(true);
            this.finishCount.setEnabled(true);
            isTimerRunning = true;
        }
    }

    private void pauseTimer() {
        if (this.vibrator != null) {
            this.vibrator.vibrate(50);
        }
        if (isTimerRunning) {
            this.start.setEnabled(true);
            this.pause.setEnabled(false);
            temporizador.cancel();
            isTimerRunning = false;
        }
    }

    private void resetTimer() {
        if (this.vibrator != null) {
            this.vibrator.vibrate(50);
        }
        if (temporizador != null) {
            temporizador.cancel();
            isTimerRunning = false;
        }
        this.finishCount.setEnabled(false);
        this.start.setEnabled(false);
        this.pause.setEnabled(false);
        this.configTime.setEnabled(true);
        timeLeftInMillis = 0;
        updateCountDownText();
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

        if (buttonEnabled())
            this.start.setEnabled(true);
    }

    private Boolean buttonEnabled() {
        if ((this.textView.getText().toString() != "00:00:00" && this.timeLeftInMillis != 0) && (!this.pause.isEnabled() && !this.finishCount.isEnabled())){
            return true;
        }
        if ((this.textView.getText().toString() != "00:00:00" && this.timeLeftInMillis != 0) && (this.pause.isEnabled() && this.finishCount.isEnabled())){
            return false;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}