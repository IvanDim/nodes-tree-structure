package com.amazing.nodes.model.dao;

import com.amazing.nodes.model.entities.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NodeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NodeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get the id of the root node
     * Every node should have information about the root id
     *
     * @return - the id of the root node
     */
    public Long getRootNode() {
        Long rootId;
        try {
            rootId = jdbcTemplate.queryForObject("SELECT " +
                    "    id " +
                    "FROM " +
                    "    nodes " +
                    "WHERE " +
                    "    parent_id IS NULL;", new Object[]{}, Long.class);
        } catch (DataAccessException e) {
            return null;
        }
        return rootId;
    }

    /**
     * Get the height of a node in the whole structure
     * The height is the total count of parents in the tree above the current one
     *
     * @param nodeId - the node id
     * @return - the height of the node
     */
    public Integer getNodeHeight(long nodeId) {
        Integer height;
        try {
            height = jdbcTemplate.queryForObject("WITH RECURSIVE nodes_path (id, parent_id) AS " +
                    "( " +
                    "  SELECT id, parent_id " +
                    "    FROM nodes " +
                    "    WHERE id = ? " +
                    "  UNION ALL " +
                    "  SELECT n.id, n.parent_id " +
                    "    FROM nodes_path AS np JOIN nodes AS n " +
                    "      ON np.parent_id = n.id " +
                    ") " +
                    "SELECT COUNT(id) FROM nodes_path;", new Object[]{nodeId}, Integer.class);
        } catch (DataAccessException e) {
            return null;
        }
        return height;

    }

    /**
     * Get the whole tree structure of nodes starting from the root node
     *
     * @param rootId - the id of the root node
     * @return - list of Node entities with all the nodes in the tree
     */
    public List<Node> getFullTree(long rootId) {

        List<Node> nodesList = new ArrayList<>();

        try {
            jdbcTemplate.query("WITH RECURSIVE nodes_path (id, name, parent_id, path, height) AS " +
                            "( " +
                            "  SELECT id, name, parent_id, name as path, 0 height " +
                            "    FROM nodes " +
                            "    WHERE parent_id IS NULL " +
                            "  UNION ALL " +
                            "  SELECT n.id, n.name, n.parent_id, CONCAT(np.path, ' > ', n.name), np.height + 1 " +
                            "    FROM nodes_path AS np JOIN nodes AS n " +
                            "      ON np.id = n.parent_id " +
                            ") " +
                            "SELECT * FROM nodes_path " +
                            "ORDER BY path;",
                    new Object[]{}, (RowMapper<Node>) (resultSet, i) -> {
                        nodesList.add(mapNode(resultSet, rootId));
                        return null;
                    });
        } catch (DataAccessException e) {
            return null;
        }
        return nodesList;
    }

    /**
     * Get a list of Node entities that are part of a subtree starting from a specific node
     * Using the height of the starting node we calculate the height of every element in the subtree
     *
     * @param nodeId - the id of the starting nde
     * @param height - the height of the starting node
     * @param rootId - the id of the root node
     * @return - list of Node entities with all the children in the subtree
     */
    public List<Node> getSubTree(long nodeId, int height, long rootId) {

        List<Node> nodesList = new ArrayList<>();

        try {
            jdbcTemplate.query("WITH RECURSIVE nodes_path (id, name, parent_id, path, height) AS " +
                            "( " +
                            "  SELECT id, name, parent_id, name as path, ? height " +
                            "    FROM nodes " +
                            "    WHERE parent_id = ? " +
                            "  UNION ALL " +
                            "  SELECT n.id, n.name, n.parent_id, CONCAT(np.path, ' > ', n.name), np.height + 1 " +
                            "    FROM nodes_path AS np JOIN nodes AS n " +
                            "      ON np.id = n.parent_id " +
                            ") " +
                            "SELECT * FROM nodes_path " +
                            "ORDER BY path;",
                    new Object[]{height, nodeId}, (RowMapper<Node>) (resultSet, i) -> {
                        nodesList.add(mapNode(resultSet, rootId));
                        return null;
                    });
        } catch (DataAccessException e) {
            return null;
        }
        return nodesList;
    }

    /**
     * Change the parent of a specific node
     * All the children of the node will move with it to the new branch
     *
     * @param nodeId   - the node that should be edited
     * @param parentId - the id of the new parent
     * @return - a boolean with the result after the edit
     */
    public boolean editNodeParent(long nodeId, long parentId) {
        int result;
        try {
            result = jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps =
                                connection.prepareStatement("UPDATE nodes " +
                                        "SET " +
                                        "    parent_id = ? " +
                                        "WHERE " +
                                        "    id = ?;");
                        ps.setLong(1, parentId);
                        ps.setLong(2, nodeId);
                        return ps;
                    });

        } catch (DataAccessException e) {
            return false;
        }
        return result == 1;
    }

    /**
     * Mapping the ResultSet to an entity Node
     *
     * @param resultSet - result of the request
     * @param rootId    - the id of the root node
     * @return - Node entity with all the fields set
     * @throws DataAccessException -
     * @throws SQLException        -
     */
    private Node mapNode(ResultSet resultSet, long rootId) throws DataAccessException, SQLException {
        Node temp = new Node();
        temp.setId(resultSet.getInt(1));
        temp.setName(resultSet.getString(2));
        temp.setParent(resultSet.getInt(3));
        temp.setRootId(rootId);
        temp.setPath(resultSet.getString(4));
        temp.setHeight(resultSet.getInt(5));
        return temp;
    }

}
