package com.coperatecoding.secodeverseback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class IntentChatService {

    public int testModel(String h5FilePath, String inputString) {
        // H5 파일을 로드하여 SavedModelBundle을 생성합니다.
        SavedModelBundle modelBundle = SavedModelBundle.load(h5FilePath, "serve");

        try (Session session = modelBundle.session()) {
            // 모델의 입력 Tensor를 생성합니다.
            Tensor<String> inputTensor = Tensor.create(inputString, String.class);

            // 모델의 예측값을 계산합니다.
            Tensor<?> outputTensor = session.runner()
                    .feed("input", inputTensor)
                    .fetch("output")
                    .run()
                    .get(0);

            // 예측 결과를 추출하여 정수로 변환합니다.
            int prediction = outputTensor.copyTo(new int[1])[0];

            // 사용이 끝난 Tensor 객체들을 명시적으로 해제합니다.
            inputTensor.close();
            outputTensor.close();

            // 사용이 끝난 모델 리소스를 해제합니다.
            modelBundle.close();

            // 예측 결과를 반환합니다.
            return prediction;
        }
    }
}
