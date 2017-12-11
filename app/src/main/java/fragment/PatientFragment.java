package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzp.remotehealth.R;

import java.util.Timer;
import java.util.TimerTask;

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
    Timer mTimer ;
    TimerTask mTimerTask;
    TextView number,bloodPressure,respiration,temperature;
    Patient patient ;



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
        rank = getArguments().getInt(ARGS_PAGE);
        mView  = inflater.inflate(R.layout.fragment_patient,container,false);
        initView(mView);
        patient = Constants.patients.get(rank);
        return mView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if( mTimer != null)  {  mTimer.cancel();}
    }

    private void initView(View view) {
        number = view.findViewById(R.id.number);
        bloodPressure = view.findViewById(R.id.bloodPressure);
        respiration = view.findViewById(R.id.respiration);
        temperature = view.findViewById(R.id.temperature);

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
            number.setText( patient.number);
            bloodPressure.setText( patient.bloodPressure);
            respiration.setText( patient.respiration);
            temperature.setText( String.valueOf( patient.temperature));
            }};
        mTimer.schedule(mTimerTask,0,1000);
    }

}
