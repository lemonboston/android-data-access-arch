package com.gk.daas.screen.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.gk.daas.R;
import com.gk.daas.framework.access.ViewToolkit;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Gabor_Keszthelyi
 */
public class MainViewImpl implements MainView {

    @Bind(R.id.ResultText)
    TextView resultTextView;

    @Bind(R.id.ProgressBar)
    ProgressBar progressBar;

    @Bind(R.id.Button_Execute)
    Button executeButton;

    @Bind(R.id.Text_WeatherUseCase)
    TextView weatherUseCase;

    @Bind(R.id.TechnicalUseCase_Description)
    TextView technicalUseCase;

    @Bind(R.id.ImplementationDescription)
    TextView implementationDescription;

    @Bind(R.id.EditText_City1)
    EditText city1Text;

    @Bind(R.id.UseCase_Spinner)
    Spinner useCaseSpinner;

    @Bind(R.id.ExecuteArea)
    ViewGroup executeArea;

    @Bind(R.id.RequirementArea)
    ViewGroup requirementArea;

    @Bind(R.id.ImplementationArea)
    ViewGroup implementationArea;

    private final ViewToolkit viewToolkit;
    private UserActionListener listener;

    public MainViewImpl(ViewToolkit viewToolkit) {
        this.viewToolkit = viewToolkit;
    }

    @Override
    public void init() {
        viewToolkit.setContentView(R.layout.activity_main);
        viewToolkit.injectViews(this);

        executeButton.setText(R.string.GetTemp_Button);
        weatherUseCase.setText(R.string.WeatherUseCase_GetTemp);
        technicalUseCase.setText(R.string.UseCase_Basic_Description);
        implementationDescription.setText(R.string.UseCase_Basic_Implementation);
    }

    @Override
    public void setUseCaseTitles(List<String> titles) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(useCaseSpinner.getContext(), android.R.layout.simple_spinner_item, titles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        useCaseSpinner.setAdapter(adapter);
        useCaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onUseCaseSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void hideEverything() {
        executeArea.setVisibility(View.GONE);
        requirementArea.setVisibility(View.GONE);
        implementationArea.setVisibility(View.GONE);
    }

    @Override
    public void showEverything() {
        executeArea.setVisibility(View.VISIBLE);
        requirementArea.setVisibility(View.VISIBLE);
        implementationArea.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.Button_Execute)
    void onExecuteButtonClick() {
        listener.onExecuteButtonClick();
    }

    @Override
    public void setUserActionListener(UserActionListener listener) {
        this.listener = listener;
    }

    @Override
    public String getCity1() {
        return city1Text.getText().toString();
    }

    @Override
    public void setResultText(String result) {
        resultTextView.setText(result);
    }

    @OnClick(R.id.Button_Clear)
    void onClearButtonClick() {
        resultTextView.setText(null);
    }
}
