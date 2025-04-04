package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    List<Train> trainList;
    private final ObjectMapper objectMapper;
    private static final String TRAIN_DB_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadTrains();
    }

    public void loadTrains()throws IOException{
        trainList = objectMapper.readValue(new File(TRAIN_DB_PATH), new TypeReference<List<Train>>() {
        });
    }

    public List<Train>searchTrains(String source,String destination){
        try {
            return trainList.stream().filter(
                    train -> validTrain(
                            train,source,destination)
            ).collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("error in searchtrains" + e.getMessage());
            return null;
        }


    }

    public void addTrain(Train newTrain) {
        // Checking here if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }
    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            System.out.println("Failed to save train list to file: " + e.getMessage());
        }
    }

    public void updateTrain(Train updatedTrain) {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        // getting are stations from that particular train in a list
        List<String> stationList = train.getStations();

        // getting the index of the source and destination in the stationOrder list
        int sourceIndex = stationList.indexOf(source);
        int destinationIndex = stationList.indexOf(destination);

        // checking if the source and destination are in the stationList and source is before destination
        // in the stationList
        // so that the train is valid
        try{
            return  sourceIndex != -1
                    && destinationIndex != -1
                    && sourceIndex < destinationIndex;
        }catch (Exception e){
            System.out.println("Error in validTrain: " + e.getMessage());
            return false;
        }
    }
}
