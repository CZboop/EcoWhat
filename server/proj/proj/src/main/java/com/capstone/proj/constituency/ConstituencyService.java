package com.capstone.proj.constituency;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.bcel.Const;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConstituencyService {

    private ConstituencyDAO constituencyDAO;

    @Autowired
    public ConstituencyService(ConstituencyDAO constituencyDAO){
        this.constituencyDAO = constituencyDAO;
    }

    public void addAllConstituencies(){
//        get data from api
//
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "https://members-api.parliament.uk/api/Location/Browse/1/England";
        ResponseEntity<JsonNode> response
                = restTemplate.getForEntity(fooResourceUrl, JsonNode.class);
        JsonNode responseObj = response.getBody();
        ArrayList<Constituency> ListOfConstituencies = new ArrayList<Constituency>();
        JsonNode constituencies = responseObj.get("value").get("childContexts");
        for (JsonNode constituency : constituencies){
            constituencyDAO.addConstituency(constituency.get("id").intValue(),
                    constituency.get("name").textValue());

//            ListOfConstituencies.add(new Constituency(constituency.get("id").intValue(),
//                    constituency.get("name").textValue()));
        }

        String fooResourceUrlWales
                = "https://members-api.parliament.uk/api/Location/Browse/1/Wales";
        ResponseEntity<JsonNode> responseWales
                = restTemplate.getForEntity(fooResourceUrlWales, JsonNode.class);
        JsonNode responseObjWales = responseWales.getBody();
        ArrayList<Constituency> ListOfConstituenciesWales = new ArrayList<Constituency>();
        JsonNode constituenciesWales = responseObjWales.get("value").get("childContexts");
        for (JsonNode constituency : constituenciesWales){
            constituencyDAO.addConstituency(constituency.get("id").intValue(),
                    constituency.get("name").textValue());

//            ListOfConstituencies.add(new Constituency(constituency.get("id").intValue(),
//                    constituency.get("name").textValue()));
        }

//        add to dao


    }

    public void createConstituencyTable(){
        constituencyDAO.createConstituencyTable();
    }

    public void dropConstituencyTable(){
        constituencyDAO.dropConstituencyTable();
    }


    public String getCountyFromConstituency(int constituency_id) {
        return constituencyDAO.getCountyFromConstituency(constituency_id);
    }

    public List<Constituency> getAllConstituencies() {
        return constituencyDAO.getAllConstituencies();
    }

    public String getConstituencyNameFromId(int constituency_id) {
        return constituencyDAO.getConstituencyNameFromId(constituency_id);
    }

    public Constituency getConstituencyFromPostcode(String postcode)  {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "https://api.postcodes.io/postcodes/" + postcode;
        ResponseEntity<JsonNode> response
                = restTemplate.getForEntity(fooResourceUrl, JsonNode.class);
        JsonNode responseObj = response.getBody();
        String name = responseObj.get("result").get("parliamentary_constituency").textValue();
        System.out.println(name);
        Constituency constituency = new Constituency(getConstituencyIdFromName(name),
                name);

        return constituency;
    }

    public Integer getConstituencyIdFromName(String name){
        return constituencyDAO.getConstituencyIdFromName(name);
    }

    public List<String> getConstituenciesWithNoMp() {
        List<String> listOfNames = new ArrayList<>();
        for (Constituency constituency : constituencyDAO.getConstituenciesWithNoMp()) {
            listOfNames.add(constituency.getConstituency_name());
        }
        return listOfNames;
    }
}
