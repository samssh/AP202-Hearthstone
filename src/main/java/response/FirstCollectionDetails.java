package response;

import client.Client;
import lombok.Getter;

import java.util.List;

public class FirstCollectionDetails extends Response {
    @Getter
    private final List<String> heroNames, classOfCardNames;

    public FirstCollectionDetails(List<String> heroNames, List<String> classOfCardNames) {
        this.classOfCardNames = classOfCardNames;
        this.heroNames = heroNames;
    }

    @Override
    public void execute() {
        Client.getInstance().setFirstCollectionDetail(heroNames, classOfCardNames);
    }
}