package com.obs.deliver4me.interfaces;


import com.obs.deliver4me.datamodels.JsonResponse;

/**
 * Created by Arun S on 14-Jun-17.
 */
public interface RequestListener {
    void onSuccess(JsonResponse jsonResp);

    void onFailure(JsonResponse jsonResp);
}
