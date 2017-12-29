package fragment;

import android.graphics.Color;
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

import com.jakewharton.rxbinding2.view.RxView;
import com.zzp.remotehealth.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Patient.Patient;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import utils.Constants;

import static utils.Constants.zzpFile;

public class PatientFragment extends Fragment implements Observer {
    View mView;
    //第几位病人
    int rank;
    public static final String ARGS_PAGE = "args_page";
    Patient patient;
    int i = 0;


    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.set)
    Button set;
    @Bind(R.id.bloodPressureLineChart)
    LineChartView bloodLineChart;
    @Bind(R.id.respirationLineChart)
    LineChartView repLineChart;
    @Bind(R.id.temperatureLineChart)
    LineChartView tempLineChart;
    private List<PointValue> bloodPointValues = new ArrayList<>();
    private List<PointValue> repPointValues = new ArrayList<>();
    private List<PointValue> tempPointValues = new ArrayList<>();
    private List<AxisValue> mAxisValues = new ArrayList<>();
    LineChartData bloodData, repData, tempData;


    public static PatientFragment newInstance(int i) {
        PatientFragment patientFragment = new PatientFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_PAGE, i);
        patientFragment.setArguments(bundle);
        return patientFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rank = getArguments().getInt(ARGS_PAGE);
        mView = inflater.inflate(R.layout.fragment_patient, container, false);
        patient = Constants.patients.get(rank);
        patient.addObserver(this);
        ButterKnife.bind(this, mView);
        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();
        initView(mView);

    }


    private void initView(View view) {

        number = view.findViewById(R.id.number);
        number.setText(patient.number);

        set = view.findViewById(R.id.set);
        RxView.clicks(set)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((view1) -> showDialog());
//        set.setOnClickListener((view1)->showDialog());


        bloodLineChart = view.findViewById(R.id.bloodPressureLineChart);
        repLineChart = view.findViewById(R.id.respirationLineChart);
        tempLineChart = view.findViewById(R.id.temperatureLineChart);

        initLineChart();
    }


    private void initLineChart() {
        //折线的颜色
        Line bloodLine = new Line(bloodPointValues).setColor(Color.GREEN).setCubic(false);
        Line repLine = new Line(repPointValues).setColor(Color.GREEN).setCubic(false);
        Line tempLine = new Line(tempPointValues).setColor(Color.GREEN).setCubic(false);
        List<Line> bloodLines = new ArrayList<>();
        List<Line> repLines = new ArrayList<>();
        List<Line> tempLines = new ArrayList<>();

        bloodLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        bloodLine.setCubic(false);//曲线是否平滑
        bloodLine.setFilled(false);//是否填充曲线的面积
        bloodLine.setHasLabels(true);//曲线的数据坐标是否加上备注
        bloodLine.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        bloodLine.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        bloodLines.add(bloodLine);
        bloodData = new LineChartData();
        bloodData.setLines(bloodLines);

        repLine.setShape(ValueShape.CIRCLE);
        repLine.setCubic(false);
        repLine.setFilled(false);
        repLine.setHasLabels(true);
        repLine.setHasLines(true);
        repLine.setHasPoints(true);
        repLines.add(repLine);
        repData = new LineChartData();
        repData.setLines(repLines);

        tempLine.setShape(ValueShape.CIRCLE);
        tempLine.setCubic(false);
        tempLine.setFilled(false);
        tempLine.setHasLabels(true);
        tempLine.setHasLines(true);
        tempLine.setHasPoints(true);
        tempLines.add(tempLine);
        tempData = new LineChartData();
        tempData.setLines(tempLines);


        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("近十次数据");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(10);  //最多几个X轴坐标
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        bloodData.setAxisXBottom(axisX); //x 轴在底部
        repData.setAxisXBottom(axisX);
        tempData.setAxisXBottom(axisX);

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(5); //默认是3，只能看最后三个数字
        axisY.setName("温度");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        bloodData.setAxisYLeft(axisY);  //Y轴设置在左边
        repData.setAxisYLeft(axisY);
        tempData.setAxisYLeft(axisY);


        //设置行为属性，支持缩放、滑动以及平移
        bloodLineChart.setInteractive(false);
        repLineChart.setInteractive(false);
        tempLineChart.setInteractive(false);

        bloodLineChart.setVisibility(View.VISIBLE);
        repLineChart.setVisibility(View.VISIBLE);
        tempLineChart.setVisibility(View.VISIBLE);

    }


    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.edit, null);
        EditText numberEdit,
                freEdit,
                bloodPressureDown,
                bloodPressureUp,
                respirationDown,
                respirationUp,
                temperatureDown,
                temperatureUp;
        Button enter, cancel;
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
        freEdit.setText(String.valueOf(patient.frequent));
        bloodPressureDown.setText(String.valueOf(patient.bloodPressureDown));
        bloodPressureUp.setText(String.valueOf(patient.bloodPressureUp));
        respirationDown.setText(String.valueOf(patient.respirationDown));
        respirationUp.setText(String.valueOf(patient.respirationUp));
        temperatureDown.setText(String.valueOf(patient.temperatureDown));
        temperatureUp.setText(String.valueOf(patient.temperatureUp));

        enter.setOnClickListener((view1) -> {
            boolean isError = false;
            try {
                String str = numberEdit.getText().toString();
                if ("".equals(str)) {
                    Toast.makeText(getContext(), "请输入病历号", Toast.LENGTH_SHORT).show();
                } else {
                    Pattern pattern = Pattern.compile("^[0-9]{9}$");
                    Matcher isNum = pattern.matcher(str);
                    if (!isNum.matches()) {
                        isError = true;

                    } else {
                        String oldNumber = patient.number;
                        patient.number = numberEdit.getText().toString();
                        number.setText(patient.number);
                        File oldFile = new File(zzpFile, oldNumber + ".text_file");
                        String rootPath = oldFile.getParent();
                        File newFile = new File(rootPath + File.separator + patient.number + ".text_file");
                        oldFile.renameTo(newFile);
                    }
                }
                if (!"".equals(freEdit.getText().toString())) {
                    int tempFre = Integer.parseInt(freEdit.getText().toString());
                    if (tempFre >= 1 && tempFre <= 3600)
                        patient.frequent = Integer.parseInt(freEdit.getText().toString());
                    else isError = true;
                }
                if (!"".equals(bloodPressureDown.getText().toString()))
                    patient.bloodPressureDown = Integer.parseInt(bloodPressureDown.getText().toString());
                if (!"".equals(bloodPressureUp.getText().toString()))
                    patient.bloodPressureUp = Integer.parseInt(bloodPressureUp.getText().toString());
                if (!"".equals(respirationDown.getText().toString()))
                    patient.respirationDown = Integer.parseInt(respirationDown.getText().toString());
                if (!"".equals(respirationUp.getText().toString()))
                    patient.respirationUp = Integer.parseInt(respirationUp.getText().toString());
                if (!"".equals(temperatureDown.getText().toString()))
                    patient.temperatureDown = Float.parseFloat(temperatureDown.getText().toString());
                if (!"".equals(temperatureUp.getText().toString()))
                    patient.temperatureUp = Float.parseFloat(temperatureUp.getText().toString());
            } catch (Exception e) {
                isError = true;
            } finally {
                if (isError)
                    Toast.makeText(getContext(), "输入有误", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener((view1) -> dialog.dismiss());
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (bloodPointValues.size() == 10) {
            bloodPointValues.remove(0);
            repPointValues.remove(0);
            tempPointValues.remove(0);
        }

        bloodPointValues.add(new PointValue(i++, patient.bloodPressure));
        repPointValues.add(new PointValue(i++, patient.respiration));
        tempPointValues.add(new PointValue(i++, patient.temperature));

        bloodLineChart.setLineChartData(bloodData);
        repLineChart.setLineChartData(repData);
        tempLineChart.setLineChartData(tempData);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}


