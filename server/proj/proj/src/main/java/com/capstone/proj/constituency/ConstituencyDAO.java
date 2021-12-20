package com.capstone.proj.constituency;

import java.util.List;
import java.util.Map;

public interface ConstituencyDAO {

    public void addConstituency(int id, String name);

    public void createConstituencyTable();

    public void dropConstituencyTable();

    public String getCountyFromConstituency(int constituency_id);

    public List<Constituency> getAllConstituencies();

    public String getConstituencyNameFromId(int constituency_id);

    public Integer getConstituencyIdFromName(String name);

    public List<Constituency> getConstituenciesWithNoMp();
}
