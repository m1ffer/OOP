package org.example.forms;

import configs.Config;
import javafx.scene.Node;
import org.example.controllers.VehicleCreateController;

public record FormResult<C extends Config>(Node node,
                                           VehicleCreateController<C> form) {}
