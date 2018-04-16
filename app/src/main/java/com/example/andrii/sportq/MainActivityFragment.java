package com.example.andrii.sportq;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivityFragment extends Fragment {

    private static final String TAG = "SportQ";

    private TextView scoreTextView;
    private ImageView ImageView;
    private LinearLayout[] guessLinearLayouts;
    private TextView answerTextView;
    private static final int CLUB_IN_QUIZ = 6;

    private List<String> fileNameList;
    private List<String> quizClubsList;

    private String correctAnswer;

    private int correctAnswers;
    private int rows = 2;
    private SecureRandom random;
    private Handler handler;
    public int score;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        fileNameList = new ArrayList<>();
        quizClubsList = new ArrayList<>();
        random = new SecureRandom();
        handler = new Handler();


        scoreTextView = (TextView) view.findViewById(R.id.time_scoreView);
        ImageView = (ImageView) view.findViewById(R.id.ImageView);

        guessLinearLayouts = new LinearLayout[2];
        guessLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.row2LinearLayout);

        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(buttonListener);
            }
        }

        return view;
    }

    private String getClubName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ');
    }

    private void loadNextClub() {

        String nextImage = quizClubsList.remove(0);
        correctAnswer = nextImage; // update the correct answer
        answerTextView.setText(""); // clear answerTextView

        String kind = nextImage.substring(0, nextImage.indexOf('-'));

        AssetManager assets = getActivity().getAssets();

        try (InputStream stream = assets.open(kind + "/" + nextImage + ".png")) {

            Drawable club = Drawable.createFromStream(stream, nextImage);
            ImageView.setImageDrawable(club);

        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        Collections.shuffle(fileNameList);
        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));
        rows = 2;

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < guessLinearLayouts[row].getChildCount(); column++) {

                Button newButton = (Button) guessLinearLayouts[row].getChildAt(column);
                newButton.setEnabled(true);

                String filename = fileNameList.get((row * 2) + column);
                newButton.setText(getClubName(filename));
            }
        }

        int row = random.nextInt(rows);
        int column = random.nextInt(2);
        LinearLayout randomRow = guessLinearLayouts[row];
        String clubName = getClubName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(clubName);
    }

    private  View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Button button = ((Button) v);
            String textButton = button.getText().toString();
            final String answer = getClubName(correctAnswer);

            ++correctAnswers;
            if (textButton.equals(answer)) {

                if (correctAnswers == CLUB_IN_QUIZ) {

                     final MainActivity activity = (MainActivity) getActivity();

                     score(true, answer);

                    new AlertDialog.Builder(activity)
                            .setTitle("The End")
                            .setMessage("Your score is :" + score)
                            .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(activity, StartActivity.class);
                                    startActivity(intent);
                                }
                            }).create().show();

                }
                else {
                    score(true, answer);
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {

                                    loadNextClub();
                                }
                            }, 1000);
                }
            }
            else {
                if (correctAnswers == CLUB_IN_QUIZ) {

                    final MainActivity activity = (MainActivity) getActivity();

                    score(false, answer);

                    new AlertDialog.Builder(activity)
                            .setTitle("The End")
                            .setMessage("Your score is :" + score)
                            .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(activity, StartActivity.class);
                                    startActivity(intent);
                                }
                            }).create().show();

                }else {

                    score(false, answer);
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {

                                    loadNextClub();
                                }
                            }, 1000);

                }
            }
        }
    };


    private void score(boolean correct, String answer){
        if (correct){
            answerTextView.setText(answer + "!");
            score += 10;
            scoreTextView.setText("Score: " + score);
            disableButtons();
        }else {
            answerTextView.setText("INCORRECT!");
            score -= 20;
            scoreTextView.setText("Score: " + score);
            disableButtons();
        }
    }


    
    private void disableButtons() {
        for (int row = 0; row < rows; row++) {
            LinearLayout linearLayoutRow = guessLinearLayouts[row];
            for (int i = 0; i < linearLayoutRow.getChildCount(); i++)
                linearLayoutRow.getChildAt(i).setEnabled(false);
        }
    }

    public void resetQuiz(String item) {

        Log.d(TAG, "resetQuiz srart");

        AssetManager assets = getActivity().getAssets();
        fileNameList.clear();

        try {
                String[] paths = assets.list(item);
            Log.d(TAG, "resetQuiz paths = " + paths);

                for (String path : paths)
                    fileNameList.add(path.replace(".png", ""));
            }

        catch (IOException exception) {
            exception.printStackTrace();
        }

        Log.d(TAG, "resetQuiz fileNameList = " + fileNameList.size());

        correctAnswers = 0;

        score = 0;
        scoreTextView.setText("Score: " + score);

        int clubCounter = 1;
        int numberOfClubs = fileNameList.size();

        Log.d(TAG, "resetQuiz quizClubsList = " + quizClubsList.size());

        while (clubCounter <= CLUB_IN_QUIZ) {
            int randomIndex = random.nextInt(numberOfClubs);

            String filename = fileNameList.get(randomIndex);

            if (!quizClubsList.contains(filename)) {

                quizClubsList.add(filename);
                ++clubCounter;
            }
        }
        Log.d(TAG, "resetQuiz quizClubsList = " + quizClubsList.size());

        loadNextClub();
    }
}

