package ru.kpfu.itis.katargina.utils;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;

import java.util.*;

public class ImageRecognitionUtil {
    private static final double MIN_PRECISION = 0.8;
    private static final List<String> objectLabels =
            Arrays.asList("изолированный", "нет человек", "мода", "женщина", "взаимодействие");
    private static final String USER_API_KEY = "8436ebf91bed4a96904af34fa85ee30b";
    private static final String USER_ID = "ksekat";
    private static final String APP_ID = "ksekatID";
    private static final String MODEL_ID = "general-image-recognition";


    public static List<String> recognizeImage(String imageUrl) {
        List<String> results = new ArrayList<>();
        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials(USER_API_KEY));

        MultiOutputResponse postModelOutputsResponse = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setUserAppId(UserAppIDSet.newBuilder().setUserId(USER_ID).setAppId(APP_ID))
                        .setModelId(MODEL_ID)
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setUrl(imageUrl)
                                        )
                                )
                        )

                        .build()
        );

        if (postModelOutputsResponse.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Post model outputs failed, status: " + postModelOutputsResponse.getStatus());
        }

        Output output = postModelOutputsResponse.getOutputs(0);
        int count = 1;
        for (Concept concept : output.getData().getConceptsList()) {
            if (!objectLabels.contains(concept.getName()) && concept.getValue() >= MIN_PRECISION) {
                String conceptName = capitalizeFirstLetter(concept.getName());
                results.add(conceptName);
                return results;
            }
            count++;
        }
        return results;
    }

    private static String capitalizeFirstLetter(String text) {
        String firstChar = text.substring(0, 1);
        String remainingChars = text.substring(1);
        return firstChar.toUpperCase() + remainingChars;
    }
}
  