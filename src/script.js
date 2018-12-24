var groups = [];
var checkboxes = [];
var references = [];
var isOpened = [];
var groupNames = [];
var init = false;
var bars = document.getElementsByTagName('br');
var flags = document.getElementsByTagName('flag');

if (!init) {
    var gr = document.getElementsByClassName("Group");
    for (var i = 0; i < gr.length; i++) {
        groups[i] = gr[i]; 
        groupNames[i] = gr[i].id;
        console.log(groupNames[i]);
        isOpened[groupNames[i]] = true;
    }
    console.log(isOpened);
    for (var i = 0; i < groups.length; i++) {
        var checks = document.getElementsByName(groups[i].id + "_checkbox");
        checkboxes[i] = new Array(checks.length);
        for (var j = 0; j < checks.length; j++) {
            checkboxes[i][j] = checks[j];
        }
    }

    for (var i = 0; i < groups.length; i++) {
        var refs = document.getElementsByName(groups[i].id + "_reference");
        references[i] = new Array(refs.length);
        for (var j = 0; j < refs.length; j++) {
            references[i][j] = refs[j];
        }
    }

    for (var i = 0; i < groups.length; i++) {
        openChecksRefsBars(i);
    }
    init = true;
}

function open_close(event) {
    var group = event.target.id;
    var index = groupNames.indexOf(group);
    if (isOpened[group] === true) {
        //opened, so close
        closeChecksRefsBars(index);
        isOpened[group] = false;
        flags[index].innerHTML = "-";
    } else {
        //closed, so open
        openChecksRefsBars(index);
        isOpened[group] = true;
        flags[index].innerHTML = "+";
    }
    console.log(isOpened);
}

function closeChecksRefsBars(index){
  for (var i = 0; i < checkboxes[index].length; i++) {
            checkboxes[index][i].style.display = 'none';
            references[index][i].style.display = 'none';
        }
        var barsBoundaries = calcBars(index);
        for (var i = barsBoundaries[0]; i < barsBoundaries[1]; i++) {
            bars[i].style.display = 'none';
        }
        
}

function openChecksRefsBars(index){
  for (var i = 0; i < checkboxes[index].length; i++) {
            checkboxes[index][i].style.display = 'table-row';
            references[index][i].style.display = 'inline-block';
        }
        var barsBoundaries = calcBars(index);
        for (var i = barsBoundaries[0]; i < barsBoundaries[1]; i++) {
            bars[i].style.display = 'inline';
        }
        
}

function calcBars(index) {
    var res = [0, 0];
    if (index - 1 != -1) {
        for (var i = 0; i < index; i++) {
            res[0] += checkboxes[i].length + 1; //+1 for Group <br>
        }
    }
    res[1] = res[0] + checkboxes[index].length + 1;
    return res;
}