package org.example.controllers;

import configs.Config;

public interface VehicleCreateController<C extends Config>{
    C buildConfig();
}
