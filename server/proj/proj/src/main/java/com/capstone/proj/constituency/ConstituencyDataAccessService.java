package com.capstone.proj.constituency;

import com.capstone.proj.comment.Comment;
import com.capstone.proj.comment.CommentRowMapper;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ConstituencyDataAccessService implements ConstituencyDAO{

    private JdbcTemplate jdbcTemplate;

    public ConstituencyDataAccessService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addConstituency(int id, String name){
        String sql = """
                INSERT INTO constituencies (constituency_id, constituency_name)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(sql, id, name);
    }

    @Override
    public void createConstituencyTable(){
        String sql = """
                CREATE TABLE constituencies (id BIGSERIAL, constituency_name VARCHAR(255), constituency_id INTEGER PRIMARY KEY);
                """;
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropConstituencyTable(){
        String sql = """
                DROP TABLE IF EXISTS constituencies;
                """;
        jdbcTemplate.execute(sql);
    }

    @Override
    public String getCountyFromConstituency(int constituency_id){
        String sql = """
                SELECT county_name FROM counties WHERE ? = ANY (constituency_ids) ;
                """;
        return jdbcTemplate.queryForObject(sql, new Object[] {constituency_id}, String.class);
    }

    @Override
    public List<Constituency> getAllConstituencies(){
        String sql =  """
                SELECT * FROM constituencies;
                """;
        List<Constituency> allConstituencies = jdbcTemplate.query(sql, new ConstituencyRowMapper());
        return allConstituencies;
    };

    @Override
    public String getConstituencyNameFromId(int constituency_id){
        String sql = """
                SELECT constituency_name FROM constituencies WHERE constituency_id =  ?;
                """;
        return jdbcTemplate.queryForObject(sql, new Object[] {constituency_id}, String.class);
    };

    @Override
    public Integer getConstituencyIdFromName(String name){
        String sql = """
                SELECT constituency_id FROM constituencies WHERE constituency_name LIKE ?;
                """;

        return jdbcTemplate.queryForObject(sql, new Object[]{name}, Integer.class);
    }

    @Override
    public List<Constituency> getConstituenciesWithNoMp(){
        String sql = """
                SELECT constituencies.constituency_id, constituencies.constituency_name FROM constituencies JOIN mps 
                ON (constituencies.constituency_id = mps.constituency_id) WHERE mps.name IS NULL;
                """;
        List<Constituency> constituencies = jdbcTemplate.query(sql, new ConstituencyRowMapper());
        return constituencies;
    }

}
