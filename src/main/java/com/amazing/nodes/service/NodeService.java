package com.amazing.nodes.service;

import com.amazing.nodes.model.dao.NodeDao;
import com.amazing.nodes.model.entities.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {

    private final NodeDao nodeDao;

    @Autowired
    public NodeService(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    /**
     * Getting the full tree structure of nodes starting from the root
     *
     * @return - list of Node entities with all the nodes in it
     */
    public List<Node> getFullTree() {

        Long rootId = nodeDao.getRootNode();
        if (rootId == null) return null;

        List<Node> nodeList = nodeDao.getFullTree(rootId);
        if (nodeList == null) return null;

        return nodeList;
    }

    /**
     * Getting a subtree starting for any node in the table
     *
     * @param nodeId - the root of the subtree
     * @return - list of Node entities with all the children in the subtree
     */
    public List<Node> getSubTree(long nodeId) {

        Integer height = nodeDao.getNodeHeight(nodeId);
        if (height == null) return null;

        Long rootId = nodeDao.getRootNode();
        if (rootId == null) return null;

        List<Node> nodeList = nodeDao.getSubTree(nodeId, height, rootId);
        if (nodeList == null) return null;

        return nodeList;
    }

    /**
     * Changing the parent of any child node in the tree
     * All the children of the node will move with it to the new branch
     *
     * @param nodeId   -   the node that should be edited
     * @param parentId - the id of the new parent
     * @return -   status 200 on success
     */
    public boolean editNodeParent(long nodeId, long parentId) {
        return nodeDao.editNodeParent(nodeId, parentId);
    }
}
