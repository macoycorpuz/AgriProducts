package thesis.agriproducts.model;

import thesis.agriproducts.model.entities.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CenterRepository {

    private static CenterRepository centerRepository;

    private List<Product> listOfProducts = Collections.synchronizedList(new ArrayList<Product>());
    private List<User> listOfUsers = Collections.synchronizedList(new ArrayList<User>());
    private List<Message> listOfMessages = Collections.synchronizedList(new ArrayList<Message>());
    private Map<String, ArrayList<Message>> mapOfMessagesInDeal = new HashMap<>();

    public static CenterRepository getCenterRepository() {

        if (null == centerRepository) {
            centerRepository = new CenterRepository();
        }
        return centerRepository;
    }

    public List<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public List<Message> getListOfMessages() {
        return listOfMessages;
    }

    public void setListOfMessages(List<Message> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }

    public Map<String, ArrayList<Message>> getMapOfMessagesInDeal() {
        return mapOfMessagesInDeal;
    }

    public void setMapOfMessagesInDeal(Map<String, ArrayList<Message>> mapOfMessagesInDeal) {
        this.mapOfMessagesInDeal = mapOfMessagesInDeal;
    }
}
