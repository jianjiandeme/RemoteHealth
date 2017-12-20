package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zzp.remotehealth.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Patient.Patient;
import utils.Constants;

/**
 * Created by zzp on 2017/11/27.
 */

public class PatientFragment extends Fragment {
    View mView;
    //第几位病人
    int rank;
    public static final String ARGS_PAGE = "args_page";
    //更新信息
    Timer mTimer = new Timer();
    TimerTask mTimerTask;
    TextView number,bloodPressure,respiration,temperature,frequent;
    Patient patient ;
    Button set ;

    public static PatientFragment newInstance(int i){
        PatientFragment patientFragment = new PatientFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_PAGE,i);
        patientFragment.setArguments(bundle);
        return patientFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        rank = getArguments().getInt(ARGS_PAGE);
        mView  = inflater.inflate(R.layout.fragment_patient,container,false);
        patient = Constants.patients.get(rank);
        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();
        initView(mView);


        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                number.post(
                        ()->number.setText( patient.number));

                bloodPressure.post(
                        ()->bloodPressure.setText( String.valueOf(patient.bloodPressure)));

                respiration.post(
                        ()->respiration.setText( String.valueOf(patient.respiration)));

                temperature.post(
                        ()->temperature.setText( String.valueOf(patient.temperature)));

                frequent.post(
                        ()->frequent.setText( String.valueOf(patient.frequence)));
            }};
        mTimer.schedule(mTimerTask,0,1000);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if( mTimer != null)  {  mTimer.cancel();}
    }

    private void initView(View view) {
        number = view.findViewById(R.id.number);
        set = view.findViewById(R.id.set);
        set.setOnClickListener((view1)->showDialog());
//        number.setOnClickListener((view1)-> showDialog());
        frequent = view.findViewById(R.id.frequent);
        bloodPressure = view.findViewById(R.id.bloodPressure);
        respiration = view.findViewById(R.id.respiration);
        temperature = view.findViewById(R.id.temperature);
        set = view.findViewById(R.id.set);

    }


    private void showDialog(){
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.edit,null);
        EditText numberEdit,
                freEdit,
                bloodPressureDown ,
                bloodPressureUp   ,
                respirationDown   ,
                respirationUp     ,
                temperatureDown   ,
                temperatureUp   ;
        Button enter,cancel;
        enter = dialogView.findViewById(R.id.enter);
        cancel = dialogView.findViewById(R.id.cancel);
        numberEdit = dialogView.findViewById(R.id.numberEdit);
        numberEdit.setText(patient.number);

        freEdit = dialogView.findViewById(R.id.freEdit);
            bloodPressureDown = dialogView.findViewById(R.id.bloodPressureDown);
            bloodPressureUp = dialogView.findViewById(R.id.bloodPressureUp);
            respirationDown = dialogView.findViewById(R.id.respirationDown);
            respirationUp = dialogView.findViewById(R.id.respirationUp);
            temperatureDown = dialogView.findViewById(R.id.temperatureDown);
            temperatureUp = dialogView.findViewById(R.id.temperatureUp);
            freEdit.setText(String.valueOf(patient.frequence));
            bloodPressureDown.setText(String.valueOf(patient.bloodPressureDown));
            bloodPressureUp.setText(String.valueOf(patient.bloodPressureUp));
            respirationDown.setText(String.valueOf(patient.respirationDown));
            respirationUp.setText(String.valueOf(patient.respirationUp));
            temperatureDown.setText(String.valueOf(patient.temperatureDown));
            temperatureUp.setText(String.valueOf(patient.temperatureUp));

        enter.setOnClickListener((view1)-> {
            try {
                String str= numberEdit.getText().toString();
            if("".equals(str)){
                Toast.makeText(getContext(),"请输入病历号",Toast.LENGTH_SHORT).show();
            }
            else {
                Pattern pattern = Pattern.compile("^[0-9]{9}$");
                Matcher isNum = pattern.matcher(str);
                if (!isNum.matches()) {
                    Toast.makeText(getContext(),"输入有误",Toast.LENGTH_SHORT).show();
                }else {
                    patient.number = numberEdit.getText().toString();
                }
            }
            if(!"".equals(freEdit.getText().toString()))
                    patient.frequence = Integer.parseInt(freEdit.getText().toString());
            if(!"".equals(bloodPressureDown.getText().toString()))
                patient.bloodPressureDown = Integer.parseInt(bloodPressureDown.getText().toString());
            if(!"".equals(bloodPressureUp.getText().toString()))
                patient.bloodPressureUp = Integer.parseInt(bloodPressureUp.getText().toString());
            if(!"".equals(respirationDown.getText().toString()))
                patient.respirationDown = Integer.parseInt(respirationDown.getText().toString());
            if(!"".equals(respirationUp.getText().toString()))
                patient.respirationUp = Integer.parseInt(respirationUp.getText().toString());
            if(!"".equals(temperatureDown.getText().toString()))
                patient.temperatureDown = Float.parseFloat(temperatureDown.getText().toString());
            if(!"".equals(temperatureUp.getText().toString()))
                patient.temperatureUp = Float.parseFloat(temperatureUp.getText().toString());
            }catch (Exception e){
                Toast.makeText(getContext(),"输入有误",Toast.LENGTH_SHORT).show();
            }finally {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener((view1)-> dialog.dismiss());
        dialog.setContentView(dialogView);
        dialog.show();
    }
}
