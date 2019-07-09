package com.amazing.nodes.controller.api;

import com.amazing.nodes.model.entities.Node;
import com.amazing.nodes.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nodes")
public class NodesController {
    private final NodeService nodeService;

    @Autowired
    NodesController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * Getting the full tree structure of nodes starting from the root
     * @return - list of Node entities with all the nodes in it
     */
    @GetMapping("/full")
    public ResponseEntity getFullTree() {
        List<Node> responseData = nodeService.getFullTree();
        if (responseData == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(responseData);
    }

    /**
     * Getting a subtree starting for any node in the table
     *
     * @param nodeId - the root of the subtree
     * @return       - list of Node entities with all the children in the subtree
     */
    @GetMapping("/{nodeId}")
    public ResponseEntity getSubtree(@PathVariable Long nodeId) {

        if (!(nodeId > 0)) return ResponseEntity.badRequest().build();

        List<Node> responseData = nodeService.getSubTree(nodeId);
        if (responseData == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(responseData);
    }

    /**
     * Changing the parent of any child node in the tree
     * All the children of the node will move with it to the new branch
     *
     * @param nodeId - the node that should be edited
     * @param data   - information about the edit (new parentId, more information in the future)
     * @return       - boolean with the result after the edit
     */
    @PutMapping("/{nodeId}")
    public ResponseEntity editNodeParent(@PathVariable Long nodeId, @RequestBody Map<String, String> data) {
        int parentId = Integer.parseInt(data.get("parentId"));

        if (!(nodeId > 0) || !(parentId > 0) || nodeId == (long) parentId)
            return ResponseEntity.badRequest().build();

        if (!nodeService.editNodeParent(nodeId, parentId)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }
}
