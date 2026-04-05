package serialization;

import classes.GroundVehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import configs.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DeserializationModel {
    static final ObjectMapper mapper = SerializationModel.mapper;
    static final String txtString = SerializationModel.txtString;
    static final String jsonString = SerializationModel.jsonString;
    static final Path txtPath = SerializationModel.txtPath;
    static final Path jsonPath = SerializationModel.jsonPath;
    private final Map<Integer, Config> res = new ConcurrentHashMap<>();
    private void deserializeConfigTxt(Path path){
        String fullName = path.getFileName().toString();
        int idx = Integer.parseInt(
                fullName.substring(0, fullName.lastIndexOf("."))
        );
        Config res = null;
        try{
            List<String> lines = Files.readAllLines(path);
            switch(lines.getFirst()){
                case "Helicopter" -> {
                    if (lines.getLast().equals("null"))
                        res = new HelicopterConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2)),
                                Double.parseDouble(lines.get(3)),
                                Double.parseDouble(lines.get(4))
                        );
                    else
                        res = new HelicopterConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2)),
                                Double.parseDouble(lines.get(3)),
                                Double.parseDouble(lines.get(4)),
                                Double.parseDouble(lines.get(5)),
                                Double.parseDouble(lines.get(6)),
                                Double.parseDouble(lines.get(7))
                        );
                }
                case "Lamborghini" -> {
                    if (lines.getLast().equals("null"))
                        res = new LamborghiniConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2))
                        );
                    else
                        res = new LamborghiniConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2)),
                                Double.parseDouble(lines.get(3)),
                                Double.parseDouble(lines.get(4)),
                                Double.parseDouble(lines.get(5)),
                                parseGroundState(lines.get(6)),
                                Double.parseDouble(lines.get(7)),
                                Double.parseDouble(lines.get(8))
                        );
                }
                case "Plane" -> {
                    if (lines.getLast().equals("null"))
                        res = new PlaneConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2)),
                                Double.parseDouble(lines.get(3))
                        );
                    else
                        res = new PlaneConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2)),
                                Double.parseDouble(lines.get(3)),
                                Double.parseDouble(lines.get(4)),
                                Double.parseDouble(lines.get(5)),
                                Double.parseDouble(lines.get(6))
                        );
                }
                case "Polo" -> {
                    if (lines.getLast().equals("null"))
                        res = new PoloConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2))
                        );
                    else
                        res = new PoloConfig(
                                Integer.parseInt(lines.get(1)),
                                Integer.parseInt(lines.get(2)),
                                Double.parseDouble(lines.get(3)),
                                Double.parseDouble(lines.get(4)),
                                Double.parseDouble(lines.get(5)),
                                parseGroundState(lines.get(6)),
                                Double.parseDouble(lines.get(7)),
                                Double.parseDouble(lines.get(8))
                        );
                }
                default -> throw new IllegalArgumentException();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.res.put(idx, res);
    }
    public List<Config> deserializeConfigsTxt(){
        res.clear();
        try(var stream = Files.walk(txtPath, 1);
            ExecutorService executor = Executors.newFixedThreadPool(16)){
            stream.filter(Files::isRegularFile)
                    .forEach((path) -> {
                        executor.submit(() -> deserializeConfigTxt(path));
                    });
        }
        catch(IOException e){
            e.printStackTrace();
        }
        List<Config> list = new ArrayList<>(res.size());
        for (int i = 0; i < res.size(); i++)
            list.add(res.get(i));
        return list;
    }

    private static GroundVehicle.GroundState parseGroundState(String line){
        return switch(line){
            case "MOVING_CONSTANT" -> GroundVehicle.GroundState.MOVING_CONSTANT;
            case "ACCELERATING" -> GroundVehicle.GroundState.ACCELERATING;
            case "STOPPED" -> GroundVehicle.GroundState.STOPPED;
            default -> throw new IllegalArgumentException();
        };
    }

    private void deserializeConfigJson(Path path){
        String fullName = path.getFileName().toString();
        int idx = Integer.parseInt(
                fullName.substring(0, fullName.lastIndexOf("."))
        );
        try {
            Config config = mapper.readValue(new File(path.toString()), Config.class);
            res.put(idx, config);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<Config> deserializeConfigsJson(){
        res.clear();
        try(var stream = Files.walk(jsonPath, 1);
            ExecutorService executor = Executors.newFixedThreadPool(16)){
            stream.filter(Files::isRegularFile)
                    .forEach((path) -> {
                        executor.submit(() -> deserializeConfigJson(path));
                    });
        }
        catch(IOException e){
            e.printStackTrace();
        }
        List<Config> list = new ArrayList<>(res.size());
        for (int i = 0; i < res.size(); i++)
            list.add(res.get(i));
        return list;
    }
}
