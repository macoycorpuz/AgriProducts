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
    private List<Deal> listOfDeals = Collections.synchronizedList(new ArrayList<Deal>());
    private List<Message> listOfMessages = Collections.synchronizedList(new ArrayList<Message>());
    private List<Order> listOfOrders = Collections.synchronizedList(new ArrayList<Order>());

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

    public List<Deal> getListOfDeals() {
        return listOfDeals;
    }

    public void setListOfDeals(List<Deal> listOfDeals) {
        this.listOfDeals = listOfDeals;
    }

    public static void setCenterRepository(CenterRepository centerRepository) {
        CenterRepository.centerRepository = centerRepository;
    }

    public List<Order> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<Order> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }
}
