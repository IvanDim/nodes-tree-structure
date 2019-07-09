const Tree = function () {

    var types;
    var nodeId = 1;
    var iteration = 0;
    var index = {};
    var data = {};
    var unordList = $('#tree');
    var selected = 22; // artificer

    var inputParent = $('.input1');
    var modal = document.getElementById("my-modal");
    var btn = document.getElementById("get-subtree");
    var editBtn = document.getElementById("edit-node");

    function init() {

// When the user clicks the button, open the modal
        btn.onclick = function () {
            ajaxGetSubtree();
        };

// When the user clicks anywhere outside of the modal, close it
        window.onclick = function (event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        };

        editBtn.onclick = function () {
            ajaxUpdateParent(inputParent.val());
        };

// build index and data store
        for (var i = 0; i < types.length; i++) {
            if (typeof index[types[i].parent] == 'undefined') {
                index[types[i].parent] = [];
            }

            index[types[i].parent].push(types[i].id);
            data[types[i].id] = types[i];
            iteration++;
        }

// get parents of selected element
        var parent_id = selected;
        var parents = [];

        while (typeof data[parent_id] !== 'undefined') {
            iteration++;
            parent_id = data[parent_id].parent;
            parents.push(parent_id);
        }


        function tree(obj, parent, level) {

            var html = '';

            if (typeof index[parent] !== 'undefined') {
                for (var i = 0; i < index[parent].length; i++) {
                    iteration++;
                    var nextLevel = level + 1;

                    var id = index[parent][i];

                    // get child
                    var sub = tree(obj, id, nextLevel);

                    // current element is a leaf or a node ?
                    var classes = 'node closed';
                    if ($.inArray(id, parents) !== -1) {
                        classes = 'node';
                    }
                    if (sub === false) {
                        classes = 'leaf';
                    }

                    var icon;
                    if (data[id].id !== 1) {
                        icon = "../images/leave.png";
                    } else {
                        icon = "../images/plant.png";
                    }
                    // create element
                    html += '<li class="' + classes + '">'
                        + '<a class="tree_elm">'
                        + '<input type="hidden" value="' + data[id].id + '">'
                        + '<img class="tree_elm_img" src=' + icon + '/>'
                        + '<label class="tree_elm_title">' + data[id].name + '</label>'
                        + '<small class="tree_elm_desc">path: ' + data[id].path + '</small>'
                        + '</a>';

                    // don't append empty child node
                    if (sub !== false) {
                        html += sub;
                    }

                    // close the element
                    html += '</li>';
                }
            }

            // if there's no data, return false
            if (html === '') {
                return false;
            } else {
                // display only the first level elements
                var style = '';
                if (parent !== 0 && $.inArray(parent, parents) === -1) {
                    style = ' style="display: none;"';
                }
                return '<ul' + style + '>' + html + '</ul>';
            }
        }


        var generated = tree(types, 0, 0);

        unordList.html(generated);

        unordList.on('click', 'a', function () {
            var selected = $(this).parent().find('input').val();
            nodeId = selected;
            $('#selected').text(selected);
            return false;
        });

        unordList.on('click', 'li.node > a', function (event) {
            //$( this ).css("text-decoration", "underline");

            var parent = $(this).parent();

            if (parent.hasClass('node') && parent.hasClass('closed')) {
                var action = 'show';
            } else {
                var action = 'hide';
            }

            // hide all nodes
            $('#tree .node').addClass('closed')
                .find('ul:first').hide();

            // show all parent nodes
            $(this).closest('ul').removeClass('closed').show();

            if (action === 'show') {
                // show clicked node
                parent.removeClass('closed')
                    .find('ul:first').show();
            } else {
                parent.addClass('closed')
                    .find('ul:first').hide();
            }

            return false;
        });
    }

    function ajaxGetFullTree(callback) {
        $.ajax({
            type: 'GET',
            url: '/api/nodes/full',
            success: function (data) {
                if (data) {
                    types = data;
                    callback();
                }
            },
            error: function () {
                swal.fire({
                    type: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                })
            }
        });
    }

    function ajaxGetSubtree() {
        $.ajax({
            type: 'GET',
            url: '/api/nodes/' + nodeId,
            success: function (data) {
                let content = "";
                for (let i = 0; i < data.length; i++) {
                    content = content + "{id: " + data[i].id + ", name: " + data[i].name + ", parentId: " + data[i].parent + ", rootId: " + data[i].rootId + ", height: " + data[i].height + ", path: " + data[i].path + "} <br/>";
                }
                if (data.length === 0) {
                    content = "There are no children nodes."
                }
                $('.modal-content').html(content);
                modal.style.display = "block";
            },
            error: function () {
                swal.fire({
                    type: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                })
            }
        });
    }

    function ajaxUpdateParent(parentId) {
        $.ajax({
            type: 'PUT',
            url: '/api/nodes/' + nodeId,
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                parentId: parentId
            }),
            success: function () {
                location.reload();
            },
            error: function () {
                location.reload();
            }
        });
    }

    return {
        init: function () {
            ajaxGetFullTree(init);
        }
    }
}();

$(document).ready(function () {
    Tree.init();
});