package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user = userRepository2.findById(userId).get();
        if(user.isConnected())throw new Exception("Already connected");
        if(user.getCountry().getCountryName().toString().equals(countryName)) return user;//-----

        List<ServiceProvider> spList = user.getServiceProviderList();

        ServiceProvider serviceProvider=null;
        Country country = null;
        int min=Integer.MAX_VALUE;
        for(ServiceProvider sp : spList){
            List<Country> cList = sp.getCountryList();
            for(Country country1 : cList){
                if(country1.getCountryName().toString().equals(countryName)){
                    if(sp.getId()<min){
                        min=sp.getId();
                        serviceProvider=sp;
                        country=country1;
                    }
                }
            }
        }
        if(country==null)throw new Exception("Unable to connect");

        Connection connection = new Connection();
        connection.setUser(user);
        connection.setServiceProvider(serviceProvider);

        user.getConnectionList().add(connection);
        //user.setCountry(country);
        user.setConnected(true);
        //updatedCountryCode.serviceProviderId.userId
        String maskedIp = country.getCode()+"."+serviceProvider.getId()+"."+userId;
        user.setMaskedIP(maskedIp);

        userRepository2.save(user);
        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user = userRepository2.findById(userId).get();
        if(!user.isConnected())throw new Exception("Already disconnected");
        user.setMaskedIP(null);
        user.setConnected(false);
        //List<Connection> cList = user.getConnectionList();//-----
        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender = userRepository2.findById(senderId).get();
        User receiver = userRepository2.findById(receiverId).get();
        CountryName senderCountry = sender.getCountry().getCountryName();
        CountryName receiverCountry;
        if(receiver.getMaskedIP()==null)receiverCountry=receiver.getCountry().getCountryName();
        else{
            String countryCode = receiver.getMaskedIP().substring(0,3);
            if(countryCode.equals("001")) receiverCountry = CountryName.IND;
            if(countryCode.equals("002")) receiverCountry = CountryName.USA;
            if(countryCode.equals("003")) receiverCountry = CountryName.AUS;
            if(countryCode.equals("004")) receiverCountry = CountryName.CHI;
            else receiverCountry = CountryName.JPN;
        }
        if(receiverCountry.equals(senderCountry))return sender;
        try{
            connect(senderId,receiverCountry.toString());
        }catch (Exception e){
            throw new Exception("Cannot establish communication");
        }
        User updatedSender = userRepository2.findById(senderId).get();
        return updatedSender;
    }
}
