'use strict';

/**
 * This function get all the instances info from AWS (via our main servlet) and re-render the table
 */
function fnRefreshAll() {
    // clear the list of instances in table (data lines only)
    $("#instList .data-line").remove();

    // retrieve latest list of instances again
    $.ajax({
        type : "POST",
        url : "main",
        data : {
            action : "describeInstances"
        },
        success : function(ret) {
            if (ret) {
                if (ret.success) {
                    var insts = ret.data.insts;
                    var len = insts.length;
                    for (var i = 0; i < len; i++) {
                        var inst = insts[i];
                        var tr = $("<tr class='data-line'></tr>");
                        tr.append("<td>" + inst.id + "</td>");
                        tr.append("<td>" + inst.name + "</td>");
                        tr.append("<td>" + inst.publicIp + "</td>");
                        tr.append("<td>" + inst.image + "</td>");
                        tr.append("<td>" + inst.type + "</td>");
                        tr.append("<td>" + inst.state + "</td>");
                        tr.append("<td><button type='button' onclick=\"terminate('" + inst.id
                                + "')\">destroy</button></td>");
                        $("#instList").append(tr);
                    }

                    // also update the "isPrime" <p> tag
                    $('#qtyIsPrime').text("is qty prime: " + ret.data.prime);

                } else {
                    alert(ret.msg);
                }
            }
        }
    });

};

/**
 * This function starts qty number of instances named 'name' and refresh the table.
 */
function fnOnDeploy() {
    var qty = $("#iptQty").val();
    var name = $("#iptName").val();

    // do basic client side checks
    if (!qty) {
        alert('invalid qty!');
        return;
    }

    if (!name) {
        alert('invalid name!');
        return;
    }

    // send ajax request to run instances
    $.ajax({
        type : "POST",
        url : "main",
        data : {
            action : "runInstances",
            qty : qty,
            name : name
        },
        success : function(ret) {
            if (ret) {
                if (ret.success) {
                    // alert(qty+' instance(s) started successfully');

                    // re-render the inst list table
                    fnRefreshAll();

                    // clear the inputs
                    $("#iptQty").val("");
                    $("#iptName").val("");
                } else {
                    alert(ret.msg);
                }
            }
        }
    });
};

/**
 * This function terminates an instance and refresh the table
 */
function terminate(id) {
    // send ajax request to stop an instance
    $.ajax({
        type : "POST",
        url : "main",
        data : {
            action : "terminateInstances",
            id : id
        },
        success : function(ret) {
            if (ret) {
                if (ret.success) {
                    //alert('instance ' + id + ' has been terminated successfully');
                    fnRefreshAll();
                } else {
                    alert(ret.msg);
                }
            }
        }
    });
};

// describe instances on page load
$(document).ready(function() {
    fnRefreshAll();
});
