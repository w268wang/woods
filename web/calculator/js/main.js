var line1 = "";
var line2 = "";
var number1 = "";
var number2 = "";
var operator = "";
var end = 0;
var buttons = document.getElementsByTagName('button');
for(var i = 0; i < buttons.length; i++) {
    buttons[i].addEventListener('click', function() {
        document.getElementById('line1').innerHTML = line1;
        document.getElementById('line2').innerHTML = line2;
    });
};

function eq() {
    if (number1 == "-") {
        number1 = "0";
    }
    if (number2 == "-") {
        number2 = "0";
    }
    if (number1 == ""){
        var answer = 0;
    } else if (number2 == "") {
        var answer = number1;
    } else {
        var answer = eval(parseFloat(number1), parseFloat(number2), operator);
    }
    number1 = answer;
    number2 = "";
    line1 = "";
    line2 = answer;
    operator = "";
    end = 1;
}

function buttonNum(i) {
    if (number1.length > 24 || number2.length > 24) {
        return
    }
    if (end == 1) {
        number1 = "";
        line2 = "";
        end = 0;
    }
    if (line2 == "0") {
        line2 = "";
    }
    if (operator == "") {
        number1 += i;
    } else {
        number2 += i;
    }
    line2 += i;
}

function buttonOp(op) {
    line2 += "";
    if (line2.indexOf(".") == (line2.length - 1)) {
        line2 = line2.substring(0, line2.indexOf("."));
    }
    if (line2 == "-") {
        line2 = "0";
        number1 = "0";
    }
    line2 += " " + op;
    operator = op;
    line1 = line2;
    line2 = "0";
    end = 0;
}

function buttonPo() {
    if (operator == "") {
        if (number1 == "0") {
            return;
        }
        if (number1.indexOf("-") > -1) {
            number1 = number1.substring(1);
        } else {
            number1 = "-" + number1;
        }
        line2 = number1;
    } else {
        if (number2 == "0") {
            return;
        }
        if (number2.indexOf("-") > -1) {
            number2 = number2.substring(1);
            return;
        } else {
            number2 = "-" + number2;
        }
        line2 = number2;
    }
}

function buttonClear() {
    line1 = "";
    line2 = "0";
    number1 = "";
    number2 = "";
    operator = "";
    end = 0;
}

function buttonDot() {
    if (operator == "") {
        if (number1.indexOf(".") > -1) {
            return;
        } else {
            if (number1 == "") {
                number1 = "0";
            }
            number1 += ".";
        }
        line2 = number1;
    } else {
        if (number2.indexOf(".") > -1) {
            return;
        } else {
            if (number2 == "") {
                number2 = "0";
            }
            number2 += ".";
        }
        line2 = number2;
    }
}

function buttonZero(num) {
    var i = "0";
    if (num == 2) {
        i = "00";
    }
    if (line2 == "0") {
        return;
    }
    if (line2 == "") {
        line2 = "0";
        return;
    }
    if (number1.length > 24 || number2.length > 24) {
        return
    }
    if (end == 1) {
        number1 = "";
        line2 = "";
        end = 0;
    }
    if (operator == "") {
        number1 += i;
    } else {
        number2 += i;
    }
    line2 += i;
}

function eval(a, b, op) {
    if (op == '+') {
        return a + b;
    } else if (op == '-') {
        return a - b;
    } else if (op == '*') {
        return a * b;
    } else if (op == '/') {
        if (b == 0) {
            return -1;
        }
        return a / b;
    } else if (op == '%') {
        return a % b;
    }
    return -1;
}