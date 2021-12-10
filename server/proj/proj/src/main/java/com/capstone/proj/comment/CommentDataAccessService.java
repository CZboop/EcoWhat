package com.capstone.proj.comment;

import com.capstone.proj.user.UserDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDataAccessService implements CommentDAO{

    private JdbcTemplate jdbcTemplate;
    private UserDAO userDAO;

    public CommentDataAccessService(JdbcTemplate jdbcTemplate, UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }

    @Override
    public void addComment(Comment comment){
        String sql = """
                INSERT INTO comments 
                (user_id, comment_title, comment, comment_category, upvotes, downvotes, constituency_id, post_date)
                VALUES
                (?, ?, ?, ?, 0, 0, ?, ?);
                """;

        jdbcTemplate.update(sql, comment.getUserId(), comment.getComment_title(), comment.getComment(),
                comment.getComment_category(), comment.getConstituencyId(), comment.getPost_date());
    }

    public void upvoteComment(int id){
        String sql = """
                UPDATE comments SET upvotes = upvotes + 1 WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
    };

    public void downvoteComment(int id){
        String sql = """
                UPDATE comments SET downvotes = downvotes + 1 WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
    };

    public List<Comment> getAllComments(){
        String sql = """
                SELECT * FROM comments;
                """;
        List<Comment> allComments = jdbcTemplate.query(sql, new CommentRowMapper(userDAO));
        return allComments;
    };
}
