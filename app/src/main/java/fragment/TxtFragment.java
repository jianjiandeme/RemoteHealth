package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zzp.remotehealth.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.Observer;

import Patient.Patient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import utils.Constants;

import static utils.Constants.array;
import static utils.Constants.patients;
import static utils.Constants.zzpFile;

/**
 * Created by ZZP on 2017-12-23.
 */

public class TxtFragment extends Fragment {
    View mView;
    //第几位病人
    int rank;
    public static final String ARGS_PAGE = "args_page";
    TextView scrollView;

    public static TxtFragment newInstance(int i){
        TxtFragment txtFragment = new TxtFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_PAGE,i);
        txtFragment.setArguments(bundle);
        return txtFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        rank = getArguments().getInt(ARGS_PAGE);
        mView  = inflater.inflate(R.layout.fragment_txt,container,false);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        scrollView = mView.findViewById(R.id.txt);

        Observable.from(array)
        .filter((file)->file.getName().endsWith(".txt"))
        .map((file)->{
            StringBuilder sb = new StringBuilder();
            try{
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String str ;
                while((str = bufferedReader.readLine()) !=null){
                    sb.append(str+"\n");
                }
                bufferedReader.close();
                reader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return sb.toString();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe((s)->scrollView.setText(s));

    }
}
