package response;

import client.Client;
import lombok.Getter;
import util.ResponseLogInfoVisitor;

import java.util.List;

public class FirstCollectionDetails extends Response {
    @Getter
    private final List<String> heroNames, classOfCardNames;

    public FirstCollectionDetails(List<String> heroNames, List<String> classOfCardNames) {
        this.classOfCardNames = classOfCardNames;
        this.heroNames = heroNames;
    }

    @Override
    public void execute(Client client) {
        client.setFirstCollectionDetail(heroNames, classOfCardNames);
    }

    @Override
    public void accept(ResponseLogInfoVisitor visitor) {
        visitor.setFirstCollectionDetailsInfo(this);
    }
}