package serialization;

import classes.Animatable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import configs.*;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SerializationModel {
    static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    static final String txtString = "txt";
    static final String jsonString = "json";
    static final Path txtPath = Path.of(txtString);
    static final Path jsonPath = Path.of(jsonString);
    public SerializationModel(){
        if (!Files.exists(txtPath)){
            try{
                Files.createDirectory(txtPath);
            }
            catch(IOException ignored){}
        }
        if (!Files.exists(jsonPath)){
            try{
                Files.createDirectory(jsonPath);
            }
            catch(IOException ignored){}
        }
    }
    private void deleteFiles(Path dir){
        try (var stream = Files.walk(dir, 1)) { // 1 = только текущий уровень
            stream
                    .filter(Files::isRegularFile) // только файлы
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    private void deleteTxt(){
        deleteFiles(txtPath);
    }
    private void deleteJson(){
        deleteFiles(jsonPath);
    }

    private void saveConfigTxt(Config config, int idx){
        try(BufferedWriter bw = Files.newBufferedWriter(
                Path.of(txtString + "/" + idx + ".txt"),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )){
            switch(config){
                case HelicopterConfig hc -> {
                    bw.write("Helicopter\n");
                    bw.write(hc.peopleCount() + "\n");
                    bw.write(hc.loadWeight() + "\n");
                    bw.write(hc.lowerBound() + "\n");
                    bw.write(hc.upperBound() + "\n");
                    bw.write(hc.x() + "\n");
                    bw.write(hc.y() + "\n");
                    bw.write(hc.elapsedTime() + "\n");
                }
                case LamborghiniConfig lc -> {
                    bw.write("Lamborghini\n");
                    bw.write(lc.peopleCount() + "\n");
                    bw.write(lc.loadWeight() + "\n");
                    bw.write(lc.x() + "\n");
                    bw.write(lc.y() + "\n");
                    bw.write(lc.elapsedTime() + "\n");
                    bw.write(lc.state() + "\n");
                    bw.write(lc.currentSpeedFactor() + "\n");
                    bw.write(lc.acceleration() + "\n");
                }
                case PlaneConfig pc -> {
                    bw.write("Plane\n");
                    bw.write(pc.peopleCount() + "\n");
                    bw.write(pc.loadWeight() + "\n");
                    bw.write(pc.startY() + "\n");
                    bw.write(pc.x() + "\n");
                    bw.write(pc.y() + "\n");
                    bw.write(pc.elapsedTime() + "\n");
                }
                case PoloConfig lc -> {
                    bw.write("Polo\n");
                    bw.write(lc.peopleCount() + "\n");
                    bw.write(lc.loadWeight() + "\n");
                    bw.write(lc.x() + "\n");
                    bw.write(lc.y() + "\n");
                    bw.write(lc.elapsedTime() + "\n");
                    bw.write(lc.state() + "\n");
                    bw.write(lc.currentSpeedFactor() + "\n");
                    bw.write(lc.acceleration() + "\n");
                }
                default -> throw new IllegalArgumentException();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    private void saveConfigJson(Config config, int idx){
        try {
            mapper.writeValue(
                    Files.newBufferedWriter(Path.of(jsonString + "/" + idx + ".json")),
                    config
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveConfigsTxt(List<Config> configs){
        deleteTxt();
        try(ExecutorService executor = Executors.newFixedThreadPool(16)) {
            for (int i = 0; i < configs.size(); i++) {
                final var fI = i;
                executor.submit(() -> saveConfigTxt(configs.get(fI), fI));
            }
        }
    }
    public void saveVehiclesTxt(List<Animatable<? extends Config>> vehicles, Animatable<? extends Config> currentVehicle){
        deleteTxt();
        try(ExecutorService executor = Executors.newFixedThreadPool(16)) {
            executor.submit(() -> saveConfigTxt(currentVehicle.snapshot(), 0));
            for (int i = 0; i < vehicles.size(); i++) {
                final var fI = i;
                executor.submit(() -> saveConfigTxt(vehicles.get(fI).snapshot(), fI + 1));
            }
        }
    }
    public void saveConfigsJson(List<Config> configs){
        deleteJson();
        try(ExecutorService executor = Executors.newFixedThreadPool(16)) {
            for (int i = 0; i < configs.size(); i++) {
                final var fI = i;
                executor.submit(() -> saveConfigJson(configs.get(fI), fI));
            }
        }
    }
    public void saveVehiclesJson(List<Animatable<? extends Config>> vehicles, Animatable<? extends Config> currentVehicle){
        deleteJson();
        try(ExecutorService executor = Executors.newFixedThreadPool(16)) {
            executor.submit(() -> saveConfigJson(currentVehicle.snapshot(), 0));
            for (int i = 0; i < vehicles.size(); i++) {
                final var fI = i;
                executor.submit(() -> saveConfigJson(vehicles.get(fI).snapshot(), fI + 1));
            }
        }
    }
}
