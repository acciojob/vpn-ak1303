package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setServiceProviders(new ArrayList<>());
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);
        serviceProvider.setUsers(new ArrayList<>());
        serviceProvider.setConnectionList(new ArrayList<>());
        serviceProvider.setCountryList(new ArrayList<>());
        Admin admin = adminRepository1.findById(adminId).get();
        admin.getServiceProviders().add(serviceProvider);
        serviceProvider.setAdmin(admin);
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        Country country = new Country();
        if(countryName.toUpperCase().equals(CountryName.AUS.toString())){
            country.setCountryName(CountryName.AUS);
            country.setCode("003");
        }
        else if(countryName.toUpperCase().equals(CountryName.CHI.toString())){
            country.setCountryName(CountryName.CHI);
            country.setCode("004");
        }
        else if(countryName.toUpperCase().equals(CountryName.IND.toString())){
            country.setCountryName(CountryName.IND);
            country.setCode("001");
        }
        else if(countryName.toUpperCase().equals(CountryName.JPN.toString())){
            country.setCountryName(CountryName.JPN);
            country.setCode("005");
        }
        else if(countryName.toUpperCase().equals(CountryName.USA.toString())){
            country.setCountryName(CountryName.USA);
            country.setCode("002");
        }else{
            throw new Exception("Country not found");
        }
        country.setUser(null);
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);
        serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
