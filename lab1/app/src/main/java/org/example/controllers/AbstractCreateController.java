package org.example.controllers;

import configs.Config;
import lombok.RequiredArgsConstructor;
import org.example.alert.AlertUtil;
import org.example.models.SceneModel;

@RequiredArgsConstructor
public abstract class AbstractCreateController <C extends Config> implements VehicleCreateController<C>{
    private final SceneModel scene;

    protected abstract C buildConfigObject();

    @Override
    public C buildConfig() {
        return buildConfigObject();
    }
}
