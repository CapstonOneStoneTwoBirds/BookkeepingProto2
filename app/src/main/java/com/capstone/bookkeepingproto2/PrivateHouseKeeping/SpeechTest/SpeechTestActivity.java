package com.capstone.bookkeepingproto2.PrivateHouseKeeping.SpeechTest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.bookkeepingproto2.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by YeomJi on 15. 4. 30..
 */
public class SpeechTestActivity extends Activity {

    // 금액 추출---> 완벽하지 않음. 구 백원 에서 구를 백원에 속하게 하는 알고리즘 생각 필요
    // 상호명에서 1955, 이천 과 같은 것을 상품 명과 지역 위치로 구분할 수 있는 알고리즘 필요
    //Caused by: java.lang.ArrayIndexOutOfBoundsException: length=3; index=3 해결 필요
    // Category는 정해진 단어를 말하여 추출 { 식비, 의류비, 주거비, 여가비, 교통비, 저축비, 기타 }
    // 식비 말고 다른 종류도 테스트 필요한 듯

    Vector<Integer> vectorCost = new Vector<Integer>();
    Vector<String> vectorProduct = new Vector<String>();

    LinkedList queue = new LinkedList();

    private TextView result;
    private final int SPEAK_ACT = 1;
    private String store, product;
    private String str, token;
    private int cost, isCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_test_main);

        result = (TextView) findViewById(R.id.speechResult);

        findViewById(R.id.speechButton).setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                voiceRecoder();
            }
        });
    }

    public void voiceRecoder() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak loudly");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA);
            startActivityForResult(intent, SPEAK_ACT);
        } catch (ActivityNotFoundException e) { //음성 인식 어플이 없을 경우
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=com.google.android.voicesearch&feature=search_result"));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEAK_ACT) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> arrResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                for (int i = 0; i < arrResult.size(); i++) {

                    cost = 0;
                    isCost = 0;

                    result.setText(arrResult.get(i) + "\n");
                    //result.setText(arrResult.get(i));


                    str = arrResult.get(i);
                    System.out.println("vectorCost 추출 위한 값  /  " + str);


                    StringTokenizer st = new StringTokenizer(str);
                    while (st.hasMoreTokens()) {


                        token = st.nextToken();

                        if (isCost == 0)
                            CategoryToken(token);
                        else
                            CostToken(token);


                    }
                    InsertCostInvectorCost(cost);

/*
                    int Checksize = 0;

                    System.out.println("vecotrProduct의 크기  /  " + vectorProduct.size());
                    while (Checksize < vectorProduct.size()) {
                        System.out.println("vectorCost의 내용물  /  " + vectorProduct.get(Checksize));
                        Checksize++;
                    }
*/


                }

                //-------------------------------------------------------------------------------------
                if (cost != 0) {
                    int[] resultCost1 = new int[]{0, 0, 0, 0, 0, 0};
                    int trueResultCost = 0; // 실제 추출된 가격 (가계부에 동기화 필요)

                    for (int h = 0; h < 5; h++) {
                        for (int g = 0; g < 5; g++) {
                            if (vectorCost.elementAt(h).equals(vectorCost.elementAt(g))) {
                                //System.out.println("vectorCost h // "+vectorCost.elementAt(h));
                                //System.out.println("vectorCost g // "+vectorCost.elementAt(g));
                                ++resultCost1[h];
                            }
                            //System.out.println("vectorCost--------------------------------------------");
                        }
                        if (resultCost1[5] < resultCost1[h]) {
                            resultCost1[5] = resultCost1[h];
                            trueResultCost = vectorCost.elementAt(h);
                        }
                    }


                    //-------------------------------------------------------------------------------------

                    if (!vectorProduct.isEmpty()) {
                        int[] resultProduct = new int[]{0, 0, 0, 0, 0, 0};
                        String trueResultProduct = ""; // 실제 추출된 가격 (가계부에 동기화 필요)

                        for (int h = 0; h < 5; h++) {
                            for (int g = 0; g < 5; g++) {
                                if (vectorProduct.elementAt(h).equals(vectorProduct.elementAt(g))) {

                                    ++resultProduct[h];
                                }
                            }
                            if (resultProduct[5] < resultProduct[h]) {
                                resultProduct[5] = resultProduct[h];
                                trueResultProduct = vectorProduct.elementAt(h);
                            }
                        }


                        //-------------------------------------------------------------------------------------


                        System.out.println("vectorCost 최종 결과 store -------> " + trueResultProduct);
                        System.out.println("vectorCost 최종 결과 product -------> " + product);
                        System.out.println("vectorCost 최종 결과 cost -------> " + trueResultCost);


                        vectorCost.removeAllElements();
                        vectorProduct.removeAllElements();
                    }
                }

            }
        }
    }

    public void CategoryToken(String costC) {
        String CheckStore = "";
        if (costC.contains("식비") || costC.contains("의류") || costC.contains("주거") || costC.contains("여가") ||
                costC.contains("교통") || costC.contains("저축") || costC.contains("기타")) {
            while (queue.peek() != null) CheckStore += " " + queue.poll();
            vectorProduct.addElement(CheckStore);
            // 식비, 의류 , 주거 등은 나중에 가계부 입력 값에 맞게 수정
            if (costC.contains("식비")) product = "식비";
            else if (costC.contains("의류")) product = "의류";
            else if (costC.contains("주거")) product = "주거";
            else if (costC.contains("여가")) product = "여가";
            else if (costC.contains("교통")) product = "교통";
            else if (costC.contains("저축")) product = "저축";
            else if (costC.contains("기타")) product = "기타";

            isCost = 1;

        } else queue.offer(costC);
    }


    public void CostToken(String costN) { //ASCII ---> 실제 숫자 + 48

        if (costN.endsWith("원")) costN = costN.replace("원", "");

        if (costN.isEmpty()) return;
        else if ((costN.charAt(0) == '1') || (costN.charAt(0) == '2') || (costN.charAt(0) == '3') ||
                (costN.charAt(0) == '4') || (costN.charAt(0) == '5') || (costN.charAt(0) == '6') ||
                (costN.charAt(0) == '7') || (costN.charAt(0) == '8') || (costN.charAt(0) == '9')) {
            if (costN.contains("만") || costN.contains("천") || costN.contains("백")) {
                if (costN.endsWith("원")) costN = costN.replace("원", "");
                switch (costN.charAt(1)) { // 십만 이상 고려 X 십원 단위 이하 고려 X
                    case '만':
                        cost += ((int) costN.charAt(0) - 48) * 10000;
                        break;
                    case '천':
                        cost += ((int) costN.charAt(0) - 48) * 1000;
                        break;
                    case '백':
                        cost += ((int) costN.charAt(0) - 48) * 100;
                        break;
                    default:
                        return;
                }
            } else cost += Integer.valueOf(costN);
            // 첫 글자가 문자인 경우
        } else if (costN.contains("만")) {
            switch (costN.charAt(0)) {
                case '만': cost += 10000; break;
                case '이': cost += 20000; break;
                case '삼': cost += 30000; break;
                case '사': cost += 40000; break;
                case '오': cost += 50000; break;
                case '육': cost += 60000; break;
                case '칠': cost += 70000; break;
                case '팔': cost += 80000; break;
                case '구': cost += 90000; break;
                default: break;
            }
        } else if (costN.contains("천")) {
            switch (costN.charAt(0)) {
                case '천': cost += 1000; break;
                case '이': cost += 2000; break;
                case '삼': cost += 3000; break;
                case '사': cost += 4000; break;
                case '오': cost += 5000; break;
                case '육': cost += 6000; break;
                case '칠': cost += 7000; break;
                case '팔': cost += 8000; break;
                case '구': cost += 9000; break;
                default: break;
            }
        } else if (costN.contains("백")) {
            switch (costN.charAt(0)) {
                case '백': cost += 100; break;
                case '이': cost += 200; break;
                case '삼': cost += 300; break;
                case '사': cost += 400; break;
                case '오': cost += 500; break;
                case '육': cost += 600; break;
                case '칠': cost += 700; break;
                case '팔': cost += 800; break;
                case '구': cost += 900; break;
                default: break;
            }
        }
    }


    public void InsertCostInvectorCost(int costB) {
        if (costB >= 100) {
            vectorCost.addElement(Integer.valueOf(costB));
            isCost = 0;
        }
    }
}
