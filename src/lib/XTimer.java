/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author PC
 */
public class XTimer implements ActionListener{
    private int remainingSeconds;
    private Timer timer;
    private JLabel label;
    private CountdownFinishedListener countdownFinishedListener;

    public XTimer(int seconds, JLabel label, CountdownFinishedListener countdownFinishedListener) {
        this.remainingSeconds = seconds - 1;
        this.label = label;
        label.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        this.countdownFinishedListener = countdownFinishedListener;
        this.timer = new Timer(1000, this);
        this.timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (remainingSeconds <= 0) {
            timer.stop();
            label.setText("Time's up!");
            if (countdownFinishedListener != null) {
                countdownFinishedListener.onCountdownFinished();
            }
        } else {
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            label.setText(String.format("%02d:%02d", minutes, seconds));
            remainingSeconds--;
        }
    }
    
    public interface CountdownFinishedListener {
        void onCountdownFinished();
    }
}
