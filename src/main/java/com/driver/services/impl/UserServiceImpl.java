package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        User user = new User();
        user.setConnected(false);
        user.setMaskedIP(null);
        user.setPassword(password);
        userRepository3.save(user);
        CountryName countryName1;
        String countryCode="";
        if(CountryName.USA.toString().equals(countryName)) {
            countryName1 = CountryName.USA;
            countryCode = "002";
        }
        if(CountryName.IND.toString().equals(countryName)) {
            countryName1 = CountryName.IND;
            countryCode = "001";
        }
        if(CountryName.JPN.toString().equals(countryName)) {
            countryName1 = CountryName.JPN;
            countryCode = "005";
        }
        if(CountryName.CHI.toString().equals(countryName)) {
            countryName1 = CountryName.CHI;
            countryCode = "003";
        }
        else {
            countryName1 = CountryName.AUS;
            countryCode = "004";
        }
        user.setOriginalIP(countryCode+"."+user.getId());
        user.setServiceProviderList(new ArrayList<>());
        user.setConnectionList(new ArrayList<>());

        Country country = new Country();
        country.setCode(countryCode);
        country.setCountryName(countryName1);
        country.setServiceProvider(null);

        user.setCountry(country);

        userRepository3.save(user);
        return user;

    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        user.getServiceProviderList().add(serviceProvider);
        userRepository3.save(user);
        return user;
    }
}
