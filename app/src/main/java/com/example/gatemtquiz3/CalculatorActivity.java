package com.example.gatemtquiz3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity {

    Calculations calculations = new Calculations(this);

    public static final String NUMBER = "num";
    public static final String OPERATOR = "operator";

    public static String angle = "Deg";
    public static String getAnswer;
    private String lastDigit;
    private String lastOperator;
    ArrayList<String> numbers;

    private Button close;

    private TextView uppertv;
    private TextView lowertv;

    private Button mc;
    private Button mr;
    private Button madd;
    private Button msub;
    private Button ms;
    private RadioGroup anglesSelector;
    private RadioButton rbDeg;
    private RadioButton rbRad;

    private Button sin;
    private Button cos;
    private Button tan;
    private Button sin_inv;
    private Button cos_inv;
    private Button tan_inv;

    private Button sinh;
    private Button cosh;
    private Button tanh;
    private Button sinh_inv;
    private Button cosh_inv;
    private Button tanh_inv;

    private Button ten_raisedto_x;
    private Button e_raisedto_x;
    private Button one_div_x;
    private Button parent_open;
    private Button parent_close;
    private Button mod;

    private Button sq;
    private Button e;
    private Button ce;
    private Button c;
    private Button bs;
    private Button div;

    private Button cb;
    private Button pi;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button mul;

    private Button indice;
    private Button fact;
    private Button four;
    private Button five;
    private Button six;
    private Button plus;

    private Button sqrt;
    private Button cbrt;
    private Button ln;
    private Button one;
    private Button two;
    private Button three;
    private Button minus;

    private Button root;
    private Button log;
    private Button logXbase2;
    private Button abs;
    private Button negate;
    private Button zero;
    private Button decimal;
    private Button equal;

    private Button exp;
    private Button logxBasey;
    private Button percentile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        uppertv = findViewById(R.id.scientific_tv_upper);
        lowertv = findViewById(R.id.scientific_tv_lower);
        lowertv.setText("0");

        anglesSelector = findViewById(R.id.angles_radiogroup);
        rbDeg = findViewById(R.id.rb_deg);
        rbRad = findViewById(R.id.rb_rag);
        rbDeg.setChecked(true);
        anglesSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton angleSelected = findViewById(anglesSelector.getCheckedRadioButtonId());
                angle = angleSelected.getText().toString();
            }
        });

        close = findViewById(R.id.close_calc_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sin = findViewById(R.id.scientific_btn_sin);
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("sin");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        cos = findViewById(R.id.scientific_btn_cos);
        cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("cos");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        tan = findViewById(R.id.scientific_btn_tan);
        tan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("tan");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        sin_inv = findViewById(R.id.scientific_btn_sin_1);
        sin_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("sin-1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        cos_inv = findViewById(R.id.scientific_btn_cos_1);
        cos_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("cos-1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        tan_inv = findViewById(R.id.scientific_btn_tan_1);
        tan_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("tan-1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        sinh = findViewById(R.id.scientific_btn_sinh);
        sinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("sinh");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        cosh = findViewById(R.id.scientific_btn_cosh);
        cosh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("cosh");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        tanh = findViewById(R.id.scientific_btn_tanh);
        tanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("tanh");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        sinh_inv = findViewById(R.id.scientific_btn_sinh_1);
        sinh_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("sinh-1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        cosh_inv = findViewById(R.id.scientific_btn_cosh_1);
        cosh_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("cosh-1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        tanh_inv = findViewById(R.id.scientific_btn_tanh_1);
        tanh_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("tanh-1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        ten_raisedto_x = findViewById(R.id.scientific_btn_10_raisedto_x);
        ten_raisedto_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("10^x");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        e_raisedto_x = findViewById(R.id.scientific_btn_e_raisedto_x);
        e_raisedto_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("e^x");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        one_div_x = findViewById(R.id.scientific_btn_1_div_x);
        one_div_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("1/x");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        parent_open = findViewById(R.id.scientific_btn_parent_open);
        parent_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.parent_openClicked();
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        parent_close = findViewById(R.id.scientific_btn_parent_close);
        parent_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.parent_closeClicked();
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        mod = findViewById(R.id.scientific_btn_mod);
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("mod");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        sq = findViewById(R.id.scientific_btn_sq);
        sq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("sq");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        e = findViewById(R.id.scientific_btn_e);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("e");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        ce = findViewById(R.id.scientific_btn_ce);
        ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.ce();
                lowertv.setText("0");
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        c = findViewById(R.id.scientific_btn_c);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.clear();
                lowertv.setText("0");
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        bs = findViewById(R.id.scientific_btn_bs);
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.bs();
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });
        div = findViewById(R.id.scientific_btn_div);
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("/");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        cb = findViewById(R.id.scientific_btn_cb);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("cb");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        pi = findViewById(R.id.scientific_btn_pi);
        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("pi");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        seven = findViewById(R.id.scientific_btn_7);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("7");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        eight = findViewById(R.id.scientific_btn_8);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("8");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        nine = findViewById(R.id.scientific_btn_9);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("9");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        mul = findViewById(R.id.scientific_btn_mul);
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("*");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        indice = findViewById(R.id.scientific_btn_indice);
        indice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("^");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        fact = findViewById(R.id.scientific_btn_fact);
        fact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("!");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        four = findViewById(R.id.scientific_btn_4);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("4");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        five = findViewById(R.id.scientific_btn_5);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("5");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        six = findViewById(R.id.scientific_btn_6);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("6");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        plus = findViewById(R.id.scientific_btn_add);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("+");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        sqrt = findViewById(R.id.scientific_btn_sqrt);
        sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("sqrt");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        ln = findViewById(R.id.scientific_btn_ln);
        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("ln");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        one = findViewById(R.id.scientific_btn_1);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("1");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        two = findViewById(R.id.scientific_btn_2);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("2");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        three = findViewById(R.id.scientific_btn_3);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("3");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        minus = findViewById(R.id.scientific_btn_sub);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("-");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        root = findViewById(R.id.scientific_btn_y_rt_x);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("rt");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        log = findViewById(R.id.scientific_btn_log);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("log");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        logXbase2 = findViewById(R.id.logxbase2);
        logXbase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("logXbase2");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        abs = findViewById(R.id.scientific_btn_abs);
        abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("abs");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        cbrt = findViewById(R.id.scientific_btn_cbrt);
        cbrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("cbrt");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        exp = findViewById(R.id.scientific_btn_exp);
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("e+");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        logxBasey = findViewById(R.id.scientific_btn_logxbasey);
        logxBasey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("logxBasey");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        percentile = findViewById(R.id.scientific_btn_percentile);
        percentile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("%");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        negate = findViewById(R.id.scientific_btn_negate);
        negate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.operatorClicked("±");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        zero = findViewById(R.id.scientific_btn_0);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.numberClicked("0");
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        decimal = findViewById(R.id.scientific_btn_decimal);
        decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations.decimalClicked();
                lowertv.setText(calculations.getCurrentNumber());
                uppertv.setText(calculations.calc(calculations.numbers));
            }
        });

        equal = findViewById(R.id.scientific_btn_equal);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> expression = calculations.numbers;
                try {
                    calculations.evaluateAnswer();
                    lowertv.setText(calculations.answer);
                    uppertv.setText("");
                }catch (Exception e){
                    Toast.makeText(CalculatorActivity.this, "Invalid operation", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

